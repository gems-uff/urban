/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.models.BusPosition;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.postgis.PGgeometry;
import org.postgis.Point;

/**
 *
 * @author schettino
 */
public class BusPositionDAO extends AbstractDAO<BusPosition> {

    @Override
    public BusPosition getFromResultSet(ResultSet rs) throws SQLException {

        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        BusPosition dado = new BusPosition();
        Date dataHora = null;
        try {
            dataHora = dt.parse(rs.getString("time"));
        } catch (ParseException ex) {
        }
        int i = rs.getInt("id");
        long coletaId = rs.getLong("loaded_file_id");
        long linhaId = rs.getLong("line_id");
        long onibusId = rs.getLong("bus_id");
        PGgeometry position = new PGgeometry(rs.getObject("position").toString());
//        float latitude = rs.getFloat("latitude");
//        float longitude = rs.getFloat("longitude");
        float speed = rs.getFloat("speed");

        dado.setTime(dataHora);
        dado.setId(i);
        dado.setLoadedFileId(coletaId);
        dado.setLineId(linhaId);
        dado.setBusId(onibusId);
        dado.setPosition(position);
        Point p = (Point) position.getGeometry();
        dado.setLatitude(p.y);
        dado.setLongitude(p.x);
        dado.setSpeed(speed);

        return dado;
    }

    @Override
    public String getTableName() {
        return "bus_positions";
    }

    @Override
    public List<String> getAttributes() {
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("time");
        attrs.add("loaded_file_id");
        attrs.add("line_id");
        attrs.add("bus_id");
        attrs.add("position");
        attrs.add("speed");
        return attrs;
    }

    public HashMap<String, List<BusPosition>> selectUltimasPosicoes() throws SQLException {
        HashMap<String, List<BusPosition>> map = new HashMap<String, List<BusPosition>>();

        String query = "SELECT * FROM ";
        query += getTableName();
        query += " d1 INNER JOIN (select di.bus_id, di.time as lastpositiontime "
                + " FROM bus_positions di GROUP BY di.bus_id, di.time order by di.time desc offset 11 limit 1) d2"
                + " ON (d1.time >= d2. lastPositionTime)"
                + " INNER JOIN buses o"
                + " ON (o.id = d1.bus_id)"
                + "ORDER BY d1.time DESC";

        ResultSet rs = stmt.executeQuery(query);
        String lastNumber = null;
        boolean getNextBus = true;
        List<BusPosition> busPositions = new ArrayList<BusPosition>();
        while (rs.next()) {
            String busNumber = rs.getString("bus_number");
            if (!(getNextBus && busNumber.equals(lastNumber))) {

                if (!busNumber.equals(lastNumber)) {
                    lastNumber = busNumber;
                    getNextBus = false;
                    map.put(busNumber, busPositions);
                    busPositions = new ArrayList<BusPosition>();
                }
                if (busPositions.size() < 10) {
                    busPositions.add(getFromResultSet(rs));
                } else {
                    map.put(busNumber, busPositions);
                    getNextBus = true;
                }
            }
        }
        return map;
    }
}
