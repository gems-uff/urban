/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.models.LineBoundingBox;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class LineBoundingBoxDAO {

    protected Statement stmt;

    public LineBoundingBox getFromResultSet(ResultSet rs) throws SQLException {

        LineBoundingBox lineBoundingBox = new LineBoundingBox();

        long lineNumber = rs.getLong("lineId");
        double minLat = rs.getDouble("minLat");
        double maxLat = rs.getDouble("maxLat");
        double minLong = rs.getDouble("minLong");
        double maxLong = rs.getDouble("maxLong");

        lineBoundingBox.setLineId(lineNumber);
        lineBoundingBox.setMinLat(minLat);
        lineBoundingBox.setMinLong(minLong);
        lineBoundingBox.setMaxLat(maxLat);
        lineBoundingBox.setMaxLong(maxLong);

        return lineBoundingBox;
    }

    public Map<Long, LineBoundingBox> all() throws SQLException {
        Map<Long, LineBoundingBox> map = new HashMap<Long, LineBoundingBox>();
        String query = "select (max(st_ymax(ST_GeomFromEWKB(position))) - 0.001) as maxLat, "
                + "(min(st_ymin(ST_GeomFromEWKB(position))) - 0.001) as minLat, "
                + "(max(st_xmax(ST_GeomFromEWKB(position))) - 0.001) as maxLong, "
                + "(min(st_xmin(ST_GeomFromEWKB(position))) - 0.001) as minLong, "
                + "lp.line_id as lineId "
                + "from line_positions lp "
                + "group by lp.line_id";

        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            LineBoundingBox lbb = getFromResultSet(rs);
            map.put(lbb.getLineId(), lbb);
        }
        return map;
    }

    public void setStatement(Statement st) {
        this.stmt = st;
    }

}
