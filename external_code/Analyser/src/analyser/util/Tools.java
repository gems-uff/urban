package analyser.util;

public class Tools {

    private static final double eqR2 = 6378137.0 * 6378137.0, pR2 = 6356752.3 * 6356752.3;
    private static final double a = 6378160, b = 6356774.719, f = 1 / 298.247167;

    public static double calculateRadius(GeoSample... points) {
        double radius = 0;
        for (GeoSample point : points) {
            double cosLat2 = Math.pow(Math.cos(point.lat()), 2), sinLat2 = Math.pow(Math.sin(point.lat()), 2);
            radius += Math.sqrt((eqR2 * eqR2 * cosLat2 + pR2 * pR2 * sinLat2) / (eqR2 * cosLat2 + pR2 * sinLat2));
        }
        return radius / points.length;
    }

    public static double distanceBetween(GeoSample a, GeoSample b) {
        double L = b.lng() - a.lng();
        double tanU1 = (1 - f) * Math.tan(a.lat()), cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1)), sinU1 = tanU1 * cosU1;
        double tanU2 = (1 - f) * Math.tan(b.lat()), cosU2 = 1 / Math.sqrt((1 + tanU2 * tanU2)), sinU2 = tanU2 * cosU2;

        double λ = L, λʹ, iterationLimit = 100;
        double sinα, cosSqα, cos2σM, sinσ, σ, cosσ;
        do {
            double sinλ = Math.sin(λ), cosλ = Math.cos(λ);
            double sinSqσ = (cosU2 * sinλ) * (cosU2 * sinλ) + (cosU1 * sinU2 - sinU1 * cosU2 * cosλ) * (cosU1 * sinU2 - sinU1 * cosU2 * cosλ);
            sinσ = Math.sqrt(sinSqσ);
            if (sinσ == 0) {
                return 0;  // co-incident points
            }
            cosσ = sinU1 * sinU2 + cosU1 * cosU2 * cosλ;
            σ = Math.atan2(sinσ, cosσ);
            sinα = cosU1 * cosU2 * sinλ / sinσ;
            cosSqα = 1 - sinα * sinα;
            cos2σM = cosσ - 2 * sinU1 * sinU2 / cosSqα;
            if (Double.isNaN(cos2σM)) {
                cos2σM = 0;  // equatorial line: cosSqα=0 (§6)
            }
            double C = f / 16 * cosSqα * (4 + f * (4 - 3 * cosSqα));
            λʹ = λ;
            λ = L + (1 - C) * f * sinα * (σ + C * sinσ * (cos2σM + C * cosσ * (-1 + 2 * cos2σM * cos2σM)));
        } while (Math.abs(λ - λʹ) > 1e-12 && --iterationLimit > 0);
        double uSq = cosSqα * (Tools.a * Tools.a - Tools.b * Tools.b) / (Tools.b * Tools.b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
        double Δσ = B * sinσ * (cos2σM + B / 4 * (cosσ * (-1 + 2 * cos2σM * cos2σM)
                - B / 6 * cos2σM * (-3 + 4 * sinσ * sinσ) * (-3 + 4 * cos2σM * cos2σM)));

        return Tools.b * A * (σ - Δσ);
    }

    public static GeoSample destinationFrom(GeoSample point, double distance, double bearing) {
        double cosα1 = Math.cos(bearing);
        double sinα1 = Math.sin(bearing);

        double tanU1 = (1 - f) * Math.tan(point.lat()), cosU1 = 1 / Math.sqrt((1 + tanU1 * tanU1)), sinU1 = tanU1 * cosU1;
        double σ1 = Math.atan2(tanU1, cosα1);
        double sinα = cosU1 * sinα1;
        double cosSqα = 1 - sinα * sinα;
        double uSq = cosSqα * (a * a - b * b) / (b * b);
        double A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
        double B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));

        double σ = distance / (b * A), σʹ;
        double cos2σM, sinσ, cosσ, Δσ;
        do {
            cos2σM = Math.cos(2 * σ1 + σ);
            sinσ = Math.sin(σ);
            cosσ = Math.cos(σ);
            Δσ = B * sinσ * (cos2σM + B / 4 * (cosσ * (-1 + 2 * cos2σM * cos2σM)
                    - B / 6 * cos2σM * (-3 + 4 * sinσ * sinσ) * (-3 + 4 * cos2σM * cos2σM)));
            σʹ = σ;
            σ = distance / (b * A) + Δσ;
        } while (Math.abs(σ - σʹ) > 1e-12);

        double tmp = sinU1 * sinσ - cosU1 * cosσ * cosα1;
        double φ2 = Math.atan2(sinU1 * cosσ + cosU1 * sinσ * cosα1, (1 - f) * Math.sqrt(sinα * sinα + tmp * tmp));
        double λ = Math.atan2(sinσ * sinα1, cosU1 * cosσ - sinU1 * sinσ * cosα1);
        double C = f / 16 * cosSqα * (4 + f * (4 - 3 * cosSqα));
        double L = λ - (1 - C) * f * sinα
                * (σ + C * sinσ * (cos2σM + C * cosσ * (-1 + 2 * cos2σM * cos2σM)));
        double λ2 = (point.lng() + L + 3 * Math.PI) % (2 * Math.PI) - Math.PI;  // normalise to -180...+180
        return new GeoSample(Math.toDegrees(φ2), Math.toDegrees(λ2), 0);
    }

    public static int calculateID(GeoSample origin, GeoSample p, int cellPerLine, double cellSize) {
        int x = (int) (Tools.distanceBetween(origin, new GeoSample(Math.toDegrees(origin.lat()), Math.toDegrees(p.lng()), 0)) / cellSize);
        int y = (int) (Tools.distanceBetween(origin, new GeoSample(Math.toDegrees(p.lat()), Math.toDegrees(origin.lng()), 0)) / cellSize);
        return y * cellPerLine + x;
    }
}
