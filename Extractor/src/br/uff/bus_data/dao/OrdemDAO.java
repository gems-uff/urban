/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.models.Ordem;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author schettino
 */
public class OrdemDAO extends AbstractDAO<Ordem> {

    @Override
    public Ordem getFromResultSet(ResultSet rs) throws SQLException {
        String num = rs.getString("ordem");
        int i = rs.getInt("id");
        return new Ordem(num, i);
    }
    
    
    @Override
    public  String getTableName() {
        return "ordens";
    }
    
    @Override
    public  List<String> getAttributes(){
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("ordem");
        return attrs;
    }
}
