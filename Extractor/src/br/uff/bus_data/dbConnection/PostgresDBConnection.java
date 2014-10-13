/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author schettino
 */
public class PostgresDBConnection implements DBConnectionInterface {

    private static final String JDBC = "postgresql";
    private static final String DATABASE = "//localhost:5432/bus_data2_development";
    private static final String USER = "schettino";
    private static final String PASSWORD = "";
    private static final String ADAPTER_CLASS_NAME = "com.mysql.jdbc.Driver";

    @Override
    public String getJDBC() {
        return JDBC;
    }

    @Override
    public String getDatabase() {
        return DATABASE;
    }

    @Override
    public String getUser() {
        return USER;
    }

    @Override
    public String getPassword() {
        return PASSWORD;
    }

    @Override
    public String getAdapterClassName() {
        return ADAPTER_CLASS_NAME;
    }

    @Override
    public Connection dbConection() {
        try {
            Class.forName(this.getAdapterClassName());
            String connectionUrl = "jdbc:" + this.getJDBC() + ":" + this.getDatabase();

            Connection con = DriverManager.getConnection(connectionUrl, this.getUser(),
                    this.getPassword());
            ((org.postgresql.jdbc4.Jdbc4Connection) con).addDataType("geometry", "org.postgis.PGgeometry");
            return con;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MySQLDBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDBConnection.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
