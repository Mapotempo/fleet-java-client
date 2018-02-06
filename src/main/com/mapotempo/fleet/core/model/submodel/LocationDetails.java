/*
 * Copyright © Mapotempo, 2018
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

    public static final String DATE = "date";
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

    private Double mAccuracy;

    private Double mSpeed;

    private Double mBearing;

    private Double mAltitude;

    private Integer mSignalStrength;

    private Integer mCid;

    private Integer mLac;

    private Integer mMcc;

    private Integer mMnc;

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
     * @param lat            latitude
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
    public LocationDetails(Double lat,
                           Double lon,
                           Date date,
                           Double accuracy,
                           Double speed,
                           Double bearing,
                           Double elevation,
                           Integer signalStrength,
                           Integer cid,
                           Integer lac,
                           Integer mcc,
                           Integer mnc,
                           Database database) {
        super(lat, lon, database);
        mDate = date;
        mAccuracy = accuracy;
        mSpeed = speed;
        mBearing = bearing;
        mAltitude = elevation;
        mSignalStrength = signalStrength;
        mCid = cid;
        mLac = lac;
        mMcc = mcc;
        mMnc = mnc;
    }

    @Override
    public void fromMap(Map map) {
        super.fromMap(map);
        String str = getProperty(DATE, String.class, "", map);
        mDate = DateHelper.fromStringISO8601(str);
        mAccuracy = getProperty(ACCURACY, Double.class, 0., map);
        mSpeed = getProperty(SPEED, Double.class, 0., map);
        mBearing = getProperty(BEARING, Double.class, 0., map);
        mAltitude = getProperty(ALTITUDE, Double.class, 0., map);
        mSignalStrength = getProperty(SIGNAL_STRENGTH, Integer.class, 0, map);
        mCid = getProperty(CID, Integer.class, -1, map);
        mLac = getProperty(LAC, Integer.class, -1, map);
        mMcc = getProperty(MCC, Integer.class, -1, map);
        mMnc = getProperty(MNC, Integer.class, -1, map);
    }

    @Override
    public Map<String, Object> toMap() {
        Map res = super.toMap();
        res.put(DATE, DateHelper.toStringISO8601(mDate));
        res.put(ACCURACY, mAccuracy);
        res.put(SPEED, mSpeed);
        res.put(BEARING, mBearing);
        res.put(ALTITUDE, mAltitude);
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

    public Double getmAccuracy() {
        return mAccuracy;
    }

    public Double getSpeed() {
        return mSpeed;
    }

    public Double getBearing() {
        return mBearing;
    }

    public Double getAltitude() {
        return mAltitude;
    }

    public Integer getSignalStrength() {
        return mSignalStrength;
    }

    public Integer getCid() {
        return mCid;
    }

    public Integer getLac() {
        return mLac;
    }

    public Integer getMcc() {
        return mMcc;
    }

    public Integer getMnc() {
        return mMnc;
    }

    @Override
    public String toString() {
        return "(" + mDate + " ; " + super.toString() + ")";
    }


    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            LocationDetails cmp = (LocationDetails) obj;
            if (cmp.toMap().equals(((LocationDetails) obj).toMap()))
                return true;
        }
        return false;
    }
}
