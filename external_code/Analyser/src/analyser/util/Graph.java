package analyser.util;

import java.util.*;

public class Graph<V, W extends Integer> implements Iterable<V> {
    private Map<V, Set<Edge<V>>> graph;

    public Graph() {
        this.graph = new HashMap<>();
    }

    public void put(V x) {
        this.graph.putIfAbsent(x, new HashSet<>());
    }

    public void remove(V x) {
        this.graph.keySet().forEach(t -> this.disconnect(t,x));
        this.graph.remove(x);
    }

    public void connect(V a, V b, W w) {
        this.graph.get(a).add(new Edge<>(b, w));
    }

    public void disconnect(V a, V b) {
        this.graph.get(a).remove(new Edge<>(b, 0));
    }

    public boolean containsVertex(V v) {
        return this.graph.containsKey(v);
    }

    public boolean containsEdge(V a, V b) {
        return this.graph.containsKey(a) && this.graph.get(a).stream().anyMatch((x) -> x.to().equals(b));
    }

    public Set<Edge<V>> edgesOf(V x) {
        return new HashSet<>(this.graph.get(x));
    }

    public boolean isEmpty() {
        return this.graph.isEmpty();
    }


    @Override
    public Iterator<V> iterator() {
        return this.graph.keySet().iterator();
    }

    public String toString() {
        StringBuilder json = new StringBuilder("");
        this.graph.forEach((v, edges) -> {
            json.append(v).append(" -> ").append(edges).append("\n");
        });
        return json.toString();
    }
}

class Edge<V> {
    private V to;
    private Integer weight;

    Edge(V to, Integer weight) {
        this.to = to;
        this.weight = weight;
    }


    public V to() {
        return to;
    }

    public Integer weight() {
        return weight;
    }

    public void weight(Integer weight) {
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge edge = (Edge) o;

        return !(to != null ? !to.equals(edge.to) : edge.to != null);

    }

    @Override
    public int hashCode() {
        return (to != null ? to.hashCode() : 0);
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "{\"to\": %s, \"weight\": %s}", to.toString(), weight.toString());
    }

}
