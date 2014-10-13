/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data;

import br.uff.bus_data.dao.LineDAO;
import br.uff.bus_data.dao.LinePositionDAO;
import br.uff.bus_data.dao.LinePositionDAO;
import br.uff.bus_data.dao.LoadedFileDAO;
import br.uff.bus_data.dbConnection.DBConnectionInterface;
import br.uff.bus_data.dbConnection.PostgresDBConnection;
import br.uff.bus_data.dbHelpers.LineDBUtils;
import br.uff.bus_data.dbHelpers.LoadedFileDBUtils;
import br.uff.bus_data.helper.CSVConstants;
import br.uff.bus_data.helper.Constants;
import br.uff.bus_data.helper.FileFinder;
import br.uff.bus_data.helper.HashUtils;
import br.uff.bus_data.models.LinePosition;
import br.uff.bus_data.models.LoadedFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class ImportLinePositions {

    public static void main(String[] args) {
        try {
            DBConnectionInterface dbCon = new PostgresDBConnection();
            Connection con = dbCon.dbConection();
            Statement stmt = con.createStatement();

            File currentDirFile = new File("");
            String projRoot = currentDirFile.getAbsolutePath();
            File[] files = FileFinder.finder(projRoot + File.separator + "line positions" + File.separator + "csvs", ".csv");
            Map<String, Long> linesHash = HashUtils.loadLinhas(stmt);

            for (File file : files) {
                if (file.isFile()) {
                    importPositionsCSV(file.getAbsolutePath(), linesHash, stmt);
                    System.out.println("file " + file.getName());
                }

            }

        } catch (SQLException ex) {
        }
    }

    public static void importPositionsCSV(String csvFilePath, Map<String, Long> linesHash, Statement stmt) {

        BufferedReader br = null;
        String line;
        try {

            br = new BufferedReader(new FileReader(csvFilePath));
            LoadedFileDAO loadedFileDao = new LoadedFileDAO();
            loadedFileDao.setStatement(stmt);
            LineDAO lineDao = new LineDAO();
            lineDao.setStatement(stmt);
            LinePositionDAO lpDao = new LinePositionDAO();
            lpDao.setStatement(stmt);
            long loadedFileId = loadedFileDao.insert(LoadedFileDBUtils.insertDefaultParams(csvFilePath.substring(csvFilePath.lastIndexOf('/') + 1), LoadedFile.TYPE_LINE_POSITIONS));

            String header = br.readLine();
            if (header != null) {
                String[] headerFields = header.split(CSVConstants.CSV_SEPARATOR);
                for (int i = 0; i < headerFields.length; i++) {
                    if (!CSVConstants.POSITIONS_COLUMNS[i].equals(headerFields[i])) {
                        LoadedFileDBUtils.finishWithErrors(loadedFileDao, loadedFileId, Constants.MSG_ERROR_COLUMNS);
                        br.close();
                        return;
                    }
                }
            }

            List<Map<String, String>> insertParamsPositions = new ArrayList<Map<String, String>>();

            while ((line = br.readLine()) != null) {

                String[] info = line.split(CSVConstants.CSV_SEPARATOR);
                String lineNumber = info[0].replace("\"", "");
                String description = info[1].replace("\"", "");
                String company = info[2].replace("\"", "");
                String sequenceNumber = info[3].replace("\"", "");
                String shapeId = info[4].replace("\"", "");
                String lat = info[5].replace("\"", "");
                String lng = info[6].replace("\"", "");
                
                long lineId = LineDBUtils.findOrInsertLine(lineDao,
                        lineNumber,
                        linesHash);
                
                LinePosition lp = new LinePosition();
                lp.setCompany(company);
                lp.setDescription(description);
                lp.setLatitude(Double.parseDouble(lat));
                lp.setLongitude(Double.parseDouble(lng));
                lp.setSequenceNumber(Integer.parseInt(sequenceNumber));
                lp.setLineId(lineId);
                lp.setShapeId(Long.parseLong(shapeId));
                insertParamsPositions.add(lp.getMap());

            }

            lpDao.insert(insertParamsPositions);
            LoadedFileDBUtils.finishSuccessfully(loadedFileDao, loadedFileId);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
    }

}
