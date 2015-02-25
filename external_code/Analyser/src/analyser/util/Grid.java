package analyser.util;

import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Grid {


    protected static final GeoSample NORTH_EAST = new GeoSample(-22.6406, -43.1509, 0);
    protected static final GeoSample SOUTH_WEST = new GeoSample(-23.0737, -43.7278, 0);

    private Map<Integer, Cell> hash;
    private Graph<Integer, Integer> graph;
    private double cellSize;
    private int cellsPerLine;

    public Grid(double cellSize) {
        this.cellSize = cellSize;
        this.hash = new HashMap<>();
        this.graph = new Graph<>();
        this.cellsPerLine = (int) (Tools.distanceBetween(Grid.SOUTH_WEST, new GeoSample(Math.toDegrees(Grid.SOUTH_WEST.lat()), Math.toDegrees(Grid.NORTH_EAST.lng()), 0)) / cellSize);
    }

    public void add(GeoSample p) {
        int ID = Tools.calculateID(SOUTH_WEST, p, this.cellsPerLine, this.cellSize);
        this.hash.putIfAbsent(ID, new Cell());
        this.hash.get(ID).moveIn(p);
        this.graph.put(ID);
    }

    public void addAll(List<? extends GeoSample> samples) {
        this.add(samples.get(0));
        for (int i = 1; i < samples.size(); i++) {
            GeoSample a = samples.get(i - 1);
            GeoSample b = samples.get(i);
            this.add(b);
            this.connect(a, b);
        }

    }

    public void connect(GeoSample a, GeoSample b) {
        int ID_A = Tools.calculateID(SOUTH_WEST, a, this.cellsPerLine, this.cellSize);
        int ID_B = Tools.calculateID(SOUTH_WEST, b, this.cellsPerLine, this.cellSize);

        if (!this.graph.containsEdge(ID_A, ID_B)) this.graph.connect(ID_A, ID_B, 0);
        this.graph.edgesOf(ID_A).stream().filter(x -> x.to() == ID_B).forEach(x -> x.weight(x.weight() + 1));
    }

    public void cleanConnections() {
        for (Integer from : graph) {
            if (graph.containsEdge(from, from)) {
                int w = graph.edgesOf(from).stream().filter(x -> Objects.equals(x.to(), from)).findAny().get().weight();
                Cell x = this.hash.get(from);
                for (int i = 0; i < w; i++) {
                    x.moveOut(x.location());
                }
            }
        }
    }

    public void cleanGraph() {
        Set<Integer> marked = new HashSet<>();
        double avg = hash.values().parallelStream().collect(Collectors.averagingDouble(Cell::population)) * 0.6;
        hash.forEach((id, cell) -> {
            if (cell.population() < avg) marked.add(id);
        });

        marked.forEach(x -> {
            Set<Edge<Integer>> edges = graph.edgesOf(x);
            for (Integer y : graph) {
                if (graph.containsEdge(x, y)) {
                    edges.forEach(t -> this.connect(hash.get(x).location(), hash.get(t.to()).location()));
                }
            }
            graph.remove(x);
        });
        marked.forEach(hash::remove);
    }

    public String route() {
        System.out.println("Iniciando calculo da rota.\n");
        System.out.println("conecoes");
        this.cleanConnections();
        System.out.println("conecoes\n\nverteces");
        this.cleanGraph();
        System.out.println("verteces\n\ntrechos");
        Set<Pair<Cell, Cell>> route = new HashSet<>();
        for (Integer from : graph) {
            if (!graph.edgesOf(from).isEmpty()) {
                double limit = graph.edgesOf(from).stream().max((a, b) -> Integer.compare(a.weight(), b.weight())).get().weight() * 0.75;
                graph.edgesOf(from).stream().filter(e -> e.weight() > limit).forEach(e -> route.add(new Pair<>(hash.get(from), hash.get(e.to()))));
            }
        }
        System.out.println("trechos\n\npedaços");
        List<Set<Pair<Cell, Cell>>> routes = new ArrayList<>();
        for (Pair<Cell, Cell> seg : route) {
            Optional<Set<Pair<Cell, Cell>>> possible = routes.stream().filter(x -> x.stream().anyMatch(y -> y.getKey() == seg.getValue() || y.getKey() == seg.getKey() || y.getValue() == seg.getValue() || y.getValue() == seg.getKey())).findAny();
            if (possible.isPresent()) {
                Set<Pair<Cell, Cell>> r = possible.get();
                r.add(seg);
            } else {
                routes.add(new HashSet<>(Arrays.asList(seg)));
            }
        }
        System.out.println("pedaços\n\nrota");
        for (Set<Pair<Cell, Cell>> r : routes) {
            routes.stream().filter(s -> !r.equals(s) && s.parallelStream().anyMatch(x -> r.parallelStream().anyMatch(y -> y.getKey() == x.getValue() || y.getKey() == x.getKey() || y.getValue() == x.getValue() || y.getValue() == x.getKey()))).forEach(s -> {
                s.addAll(r);
            });
        }
        System.out.println("rota\n\njson");
        Set<Cell> r = new HashSet<>();
        for (Pair<Cell, Cell> pair : routes.stream().max((a, b) -> Integer.compare(a.size(), b.size())).get()) {
            r.add(pair.getValue());
            r.add(pair.getKey());
        }
        System.out.println("json");
        return r.toString();


    }


    public String toString() {
        double avg = hash.values().stream().collect(Collectors.averagingDouble(Cell::population)) * 0.5;
        return hash.values().stream().filter(x -> x.population() > avg).collect(Collectors.toList()).toString();
    }
}

class Cell {
    private double lat, lng, vel;
    private int population;

    public Cell() {
        this.lat = 0;
        this.lng = 0;
        this.vel = 0;
        this.population = 0;
    }

    public double lat() {
        return lat / population;
    }

    public double lng() {
        return lng / population;
    }

    public double vel() {
        return vel / population;
    }

    public int population() {
        return population;
    }

    public void moveIn(GeoSample x) {
        this.lat += x.lat();
        this.lng += x.lng();
        this.vel += x.vel();
        this.population++;
    }

    public void moveOut(GeoSample x) {
        this.lat -= x.lat();
        this.lng -= x.lng();
        this.vel -= x.vel();
        this.population--;
    }

    public GeoSample location() {
        return new GeoSample(Math.toDegrees(lat()), Math.toDegrees(lng()), vel());
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "{\"lat\": %f, \"lng\": %f, \"vel\": %f, \"population\": %d}",
                             Math.toDegrees(lat / population), Math.toDegrees(lng / population), vel / population, population);
    }
}

class TriPoint {
    private int from, in, to;
    private int population;
    private Set<TriPoint> last, next;

    public TriPoint(int from, int in, int to) {
        this.from = from;
        this.in = in;
        this.to = to;
        this.population = 1;
        this.last = new HashSet<>();
        this.next = new HashSet<>();
    }

    public int getFrom() {
        return from;
    }

    public int getIn() {
        return in;
    }

    public int getTo() {
        return to;
    }

    public void next(TriPoint x) {
        this.next.add(x);
    }

    public void last(TriPoint x) {
        this.last.add(x);
    }

    public Set<TriPoint> getLast() {
        return last;
    }

    public Set<TriPoint> getNext() {
        return next;
    }

    public int getPopulation() {
        return population;
    }

    public void moveIn() {
        this.population++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TriPoint triPoint = (TriPoint) o;

        if (from != triPoint.from) return false;
        if (in != triPoint.in) return false;
        if (to != triPoint.to) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = from;
        result = 31 * result + in;
        result = 31 * result + to;
        return result;
    }
}
