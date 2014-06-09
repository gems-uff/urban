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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
            System.out.println("Removing indexes");
            stmt.execute("DROP INDEX index_ordens_on_ordem ON ordens");
            stmt.execute("DROP INDEX index_linhas_on_linha ON linhas");
            stmt.execute("DROP INDEX index_coletas_on_data_hora_inicio_and_filename ON coletas");
            stmt.execute("DROP INDEX index_dados_rj_on_data_hora_and_ordem_id ON dados_rj");
            System.out.println("Removing foreign keys");
            stmt.execute("ALTER TABLE dados_rj DROP FOREIGN KEY fk_dados_rj_coleta_id");
            stmt.execute("ALTER TABLE dados_rj DROP FOREIGN KEY fk_dados_rj_linha_id");
            stmt.execute("ALTER TABLE dados_rj DROP FOREIGN KEY fk_dados_rj_ordem_id");
            stmt.execute("DROP INDEX fk__dados_rj_linha_id ON dados_rj");
            stmt.execute("DROP INDEX fk__dados_rj_coleta_id ON dados_rj");
            stmt.execute("DROP INDEX fk__dados_rj_ordem_id ON dados_rj");
            System.out.println("Starting");

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

                                HashMap<String, String> params = new HashMap<String, String>();

                                String linha = String.valueOf(dado.get(Constants.INDEX_LINHA));
                                linhaId = null;

                                if (!linha.isEmpty()) {
                                    linha = linha.split("\\.")[0];
                                    linhaId = linhasHash.get(linha);
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
                                    ordemId = ordensHash.get(ordem);
                                    if (ordemId == null) {
                                        params.clear();
                                        params.put(Constants.KEY_ORDEM, "'" + ordem + "'");
                                        ordemId = ordemDao.insert(params);
                                        ordensHash.put(ordem, ordemId);
                                    }

                                }
                                DadoRJ dadoAtual = dadosHash.get(ordem);
                                DadoRJ novoDado = DadoRJ.fromJsonFile(dado, linhaId, ordemId, coletaId);
                                if ((dadoAtual == null) || (dadoAtual.foiAtualizado(novoDado))) {
                                    params.clear();
                                    SimpleDateFormat dt = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                                    SimpleDateFormat dt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
                                    params.put("coleta_id", String.valueOf(coletaId));
                                    params.put("data_hora", "'" + data + "'");
                                    params.put("latitude", String.valueOf(dado.get(Constants.INDEX_LATITUDE)));
                                    params.put("longitude", String.valueOf(dado.get(Constants.INDEX_LONGITUDE)));
                                    params.put("velocidade", String.valueOf(dado.get(Constants.INDEX_VELOCIDADE)));

                                    dadoRJDao.insert(params);
                                    dadosHash.put(ordem, novoDado);

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

            System.out.println("Re-creating indexes");
            stmt.execute("CREATE UNIQUE INDEX index_ordens_on_ordem ON ordens (ordem)");
            System.out.println("Index ordens OK");
            stmt.execute("CREATE UNIQUE INDEX index_linhas_on_linha ON linhas (linha)");
            System.out.println("Index linhas OK");
            stmt.execute("CREATE UNIQUE INDEX index_coletas_on_data_hora_inicio_and_filename ON coletas (data_hora_inicio, filename)");
            System.out.println("Index coletas OK");
            stmt.execute("CREATE UNIQUE INDEX index_dados_rj_on_data_hora_and_ordem_id ON dados_rj (data_hora, ordem_id)");
            System.out.println("Index dados OK");
            System.out.println("Re-Creating FKs");
            stmt.execute("ALTER TABLE dados_rj ADD FOREIGN KEY (coleta_id) REFERENCES coletas(id)");
            System.out.println("FK dados_rj coleta_id OK");
            stmt.execute("ALTER TABLE dados_rj ADD FOREIGN KEY (linha_id) REFERENCES linhas(id)");
            System.out.println("FK dados_rj linha_id OK");
            stmt.execute("ALTER TABLE dados_rj ADD FOREIGN KEY (ordem_id) REFERENCES ordens(id)");
            System.out.println("FK dados_rj ordem_id OK");
            System.out.println("Re-Creating FKs Indexes");
            stmt.execute("CREATE INDEX fk__dados_rj_ordem_id ON dados_rj (ordem_id)");
            System.out.println("FK index dados_rj ordem_id OK");
            stmt.execute("CREATE INDEX fk__dados_rj_linha_id ON dados_rj (linha_id)");
            System.out.println("FK index dados_rj linha_id OK");
            stmt.execute("CREATE INDEX fk__dados_rj_coleta_id ON dados_rj (coleta_id)");
            System.out.println("FK index dados_rj coleta_id OK");
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: " + cE.toString());
        }
    }
}


