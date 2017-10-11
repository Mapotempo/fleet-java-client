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
import com.mapotempo.fleet.api.model.submodel.LocationInterface;
import com.mapotempo.fleet.core.base.SubModelBase;

import java.util.HashMap;
import java.util.Map;

public class Location extends SubModelBase implements LocationInterface {
    // MAPOTEMPO KEY
    public static final String LAT = "lat";
    public static final String LON = "lon";

    private Double mLat;
    private Double mLon;

    /**
     * Location.
     *
     * @param map      map
     * @param database database
     */
    public Location(Map map, Database database) {
        super(map, database);
    }

    /**
     * Location.
     *
     * @param lat      lattide
     * @param lon      longitude
     * @param database database
     */
    public Location(double lat, double lon, Database database) {
        super(database);
        mLat = lat;
        mLon = lon;
    }

    @Override
    public void fromMap(Map map) {
        mLat = getProperty(LAT, Double.class, 0., map);
        mLon = getProperty(LON, Double.class, 0., map);
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put(LAT, mLat);
        res.put(LON, mLon);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLon() {
        return mLon;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getLat() {
        return mLat;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null)
            if (mLat == ((Location) obj).mLat)
                if (mLon == ((Location) obj).mLon)
                    return true;
        return false;
    }

    @Override
    public String toString() {
        return "(" + mLat + " ; " + mLon + ")";
    }
}
