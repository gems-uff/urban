/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.dao;

import java.sql.Statement;

/**
 *
 * @author schettino
 */
public class DAOContainer {

    public static final int LOADED_FILE = 0;
    public static final int LINE = 1;
    public static final int BUS = 2;
    public static final int BUS_POSITION = 3;
    public static final int DISPOSAL = 4;
    
    private LoadedFileDAO loadedFileDao;
    private LineDAO lineDao;
    private BusDAO busDao;
    private BusPositionDAO busPositionDao;
    private DisposalDAO disposalDao;

    public DAOContainer(Statement stmt) {
        loadedFileDao = new LoadedFileDAO();
        loadedFileDao.setStatement(stmt);
        
        lineDao = new LineDAO();
        lineDao.setStatement(stmt);
        
        busDao = new BusDAO();
        busDao.setStatement(stmt);

        busPositionDao = new BusPositionDAO();
        busPositionDao.setStatement(stmt);

        disposalDao = new DisposalDAO();
        disposalDao.setStatement(stmt);
    }
    
    public AbstractDAO get(int klass){
        switch (klass){
            case LOADED_FILE: return loadedFileDao;
            case LINE: return lineDao;
            case BUS: return busDao;
            case BUS_POSITION: return busPositionDao;
            case DISPOSAL: return disposalDao;
            default: return null;
        }
    }
    
}
