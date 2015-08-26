package corrector;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SimpleLineString implements LineString {
    private List<Segment> segments;

    public SimpleLineString(List<GeodesicSample> nodes) {
        segments = new ArrayList<>();
        for (int i = 1; i < this.segments.size(); i++) segments.add(new Segment(nodes.get(i - 1), nodes.get(i)));
    }

    public SimpleLineString(GeodesicSample... nodes) {
        this(Arrays.asList(nodes));
    }

    @Override
    public Collection<Segment> segmentsOf() {
        return new ArrayList<>(segments);
    }

    
    public String toPrettyJSON() {
        StringBuilder json = new StringBuilder();
        json.append("[");
        segments.forEach(segment -> json.append(segment.toPrettyJSON()).append(", "));
        return json.delete(json.length() - 2, json.length()).append("]").toString();
    }

    
    public String toShittyJSON() {
        StringBuilder json = new StringBuilder();
        json.append("[");
        segments.forEach(segment -> json.append(segment.toShittyJSON()).append(", "));
        return json.delete(json.length() - 2, json.length()).append("]").toString();
    }


    
    public String toCSV() {
        StringBuilder csv = new StringBuilder();
        segments.forEach(segment -> csv.append(segment.toCSV()).append(", "));
        return csv.delete(csv.length() - 2, csv.length()).toString();
    }

    
    public String toWKT() {
        StringBuilder wkt = new StringBuilder();
        segments.forEach(segment -> wkt.append(segment.toWKT()).append(", "));
        return wkt.delete(wkt.length() - 2, wkt.length()).toString();
    }
}
