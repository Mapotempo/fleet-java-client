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

package com.mapotempo.fleet.core;

import com.couchbase.lite.Context;
import com.mapotempo.fleet.api.MapotempoFleetManagerInterface;
import com.mapotempo.fleet.api.model.accessor.AccessInterface;
import com.mapotempo.fleet.api.model.submodel.LocationDetailsInterface;
import com.mapotempo.fleet.api.model.submodel.SubModelFactoryInterface;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Company;
import com.mapotempo.fleet.core.model.CurrentLocation;
import com.mapotempo.fleet.core.model.Mission;
import com.mapotempo.fleet.core.model.User;
import com.mapotempo.fleet.core.model.accessor.*;
import com.mapotempo.fleet.core.model.submodel.LocationDetails;
import com.mapotempo.fleet.core.model.submodel.SubModelFactory;
import com.mapotempo.fleet.utils.DateHelper;
import com.mapotempo.fleet.utils.LocationManager;

import java.util.List;

/**
 * {@inheritDoc}
 */
public class MapotempoFleetManager implements MapotempoFleetManagerInterface {

    private Context mContext;

    public DatabaseHandler mDatabaseHandler;

    private CompanyAccess mCompanyAccess;

    private UserAccess mUserAccess;

    private MissionAccess mMissionAccess;

    private TrackAccess mTrackAccess;

    private MissionStatusTypeAccess mMissionStatusTypeAccess;

    private MissionStatusActionAccess mMissionStatusActionAccess;

    private CurrentLocationAccess mCurrentLocationAccess;

    private static int LOCATION_TIMEOUT = 1000;
    private LocationManager mLocationManager = new LocationManager(null, LOCATION_TIMEOUT);

    private boolean mConnexionIsVerify = false;

    private boolean mChannelInit = false;

    private SubModelFactoryInterface mSubModelFactoryInterface;

    private OnServerConnexionVerify mOnServerConnexionVerify;

    /**
     * {@inheritDoc}
     */
    @Override
    public Company getCompany() {
        List<Company> companies = mCompanyAccess.getAll();
        if (companies.size() > 0)
            return companies.get(0);
        else
            return null;
        // return mCompanyAccess.getNew();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser() {
        List<User> users = mUserAccess.getAll();
        if (users.size() > 0)
            return users.get(0);
        else
            return null;
    }

    private CurrentLocation getCurrentLocation() {
        List<CurrentLocation> currentLocations = mCurrentLocationAccess.getAll();
        if (currentLocations.size() > 0)
            return currentLocations.get(0);
        else
            return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDetailsInterface getCurrentLocationDetails() {
        return getCurrentLocation().getLocation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentLocationDetails(LocationDetailsInterface locationDetailsInterface) {
        mLocationManager.updateLocation((LocationDetails) locationDetailsInterface);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionAccess getMissionAccess() {
        return mMissionAccess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionStatusTypeAccess getMissionStatusTypeAccessInterface() {
        return mMissionStatusTypeAccess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionStatusActionAccess getMissionStatusActionAccessInterface() {
        return mMissionStatusActionAccess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrackAccess getTrackAccess() {
        return mTrackAccess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SubModelFactoryInterface getSubmodelFactory() {
        return mSubModelFactoryInterface;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onlineStatus(boolean status) {
        mDatabaseHandler.onlineStatus(status);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnline() {
        return mDatabaseHandler.isOnline();
    }

    private void initChannelsConfigurationSequence(final String userName) throws CoreException {

        mDatabaseHandler.setUserChannel(userName);

        // Ecoute des Documents User
        mUserAccess.addChangeListener(new AccessInterface.ChangeListener<User>() {
            @Override
            public void changed(List<User> items) {
                if (items.size() > 0) {
                    if (items.size() > 1)
                        System.err.println("Warning : " + getClass().getSimpleName() + " more than one user available, return the first");

                    // When user is received we can add channel
                    if (!mChannelInit) {
                        tryToInitchannels(items.get(0));
                    }

                } else {
                    System.err.println("Warning : " + getClass().getSimpleName() + "no user found");
                }
                verifyConnexion();
            }
        });
        verifyConnexion();
    }

    private void verifyConnexion() {
        User user = getUser();
        CurrentLocation currentLocation = getCurrentLocation();

        if (user != null && currentLocation != null) {
            mLocationManager.setCurrentLocation(currentLocation);

            if (!mChannelInit) {
                tryToInitchannels(user);
                // Set current location into location manager
            }

            if (!mConnexionIsVerify) {
                mConnexionIsVerify = true;
                mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.VERIFY, this);
            }
        }
    }

    private void tryToInitchannels(User user) {
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(-4));
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(-3));
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(-2));
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(-1));
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(0));
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(1));
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(2));
        mDatabaseHandler.setCompanyChannel(user.getCompanyId());
        mDatabaseHandler.setMissionStatusTypeChannel(user.getCompanyId());
        mDatabaseHandler.setMissionStatusActionChannel(user.getCompanyId());
        mDatabaseHandler.setCurrentLocationChannel(user.getUser());

        // Synchronise all mission present in the database.
        List<Mission> missions = mMissionAccess.getAll();
        for (Mission mission : missions) {
            mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(mission.getDate()));
        }

        mChannelInit = true;
        mDatabaseHandler.restartPuller();
    }

    @Override
    public void release() {
        // Release ask by user, we don't delete database
        mDatabaseHandler.release(false);
    }

    /**
     * todo
     *
     * @param context
     * @return return a {@link MapotempoFleetManagerInterface}
     */
    public static MapotempoFleetManagerInterface getDefaultManager(Context context) {
        return new MapotempoFleetManager(context);
    }

    private DatabaseHandler.OnCatchLoginError onCatchLoginError = new DatabaseHandler.OnCatchLoginError() {
        @Override
        public void CatchLoginError() {
            mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.LOGIN_ERROR, null);
            mDatabaseHandler.release(true);
        }
    };

    /**
     * Default manager.
     *
     * @param context java context
     */
    public MapotempoFleetManager(Context context) {
        mContext = context;
        try {
            mDatabaseHandler = new DatabaseHandler("default_abcde", "default_abcde", mContext, onCatchLoginError);
            mSubModelFactoryInterface = new SubModelFactory(mDatabaseHandler.mDatabase);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
            mTrackAccess = new TrackAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);
            mMissionStatusActionAccess = new MissionStatusActionAccess(mDatabaseHandler);
            mCurrentLocationAccess = new CurrentLocationAccess(mDatabaseHandler);
        } catch (CoreException e) {
            e.printStackTrace();
        }
        ;
    }


    /**
     * TODO
     *
     * @param context
     * @param user
     * @param password
     * @param onServerConnexionVerify
     */
    public MapotempoFleetManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify) {
        mContext = context;
        mOnServerConnexionVerify = onServerConnexionVerify;

        try {
            mDatabaseHandler = new DatabaseHandler(user, password, mContext, onCatchLoginError);
            mSubModelFactoryInterface = new SubModelFactory(mDatabaseHandler.mDatabase);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
            mTrackAccess = new TrackAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);
            mMissionStatusActionAccess = new MissionStatusActionAccess(mDatabaseHandler);
            mCurrentLocationAccess = new CurrentLocationAccess(mDatabaseHandler);

            // Set channels
            mDatabaseHandler.initConnexion("http://localhost:4984/db");
            initChannelsConfigurationSequence(user);
        } catch (CoreException e) {
            mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.LOGIN_ERROR, null);
            e.printStackTrace();
        }
        ;
    }

    /**
     * TODO
     *
     * @param context
     * @param user
     * @param password
     * @param onServerConnexionVerify
     * @param url
     */
    public MapotempoFleetManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify, String url) {
        mContext = context;
        mOnServerConnexionVerify = onServerConnexionVerify;

        try {
            mDatabaseHandler = new DatabaseHandler(user, password, mContext, onCatchLoginError);
            mSubModelFactoryInterface = new SubModelFactory(mDatabaseHandler.mDatabase);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
            mTrackAccess = new TrackAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);
            mMissionStatusActionAccess = new MissionStatusActionAccess(mDatabaseHandler);
            mCurrentLocationAccess = new CurrentLocationAccess(mDatabaseHandler);

            // Set channels
            mDatabaseHandler.initConnexion(url);

            initChannelsConfigurationSequence(user);
        } catch (CoreException e) {
            mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.LOGIN_ERROR, null);
            e.printStackTrace();
        }
    }
}
