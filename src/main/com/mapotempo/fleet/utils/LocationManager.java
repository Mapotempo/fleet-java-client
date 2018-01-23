package com.mapotempo.fleet.utils;

import com.mapotempo.fleet.core.model.UserCurrentLocation;
import com.mapotempo.fleet.core.model.submodel.LocationDetails;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nonnull;

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


    public void updateLocation(@Nonnull LocationDetails locationDetails) {
        mLastLocationDetails = selectBestLocation(locationDetails);
        if (mUserCurrentLocation != null && mLastLocationDetails != null) {
            mUserCurrentLocation.setLocation(mLastLocationDetails);
            initTimerSave();
        }
    }

    private LocationDetails selectBestLocation(@Nonnull LocationDetails locationDetails) {
        if (mLastLocationDetails == null || mLastLocationDetails.getmAccuracy() > locationDetails.getmAccuracy()) {
            return locationDetails;
        } else {
            return mLastLocationDetails;
        }
    }

    private void initTimerSave() {
        Date timerDate = new Date(mLastUpdate.getTime() + mTimeout);
        if (!lock) {
            mTimer.schedule(new TimerTask() {
                public void run() {
                    mUserCurrentLocation.save();
                    mLastLocationDetails = null;
                    lock = false;
                }
            }, timerDate);
            lock = true;
            mLastUpdate = new Date();
        }
    }

    public void setCurrentLocation(UserCurrentLocation mUserCurrentLocation) {
        this.mUserCurrentLocation = mUserCurrentLocation;
        if (mLastLocationDetails != null)
            updateLocation(mLastLocationDetails);
    }

    public LocationDetails getNextAvailableLocation() {
        return mLastLocationDetails;

    }

    public void releaseManager() {
        // Terminates this timer, discarding any currently scheduled tasks.
        mTimer.cancel();
        // Removes all cancelled tasks from this timer's task queue.
        mTimer.purge();
    }
}
