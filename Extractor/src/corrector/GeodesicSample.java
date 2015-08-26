package corrector;

import java.util.Locale;

public class GeodesicSample {
    private double lat, lng, vel;

    public GeodesicSample(double lat, double lng, double vel) {
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

    
    public String toPrettyJSON() {
        return String.format(Locale.ENGLISH, "{\"lat\": %f, \"lng\": %f, \"vel\": %f}", Math.toDegrees(lat), Math.toDegrees(lng), vel);
    }

    
    public String toShittyJSON() {
        return String.format(Locale.ENGLISH, "[%f, %f, %f]", Math.toDegrees(lat), Math.toDegrees(lng), vel);
    }

    
    public String toCSV() {
        return String.format(Locale.ENGLISH, "%f,%f,%f", Math.toDegrees(lat), Math.toDegrees(lng), vel);
    }

    
    public String toWKT() {
        return String.format(Locale.ENGLISH, "%f %f", Math.toDegrees(lat), Math.toDegrees(lng));
    }
}
