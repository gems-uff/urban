/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.models.Disposal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.postgis.PGgeometry;

/**
 *
 * @author schettino
 */
public class DisposalDAO extends AbstractDAO<Disposal> {

    @Override
    public Disposal getFromResultSet(ResultSet rs) throws SQLException {

        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        Disposal disposal = new Disposal();
        Date time = null;
        try {
            time = dt.parse(rs.getString("time"));
        } catch (ParseException ex) {
        }
        int i = rs.getInt("id");
        long loadedFileId = rs.getLong("loaded_file_id");
        long lineId = rs.getLong("line_id");
        long busId = rs.getLong("bus_id");
        long lastPositionId = rs.getLong("last_postion_id");
        PGgeometry position =  new PGgeometry(rs.getObject("position").toString());
        float speed = rs.getFloat("speed");
        Long disposal_reason = rs.getLong("disposal_reason_id");

        disposal.setTime(time);
        disposal.setId(i);
        disposal.setLoadedFileId(loadedFileId);
        disposal.setLineId(lineId);
        disposal.setBusId(busId);
        disposal.setPosition(position);
        disposal.setSpeed(speed);
        disposal.setLastPositionId(lastPositionId);
        disposal.setDisposalReasonId(disposal_reason);

        return disposal;
    }

    @Override
    public String getTableName() {
        return "disposals";
    }

    @Override
    public List<String> getAttributes() {
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("time");
        attrs.add("loaded_file_id");
        attrs.add("line_id");
        attrs.add("bus_id");
        attrs.add("last_postion_id");
        attrs.add("position");
//        attrs.add("latitude");
//        attrs.add("longitude");
        attrs.add("speed");
        attrs.add("disposal_reason_id");
        return attrs;
    }

}


