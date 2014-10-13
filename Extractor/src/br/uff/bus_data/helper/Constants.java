/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

/**
 *
 * @author schettino
 */
public class Constants {

    public static final int INDEX_TIME = 0;
    public static final int INDEX_BUS_NUMBER = 1;
    public static final int INDEX_LINE = 2;
    public static final int INDEX_LATITUDE = 3;
    public static final int INDEX_LONGITUDE = 4;
    public static final int INDEX_SPEED = 5;

    public static final String KEY_TIME = "time";
    public static final String KEY_BUS_NUMBER = "bus_number";
    public static final String KEY_LINE = "line";
    public static final String KEY_POSITION = "position";
    public static final String KEY_SPEED = "speed";

    public static final String[] COLUMNS = {"DATAHORA", "ORDEM", "LINHA",
        "LATITUDE", "LONGITUDE", "VELOCIDADE"};

    public static final int STATUS_STARTED = 1;
    public static final int STATUS_FINISHED_SUCCESSFULLY = 2;
    public static final int STATUS_FINISHED_WITH_ERRORS = 3;

    public static final String MSG_ERROR_COLUMNS = "'File columns in invalid format'";

    public static final String KEY_COLUMNS = "COLUMNS";
    public static final String KEY_DATA = "DATA";
    
    public static final String DB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String JSON_DATE_FORMAT = "MM-dd-yyyy HH:mm:ss";

}


