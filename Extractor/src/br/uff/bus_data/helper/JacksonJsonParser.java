/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

import br.uff.bus_data.ImportBusPositions;
import br.uff.bus_data.dao.BusDAO;
import br.uff.bus_data.dao.DAOContainer;
import br.uff.bus_data.dao.LineDAO;
import br.uff.bus_data.dbHelpers.BusDBUtils;
import br.uff.bus_data.dbHelpers.LineDBUtils;
import br.uff.bus_data.models.BusPosition;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgis.PGgeometry;
import org.postgis.Point;

/**
 *
 * @author schettino
 */
public class JacksonJsonParser {

    private BusPosition busPosition;
    private final DAOContainer daoContainer;
    private Map<String, Long> linesHash;
    private Map<String, Long> busesHash;
    private final JsonFactory jfactory;
    private JsonParser jParser;
    private String busNumber;

    private static final String DATA_KEY = "DATA";
    private static final String COLUMNS_KEY = "COLUMNS";
    private static final String JSON_DATE_FORMAT = "MM-dd-yyyy HH:mm:ss";
    private static final int INDEX_TIME = 0;
    private static final int INDEX_BUS_NUMBER = 1;
    private static final int INDEX_LINE = 2;
    private static final int INDEX_LATITUDE = 3;
    private static final int INDEX_LONGITUDE = 4;
    private static final int INDEX_SPEED = 5;

    public JacksonJsonParser(DAOContainer daoContainer) {
        this.jfactory = new JsonFactory();
        this.busPosition = new BusPosition();
        this.daoContainer = daoContainer;
    }

    public void parseJsonFile(File file, Map<String, Long> linesHash,
            Map<String, Long> busesHash, Long loadedFileId) throws SQLException,
            IOException, JsonGenerationException {
        this.linesHash = linesHash;
        this.busesHash = busesHash;

        int index = 0;
        jParser = jfactory.createParser(file);
        while (jParser.nextToken() != JsonToken.END_OBJECT && jParser.getCurrentToken() != null) {
            String fieldname = jParser.getCurrentName();

            if (COLUMNS_KEY.equals(fieldname)) {
                while (jParser.nextToken() != JsonToken.END_ARRAY) {
                    //skip columns field
                }
            }
            if (jParser.getCurrentToken() == (JsonToken.START_ARRAY)) {
                clearBusPosition();
                index = 0;
                while (jParser.nextToken() != JsonToken.END_ARRAY) {
                    if (jParser.getCurrentToken() != (JsonToken.START_ARRAY)) {
                        String value = jParser.getText();
                        switchValues(index, value);
                        index++;
                    }
                }
                if (!busPosition.empty()) {
                    afterEndElement(loadedFileId);
                }
            }
        }
        jParser.close();
    }

    protected void switchValues(int index, String value) throws SQLException {
        switch (index) {
            case INDEX_TIME:
                parseTime(value);
                break;
            case INDEX_BUS_NUMBER:
                parseBus(value);
                break;
            case INDEX_LINE:
                parseLine(value);
                break;
            case INDEX_LATITUDE:
                parseLatitude(value);
                break;
            case INDEX_LONGITUDE:
                parseLongitude(value);
                break;
            case INDEX_SPEED:
                parseSpeed(value);
                break;
            default:
                break;
        }
    }

    protected void parseTime(String value) {
        SimpleDateFormat dt = new SimpleDateFormat(JSON_DATE_FORMAT);
        Date date = null;
        if (!value.isEmpty()) {
            try {
                date = dt.parse(value);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        busPosition.setTime(date);
    }

    protected void parseBus(String value) throws SQLException {
        busNumber = value;
        busPosition.setBusId(BusDBUtils.insertBus(
                (BusDAO) daoContainer.get(DAOContainer.BUS),
                value,
                busesHash)
        );
    }

    protected void parseLine(String value) throws SQLException {
        busPosition.setLineId(LineDBUtils.findOrInsertLine(
                (LineDAO) daoContainer.get(DAOContainer.LINE),
                value,
                linesHash)
        );
    }

    protected void parseLatitude(String value) {
        busPosition.setLatitude(Double.valueOf(value));
    }

    protected void parseLongitude(String value) {
        busPosition.setLongitude(Double.valueOf(value));
    }

    protected void parseSpeed(String value) {
        try {
            busPosition.setSpeed(Float.valueOf(value));
        } catch (java.lang.IndexOutOfBoundsException ex) {
            Logger.getLogger(ImportBusPositions.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void afterEndElement(Long loadedFileId) throws SQLException {
        if ((busPosition.getLatitude() != null) && (busPosition.getLongitude() != null)) {
            setPosition();
        }
        busPosition.setLoadedFileId(loadedFileId);
        ImportBusPositions.importData(busPosition, busNumber);
    }

    protected void setPosition() {
        Point p = new Point(busPosition.getLongitude(), busPosition.getLatitude());
        p.setSrid(4326);
        busPosition.setPosition(new PGgeometry(p));
    }

    protected void clearBusPosition() {
        busPosition = new BusPosition();
        busNumber = null;
    }

}
