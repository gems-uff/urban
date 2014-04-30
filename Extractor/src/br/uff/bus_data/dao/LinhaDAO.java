/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.models.Linha;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author schettino
 */
public class LinhaDAO extends AbstractDAO<Linha> {

    @Override
    public Linha getFromResultSet(ResultSet rs) throws SQLException {
        String num = rs.getString("linha");
        int i = rs.getInt("id");
        return new Linha(num, i);
    }
    
    
    @Override
    public  String getTableName() {
        return "linhas";
    }
    
    @Override
    public  List<String> getAttributes(){
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("linha");
        return attrs;
    }
}
