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
public class Line implements Comparable<Line>, Mappable<String,String>{

    String lineNumber;
    long id;

    public Line(String number, int id) {
        this.lineNumber = number;
        this.id = id;
    }

    public String getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(String lineNumber) {
        this.lineNumber = lineNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    

    @Override
    public int compareTo(Line o) {
        if (this.lineNumber.equals(o.lineNumber)) {
            return 0;
        }
        return -1;
    }

    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(this.id));
        map.put("line_number", "'"+ this.lineNumber + "'");
        return map;
    }

}


