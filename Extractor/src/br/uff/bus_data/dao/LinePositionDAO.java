/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.models.LinePosition;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.postgis.PGgeometry;

/**
 *
 * @author schettino
 */
public class LinePositionDAO extends AbstractDAO<LinePosition> {

    @Override
    public LinePosition getFromResultSet(ResultSet rs) throws SQLException {

        LinePosition linePosition = new LinePosition();

        long i = rs.getLong("id");
        long lineId = rs.getLong("line_id");
        long loadedFileId = rs.getLong("loaded_file_id");
        long shapeId = rs.getLong("shape_id");
        int sequenceNumber = rs.getInt("sequence_number");
        PGgeometry position =  new PGgeometry(rs.getObject("position").toString());
        String description = rs.getString("description");
        String company = rs.getString("company");
        

        linePosition.setId(i);
        linePosition.setLineId(lineId);
        linePosition.setSequenceNumber(sequenceNumber);
        linePosition.setPosition(position);
        linePosition.setDescription(description);
        linePosition.setCompany(company);
        linePosition.setShapeId(shapeId);
        linePosition.setLoadedFileId(loadedFileId);

        return linePosition;
    }

    @Override
    public String getTableName() {
        return "line_positions";
    }

    @Override
    public List<String> getAttributes() {
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("line_id");
        attrs.add("shape_id");
        attrs.add("loaded_file_id");
        attrs.add("sequence_number");
        attrs.add("position");
        attrs.add("description");
        attrs.add("company");
        return attrs;
    }

}