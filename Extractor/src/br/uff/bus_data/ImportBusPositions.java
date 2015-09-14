package br.uff.bus_data;

import br.uff.bus_data.dao.DAOContainer;
import br.uff.bus_data.dao.LineBoundingBoxDAO;
import br.uff.bus_data.dbConnection.DBConnectionInterface;
import br.uff.bus_data.dbConnection.PostgresDBConnection;
import br.uff.bus_data.dbHelpers.LoadedFileDBUtils;
import br.uff.bus_data.dbHelpers.BusPositionDBUtils;
import br.uff.bus_data.dbHelpers.IndexesDBUtils;
import br.uff.bus_data.helper.BusPositionContainer;
import br.uff.bus_data.helper.DeleteDirectory;
import br.uff.bus_data.helper.DiscardRecordHelper;
import br.uff.bus_data.helper.FileFinder;
import br.uff.bus_data.helper.HashUtils;
import br.uff.bus_data.helper.JacksonJsonParser;
import br.uff.bus_data.helper.UnZip;
import br.uff.bus_data.models.BusPosition;
import br.uff.bus_data.models.LineBoundingBox;
import br.uff.bus_data.models.LoadedFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImportBusPositions {

    private static final boolean USE_DISPOSALS = false;
    private static Long loadedFileId;
    private static Connection con;
    private static Statement stmt;
    private static Map<String, Long> linesHash;
    private static Map<String, Long> busesHash;
    private static Map<Long, LineBoundingBox> lineBoundingBoxHash;
    private static BusPositionContainer busPositionsHash;
    private static List<Map<String, String>> insertParamsPositions;
    private static List<Map<String, String>> insertParamsDispolsals;
    private static DAOContainer daoContainer;
    private static LineBoundingBoxDAO lineBoundingBoxDao;
    private static JacksonJsonParser jacksonJsonParser;
    private static DiscardRecordHelper discardRecordHelper;

    public static void main(String[] args) throws FileNotFoundException {
        try {
            initialize();

            File currentDirFile = new File("");
            String zipsPath = currentDirFile.getAbsolutePath() + File.separator + "zips";
            File[] zips = FileFinder.finder(zipsPath, ".zip");
            if (zips != null) {
                Arrays.sort(zips);

                for (File zip : zips) {
                    String zipName = zip.getName().replaceFirst("[.][^.]+$", "");
                    UnZip.unZip(zip.getAbsolutePath(), zipsPath + File.separator + zipName);

                    File[] files = FileFinder.finder(zipsPath + File.separator
                            + zipName + File.separator + "tmp", ".json");
                    if (files == null) {
                        files = FileFinder.finder(zipsPath + File.separator + zipName, ".json");
                    }

                    if (files != null) {
                        Arrays.sort(files);
                        System.out.println("Jsons: " + files.length);
                        for (File file : files) {
                            if (file.isFile()) {
                                importJacksonFile(file);
                            }
                        }
                    }

                    try {
                        DeleteDirectory.delete(zipsPath + File.separator + zipName);
                    } catch (IOException ex) {
                        Logger.getLogger(UnZip.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            IndexesDBUtils.createIndexes(stmt, con, USE_DISPOSALS);

            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception: " + e.toString());
        }
    }

    private static void initialize() throws SQLException {
        DBConnectionInterface dbCon = new PostgresDBConnection();
        con = dbCon.dbConection();
        stmt = null;
        con.setAutoCommit(false);
        stmt = con.createStatement();
        linesHash = HashUtils.loadLinhas(stmt);
        busesHash = HashUtils.loadOrdens(stmt);
        busPositionsHash = new BusPositionContainer(HashUtils.loadPosicoes(stmt));
        insertParamsPositions = new ArrayList<Map<String, String>>();
        insertParamsDispolsals = new ArrayList<Map<String, String>>();
        daoContainer = new DAOContainer(stmt);
        lineBoundingBoxDao = new LineBoundingBoxDAO();
        lineBoundingBoxDao.setStatement(stmt);
        lineBoundingBoxHash = lineBoundingBoxDao.all();
        discardRecordHelper = new DiscardRecordHelper(busPositionsHash, lineBoundingBoxHash);
        IndexesDBUtils.dropIndexes(stmt, con, USE_DISPOSALS);
        jacksonJsonParser = new JacksonJsonParser(daoContainer);
    }

    private static void importJacksonFile(File file) throws SQLException {
        try {
            loadedFileId = daoContainer.get(DAOContainer.LOADED_FILE).insert(LoadedFileDBUtils.insertDefaultParams(file.getName(), LoadedFile.TYPE_BUS_POSITIONS));
            jacksonJsonParser.parseJsonFile(file, linesHash, busesHash, loadedFileId);
            endLoadedFile();
            LoadedFileDBUtils.finishSuccessfully(daoContainer.get(DAOContainer.LOADED_FILE), loadedFileId);
            con.commit();
            System.out.println("file " + file.getName());
        } catch (SQLException sqlEx) {
            Logger.getLogger(ImportBusPositions.class.getName()).log(Level.SEVERE, null, sqlEx);
            clearParams();
            con.rollback();
            LoadedFileDBUtils.finishWithErrors(daoContainer.get(DAOContainer.LOADED_FILE), loadedFileId, sqlEx.getMessage());
            con.commit();
        } catch (Exception ex) {
            Logger.getLogger(ImportBusPositions.class.getName()).log(Level.SEVERE, null, ex);
            endLoadedFile();
            LoadedFileDBUtils.finishWithErrors(daoContainer.get(DAOContainer.LOADED_FILE), loadedFileId, ex.getMessage());
            con.commit();
        }
    }

    public static void importData(BusPosition newBusPosition, String busNumber) throws SQLException {
        BusPosition currentPosition = null;
        if (newBusPosition.getTime() != null) {
            currentPosition = busPositionsHash.getPrecursor(busNumber, newBusPosition);
        }
        HashMap<String, String> params = BusPositionDBUtils.generateParams(newBusPosition);

        Long disposalReason = discardRecordHelper.disposalReason(newBusPosition, currentPosition, busNumber);
        if (disposalReason == null) {
            insertParamsPositions.add(params);
            busPositionsHash.put(busNumber, newBusPosition);
        } else {
            if (USE_DISPOSALS) {
                params.put("disposal_reason_id", "'" + disposalReason + "'");
                insertParamsDispolsals.add(params);
            }
        }

    }

    private static void endLoadedFile() throws SQLException {
        daoContainer.get(DAOContainer.BUS_POSITION).insert(insertParamsPositions);
        if (USE_DISPOSALS) {
            daoContainer.get(DAOContainer.DISPOSAL).insert(insertParamsDispolsals);
        }
        clearParams();
    }

    private static void clearParams() throws SQLException {
        insertParamsPositions.clear();
        if (USE_DISPOSALS) {
            insertParamsDispolsals.clear();
        }
    }
}
