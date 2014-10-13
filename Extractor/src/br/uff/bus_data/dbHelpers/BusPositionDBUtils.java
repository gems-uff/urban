/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbHelpers;

import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.models.BusPosition;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

/**
 *
 * @author schettino
 */
public class BusPositionDBUtils {

    public static HashMap<String, String> generateParams(BusPosition busPosition) throws SQLException {
        HashMap<String, String> params = new HashMap<String, String>();

        SimpleDateFormat dt = new SimpleDateFormat(Constants.JSON_DATE_FORMAT);
        SimpleDateFormat dt2 = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        String date = String.valueOf(busPosition.getTime());
        params.put("line_id", String.valueOf(busPosition.getLineId()));
        params.put("bus_id", String.valueOf(busPosition.getBusId()));
        params.put("loaded_file_id", String.valueOf(busPosition.getLoadedFileId()));
        params.put("time", "'" + date + "'");
        params.put("position", String.valueOf("'" + busPosition.getPosition()+ "'"));
        params.put("speed", String.valueOf(busPosition.getSpeed()));
        return params;
    }
}
