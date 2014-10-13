/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.models.LineStop;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.postgis.PGgeometry;

/**
 *
 * @author schettino
 */
public class LineStopDAO extends AbstractDAO<LineStop> {

    @Override
    public LineStop getFromResultSet(ResultSet rs) throws SQLException {

        LineStop lineStop = new LineStop();

        long i = rs.getLong("id");
        long lineId = rs.getLong("line_id");
        long loadedeFileId = rs.getLong("loaded_file_id");
        int sequenceNumber = rs.getInt("sequence_number");
        PGgeometry position =  new PGgeometry(rs.getObject("position").toString());
        String description = rs.getString("description");
        String company = rs.getString("company");
        

        lineStop.setId(i);
        lineStop.setLineId(lineId);
        lineStop.setSequenceNumber(sequenceNumber);
        lineStop.setPosition(position);
        lineStop.setDescription(description);
        lineStop.setCompany(company);
        lineStop.setLoadedFileId(loadedeFileId);

        return lineStop;
    }

    @Override
    public String getTableName() {
        return "line_stops";
    }

    @Override
    public List<String> getAttributes() {
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("line_id");
        attrs.add("loaded_file_id");
        attrs.add("sequence_number");
        attrs.add("position");
        attrs.add("description");
        attrs.add("company");
        return attrs;
    }

}


