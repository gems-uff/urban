/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.Provenance;

import br.uff.bus_data.dao.LoadedFileDAO;
import br.uff.bus_data.dao.BusPositionDAO;
import br.uff.bus_data.dao.DisposalDAO;
import br.uff.bus_data.dao.LineDAO;
import br.uff.bus_data.dao.BusDAO;
import br.uff.bus_data.dbConnection.DBConnectionInterface;
import br.uff.bus_data.dbConnection.PostgresDBConnection;
import br.uff.bus_data.dbHelpers.LoadedFileDBUtils;
import br.uff.bus_data.dbHelpers.BusPositionDBUtils;
import br.uff.bus_data.dbHelpers.IndexesDBUtils;
import br.uff.bus_data.dbHelpers.LineDBUtils;
import br.uff.bus_data.dbHelpers.BusDBUtils;
import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.helper.DeleteDirectory;
import br.uff.bus_data.helper.FileFinder;
import br.uff.bus_data.helper.HashUtils;
import br.uff.bus_data.helper.UnZip;
import br.uff.bus_data.models.BusPosition;
import br.uff.bus_data.models.LoadedFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
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
public class ImportBusPositions {

    private static Long loadedFileId;
    private static Long lineId;
    private static Long busId;

    public static void main(String[] args) throws FileNotFoundException {

        try {
            DBConnectionInterface dbCon = new PostgresDBConnection();
            Connection con = dbCon.dbConection();
            Statement stmt = null;
            con.setAutoCommit(false);
            stmt = con.createStatement();
            JSONParser parser = new JSONParser();

            Map<String, Long> linesHash = HashUtils.loadLinhas(stmt); 
            Map<String, Long> busesHash = HashUtils.loadOrdens(stmt);
            Map<String, BusPosition> busPositionsHash = HashUtils.loadPosicoes(stmt);
            List<Map<String, String>> insertParamsPositions = new ArrayList<Map<String, String>>();
            List<Map<String, String>> insertParamsDispolsals = new ArrayList<Map<String, String>>();

            LoadedFileDAO loadedFileDao = new LoadedFileDAO();
            loadedFileDao.setStatement(stmt);
            LineDAO lineDao = new LineDAO();
            lineDao.setStatement(stmt);
            BusDAO busDao = new BusDAO();
            busDao.setStatement(stmt);
            BusPositionDAO busPositionDao = new BusPositionDAO();
            busPositionDao.setStatement(stmt);
            DisposalDAO disposalDao = new DisposalDAO();
            disposalDao.setStatement(stmt);

            IndexesDBUtils.dropIndexes(stmt, con);

            File currentDirFile = new File("");
            String projRoot = currentDirFile.getAbsolutePath();
            String zipsPath = projRoot + "/zips";
            File[] zips = FileFinder.finder(zipsPath, ".zip");
            Arrays.sort(zips);

            for (File zip : zips) {
                String zipName = zip.getName().replaceFirst("[.][^.]+$", "");

                UnZip.unZip(zip.getAbsolutePath(), zipsPath + File.separator + zipName);

                File[] files = FileFinder.finder(zipsPath + File.separator + zipName + "/tmp", ".json");
                Arrays.sort(files);

                for (File file : files) {
                    if (file.isFile()) {
                        try {
                            loadedFileId = loadedFileDao.insert(LoadedFileDBUtils.insertDefaultParams(file.getName(), LoadedFile.TYPE_BUS_POSITIONS));
                            JSONObject rootObject = (JSONObject) parser.parse(new FileReader(file.getAbsoluteFile()));
                            ArrayList<String> columns = (ArrayList<String>) rootObject.get(Constants.KEY_COLUMNS);
                            for (int i = 0; i < columns.size(); i++) {
                                if (!columns.get(i).equals(Constants.COLUMNS[i])) {
                                    LoadedFileDBUtils.finishWithErrors(loadedFileDao, loadedFileId, Constants.MSG_ERROR_COLUMNS);
                                    return;
                                }
                            }
                            ArrayList<Object> dataSet = (ArrayList<Object>) rootObject.get(Constants.KEY_DATA);
                            for (int i = 0; i < dataSet.size(); i++) {
                                ArrayList<Object> data = (ArrayList<Object>) dataSet.get(i);
                                lineId = LineDBUtils.findOrInsertLine(lineDao,
                                        String.valueOf(data.get(Constants.INDEX_LINE)),
                                        linesHash);

                                String busNumber = String.valueOf(data.get(Constants.INDEX_BUS_NUMBER));
                                busId = BusDBUtils.insertBus(busDao, busNumber,
                                        busesHash);

                                BusPosition currentPosition = busPositionsHash.get(busNumber);
                                BusPosition newPosition = BusPosition.fromJsonFile(data, lineId, busId, loadedFileId);
                                HashMap<String, String> params = BusPositionDBUtils.generateParams(newPosition);

                                if ((currentPosition != null)) {
                                    String disposalReason = currentPosition.motivoDescarte(newPosition);
                                    if (disposalReason == null) {
                                        insertParamsPositions.add(params);
//                                        Long dadoId = dadoRJDao.insert(params);
//                                        novoDado.setId(dadoId);
                                        busPositionsHash.put(busNumber, newPosition);
                                    } else {
                                        params.put("disposal_reason", "'" + disposalReason + "'");
//                                        params.put("last_postion_id", String.valueOf(dadoAtual.getId()));
                                        insertParamsDispolsals.add(params);
//                                        descarteDao.insert(params);
                                    }
                                } else {
                                    insertParamsPositions.add(params);
//                                    Long dadoId = dadoRJDao.insert(params);
//                                    novoDado.setId(dadoId);
                                    busPositionsHash.put(busNumber, newPosition);
                                }
                            }
                            busPositionDao.insert(insertParamsPositions);
                            disposalDao.insert(insertParamsDispolsals);
                            insertParamsPositions.clear();
                            insertParamsDispolsals.clear();

                            LoadedFileDBUtils.finishSuccessfully(loadedFileDao, loadedFileId);
                            con.commit();
                            System.out.println("file " + file.getName());
                        } catch (IOException ex) {
                            busPositionDao.insert(insertParamsPositions);
                            disposalDao.insert(insertParamsDispolsals);
                            insertParamsPositions.clear();
                            insertParamsDispolsals.clear();
                            LoadedFileDBUtils.finishWithErrors(loadedFileDao, loadedFileId, ex.getMessage());
                            con.commit();
                            Logger.getLogger(ImportBusPositions.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                            busPositionDao.insert(insertParamsPositions);
                            disposalDao.insert(insertParamsDispolsals);
                            insertParamsPositions.clear();
                            insertParamsDispolsals.clear();
                            LoadedFileDBUtils.finishWithErrors(loadedFileDao, loadedFileId, ex.getMessage());
                            con.commit();
                            Logger.getLogger(ImportBusPositions.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }

                try {
                    DeleteDirectory.delete(zipsPath + File.separator + zipName);
                } catch (IOException ex) {
                    Logger.getLogger(UnZip.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            IndexesDBUtils.createIndexes(stmt, con);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        }
    }
}
