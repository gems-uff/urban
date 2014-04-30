/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data;

import br.uff.bus_data.dao.ColetaDAO;
import br.uff.bus_data.dao.DadoRJDAO;
import br.uff.bus_data.dao.LinhaDAO;
import br.uff.bus_data.dao.OrdemDAO;
import br.uff.bus_data.helper.ColetaDBUtils;
import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.helper.DBConnection;
import br.uff.bus_data.helper.HashUtils;
import br.uff.bus_data.models.Linha;
import br.uff.bus_data.models.Ordem;
import br.uff.bus_data.models.Position;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author schettino
 */
public class Main {

    private static Long coletaId;
    private static Long linhaId;
    private static Long ordemId;

    public static File[] finder(String dirName) {
        File dir = new File(dirName);
        return dir.listFiles((File dir1, String filename) -> filename.endsWith(".json"));
    }

    public static void main(String[] args) throws FileNotFoundException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:" + DBConnection.JDBC + ":" + DBConnection.DATABASE + "?"
                    + "user=" + DBConnection.USER + "&password=" + DBConnection.PASSWORD;
            Connection con = DriverManager.getConnection(connectionUrl);

            Statement stmt = null;
            ResultSet rs = null;
            File currentDirFile = new File("");
            String helper = currentDirFile.getAbsolutePath();
            JSONParser parser = new JSONParser();

            stmt = con.createStatement();
            File[] files = finder(helper + "/jsons");
            Map<String, Long> linhasHash = HashUtils.loadLinhas(stmt); // entrada: numero da linha; saída: id da linha
            Map<String, Long> ordensHash = HashUtils.loadOrdens(stmt);// entrada: ordem do ônibus; saída: id da ordem
            Map<Long, Position> posicoesHash = HashUtils.loadPosicoes(stmt);// entrada: ordem do ônibus; saída: ultima posicao do onibus
            ColetaDAO coletaDao = new ColetaDAO();
            coletaDao.setStatement(stmt);

            LinhaDAO linhaDao = new LinhaDAO();
            linhaDao.setStatement(stmt);
            OrdemDAO ordemDao = new OrdemDAO();
            ordemDao.setStatement(stmt);
            DadoRJDAO dadoRJDao = new DadoRJDAO();
            dadoRJDao.setStatement(stmt);

            for (File file : files) {
                if (file.isFile()) {
                    try {
                        coletaId = coletaDao.insert(ColetaDBUtils.insertDefaultParams(file.getName()));
                        JSONObject rootObject = (JSONObject) parser.parse(new FileReader(file.getAbsoluteFile()));
                        ArrayList<String> colunas = (ArrayList<String>) rootObject.get(Constants.KEY_COLUNAS);
                        for (int i = 0; i < colunas.size(); i++) {
                            if (!colunas.get(i).equals(Constants.COLUNAS[i])) {
                                ColetaDBUtils.finalizaColetaComErros(coletaDao, coletaId, Constants.MSG_ERRO_COLUNAS);
                                return;
                            }
                        }
                        ArrayList<Object> dados = (ArrayList<Object>) rootObject.get(Constants.KEY_DADOS);
                        for (int i = 0; i < dados.size(); i++) {
                            ArrayList<Object> dado = (ArrayList<Object>) dados.get(i);

                            HashMap<String, String> params = new HashMap<>();

                            String linha = String.valueOf(dado.get(Constants.INDEX_LINHA));
                            linhaId = null;

                            if (!linha.isEmpty()) {
                                linha = linha.split("\\.")[0];
//                                params.put(Constants.KEY_LINHA, "'" + linha + "'");
                               linhaId = linhasHash.get(linha);
//                                List<Linha> result = linhaDao.select(params);
                                if (linhaId == null) {
                                    params.clear();
                                    params.put(Constants.KEY_LINHA, "'" + linha + "'");
                                    linhaId = linhaDao.insert(params);
                                    linhasHash.put(linha, linhaId);
                                }

                            }

                            String ordem = String.valueOf(dado.get(Constants.INDEX_ORDEM));
                            ordemId = null;

                            if (!ordem.isEmpty()) {
//                                params.clear();
//                                params.put(Constants.KEY_ORDEM, "'" + ordem + "'");
//                                List<Ordem> res = ordemDao.select(params);
                                ordemId = ordensHash.get(ordem);
                                if (ordemId == null) {
                                    params.clear();
                                    params.put(Constants.KEY_ORDEM, "'" + ordem + "'");
                                    ordemId = ordemDao.insert(params);
                                    ordensHash.put(ordem, ordemId);
                                }

                            }
                            params.clear();
                            SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
                            SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            String data = String.valueOf(dado.get(Constants.INDEX_DATA_HORA));
                            if (!data.isEmpty()) {
                                try {
                                    Date date = dt.parse(data);
                                    data = dt2.format(date);
                                } catch (java.text.ParseException ex) {
                                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            params.clear();
                            params.put("linha_id", String.valueOf(linhaId));
                            params.put("ordem_id", String.valueOf(ordemId));
                            params.put("data_hora", "'" + data + "'");
                            params.put("latitude", String.valueOf(dado.get(Constants.INDEX_LATITUDE)));
                            params.put("longitude", String.valueOf(dado.get(Constants.INDEX_LONGITUDE)));
                            params.put("velocidade", String.valueOf(dado.get(Constants.INDEX_VELOCIDADE)));
                            try {
                                dadoRJDao.insert(params);
                            } catch (MySQLIntegrityConstraintViolationException e) {
                            }
                        }
                        ColetaDBUtils.finalizaColetaComSucesso(coletaDao, coletaId);

                        System.out.println("file " + file.getName());
                    } catch (IOException | ParseException ex) {
                        ColetaDBUtils.finalizaColetaComErros(coletaDao, coletaId, ex.getMessage());
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }
    }

}
