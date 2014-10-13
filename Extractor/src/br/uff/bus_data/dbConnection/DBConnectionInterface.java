/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.uff.bus_data.dbConnection;

import java.sql.Connection;

/**
 *
 * @author schettino
 */
public interface DBConnectionInterface {
    public String getJDBC();
    public String getDatabase();
    public String getUser();
    public String getPassword();
    public String getAdapterClassName();
    public Connection dbConection();
}
