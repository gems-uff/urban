/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.models.LoadedFile;
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
public class LoadedFileDAO extends AbstractDAO<LoadedFile> {
    
    @Override public LoadedFile getFromResultSet(ResultSet rs) throws SQLException {
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);   
        LoadedFile uf = new LoadedFile();
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = dt.parse(rs.getString("start_time"));
        } catch (ParseException ex) {
        }
        try {
            endTime = dt.parse(rs.getString("end_time"));
        } catch (ParseException ex) {
        }
        int i = rs.getInt("id");
        int status = rs.getInt("status");
        int type = rs.getInt("type");
        String errors = rs.getString("errors");
        String filename = rs.getString("filename");
        
        uf.setStartTime(startTime);
        uf.setEndTime(endTime);
        uf.setId(i);
        uf.setType(type);
        uf.setStatus(status);
        uf.setErrors(errors);
        uf.setFilename(filename);
        
        return uf;
    }
    
    @Override
    public String getTableName() {
        return "loaded_files";
    }
    
    @Override
    public List<String> getAttributes() {
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("start_time");
        attrs.add("end_time");
        attrs.add("status");
        attrs.add("type");
        attrs.add("errors");
        attrs.add("filename");
        return attrs;
    }
}


