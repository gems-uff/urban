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
public class MySQLDBConnection implements DBConnectionInterface {

    private static final String JDBC = "mysql";
    private static final String DATABASE = "//localhost/bus_data_development6";
    private static final String USER = "root";
    private static final String PASSWORD = "tzzk7662";
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
            String connectionUrl = "jdbc:" + this.getJDBC() + ":" + this.getDatabase() + "?"
                    + "user=" + this.getUser() + "&password=" + this.getPassword();
            Connection con = DriverManager.getConnection(connectionUrl);
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
