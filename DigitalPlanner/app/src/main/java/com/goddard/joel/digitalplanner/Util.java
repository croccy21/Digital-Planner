package com.goddard.joel.digitalplanner;

import java.util.Calendar;

/**
 * Created by Joel Goddard on 11/02/2016.
 *
 * @author Joel Goddard
 */
public class Util {
    public static Calendar setDateToStart(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        return date;
    }

    public static int daysBetweenCalendars(Calendar a, Calendar b){
        a = setDateToStart(a);
        b = setDateToStart(b);
        long aT = a.getTimeInMillis();
        long bT = b.getTimeInMillis();
        long usDiff = aT-bT;
        return (int) (usDiff / (24*60*60*1000));
    }
}
