/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.bus_data.models;

/**
 *
 * @author schettino
 */
public class Position implements Comparable<Position> {
    double lat;
    double lon;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Position(double la, double lo){
        this.lat = la;
        this.lon = lo;
    }

    @Override
    public int compareTo(Position o) {
        if((this.lat == o.lat) && (this.lon == o.lon)) return 0;
        return -1;
    }
}


