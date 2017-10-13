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

package com.mapotempo.fleet.core.model.submodel;

import com.couchbase.lite.Database;
import com.mapotempo.fleet.api.model.submodel.TimeWindowsInterface;
import com.mapotempo.fleet.core.base.SubModelBase;
import com.mapotempo.fleet.utils.DateHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeWindow extends SubModelBase implements TimeWindowsInterface {
    // MAPOTEMPO KEY
    public static final String START = "start";
    public static final String END = "end";

    private Date mStart;
    private Date mEnd;

    /**
     * TimeWindow.
     *
     * @param map      map
     * @param database database
     */
    public TimeWindow(Map map, Database database) {
        super(map, database);
    }

    /**
     * TimeWindow.
     *
     * @param start    start time
     * @param end      end time
     * @param database database
     */
    public TimeWindow(Date start, Date end, Database database) {
        super(database);
        mStart = start;
        mEnd = end;
    }

    @Override
    public void fromMap(Map map) {
        String str = getProperty(START, String.class, "", map);
        mStart = DateHelper.fromStringISO8601(str);
        str = getProperty(END, String.class, "", map);
        mEnd = DateHelper.fromStringISO8601(str);
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put(START, DateHelper.toStringISO8601(mStart));
        res.put(END, DateHelper.toStringISO8601(mEnd));
        return res;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            TimeWindow tmp = (TimeWindow) obj;
            if (mStart.equals(tmp.mStart))
                if (mEnd.equals(tmp.mEnd))
                    return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getStart() {
        return mStart;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getEnd() {
        return mEnd;
    }

    @Override
    public String toString() {
        return "(" + mStart + " ; " + mEnd + ")";
    }
}
