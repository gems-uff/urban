package analyser.util;

import java.util.Locale;

public class GeoSample {
    private double lat, lng, vel;

    public GeoSample(double lat, double lng, double vel) {
        this.lat = Math.toRadians(lat);
        this.lng = Math.toRadians(lng);
        this.vel = vel;
    }

    public double lat() {
        return lat;
    }

    public void lat(double lat) {
        this.lat = lat;
    }

    public double lng() {
        return lng;
    }

    public void lng(double lng) {
        this.lng = lng;
    }

    public double vel() {
        return vel;
    }

    public void vel(double vel) {
        this.vel = vel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeoSample geoSample = (GeoSample) o;

        return Double.compare(geoSample.lat, lat) == 0 && Double.compare(geoSample.lng, lng) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lng);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "{\"lat\": %s, \"lng\": %s, \"vel\": %s}", Math.toDegrees(this.lat), Math.toDegrees(this.lng), this.vel);
    }
}
