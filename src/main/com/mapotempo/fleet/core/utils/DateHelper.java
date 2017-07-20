package com.mapotempo.fleet.core.utils;

import java.util.Calendar;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * DateHelper.
 */
public class DateHelper {

    private static DateHelper ourInstance = new DateHelper();

    public static DateHelper getInstance() {
        return ourInstance;
    }

    private DateHelper() {
    }

    // ####################################
    // ISO8601 date String
    // ####################################
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    public static String dateToString(Date value) {
        return sdf.format(value);
    }

    public static Date dateFromString(String value) {
        try {
            return sdf.parse(value);
        } catch (ParseException e)
        {
            return new Date(0);
        }
    }

    // ###################################
    // Date String for SyncGateway channel
    // ###################################
    private static SimpleDateFormat sdf_for_channel = new SimpleDateFormat("ddMMyyyy");

    public static String dateForChannel(int dayOffset) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.DATE, dayOffset);
        return sdf_for_channel.format(calendar.getTime());
    }

    // #########################
    // Date String for the debug
    // #########################
    private static SimpleDateFormat sdf_for_display = new SimpleDateFormat("dd MMMMMMM yyyy ':' hh'H' mm'M' ss's' SSS'ms'");

    public static String displayDate(Date value) {
        if(value != null)
            return sdf_for_display.format(value);
        else
            return null;
    }
}
