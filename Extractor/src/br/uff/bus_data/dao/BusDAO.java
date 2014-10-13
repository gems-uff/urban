/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.models.Bus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author schettino
 */
public class BusDAO extends AbstractDAO<Bus> {

    @Override
    public Bus getFromResultSet(ResultSet rs) throws SQLException {
        String num = rs.getString("bus_number");
        int i = rs.getInt("id");
        return new Bus(num, i);
    }
    
    
    @Override
    public  String getTableName() {
        return "buses";
    }
    
    @Override
    public  List<String> getAttributes(){
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("bus_number");
        return attrs;
    }
}


