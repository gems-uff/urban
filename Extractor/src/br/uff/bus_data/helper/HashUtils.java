/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

import br.uff.bus_data.dao.BusPositionDAO;
import br.uff.bus_data.dao.LineDAO;
import br.uff.bus_data.dao.BusDAO;
import br.uff.bus_data.models.BusPosition;
import br.uff.bus_data.models.Line;
import br.uff.bus_data.models.Bus;
import br.uff.bus_data.models.Position;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class HashUtils {

    public static Map<String, Long> loadLinhas(Statement stmt) throws SQLException {
        Map<String, Long> linhasHash = new HashMap<String, Long>();
        LineDAO linhaDao = new LineDAO();
        linhaDao.setStatement(stmt);
        List<Line> listLinhas = linhaDao.all();
        for (Line linha : listLinhas) {
            linhasHash.put(linha.getLineNumber(), linha.getId());
        }
        return linhasHash;
    }

    public static Map<String, Long> loadOrdens(Statement stmt) throws SQLException {
        Map<String, Long> ordensHash = new HashMap<String, Long>();
        BusDAO onibusDao = new BusDAO();
        onibusDao.setStatement(stmt);
        List<Bus> listOnibus = onibusDao.all();
        for (Bus onibus : listOnibus) {
            ordensHash.put(onibus.getBusNumber(), onibus.getId());
        }
        return ordensHash;
    }

    public static Map<String, BusPosition> loadPosicoes(Statement stmt) throws SQLException {
        BusPositionDAO busPositionDao = new BusPositionDAO();
        busPositionDao.setStatement(stmt);
        return busPositionDao.selectUltimasPosicoes();
    }

}
