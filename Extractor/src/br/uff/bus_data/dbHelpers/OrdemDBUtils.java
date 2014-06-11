/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbHelpers;

import br.uff.bus_data.dao.OrdemDAO;
import br.uff.bus_data.helper.Constants;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class OrdemDBUtils {

    public static Long insereOrdem(OrdemDAO dao, String ordem, Map<String, Long> ordensHash) throws SQLException {
        HashMap<String, String> params = new HashMap<String, String>();

        Long ordemId = null;

        if (!ordem.isEmpty()) {
            ordemId = ordensHash.get(ordem);
            if (ordemId == null) {
                params.clear();
                params.put(Constants.KEY_ORDEM, "'" + ordem + "'");
                ordemId = dao.insert(params);
                ordensHash.put(ordem, ordemId);
            }

        }
        return ordemId;
    }
}
