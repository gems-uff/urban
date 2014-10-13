/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.bus_data.helper;

/**
 *
 * @author gerente
 */
import java.util.Date;

public class DateHelper {
    public static int minutesDiff(Date earlierDate, Date laterDate) {
        if (earlierDate == null || laterDate == null) {
            return 0;
        }

        return (int) ((laterDate.getTime() / 60000) - (earlierDate.getTime() / 60000));
    }
}
