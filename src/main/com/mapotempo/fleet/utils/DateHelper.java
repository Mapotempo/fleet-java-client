/*
 * Copyright © Mapotempo, 2017
 *
 * This file is part of Mapotempo.
 *
 * Mapotempo is free software. You can redistribute it and/or
 * modify since you respect the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Mapotempo is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Licenses for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Mapotempo. If not, see:
 * <http://www.gnu.org/licenses/agpl.html>
 */

package com.mapotempo.fleet.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static String toStringISO8601(Date value) {
        return sdf.format(value);
    }

    public static Date fromStringISO8601(String value) {
        try {
            return sdf.parse(value);
        } catch (ParseException e) {
            return new Date(0);
        }
    }

    // ###################################
    // Date String for SyncGateway channel
    // ###################################
    private static SimpleDateFormat sdf_for_channel = new SimpleDateFormat("yyyyMMdd");

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
        if (value != null)
            return sdf_for_display.format(value);
        else
            return null;
    }
}
