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
import com.mapotempo.fleet.api.model.submodel.LocationDetailsInterface;
import com.mapotempo.fleet.utils.DateHelper;

import java.util.Date;
import java.util.Map;

public class LocationDetails extends Location implements LocationDetailsInterface {

    public static final String TIME = "time";
    public static final String ACCURACY = "accuracy";
    public static final String SPEED = "speed";
    public static final String BEARING = "bearing";
    public static final String ALTITUDE = "altitude";
    public static final String PROVIDER = "provider";
    public static final String SIGNAL_STRENGTH = "signal_strength";
    public static final String CID = "cid";
    public static final String LAC = "lac";
    public static final String MCC = "mcc";
    public static final String MNC = "mnc";

    private Date mDate;

    private double mAccuracy;

    private double mSpeed;

    private double mBearing;

    private double mElevation;

    private double mSignalStrength;

    private String mCid;

    private String mLac;

    private String mMcc;

    private String mMnc;

    /**
     * LocationDetails.
     *
     * @param map      map
     * @param database database
     */
    public LocationDetails(Map map, Database database) {
        super(map, database);
    }

    /**
     * LocationDetails.
     *
     * @param lat            lattide
     * @param lon            longitude
     * @param date           date
     * @param accuracy       accuracy
     * @param speed          speed
     * @param bearing        bearing
     * @param elevation      elevation
     * @param signalStrength signal strength
     * @param cid            cell id
     * @param lac            location area code
     * @param mcc            mobile country code
     * @param mnc            mobile network code
     * @param database       database
     */
    public LocationDetails(double lat,
                           double lon,
                           Date date,
                           double accuracy,
                           double speed,
                           double bearing,
                           double elevation,
                           int signalStrength,
                           String cid,
                           String lac,
                           String mcc,
                           String mnc,
                           Database database) {
        super(lat, lon, database);
        mDate = date;
        mAccuracy = accuracy;
        mSpeed = speed;
        mBearing = bearing;
        mElevation = elevation;
        mSignalStrength = signalStrength;
        mCid = cid;
        mLac = lac;
        mMcc = mcc;
        mMnc = mnc;
    }

    @Override
    public void fromMap(Map map) {
        super.fromMap(map);
        String str = (String) map.get(TIME);
        mDate = DateHelper.fromStringISO8601(str);
        //mAccuracy = (int) map.get(ACCURACY);
        //mSpeed = (int) map.get(SPEED);
        //mBearing = (int) map.get(BEARING);
        //mElevation = (int) map.get(ALTITUDE);
        //mSignalStrength = (int) map.get(SIGNAL_STRENGTH);
        mCid = getProperty(CID, String.class, "-1", map);
        mLac = getProperty(LAC, String.class, "-1", map);
        mMcc = getProperty(MCC, String.class, "-1", map);
        mMnc = getProperty(MNC, String.class, "-1", map);
    }

    @Override
    public Map<String, String> toMap() {
        Map res = super.toMap();
        res.put(TIME, DateHelper.toStringISO8601(mDate));
        res.put(ACCURACY, mAccuracy);
        res.put(SPEED, mSpeed);
        res.put(BEARING, mBearing);
        res.put(ALTITUDE, mElevation);
        res.put(SIGNAL_STRENGTH, mSignalStrength);
        res.put(CID, mCid);
        res.put(LAC, mLac);
        res.put(MCC, mMcc);
        res.put(MNC, mMnc);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate() {
        return mDate;
    }

    @Override
    public String toString() {
        return "(" + mDate + " ; " + super.toString() + ")";
    }
}
