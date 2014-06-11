/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbHelpers;

import br.uff.bus_data.Main;
import br.uff.bus_data.helper.Constants;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author schettino
 */
public class DadoRJDBUtils {

    public static HashMap<String, String> geraParams(List<Object> dado, Long linhaId, Long ordemId, Long coletaId) throws SQLException {
        HashMap<String, String> params = new HashMap<String, String>();

        SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String data = String.valueOf(dado.get(Constants.INDEX_DATA_HORA));
        if (!data.isEmpty()) {
            try {
                Date date = dt.parse(data);
                data = dt2.format(date);
            } catch (java.text.ParseException ex) {
                Logger.getLogger(DadoRJDBUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        params.put("linha_id", String.valueOf(linhaId));
        params.put("ordem_id", String.valueOf(ordemId));
        params.put("coleta_id", String.valueOf(coletaId));
        params.put("data_hora", "'" + data + "'");
        params.put("latitude", String.valueOf(dado.get(Constants.INDEX_LATITUDE)));
        params.put("longitude", String.valueOf(dado.get(Constants.INDEX_LONGITUDE)));
        params.put("velocidade", String.valueOf(dado.get(Constants.INDEX_VELOCIDADE)));
        return params;
    }
}
