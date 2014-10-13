/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.models.Line;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author schettino
 */
public class LineDAO extends AbstractDAO<Line> {

    @Override
    public Line getFromResultSet(ResultSet rs) throws SQLException {
        String num = rs.getString("line_number");
        int i = rs.getInt("id");
        return new Line(num, i);
    }
    
    
    @Override
    public  String getTableName() {
        return "lines";
    }
    
    @Override
    public  List<String> getAttributes(){
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("line_number");
        return attrs;
    }
}


