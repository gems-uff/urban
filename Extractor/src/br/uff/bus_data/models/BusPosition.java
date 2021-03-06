/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.models;

import br.uff.bus_data.ImportBusPositions;
import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.helper.DateHelper;
import br.uff.bus_data.helper.LatLongConvertion;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgis.PGgeometry;
import org.postgis.Point;

/**
 *
 * @author schettino
 */
public class BusPosition implements Comparable<BusPosition>, Mappable<String, String> {

    Long id;
    Long loadedFileId;
    Long lineId;
    Long busId;
    Date time;
    Double latitude;
    Double longitude;
    PGgeometry position;
    Float speed;

    @Override
    public int compareTo(BusPosition o) {
        if ((this.time.compareTo(o.time) == 0) && (this.busId == o.busId)) {
            return 0;
        }
        return -1;
    }

    @Override
    public Map<String, String> getMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        map.put("id", String.valueOf(this.id));
        map.put("loaded_file_id", String.valueOf(this.loadedFileId));
        map.put("line_id", String.valueOf(this.lineId));
        map.put("bus_id", String.valueOf(this.busId));
        Point p = new Point(this.longitude, this.latitude);
        p.setSrid(4326);
        map.put("position", "'" + String.valueOf(p.toString()) + "'");
        map.put("speed", String.valueOf(this.speed));
        map.put("time", "'" + dt.format(this.time) + "'");
        return map;
    }

    public static BusPosition fromJsonFile(ArrayList<Object> params, Long linhaId, Long onibusId, Long coletaId) {
        BusPosition dado = new BusPosition();
        dado.loadedFileId = coletaId;
        dado.busId = onibusId;
        dado.lineId = linhaId;
        SimpleDateFormat dt = new SimpleDateFormat(Constants.JSON_DATE_FORMAT);
        String data = String.valueOf(params.get(Constants.INDEX_TIME));
        Date date = null;
        if (!data.isEmpty()) {
            try {
                date = dt.parse(data);
            } catch (Exception ex) {
                Logger.getLogger(ImportBusPositions.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        dado.time = date;
        dado.latitude = Double.valueOf(String.valueOf(params.get(Constants.INDEX_LATITUDE)));
        dado.longitude = Double.valueOf(String.valueOf(params.get(Constants.INDEX_LONGITUDE)));
        try {
            dado.speed = Float.valueOf(String.valueOf(params.get(Constants.INDEX_SPEED)));
        } catch (java.lang.IndexOutOfBoundsException ex) {
            Logger.getLogger(ImportBusPositions.class.getName()).log(Level.SEVERE, null, ex);
        }
        Point p = new Point(dado.longitude, dado.latitude);
        p.setSrid(4326);
        dado.position = new PGgeometry(p);
        return dado;
    }

    public void clear() {
        this.id = null;
        this.loadedFileId = null;
        this.lineId = null;
        this.busId = null;
        this.time = null;
        this.latitude = null;
        this.longitude = null;
        this.position = null;
        this.speed = null;
    }

    public boolean empty() {
        return ((id == null) && (loadedFileId == null) && (lineId == null)
                && (busId == null) && (time == null) && (latitude == null)
                && (longitude == null) && (position == null) && (speed == null));
    }

    @Override
    public String toString() {
        return "Latitude: " + this.latitude + "; Longitude: " + this.longitude
                + "; Speed: " + this.speed + "; Time: "
                + this.time + "; Bus: " + this.busId + "; Line: "
                + this.lineId + "; Loaded File: " + this.loadedFileId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public PGgeometry getPosition() {
        return position;
    }

    public void setPosition(PGgeometry position) {
        this.position = position;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Position getLatLongPos() {
        Point p = (Point) this.position.getGeometry();
        Position pos = new Position(p.x, p.y);
        return pos;
    }
}
