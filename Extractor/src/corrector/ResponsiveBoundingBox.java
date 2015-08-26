/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package corrector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Pedro
 */
public class ResponsiveBoundingBox extends BoundingBox {

    private List<GeodesicSample> pontosDentro;

    private static Comparator<GeodesicSample> comparadorLat = ((a, b) -> {

        return Double.compare(a.lat(), b.lat());

    });

    private static Comparator<GeodesicSample> comparadorLng = ((a, b) -> {

        return Double.compare(a.lng(), b.lng());

    });

    public ResponsiveBoundingBox(GeodesicSample center) {
        super(center, center, center, center);
        pontosDentro = new ArrayList<>();
        pontosDentro.add(center);
    }

    public void add(GeodesicSample newPoint) {

        pontosDentro.add(newPoint);
        GeodesicSample latmin = pontosDentro.stream().min(comparadorLat).get();
        GeodesicSample latmax = pontosDentro.stream().max(comparadorLat).get();
        GeodesicSample lngmin = pontosDentro.stream().min(comparadorLng).get();
        GeodesicSample lngmax = pontosDentro.stream().max(comparadorLng).get();

        this.southWest().lat(latmin.lat());
        this.southWest().lng(lngmin.lng());

        this.southEast().lat(latmin.lat());
        this.southEast().lng(lngmax.lng());

        this.northWest().lat(latmax.lat());
        this.northWest().lng(lngmin.lng());

        this.northEast().lat(latmax.lat());
        this.northEast().lng(lngmax.lng());

    }

    public void addAll(LineString linha) {
        linha.segmentsOf().stream().forEach(a -> {
            this.add(a.begin());
            this.add(a.end());
        });
    }

    public List<GeodesicSample> getPontosDentro() {
        return pontosDentro;
    }

}
