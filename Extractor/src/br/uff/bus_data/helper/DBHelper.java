/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class DBHelper {

    private static final String SQL_SELECT = "SELECT * FROM ";
    private static final String SQL_INSERT_DADOS = "INSERT INTO dados_rj (coleta_id, created_at, data_hora, latitude, linha_id, longitude, ordem_id, updated_at, velocidade) VALUES (";
    private static final String SQL_INSERT_LINHAS = "INSERT INTO `linhas` (`created_at`, `linha`, `updated_at`) VALUES (";
//    INSERT INTO `linhas` (`created_at`, `linha`, `updated_at`) VALUES ('2014-04-23 17:40:12', 485.0, '2014-04-23 17:40:12')
    private static final String SQL_INSERT_ORDENS = "INSERT INTO `ordens` (`created_at`, `ordem`, `updated_at`) VALUES (";
//     INSERT INTO `ordens` (`created_at`, `ordem`, `updated_at`) VALUES ('2014-04-23 17:40:12', 'B75633', '2014-04-23 17:40:12')
    private static final String SQL_INSERT_COLETAS = "INSERT INTO `coletas` (`created_at`, `data_hora_fim`, `data_hora_inicio`, `erros`, `status`, `updated_at`) VALUES (";
//    INSERT INTO `coletas` (`created_at`, `data_hora_fim`, `data_hora_inicio`, `erros`, `status`, `updated_at`) VALUES ('2014-04-23 17:40:10', NULL, '2014-04-23 17:40:10', NULL, 1, '2014-04-23 17:40:10')
    private static final String SQL_UPDATE_COLETAS = "UPDATE coletas SET";
    private static final String SQL_NULL = "NULL";

    public static String queryInsertDados(Map<String, String> params) {
        String query = SQL_INSERT_DADOS;
        query += params.get("coleta_id") + "," + params.get("created_at") + ","
                + params.get("data_hora") + "," + params.get("latitude") + ","
                + params.get("linha_id") + "," + params.get("longitude") + ","
                + params.get("ordem_id") + "," + params.get("updated_at") + ","
                + params.get("velocidade") + ")";
        return query;
    }

    public static String queryInsertLinhas(Map<String, String> params) {
        String query = SQL_INSERT_LINHAS;
        query += params.get("created_at") + "," + params.get("linha") + ","
                + params.get("updated_at") + ")";
        return query;
    }

    public static String queryInsertOrdens(Map<String, String> params) {
        String query = SQL_INSERT_ORDENS;
        query += params.get("created_at") + "," + params.get("ordem") + ","
                + params.get("updated_at") + ")";
        return query;
    }

    public static String queryInsertColetas(Map<String, String> params) {
        String query = SQL_INSERT_COLETAS;
        query += params.get("created_at") + "," + params.get("data_hora_fim") + ","
                + params.get("data_hora_inicio") + "," + params.get("erros") + ","
                + params.get("status") + "," + params.get("updated_at") + ")";
        return query;
    }

    public static Integer insereColeta(Statement stmt) throws SQLException {
        Map<String, String> params = new HashMap<>();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date();

        params.put("created_at", "'" + dt.format(d) + "'");
        params.put("data_hora_inicio", "'" + dt.format(d) + "'");
        params.put("updated_at", "'" + dt.format(d) + "'");
        params.put("data_hora_fim", "NULL");
        params.put("erros", "NULL");
        params.put("status", String.valueOf(Constants.STATUS_INICIADA));
        Integer size = stmt.executeUpdate(queryInsertColetas(params), new String[]{"id"});
        if (size != 1) {
            return null;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        return (int) rs.getLong(1);
    }

    public static Integer insereLinha(Statement stmt, Map<String, String> params) throws SQLException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date();
        params.put("created_at", "'" + dt.format(d) + "'");
        params.put("updated_at", "'" + dt.format(d) + "'");
        Integer size = stmt.executeUpdate(queryInsertLinhas(params), new String[]{"id"});
        if (size != 1) {
            return null;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        return (int) rs.getLong(1);
    }

    public static Integer insereOrdem(Statement stmt, Map<String, String> params) throws SQLException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date();
        params.put("created_at", "'" + dt.format(d) + "'");
        params.put("updated_at", "'" + dt.format(d) + "'");
        Integer size = stmt.executeUpdate(queryInsertOrdens(params), new String[]{"id"});
        if (size != 1) {
            return null;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        return (int) rs.getLong(1);
    }

    public static Integer insereDados(Statement stmt, Map<String, String> params) throws SQLException {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date();
        params.put("created_at", "'" + dt.format(d) + "'");
        params.put("updated_at", "'" + dt.format(d) + "'");
        Integer size = stmt.executeUpdate(queryInsertDados(params), new String[]{"id"});
        if (size != 1) {
            return null;
        }
        ResultSet rs = stmt.getGeneratedKeys();
        rs.next();
        return (int) rs.getLong(1);
    }

    public static String queryUpdateColetas(Map<String, String> params, Integer id) {
        String query = SQL_UPDATE_COLETAS;

        for (String c : params.keySet()) {
            query += " " + c + " = " + params.get(c) + ",";
        }
        String q = query.substring(0, query.lastIndexOf(","));
        q += " WHERE id = " + id;
        return q;
    }

    public static ResultSet busca(Statement stmt, String tabela, Map<String, String> params) throws SQLException {
        String query = SQL_SELECT;
        query += tabela;
        if (!params.isEmpty()) {
            query += " WHERE";
            for (String c : params.keySet()) {
                if(params.get(c).equals("null")){
                    query += " " + c + " is NULL AND";
                }else{
                    query += " " + c + " = " + params.get(c) + " AND";
                }
//                System.out.println("c " + c + ": " + params.get(c));
                
            }
            query = query.substring(0, query.lastIndexOf("AND"));
        }
        return stmt.executeQuery(query);
    }
    
    public static ResultSet busca(Statement stmt, String tabela, String campo, Map<String, String> params) throws SQLException {
        String query = "SELECT " + campo + " FROM " + tabela;
        if (!params.isEmpty()) {
            query += " WHERE";
            for (String c : params.keySet()) {
                                if(params.get(c).equals("null")){
                    query += " " + c + " is NULL AND";
                }else{
                    query += " " + c + " = " + params.get(c) + " AND";
                }
            }
            query = query.substring(0, query.lastIndexOf("AND"));
        }
        return stmt.executeQuery(query);
    }

    public static Integer finalizaColetaComSucesso(Statement stmt, Integer coletaId) throws SQLException {
        Map<String, String> params = new HashMap<>();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date();
        params.put("updated_at", "'" + dt.format(d) + "'");
        params.put("data_hora_fim", "'" + dt.format(d) + "'");
        params.put("status", String.valueOf(Constants.STATUS_ENCERRADA_COM_SUCESSO));
        return stmt.executeUpdate(queryUpdateColetas(params, coletaId));
    }

    public static Integer finalizaColetaComErros(Statement stmt, Integer coletaId, String erros) throws SQLException {
        Map<String, String> params = new HashMap<>();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date();
        params.put("updated_at", "'" + dt.format(d) + "'");
        params.put("data_hora_fim", "'" + dt.format(d) + "'");
        params.put("status", String.valueOf(Constants.STATUS_ENCERRADA_COM_ERROS));
        params.put("erros", erros);
        return stmt.executeUpdate(queryUpdateColetas(params, coletaId));
    }

}
