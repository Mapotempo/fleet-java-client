package com.mapotempo.fleet.model.submodel;

import com.mapotempo.fleet.core.base.SubModelBase;
import com.mapotempo.fleet.core.DatabaseHandler;

import java.util.HashMap;
import java.util.Map;

public class Location  extends SubModelBase
{
    public Location(Map map, DatabaseHandler databaseHandler) {
        super(map, databaseHandler);
    }

    /**
     * Location.
     * @param lat lattide
     * @param lon longitude
     */
    public Location(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void fromMap(Map map) {
        this.lon = Double.valueOf(map.get("lon").toString());
        this.lat = Double.valueOf(map.get("lat").toString());
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> res = new HashMap<>();
        res.put("lat", Double.toString(lat));
        res.put("lon", Double.toString(lon));
        return res;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double lat;
    public double lon;

    @Override
    public boolean equals(Object obj) {
        if(obj != null)
            if(this.lat == ((Location)obj).lat)
                if(this.lon == ((Location)obj).lon)
                    return true;
        return false;
    }

    @Override
    public String toString() {
        return "(" + lat + " ; " + lon + ")";
    }
}
