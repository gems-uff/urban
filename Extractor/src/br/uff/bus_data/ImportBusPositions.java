/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data;

import br.uff.bus_data.dao.ColetaDAO;
import br.uff.bus_data.dao.DadoRJDAO;
import br.uff.bus_data.dao.DescarteDAO;
import br.uff.bus_data.dao.LinhaDAO;
import br.uff.bus_data.dao.OrdemDAO;
import br.uff.bus_data.dbHelpers.ColetaDBUtils;
import br.uff.bus_data.dbHelpers.DadoRJDBUtils;
import br.uff.bus_data.dbHelpers.IndexesDBUtils;
import br.uff.bus_data.dbHelpers.LinhaDBUtils;
import br.uff.bus_data.dbHelpers.OrdemDBUtils;
import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.helper.DBConnection;
import br.uff.bus_data.helper.DeleteDirectory;
import br.uff.bus_data.helper.FileFinder;
import br.uff.bus_data.helper.HashUtils;
import br.uff.bus_data.helper.UnZip;
import br.uff.bus_data.models.DadoRJ;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    public static void main(String[] args) throws FileNotFoundException {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionUrl = "jdbc:" + DBConnection.JDBC + ":" + DBConnection.DATABASE + "?"
                    + "user=" + DBConnection.USER + "&password=" + DBConnection.PASSWORD;
            Connection con = DriverManager.getConnection(connectionUrl);

            Statement stmt = null;
            con.setAutoCommit(false);
            stmt = con.createStatement();
            JSONParser parser = new JSONParser();

            Map<String, Long> linhasHash = HashUtils.loadLinhas(stmt); // entrada: numero da linha; saída: id da linha
            Map<String, Long> ordensHash = HashUtils.loadOrdens(stmt);// entrada: ordem do ônibus; saída: id da ordem
            Map<String, DadoRJ> dadosHash = HashUtils.loadPosicoes(stmt);// entrada: ordem do ônibus; saída: ultima posicao do onibus

            ColetaDAO coletaDao = new ColetaDAO();
            coletaDao.setStatement(stmt);
            LinhaDAO linhaDao = new LinhaDAO();
            linhaDao.setStatement(stmt);
            OrdemDAO ordemDao = new OrdemDAO();
            ordemDao.setStatement(stmt);
            DadoRJDAO dadoRJDao = new DadoRJDAO();
            dadoRJDao.setStatement(stmt);
            DescarteDAO descarteDao = new DescarteDAO();
            descarteDao.setStatement(stmt);

            IndexesDBUtils.dropIndexes(stmt);

            File currentDirFile = new File("");
            String projRoot = currentDirFile.getAbsolutePath();
            String zipsPath = projRoot + "/zips";
            File[] zips = FileFinder.finder(zipsPath, ".zip");
            Arrays.sort(zips);

            for (File zip : zips) {
                String zipName = zip.getName().replaceFirst("[.][^.]+$", "");

                UnZip.unZip(zip.getAbsolutePath(), zipsPath + File.separator + zipName);

                File[] files = FileFinder.finder(zipsPath + File.separator + zipName, ".json");
                Arrays.sort(files);

                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            con.close();
                            con = DriverManager.getConnection(connectionUrl);
                            con.setAutoCommit(false);
                            stmt = con.createStatement();
                            coletaDao.setStatement(stmt);
                            linhaDao.setStatement(stmt);
                            ordemDao.setStatement(stmt);
                            dadoRJDao.setStatement(stmt);
                            descarteDao.setStatement(stmt);

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
                                linhaId = LinhaDBUtils.insereLinha(linhaDao,
                                        String.valueOf(dado.get(Constants.INDEX_LINHA)),
                                        linhasHash);

                                String ordem = String.valueOf(dado.get(Constants.INDEX_ORDEM));
                                ordemId = OrdemDBUtils.insereOrdem(ordemDao, ordem,
                                        ordensHash);

                                DadoRJ dadoAtual = dadosHash.get(ordem);
                                DadoRJ novoDado = DadoRJ.fromJsonFile(dado, linhaId, ordemId, coletaId);
                                HashMap<String, String> params = DadoRJDBUtils.geraParams(dado, linhaId, ordemId, coletaId);

                                if ((dadoAtual == null)) {
                                    String motivoDescarte = dadoAtual.motivoDescarte(novoDado);
                                    if (motivoDescarte == null) {
                                        dadoRJDao.insert(params);
                                        dadosHash.put(ordem, novoDado);
                                    } else {
                                        params.put("motivo", "'" + motivoDescarte + "'");
                                        descarteDao.insert(params);
                                    }
                                }
                            }

                            ColetaDBUtils.finalizaColetaComSucesso(coletaDao, coletaId);
                            con.commit();
                            System.out.println("file " + file.getName());
                        } catch (IOException ex) {
                            ColetaDBUtils.finalizaColetaComErros(coletaDao, coletaId, ex.getMessage());
                            con.commit();
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            ColetaDBUtils.finalizaColetaComErros(coletaDao, coletaId, ex.getMessage());
                            con.commit();
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }

                try {
                    DeleteDirectory.delete(zipsPath + File.separator + zipName);
                } catch (IOException ex) {
                    Logger.getLogger(UnZip.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            IndexesDBUtils.createIndexes(stmt);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }
    }
}
