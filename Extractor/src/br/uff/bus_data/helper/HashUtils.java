/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

import br.uff.bus_data.dao.DadoRJDAO;
import br.uff.bus_data.dao.LinhaDAO;
import br.uff.bus_data.dao.OrdemDAO;
import br.uff.bus_data.models.DadoRJ;
import br.uff.bus_data.models.Linha;
import br.uff.bus_data.models.Ordem;
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
        LinhaDAO linhaDao = new LinhaDAO();
        linhaDao.setStatement(stmt);
        List<Linha> listLinhas = linhaDao.all();
        for (Linha linha : listLinhas) {
            linhasHash.put(linha.getNumero(), linha.getId());
        }
        return linhasHash;
    }

    public static Map<String, Long> loadOrdens(Statement stmt) throws SQLException {
        Map<String, Long> ordensHash = new HashMap<String, Long>();
        OrdemDAO ordemDao = new OrdemDAO();
        ordemDao.setStatement(stmt);
        List<Ordem> listOrdens = ordemDao.all();
        for (Ordem ordem : listOrdens) {
            ordensHash.put(ordem.getNumero(), ordem.getId());
        }
        return ordensHash;
    }
    
    public static Map<String, DadoRJ> loadPosicoes(Statement stmt) throws SQLException {
        DadoRJDAO dadoRJDao = new DadoRJDAO();
        dadoRJDao.setStatement(stmt);
        return dadoRJDao.selectUltimasPosicoes();
    }

}


