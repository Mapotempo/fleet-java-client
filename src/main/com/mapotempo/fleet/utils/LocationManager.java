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


    // This function select the best location
    //  - Calcul distance between position
    //  - If new position's accuracy include last point with it's accuracy new position is rejected
    private LocationDetails selectBestLocation(@Nonnull LocationDetails locationDetails) {
        if (mLastLocationDetails == null) {
            return locationDetails;
        } else if (accuracyTester(locationDetails, mLastLocationDetails)) {
            return locationDetails;
        } else {
            return mLastLocationDetails;
        }
    }

    private boolean accuracyTester(@Nonnull LocationDetails newLoc, @Nonnull LocationDetails oldLoc) {
        Double dist = Haversine.distance(newLoc.getLat(), newLoc.getLon(), oldLoc.getLat(), oldLoc.getLon());
        if (dist + oldLoc.getmAccuracy() > newLoc.getmAccuracy()) {
            return true;
        } else
            return false;
    }
}
