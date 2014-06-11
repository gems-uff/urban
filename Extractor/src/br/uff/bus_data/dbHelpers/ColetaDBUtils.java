/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbHelpers;

import br.uff.bus_data.dao.ColetaDAO;
import br.uff.bus_data.helper.Constants;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class ColetaDBUtils {

    public static Map<String, String> insertDefaultParams(String filename) {
        Map<String, String> params = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date();

        params.put("data_hora_inicio", "'" + dt.format(d) + "'");
        params.put("data_hora_fim", "NULL");
        params.put("erros", "NULL");
        params.put("filename", "'" + filename + "'");
        params.put("status", String.valueOf(Constants.STATUS_INICIADA));
        return params;
    }

    public static void finalizaColetaComSucesso(ColetaDAO dao, Long coletaId) throws SQLException {
        Map<String, String> params = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date();
        params.put("data_hora_fim", "'" + dt.format(d) + "'");
        params.put("status", String.valueOf(Constants.STATUS_ENCERRADA_COM_SUCESSO));
        dao.update(params, coletaId);
    }

    public static void finalizaColetaComErros(ColetaDAO dao, Long coletaId, String erros) throws SQLException {
        Map<String, String> params = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date();
        params.put("data_hora_fim", "'" + dt.format(d) + "'");
        params.put("status", String.valueOf(Constants.STATUS_ENCERRADA_COM_ERROS));
        params.put("erros", erros);
        dao.update(params, coletaId);
    }

}


