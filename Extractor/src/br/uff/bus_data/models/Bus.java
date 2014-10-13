/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.models;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class Bus implements Comparable<Bus>, Mappable<String, String> {

    String busNumber;
    long id;

    public Bus(String number, int id) {
        this.busNumber = number;
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    @Override
    public int compareTo(Bus o) {
        if (this.busNumber.equals(o.busNumber)) {
            return 0;
        }
        return -1;
    }

    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(this.id));
        map.put("bus_number", "'" + this.busNumber+ "'");
        return map;
    }
}


