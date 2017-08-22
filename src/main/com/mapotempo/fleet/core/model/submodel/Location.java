package com.mapotempo.fleet.core.model.submodel;

import com.couchbase.lite.Database;
import com.mapotempo.fleet.core.base.SubModelBase;

import java.util.HashMap;
import java.util.Map;

public class Location extends SubModelBase {
    // MAPOTEMPO KEY
    public static final String LAT = "lat";
    public static final String LON = "lon";

    private double mLat;
    private double mLon;

    /**
     * Location.
     * @param map map
     * @param database database
     */
    public Location(Map map, Database database) {
        super(map, database);
    }

    /**
     * Location.
     * @param lat lattide
     * @param lon longitude
     * @param database database
     */
    public Location(double lat, double lon, Database database) {
        super(database);
        this.mLat = lat;
        this.mLon = lon;
    }

    @Override
    public void fromMap(Map map) {
        this.mLat = Double.valueOf(map.get(LAT).toString());
        this.mLon = Double.valueOf(map.get(LON).toString());
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> res = new HashMap<>();
        res.put(LAT, Double.toString(mLat));
        res.put(LON, Double.toString(mLon));
        return res;
    }

    public double getLon() {
        return mLon;
    }

    public double getLat() {
        return mLat;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null)
            if(this.mLat == ((Location)obj).mLat)
                if(this.mLon == ((Location)obj).mLon)
                    return true;
        return false;
    }

    @Override
    public String toString() {
        return "(" + mLat + " ; " + mLon + ")";
    }
}
