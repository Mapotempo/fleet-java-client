package com.mapotempo.fleet.utils;

public class Haversine {

    private static final int EARTH_RADIUS = 6371008; // Earth radius in meter

    public static double distance(double startLat, double startLon,
                                  double endLat, double endLon) {

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLon - startLon));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}

