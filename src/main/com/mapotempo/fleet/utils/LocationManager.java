package com.mapotempo.fleet.utils;

import com.mapotempo.fleet.core.model.UserCurrentLocation;
import com.mapotempo.fleet.core.model.submodel.LocationDetails;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class LocationManager {
    private UserCurrentLocation mUserCurrentLocation;

    private LocationDetails mLastLocationDetails;

    private Date mLastUpdate;

    private Integer mTimeout;

    private Timer mTimer;

    private boolean lock = false;

    public LocationManager(UserCurrentLocation userCurrentLocation, Integer timeout) {
        mUserCurrentLocation = userCurrentLocation;
        mLastUpdate = new Date();
        mTimeout = timeout;
        mLastLocationDetails = null;
        mTimer = new Timer();
    }


    public void updateLocation(LocationDetails locationDetails) {
        mLastLocationDetails = locationDetails;
        if (mUserCurrentLocation != null && mLastLocationDetails != null) {
            mUserCurrentLocation.setLocation(mLastLocationDetails);
            Date timerDate = new Date(mLastUpdate.getTime() + mTimeout);
            if (!lock) {
                mTimer.schedule(new TimerTask() {
                    public void run() {
                        mUserCurrentLocation.save();
                        lock = false;
                    }
                }, timerDate);
                lock = true;
                mLastUpdate = new Date();
            }
        }
    }

    public void setCurrentLocation(UserCurrentLocation mUserCurrentLocation) {
        this.mUserCurrentLocation = mUserCurrentLocation;
        if (mLastLocationDetails != null)
            updateLocation(mLastLocationDetails);
    }
}
