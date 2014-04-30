/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.models.Coleta;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author schettino
 */
public class ColetaDAO extends AbstractDAO<Coleta> {
    
    @Override
    public Coleta getFromResultSet(ResultSet rs) throws SQLException {
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        Coleta c = new Coleta();
        Date data_hora_inicio = null;
        Date data_hora_fim = null;
        try {
            data_hora_inicio = dt.parse(rs.getString("data_hora_inicio"));
        } catch (ParseException ex) {
        }
        try {
            data_hora_fim = dt.parse(rs.getString("data_hora_fim"));
        } catch (ParseException ex) {
        }
        int i = rs.getInt("id");
        int status = rs.getInt("status");
        String erros = rs.getString("erros");
        String filename = rs.getString("filename");
        
        c.setDataHoraInicio(data_hora_inicio);
        c.setDataHoraFim(data_hora_fim);
        c.setId(i);
        c.setStatus(status);
        c.setErros(erros);
        c.setFilename(filename);
        
        return c;
    }
    
    @Override
    public String getTableName() {
        return "coletas";
    }
    
    @Override
    public List<String> getAttributes() {
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("data_hora_inicio");
        attrs.add("data_hora_fim");
        attrs.add("status");
        attrs.add("erros");
        attrs.add("filename");
        return attrs;
    }
}
