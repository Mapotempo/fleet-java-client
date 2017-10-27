package com.mapotempo.fleet.utils;

import com.mapotempo.fleet.core.model.CurrentLocation;
import com.mapotempo.fleet.core.model.submodel.LocationDetails;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class LocationManager {
    private CurrentLocation mCurrentLocation;

    private LocationDetails mLastLocationDetails;

    private Date mLastUpdate;

    private Integer mTimeout;

    private Timer mTimer;

    private boolean lock = false;

    public LocationManager(CurrentLocation currentLocation, Integer timeout) {
        mCurrentLocation = currentLocation;
        mLastUpdate = new Date();
        mTimeout = timeout;
        mLastLocationDetails = null;
        mTimer = new Timer();
    }


    public void updateLocation(LocationDetails locationDetails) {
        mLastLocationDetails = locationDetails;
        if (mCurrentLocation != null && mLastLocationDetails != null) {
            mCurrentLocation.setLocation(mLastLocationDetails);
            Date timerDate = new Date(mLastUpdate.getTime() + mTimeout);
            if (!lock) {
                mTimer.schedule(new TimerTask() {
                    public void run() {
                        mCurrentLocation.save();
                        lock = false;
                    }
                }, timerDate);
                lock = true;
                mLastUpdate = new Date();
            }
        }
    }

    public void setCurrentLocation(CurrentLocation mCurrentLocation) {
        this.mCurrentLocation = mCurrentLocation;
        if (mLastLocationDetails != null)
            updateLocation(mLastLocationDetails);
    }
}
