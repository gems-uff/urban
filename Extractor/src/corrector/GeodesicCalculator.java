package corrector;


public enum GeodesicCalculator {

    CALCULATOR;

    private static final double EQ_R2 = 6378137.0 * 6378137.0, P_R2 = 6356752.3 * 6356752.3, DELTA = 1e-4;

    public double distanceBetween(GeodesicSample a, GeodesicSample b) {
        double deltaLat = (b.lat() - a.lat()) / 2;
        double deltaLng = (b.lng() - a.lng()) / 2;
        double k = Math.sin(deltaLat) * Math.sin(deltaLat) + Math.cos(a.lat()) * Math.cos(b.lat()) * Math.sin(deltaLng) * Math.sin(deltaLng);
        return 2 * this.meanRadiusOn(a, b) * Math.atan2(Math.sqrt(k), Math.sqrt(1 - k));
    }

    public GeodesicSample barycenterOf(GeodesicSample... amostras) {

        double lat = 0;
        double lng = 0;
        for (GeodesicSample amostra : amostras) {
            lat += amostra.lat();
            lng += amostra.lng();
        }
        GeodesicSample resp = new GeodesicSample(Math.toDegrees(lat / amostras.length), Math.toDegrees(lng / amostras.length), 0);

        return resp;
    }

    public boolean closeEnough(BoundingBox caixa, GeodesicSample ping, double error) {

        GeodesicSample novoNE;
        GeodesicSample novoNW;
        GeodesicSample novoSE;
        GeodesicSample novoSW;
        novoNE = CALCULATOR.destinationFrom(caixa.northEast(), error, Math.PI / 4);
        novoNW = CALCULATOR.destinationFrom(caixa.northWest(), error, -Math.PI / 4);
        novoSE = CALCULATOR.destinationFrom(caixa.southEast(), error, 3 * Math.PI / 4);
        novoSW = CALCULATOR.destinationFrom(caixa.southWest(), error, -3 * Math.PI / 4);

        BoundingBox newCaixa = new BoundingBox(novoSW, novoSE, novoNE, novoSW);

        return this.within(newCaixa, ping);
    }

    public double meanRadiusOn(GeodesicSample... points) {
        double radius = 0;
        for (GeodesicSample point : points) {
            double cosLat2 = Math.pow(Math.cos(point.lat()), 2), sinLat2 = Math.pow(Math.sin(point.lat()), 2);
            radius += Math.sqrt((EQ_R2 * EQ_R2 * cosLat2 + P_R2 * P_R2 * sinLat2) / (EQ_R2 * cosLat2 + P_R2 * sinLat2));
        }
        return radius / points.length;
    }

    public GeodesicSample destinationFrom(GeodesicSample point, double distance, double bearing) {
        distance /= this.meanRadiusOn(point);
        double lat = Math.asin(Math.sin(point.lat()) * Math.cos(distance) + Math.cos(point.lat()) * Math.sin(distance) * Math.cos(bearing));
        double lng = point.lng() + Math.atan2(Math.sin(bearing) * Math.sin(distance) * Math.cos(point.lat()), Math.cos(distance) - Math.sin(point.lat()) * Math.sin(lat));
        return new GeodesicSample(Math.toDegrees(lat), Math.toDegrees(lng), 0);
    }

    public double bearing(GeodesicSample a, GeodesicSample b) {
        double y = Math.sin(b.lng() - a.lng()) * Math.cos(b.lat());
        double x = Math.cos(a.lat()) * Math.sin(b.lat()) - Math.sin(a.lat()) * Math.cos(b.lat()) * Math.cos(b.lng() - a.lng());
        return Math.atan2(y, x);
    }

    public double bearing(Segment segment) {
        return this.bearing(segment.begin(), segment.end());
    }

    public double crossTrackDistance(LineString line, GeodesicSample target) {
        double smallestDistance = Double.MAX_VALUE;
        for (Segment s : line.segmentsOf()) {
            GeodesicSample begin = s.begin(), end = s.end();
            double radius = this.meanRadiusOn(begin, end, target);
            double possibleSmallestDistance = Math.abs(Math.asin(Math.sin(this.distanceBetween(begin, target) / radius) * Math.sin(this.bearing(begin, end) - this.bearing(begin, target))) * radius);
            if (smallestDistance > possibleSmallestDistance) {
                smallestDistance = possibleSmallestDistance;
            }
        }
        return smallestDistance;
    }

    public boolean within(BoundingBox box, GeodesicSample p) {
        GeodesicSample a = projectionOn(new Segment(box.southWest(), box.southEast()), p);
        GeodesicSample b = projectionOn(new Segment(box.northWest(), box.northEast()), p);
        System.out.println(DELTA * distanceBetween(box.northWest(), box.southEast()));
        return Math.abs(distanceBetween(a, b) - (distanceBetween(a, p) + distanceBetween(p, b))) < DELTA * distanceBetween(box.northWest(), box.southEast());
    }

    public GeodesicSample projectionOn(LineString line, GeodesicSample target) {
        double smallestDistance = Double.MAX_VALUE;
        GeodesicSample projectedPoint = target;
        for (Segment s : line.segmentsOf()) {
            GeodesicSample begin = s.begin(), end = s.end();
            double radius = this.meanRadiusOn(begin, end, target);
            GeodesicSample possibleProjectedPoint = this.destinationFrom(begin, Math.acos(Math.cos(this.distanceBetween(begin, target) / radius) / Math.cos(this.crossTrackDistance(s, target) / radius)) * radius, this.bearing(begin, end));
            double possibleSmallestDistance = this.distanceBetween(possibleProjectedPoint, target);
            if (smallestDistance > possibleSmallestDistance) {
                smallestDistance = possibleSmallestDistance;
                projectedPoint = possibleProjectedPoint;
            }
        }
        return projectedPoint;
    }
}
