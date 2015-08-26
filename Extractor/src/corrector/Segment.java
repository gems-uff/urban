package corrector;


import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class Segment implements LineString {

    private GeodesicSample a, b;

    public Segment(GeodesicSample a, GeodesicSample b) {
        this.a = a;
        this.b = b;
    }

    public GeodesicSample begin() {
        return a;
    }

    public void begin(GeodesicSample a) {
        this.a = a;
    }

    public GeodesicSample end() {
        return b;
    }

    public void end(GeodesicSample b) {
        this.b = b;
    }

    @Override
    public Collection<Segment> segmentsOf() {
        return Collections.singletonList(this);
    }

    
    public String toPrettyJSON() {
        return String.format(Locale.ENGLISH, "{\"being\": %s, \"end\": %s}", a.toPrettyJSON(), b.toPrettyJSON());
    }

    
    public String toShittyJSON() {
        return String.format(Locale.ENGLISH, "[%s, %s]", a.toPrettyJSON(), b.toPrettyJSON());
    }

    
    public String toCSV() {
        return String.format(Locale.ENGLISH, "%s,%s", a.toCSV(), b.toCSV());
    }

    
    public String toWKT() {
        return String.format(Locale.ENGLISH, "%s, %s", a.toWKT(), b.toWKT());
    }
}
