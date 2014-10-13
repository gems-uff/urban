/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbHelpers;

import br.uff.bus_data.dao.LineDAO;
import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.models.Line;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class LineDBUtils {

    public static Long findOrInsertLine(LineDAO lineDao, String line, Map<String, Long> linesHash) throws SQLException {
        HashMap<String, String> params = new HashMap<String, String>();

        Long lineId = null;

        if (!line.isEmpty()) {
            line = line.split("\\.")[0];
            lineId = linesHash.get(line);
            if (lineId == null) {
                params.put("line_number", "'" + line + "'");
                lineId = lineDao.insert(params);
                linesHash.put(line, lineId);
            }
        }
        return lineId;
    }

}
