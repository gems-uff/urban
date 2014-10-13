/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbHelpers;

import br.uff.bus_data.dao.BusDAO;
import br.uff.bus_data.helper.Constants;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class BusDBUtils {

    public static Long insertBus(BusDAO dao, String busNumber, Map<String, Long> busesHash) throws SQLException {
        HashMap<String, String> params = new HashMap<String, String>();

        Long busId = null;

        if (!busNumber.isEmpty()) {
            busId = busesHash.get(busNumber);
            if (busId == null) {
                params.clear();
                params.put(Constants.KEY_BUS_NUMBER, "'" + busNumber + "'");
                busId = dao.insert(params);
                busesHash.put(busNumber, busId);
            }

        }
        return busId;
    }
}
