/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dbHelpers;

import br.uff.bus_data.dao.AbstractDAO;
import br.uff.bus_data.dao.LoadedFileDAO;
import br.uff.bus_data.helper.Constants;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class LoadedFileDBUtils {

    public static Map<String, String> insertDefaultParams(String filename, int type) {

        Map<String, String> params = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        Date d = new Date();

        params.put("start_time", "'" + dt.format(d) + "'");
        params.put("end_time", "NULL");
        params.put("errors", "NULL");
        params.put("filename", "'" + filename + "'");
        params.put("status", String.valueOf(Constants.STATUS_STARTED));
        params.put("type", String.valueOf(type));
        return params;
    }

    public static void finishSuccessfully(AbstractDAO ufDao, Long uploadedFileId) throws SQLException {
        Map<String, String> params = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        Date d = new Date();
        params.put("end_time", "'" + dt.format(d) + "'");
        params.put("status", String.valueOf(Constants.STATUS_FINISHED_SUCCESSFULLY));
        ufDao.update(params, uploadedFileId);
    }

    public static void finishWithErrors(AbstractDAO ufDao, Long uploadedFileId, String errors) throws SQLException {
        Map<String, String> params = new HashMap<String, String>();
        SimpleDateFormat dt = new SimpleDateFormat(Constants.DB_DATE_FORMAT);
        Date d = new Date();
        params.put("end_time", "'" + dt.format(d) + "'");
        params.put("status", String.valueOf(Constants.STATUS_FINISHED_WITH_ERRORS));
        params.put("errors", "'" + errors.replaceAll("'", "''") + "'");
        ufDao.update(params, uploadedFileId);
    }

}
