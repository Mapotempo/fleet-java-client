package com.mapotempo.fleet.core.accessor;

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

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    private static SimpleDateFormat sdf_for_display = new SimpleDateFormat("dd MMMMMMM yyyy ':' hh'H' mm'M' ss's' SSS'ms'");

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

    public static String displayDate(Date value) {
        return sdf_for_display.format(value);
    }
}
