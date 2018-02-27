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

package com.mapotempo.fleet.core;

import com.couchbase.lite.Context;
import com.mapotempo.fleet.api.MapotempoFleetManagerInterface;
import com.mapotempo.fleet.api.model.accessor.AccessInterface;
import com.mapotempo.fleet.api.model.submodel.LocationDetailsInterface;
import com.mapotempo.fleet.api.model.submodel.SubModelFactoryInterface;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Company;
import com.mapotempo.fleet.core.model.MetaInfo;
import com.mapotempo.fleet.core.model.Mission;
import com.mapotempo.fleet.core.model.User;
import com.mapotempo.fleet.core.model.UserCurrentLocation;
import com.mapotempo.fleet.core.model.UserSettings;
import com.mapotempo.fleet.core.model.accessor.CompanyAccess;
import com.mapotempo.fleet.core.model.accessor.MetaInfoAccess;
import com.mapotempo.fleet.core.model.accessor.MissionAccess;
import com.mapotempo.fleet.core.model.accessor.MissionStatusAccess;
import com.mapotempo.fleet.core.model.accessor.MissionStatusActionAccess;
import com.mapotempo.fleet.core.model.accessor.MissionStatusTypeAccess;
import com.mapotempo.fleet.core.model.accessor.UserAccess;
import com.mapotempo.fleet.core.model.accessor.UserCurrentLocationAccess;
import com.mapotempo.fleet.core.model.accessor.UserSettingsAccess;
import com.mapotempo.fleet.core.model.accessor.UserTrackAccess;
import com.mapotempo.fleet.core.model.submodel.LocationDetails;
import com.mapotempo.fleet.core.model.submodel.SubModelFactory;
import com.mapotempo.fleet.utils.DateHelper;
import com.mapotempo.fleet.utils.HashHelper;
import com.mapotempo.fleet.utils.LocationManager;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@inheritDoc}
 */
public class MapotempoFleetManager implements MapotempoFleetManagerInterface {

    private MapotempoFleetManager INSTANCE = this;

    private String mUser;

    private String mPassword;

    private Context mContext;

    public DatabaseHandler mDatabaseHandler;

    private MetaInfoAccess mMetaInfoAccess;

    private CompanyAccess mCompanyAccess;

    private UserAccess mUserAccess;

    private UserSettingsAccess mUserSettingsAccess;

    private MissionAccess mMissionAccess;

    private UserTrackAccess mUserTrackAccess;

    private MissionStatusAccess mMissionStatusAccess;

    private MissionStatusTypeAccess mMissionStatusTypeAccess;

    private MissionStatusActionAccess mMissionStatusActionAccess;

    private UserCurrentLocationAccess mUserCurrentLocationAccess;

    // Location timeout in ms
    private static int LOCATION_TIMEOUT = 30000;

    private LocationManager mLocationManager = new LocationManager(null, LOCATION_TIMEOUT);

    private boolean mConnexionIsVerify = false;

    private boolean mChannelInit = false;

    private SubModelFactoryInterface mSubModelFactoryInterface;

    private OnServerConnexionVerify mOnServerConnexionVerify;

    private Timer mVerifyTimer = new Timer();

    private int mVerifyCounter = 0;

    private final static int MAX_VERIFY = 5;

    /**
     * TODO
     * {@inheritDoc}
     */
    // @Override
    public MetaInfo getMetaInfo() {
        List<MetaInfo> metaInfos = mMetaInfoAccess.getAll();
        if (metaInfos.size() > 0)
            return metaInfos.get(0);
        else
            return null;
        // return mCompanyAccess.getNew();
    }

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
            return mUserAccess.getNew();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserSettings getUserPreference() {
        List<UserSettings> users = mUserSettingsAccess.getAll();
        if (users.size() > 0)
            return users.get(0);
        else
            return null;
    }

    private UserCurrentLocation getCurrentLocation() {
        List<UserCurrentLocation> userCurrentLocations = mUserCurrentLocationAccess.getAll();
        if (userCurrentLocations.size() > 0)
            return userCurrentLocations.get(0);
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
    public MissionStatusAccess getMissionStatusAccessInterface() {
        return mMissionStatusAccess;
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
    public UserTrackAccess getTrackAccess() {
        return mUserTrackAccess;
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
        // ==================================================================================================
        // == 1) - Init public channel and user channel, other channel will be set on user document reception
        // ==================================================================================================
        mDatabaseHandler.setPublicChannel();
        mDatabaseHandler.setUserChannel(userName);

        // ============================================================
        // == 2) - Set listener on user access to be notified reception
        // ============================================================
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

        // ====================
        // == 3) - Launch mVerifyTimer
        // ====================
        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                INSTANCE.verifyConnexion();
            }
        };
        mVerifyTimer.schedule(timerTask, 2000, 2000);

        // ================================================================
        // == 4) - Verify connexion to ensure user document already present
        // ================================================================
        verifyConnexion();
    }

    private void verifyConnexion() {
        mVerifyCounter++;
        if (mVerifyCounter > MAX_VERIFY) {
            verify(OnServerConnexionVerify.Status.LOGIN_ERROR);
            mDatabaseHandler.release(false);
        } else {

            User user = getUser();
            UserCurrentLocation userCurrentLocation = getCurrentLocation();
            UserSettings userSettings = getUserPreference();
            MetaInfo metaInfo = getMetaInfo();

            if (user != null
                    && userCurrentLocation != null
                    && userSettings != null
                    && metaInfo != null) {

                // Set current location into location manager
                mLocationManager.setCurrentLocation(userCurrentLocation);

                if (!mConnexionIsVerify) {
                    mConnexionIsVerify = true;
                    mMissionAccess.purgeOutdated();

                    // Notify user
                    verify(OnServerConnexionVerify.Status.VERIFY);
                    mOnServerConnexionVerify = null;
                }

                System.out.println("MinimalClientVersion" + metaInfo.getMinimalClientVersion());
            } else if (user != null && !mChannelInit) {
                tryToInitchannels(user);
            }
        }
    }

    private void tryToInitchannels(User user) {
        try {
            mDatabaseHandler.setMissionChannel(user.getSyncUser(), DateHelper.dateForChannel(-1));
            mDatabaseHandler.setMissionChannel(user.getSyncUser(), DateHelper.dateForChannel(0));
            mDatabaseHandler.setMissionChannel(user.getSyncUser(), DateHelper.dateForChannel(1));
            mDatabaseHandler.setMissionChannel(user.getSyncUser(), DateHelper.dateForChannel(2));
            mDatabaseHandler.setCompanyChannel(user.getCompanyId());
            mDatabaseHandler.setMissionStatusTypeChannel(user.getCompanyId());
            mDatabaseHandler.setMissionStatusActionChannel(user.getCompanyId());
            mDatabaseHandler.setCurrentLocationChannel(user.getSyncUser());

            // Synchronise all mission present in the database, use getAllWithoutFilter instead of getAll.
            List<Mission> missions = mMissionAccess.getAllWithoutFilter();
            for (Mission mission : missions) {
                mDatabaseHandler.setMissionChannel(user.getSyncUser(), DateHelper.dateForChannel(mission.getDate()));
            }

            mChannelInit = true;
            mDatabaseHandler.restartPuller();
        } catch (CoreException e) {
            e.printStackTrace();
            verify(OnServerConnexionVerify.Status.LOGIN_ERROR);
            mDatabaseHandler.release(false);
        }
    }

    private void verify(OnServerConnexionVerify.Status status) {
        mVerifyTimer.cancel();
        mVerifyTimer.purge();

        switch (status) {
            case VERIFY:
                mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.VERIFY, this);
                break;
            case LOGIN_ERROR:
            default:
                mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.LOGIN_ERROR, null);
                break;
        }
    }

    @Override
    public void release() {
        // Release ask by user, we don't delete database
        mLocationManager.releaseManager(); // Release location manager before all
        mDatabaseHandler.release(false);
    }

    private void InitMapotempoFleetManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify, String url) {
        mContext = context;
        mOnServerConnexionVerify = onServerConnexionVerify;
        try {
            mUser = HashHelper.sha256(user); // Hash user to cover email case, 'see HashHelper.sha256 for more explication'.
            mPassword = password;
            mDatabaseHandler = new DatabaseHandler(mUser, password, mContext, url, onCatchLoginError);
            mSubModelFactoryInterface = new SubModelFactory(mDatabaseHandler.mDatabase);
            mMetaInfoAccess = new MetaInfoAccess(mDatabaseHandler);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
            mUserSettingsAccess = new UserSettingsAccess(mDatabaseHandler);
            mUserTrackAccess = new UserTrackAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);
            mMissionStatusActionAccess = new MissionStatusActionAccess(mDatabaseHandler);
            mUserCurrentLocationAccess = new UserCurrentLocationAccess(mDatabaseHandler);

            // Set channels
            mDatabaseHandler.initConnexion();

            initChannelsConfigurationSequence(mUser);
        } catch (CoreException e) {
            verify(OnServerConnexionVerify.Status.LOGIN_ERROR);
            e.printStackTrace();
        }
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
            verify(OnServerConnexionVerify.Status.LOGIN_ERROR);
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
            mDatabaseHandler = new DatabaseHandler("default_abcde", "default_abcde", mContext, "localhost", onCatchLoginError);
            mSubModelFactoryInterface = new SubModelFactory(mDatabaseHandler.mDatabase);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mMetaInfoAccess = new MetaInfoAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
            mUserTrackAccess = new UserTrackAccess(mDatabaseHandler);
            mMissionStatusAccess = new MissionStatusAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);
            mMissionStatusActionAccess = new MissionStatusActionAccess(mDatabaseHandler);
            mUserCurrentLocationAccess = new UserCurrentLocationAccess(mDatabaseHandler);
        } catch (CoreException e) {
            e.printStackTrace();
        }
    }

    /**
     * TODO
     *
     * @param context                 context
     * @param user                    user login
     * @param password                password
     * @param onServerConnexionVerify callback
     */
    public MapotempoFleetManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify) {
        InitMapotempoFleetManager(context, user, password, onServerConnexionVerify, "http://localhost:4984/db");
    }

    /**
     * TODO
     *
     * @param context                 context
     * @param user                    user login
     * @param password                password
     * @param onServerConnexionVerify callback
     * @param url                     server
     */
    public MapotempoFleetManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify, String url) {
        InitMapotempoFleetManager(context, user, password, onServerConnexionVerify, url);
    }
}
