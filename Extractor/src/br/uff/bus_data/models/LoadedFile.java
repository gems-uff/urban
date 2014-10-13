/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.models;

import br.uff.bus_data.helper.Constants;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class LoadedFile implements Comparable<LoadedFile>, Mappable<String, String> {

    long id;
    int status;
    int type;
    Date endTime;
    Date startTime;
    String errors;
    String filename;
    
    public static final int TYPE_BUS_POSITIONS = 0;
    public static final int TYPE_LINE_STOPS = 1;
    public static final int TYPE_LINE_POSITIONS = 2;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    @Override
    public int compareTo(LoadedFile o) {
        if ((this.startTime.compareTo(o.startTime) == 0) && (this.filename.equals(o.filename))) {
            return 0;
        }
        return -1;
    }

    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        map.put("id", String.valueOf(this.id));
        map.put("status", String.valueOf(this.status));
        map.put("type", String.valueOf(this.type));
        map.put("end_time", "'" + dt.format(this.endTime) + "'");
        map.put("start_time", "'" + dt.format(this.startTime) + "'");
        map.put("errors", "'" + errors + "'");
        map.put("filename", "'" + filename + "'");
        return map;
    }
}


