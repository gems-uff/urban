/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.models.Mappable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author schettino
 * @param <T>
 */
public abstract class AbstractDAO<T extends Mappable<String, String>> {

    protected List<T> list;
    protected Statement stmt;

    public abstract T getFromResultSet(ResultSet rs) throws SQLException;

    public abstract String getTableName();

    public abstract List<String> getAttributes();

    public List<Long> insert(List<Map<String, String>> params) throws SQLException {
        String attributes = "(";
        List<String> attrs = getAttributes();
        for (String attr : attrs) {
            attributes += attr + " , ";
        }
        attributes += "created_at , updated_at)";

        String query = "INSERT IGNORE INTO " + getTableName() + " " + attributes + " VALUES ";
        String values = "(";
        for (Map<String, String> map : params) {
            values = "(";
            for (String attr : attrs) {
                values += map.get(attr) + " , ";
            }
            Date d = new Date();
            SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
            values += "'" + dt.format(d) + "'" + " , " + "'" + dt.format(d) + "' ),";
        }
        values = values.substring(0, values.lastIndexOf(","));
        query += values;
        stmt.executeUpdate(query, new String[] {"id"});
        ResultSet rs = stmt.getGeneratedKeys();
        ArrayList<Long> ids = new ArrayList<Long>();
        while (rs.next()) {
            ids.add(rs.getLong(1));
        }
        return ids;
    }

    public Long insert(Map<String, String> params) throws SQLException {
        String attributes = "(";
        List<String> attrs = getAttributes();
        for (String attr : attrs) {
            attributes += attr + " , ";
        }
        attributes += "created_at , updated_at)";

        String query = "INSERT IGNORE INTO " + getTableName() + " " + attributes + " VALUES ";
        String values = "(";
        for (String attr : attrs) {
            values += params.get(attr) + " , ";
        }
        Date d = new Date();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        values += "'" + dt.format(d) + "'" + " , " + "'" + dt.format(d) + "' ),";
        values = values.substring(0, values.lastIndexOf(","));
        query += values;
        int size = stmt.executeUpdate(query, new String[] {"id"});
        ResultSet rs = stmt.getGeneratedKeys();
        if (size != 1) {
            return null;
        }
        rs.next();
        return rs.getLong(1);
    }

    public void update(T object) throws SQLException {
        String query = "UPDATE " + getTableName() + " SET ";
        Map<String, String> params = object.getMap();
        for (String c : params.keySet()) {
            query += " " + c + " = " + params.get(c) + ",";
        }
        Date d = new Date();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        query += "updated_at = '" + dt.format(d) + "'";
        query += " WHERE id = " + params.get("id");
        stmt.executeUpdate(query);
    }

    public void update(Map<String, String> params, Long id) throws SQLException {
        String query = "UPDATE " + getTableName() + " SET ";
        for (String c : params.keySet()) {
            query += " " + c + " = " + params.get(c) + ",";
        }
        Date d = new Date();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        query += "updated_at = '" + dt.format(d) + "'";
        query += " WHERE id = " + id;
        stmt.executeUpdate(query);
    }

    public List<T> select(HashMap<String, String> params) throws SQLException {
        list = new ArrayList<T>();
        String query = "SELECT * FROM ";
        query += getTableName();

        if (!params.isEmpty()) {
            query += " WHERE";
            for (String c : params.keySet()) {
                if (params.get(c).equals("null")) {
                    query += " " + c + " is NULL AND";
                } else {
                    query += " " + c + " = " + params.get(c) + " AND";
                }
            }
            query = query.substring(0, query.lastIndexOf("AND"));
        }

        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            list.add(getFromResultSet(rs));
        }
        return list;
    }

    public List<T> all() throws SQLException {
        list = new ArrayList<T>();
        String query = "SELECT * FROM ";
        query += getTableName();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            list.add(getFromResultSet(rs));
        }
        return list;
    }

    public void setStatement(Statement st) {
        this.stmt = st;
    }
}


