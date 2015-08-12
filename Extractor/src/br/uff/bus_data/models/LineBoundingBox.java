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
public class LineBoundingBox {

    private long lineId;
    private double minLat;
    private double maxLat;
    private double minLong;
    private double maxLong;

    public LineBoundingBox() {
    }

    
    public LineBoundingBox(long number, long minLat, long maxLat, long minLong,long maxLong) {
        this.lineId = number;
        this.minLat = minLat;
        this.maxLat = maxLat;
        this.minLong = minLong;
        this.maxLong = maxLong;
    }
    
    public boolean isInside(BusPosition bp){
        return ((bp.getLatitude() > minLat) && (bp.getLatitude() < maxLat) && (bp.getLongitude() > minLong) && (bp.getLongitude() < maxLong));
    }

    public long getLineId() {
        return lineId;
    }

    public void setLineId(long lineId) {
        this.lineId = lineId;
    }

    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }

    public double getMinLong() {
        return minLong;
    }

    public void setMinLong(double minLong) {
        this.minLong = minLong;
    }

    public double getMaxLong() {
        return maxLong;
    }

    public void setMaxLong(double maxLong) {
        this.maxLong = maxLong;
    }

}


