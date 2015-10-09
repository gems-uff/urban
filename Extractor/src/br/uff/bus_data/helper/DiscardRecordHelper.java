/*
 * To change newBusPosition template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

import br.uff.bus_data.dao.LineBoundingBoxDAO;
import br.uff.bus_data.models.BusPosition;
import br.uff.bus_data.models.LineBoundingBox;
import java.sql.SQLException;
import java.util.Map;
import java.sql.Statement;
import java.util.List;

/**
 *
 * @author schettino
 */
public class DiscardRecordHelper {

    private static final float MAX_SPEED = (float) 85.57;
    private static final float MAX_DISTANCE = (float) 1.43;

    private static final Long BUS_OUT_OF_SERVICE = 1l;
    private static final Long DISTANCE_HIGHER = 2l;
    private static final Long WITHOUT_LINE = 3l;
    private static final Long REPEATED = 4l;
    private static final Long SPEED_HIGHER = 5l;
    private static final Long INVALID_DATA = 6l;
    private static final Long NOT_UPDATED = 7l;
    private static final Long BUS_AT_GARAGE = 8l;

    private final BusPositionContainer busPositionsContainer;
    private final Map<Long, LineBoundingBox> lineBoundingBoxHash;
    private final List<LineBoundingBox> garageList;

    public DiscardRecordHelper(Statement stmt) throws SQLException {
        busPositionsContainer = new BusPositionContainer(HashUtils.loadPosicoes(stmt));
        LineBoundingBoxDAO lineBoundingBoxDao = new LineBoundingBoxDAO();
        lineBoundingBoxDao.setStatement(stmt);
        lineBoundingBoxHash = lineBoundingBoxDao.all();
        garageList = Constants.getGarageList();

    }

    public DiscardRecordHelper(BusPositionContainer bpContainer, Map<Long, LineBoundingBox> lineBBHash) {
        busPositionsContainer = bpContainer;
        lineBoundingBoxHash = lineBBHash;
        garageList = Constants.getGarageList();
    }

    public Long disposalReason(BusPosition newBusPosition, BusPosition lastBusPosition, String busNumber) {

        if (discardRepeatedRecord(newBusPosition, busNumber)) {
            return REPEATED;
        }

        if (discardSpeedHigherThanMax(newBusPosition)) {
            return SPEED_HIGHER;
        }

        if (discardDistanceHigherThanMax(newBusPosition, lastBusPosition)) {
            return DISTANCE_HIGHER;
        }

        if (discardInvalidData(newBusPosition)) {
            return INVALID_DATA;
        }

        if (discardRecordWithoutLine(newBusPosition)) {
            return WITHOUT_LINE;
        }

        if (discardBusOutOfService(newBusPosition)) {
            return BUS_OUT_OF_SERVICE;
        }

        if (discardBusAtGarage(newBusPosition)) {
            return BUS_AT_GARAGE;
        }

        return null;
    }

    private boolean discardInvalidData(BusPosition newBusPosition) {
        return (newBusPosition.getTime() == null)
                || (newBusPosition.getSpeed() == null)
                || (newBusPosition.getLatitude() == null)
                || (newBusPosition.getLongitude() == null);
    }

    private boolean discardRecordWithoutLine(BusPosition newBusPosition) {
        return (newBusPosition.getLineId() == null);
    }

    private boolean discardBusOutOfService(BusPosition newBusPosition) {
        LineBoundingBox lbb = lineBoundingBoxHash.get(newBusPosition.getLineId());
        return ((lbb != null) && !lbb.isInside(newBusPosition));
    }

    private boolean discardSpeedHigherThanMax(BusPosition newBusPosition) {
        return (newBusPosition.getSpeed() > MAX_SPEED);
    }

    private boolean discardRepeatedRecord(BusPosition newBusPosition,
            String busNumber) {
        return (busPositionsContainer.isIn(busNumber, newBusPosition));
    }

    private boolean discardDistanceHigherThanMax(BusPosition newBusPosition,
            BusPosition lastBusPosition) {
        if (lastBusPosition != null) {
            double distance = LatLongConvertion.distance(newBusPosition.getLatitude(),
                    newBusPosition.getLongitude(), lastBusPosition.getLatitude(),
                    lastBusPosition.getLongitude(), 'K');
            return ((!Double.isNaN(distance)) && (distance > MAX_DISTANCE
                    * DateHelper.minutesDiff(lastBusPosition.getTime(),
                            newBusPosition.getTime())));
        }
        return false;
    }

    private boolean discardBusAtGarage(BusPosition newBusPosition) {
        if (newBusPosition.getSpeed() < (float) 1.0) {
            for (LineBoundingBox lbb : garageList) {
                if (lbb.isInside(newBusPosition)) {
                    return true;
                }
            }
        }
        return false;
    }
}
