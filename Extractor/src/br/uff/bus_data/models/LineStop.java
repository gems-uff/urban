/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.models;

import java.util.HashMap;
import java.util.Map;
import org.postgis.PGgeometry;
import org.postgis.Point;

/**
 *
 * @author schettino
 */
public class LineStop implements Mappable<String, String> {

    Long id;
    int sequenceNumber;
    Long lineId;
    Long loadedFileId;
    Double latitude;
    Double longitude;
    PGgeometry position;
    String description;
    String company;

    public Long getLoadedFileId() {
        return loadedFileId;
    }

    public void setLoadedFileId(Long loadedFileId) {
        this.loadedFileId = loadedFileId;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public PGgeometry getPosition() {
        return position;
    }

    public void setPosition(PGgeometry position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(this.id));
        map.put("sequence_number", String.valueOf(this.sequenceNumber));
        map.put("line_id", String.valueOf(this.lineId));
        map.put("loaded_file_id", String.valueOf(this.loadedFileId));
        Point p = new Point(this.longitude, this.latitude);
        p.setSrid(4326);
        map.put("position", "'" + String.valueOf(p.toString()) + "'");
        map.put("description", "'" + this.description + "'");
        map.put("company", "'" + this.company + "'");
        return map;
    }
}
