/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbHelpers;

import br.uff.bus_data.dao.LinhaDAO;
import br.uff.bus_data.helper.Constants;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class LinhaDBUtils {

    public static Long insereLinha(LinhaDAO dao, String linha, Map<String, Long> linhasHash) throws SQLException {
        HashMap<String, String> params = new HashMap<String, String>();

        Long linhaId = null;

        if (!linha.isEmpty()) {
            linha = linha.split("\\.")[0];
            linhaId = linhasHash.get(linha);
            if (linhaId == null) {
                params.clear();
                params.put(Constants.KEY_LINHA, "'" + linha + "'");
                linhaId = dao.insert(params);
                linhasHash.put(linha, linhaId);
            }
        }
        return linhaId;
    }
}
