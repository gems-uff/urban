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
    float lat;
    float lon;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public Position(float la, float lo){
        this.lat = la;
        this.lon = lo;
    }

    @Override
    public int compareTo(Position o) {
        if((this.lat == o.lat) && (this.lon == o.lon)) return 0;
        return -1;
    }
}
