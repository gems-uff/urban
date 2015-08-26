package corrector;


import java.util.Locale;

public class BoundingBox {
    private GeodesicSample southWest, southEast, northEast, northWest;

    public BoundingBox(GeodesicSample southWest, GeodesicSample southEast, GeodesicSample northEast, GeodesicSample northWest) {
        this.southWest = southWest;
        this.southEast = southEast;
        this.northEast = northEast;
        this.northWest = northWest;
    }

    public GeodesicSample southWest() {
        return southWest;
    }

    public GeodesicSample southEast() {
        return southEast;
    }

    public GeodesicSample northEast() {
        return northEast;
    }

    public GeodesicSample northWest() {
        return northWest;
    }

    
    public String toPrettyJSON() {
        return String.format(Locale.ENGLISH, "{\"sw\": %s, \"se\": %s, \"ne\": %s,\"nw\": %s}", southWest.toPrettyJSON(), southEast.toPrettyJSON(), northEast.toPrettyJSON(), northWest.toPrettyJSON());
    }

    
    public String toShittyJSON() {
        return String.format(Locale.ENGLISH, "[%s, %s, %s, %s]", southWest.toShittyJSON(), southEast.toShittyJSON(), northEast.toShittyJSON(), northWest.toShittyJSON());

    }

    
    public String toCSV() {
        return String.format(Locale.ENGLISH, "%s,%s,%s,%s", southWest.toCSV(), southEast.toCSV(), northEast.toCSV(), northWest.toCSV());
    }

    
    public String toWKT() {
        return String.format(Locale.ENGLISH, "%s, %s, %s, %s, %s", southWest.toWKT(), southEast.toWKT(), northEast.toWKT(), northWest.toWKT(), southWest.toWKT());
    }
}
