/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

import br.uff.bus_data.models.BusPosition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author schettino
 */
public class BusPositionContainer {

    private static final int MAX_SIZE = 10;

    private Map<String, List<BusPosition>> busPositions;

    public BusPositionContainer() {
        busPositions = new HashMap<String, List<BusPosition>>();
    }

    public BusPositionContainer(Map<String, List<BusPosition>> positions) {
        busPositions = positions;
    }

    public boolean put(String busNumber, BusPosition busPosition) {
        if (busPositions.containsKey(busNumber)) {
            List<BusPosition> list = busPositions.get(busNumber);
            int i = 0;
            for (i = 0; i < list.size(); i++) {
                BusPosition bp = list.get(i);
                if (busPosition.getTime().equals(bp.getTime())) {
                    return false;
                }
                if (busPosition.getTime().after(bp.getTime())) {
                    break;
                }
            }
            list.add(i, busPosition);
            if (list.size() > MAX_SIZE) {
                list.remove(list.size() - 1);
            }
        } else {
            List<BusPosition> listBP = new ArrayList<BusPosition>();
            listBP.add(busPosition);
            busPositions.put(busNumber, listBP);
        }
        return true;
    }

    public BusPosition getLast(String busNumber) {
        try {
            if (busPositions.containsKey(busNumber)) {
                return busPositions.get(busNumber).get(0);
            }
        } catch (java.lang.IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return null;
    }

    public BusPosition getPrecursor(String busNumber, BusPosition busPosition) {
        if (busPositions.containsKey(busNumber)) {
            List<BusPosition> list = busPositions.get(busNumber);
            for (BusPosition bp : list) {
                if (busPosition.getTime().after(bp.getTime())) {
                    return bp;
                }
            }
        }
        return null;
    }

    public boolean isIn(String busNumber, BusPosition busPosition) {
        if (busPositions.containsKey(busNumber)) {
            List<BusPosition> list = busPositions.get(busNumber);
            if ((list.size() == MAX_SIZE)
                    && (busPosition.getTime().before(list.get(MAX_SIZE - 1).getTime()))) {
                return true;
            }
            for (BusPosition bp : list) {
                if (busPosition.getTime().equals(bp.getTime())) {
                    return true;
                }
            }
        }
        return false;
    }

}
