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
import org.postgis.PGgeometry;
import org.postgis.Point;

/**
 *
 * @author schettino
 */
public class Disposal implements Mappable<String, String> {

    Long id;
    Long loadedFileId;
    Long lineId;
    Long busId;
    Long lastPositionId;
    Date time;
    Double latitude;
    Double longitude;
    PGgeometry position;
    Float speed;
    String disposalReason;

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Position getLonLatPosition() {
        Position p = new Position(this.latitude, this.longitude);
        return p;
    }

    public Long getLoadedFileId() {
        return loadedFileId;
    }

    public void setLoadedFileId(Long loadedFileId) {
        this.loadedFileId = loadedFileId;
    }

    public Long getLineId() {
        return lineId;
    }

    public void setLineId(Long lineId) {
        this.lineId = lineId;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public Long getLastPositionId() {
        return lastPositionId;
    }

    public void setLastPositionId(Long lastPositionId) {
        this.lastPositionId = lastPositionId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public String getDisposalReason() {
        return disposalReason;
    }

    public void setDisposalReason(String disposalReason) {
        this.disposalReason = disposalReason;
    }

    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        map.put("id", String.valueOf(this.id));
        map.put("loaded_file_id", String.valueOf(this.loadedFileId));
        map.put("line_id", String.valueOf(this.lineId));
        map.put("last_postion_id", String.valueOf(this.lastPositionId));
        map.put("bus_id", String.valueOf(this.busId));
        Point p = new Point(this.longitude, this.latitude);
        p.setSrid(4326);
        map.put("position", "'" + String.valueOf(p.toString()) + "'");
        map.put("speed", String.valueOf(this.speed));
        map.put("time", "'" + dt.format(this.time) + "'");
        map.put("disposal_reason", "'" + this.disposalReason + "'");
        return map;
    }

    @Override
    public String toString() {
        return "Latitude: " + this.latitude + "; Longitude: " + this.longitude
                + "; Speed: " + this.speed + "; Time: "
                + this.time + "; Bus: " + this.busId + "; Line: "
                + this.lineId + "; Loaded File: " + this.loadedFileId + "; Last Position: "
                + this.lastPositionId + "; Disposal Reason: " + this.disposalReason;
    }
}
