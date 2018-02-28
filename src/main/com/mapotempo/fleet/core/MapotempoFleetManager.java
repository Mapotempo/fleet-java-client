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
import com.mapotempo.fleet.api.model.MapotempoModelBaseInterface;
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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Nullable;

/**
 * {@inheritDoc}
 */
public class MapotempoFleetManager implements MapotempoFleetManagerInterface {

    // == General ===========================================

    private MapotempoFleetManager INSTANCE = this;

    private String mUser;

    private String mPassword;

    private Context mContext;

    public DatabaseHandler mDatabaseHandler;

    // == Access ============================================

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

    // == Location ==========================================

    private static int LOCATION_TIMEOUT = 30000; // Location timeout in ms

    private LocationManager mLocationManager = new LocationManager(null, LOCATION_TIMEOUT);

    // == Connexion sequence ================================

    private boolean mConnexionIsVerify = false;

    private boolean mChannelInit = false;

    private SubModelFactoryInterface mSubModelFactoryInterface;

    private OnServerConnexionVerify mOnServerConnexionVerify;

    private Timer mVerifyTimer = new Timer();

    private int mVerifyCounter = 0;

    private final static int MAX_VERIFY = 5;

    private boolean lockOffline = false;

    private OnServerCompatibility mOnServerCompatibility;

    // == Listener ==========================================

    private final AccessInterface.ChangeListener<User> userAccessListener = new AccessInterface.ChangeListener<User>() {
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
    };

    private final MapotempoModelBaseInterface.ChangeListener<MetaInfo> mMetaInfoListener = new MapotempoModelBaseInterface.ChangeListener<MetaInfo>() {
        @Override
        public void changed(MetaInfo item, boolean onDeletion) {
            ensureServerCompatibility(item);
        }
    };

    private DatabaseHandler.OnCatchLoginError onCatchLoginError = new DatabaseHandler.OnCatchLoginError() {
        @Override
        public void CatchLoginError() {
            finishConnexion(OnServerConnexionVerify.Status.LOGIN_ERROR);
            mDatabaseHandler.release(true);
        }
    };

    // == Constructor =======================================

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
            finishConnexion(OnServerConnexionVerify.Status.LOGIN_ERROR);
            e.printStackTrace();
        }
    }

    // == MapotempoFleetManagerInterface =====================

    /**
     * {@inheritDoc}
     */
    @Override
    public MetaInfo getMetaInfo() {
        List<MetaInfo> metaInfos = mMetaInfoAccess.getAll();
        if (metaInfos.size() > 0)
            return metaInfos.get(0);
        else
            return null;
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
        if (!lockOffline) {
            mDatabaseHandler.onlineStatus(status);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnline() {
        return mDatabaseHandler.isOnline();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void release() {
        // Release ask by user, we don't delete database
        mLocationManager.releaseManager(); // Release location manager before all
        mDatabaseHandler.release(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean serverCompatibility() {
        return serverCompatibility(getMetaInfo());
    }

    /**
     * Attache a callback to be notify if server version up during app run.
     */
    @Override
    public void addOnServerCompatibilityChange(OnServerCompatibility onServerCompatibility) {
        mOnServerCompatibility = onServerCompatibility;
    }

    // == Connexion sequence initialisation ==================

    private void initChannelsConfigurationSequence(final String userName) throws CoreException {
        // ==================================================================================================
        // == 1) - Init public channel and user channel, other channel will be set on user document reception
        // ==================================================================================================
        mDatabaseHandler.setPublicChannel();
        mDatabaseHandler.setUserChannel(userName);

        // ============================================================
        // == 2) - Set listener on user access to be notified reception
        // ============================================================
        mUserAccess.addChangeListener(userAccessListener);

        // ====================
        // == 3) - Launch timer
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
            finishConnexion(OnServerConnexionVerify.Status.LOGIN_ERROR);
            mDatabaseHandler.release(false);
        } else {

            User user = getUser();
            UserCurrentLocation userCurrentLocation = getCurrentLocation();
            UserSettings userSettings = getUserPreference();
            MetaInfo metaInfo = getMetaInfo();

            // The user document is present
            if (user != null) {

                if (!mChannelInit)
                    tryToInitchannels(user);

                // The others documents require are present
                if (userCurrentLocation != null
                        && userSettings != null
                        && metaInfo != null) {

                    // Set current location into location manager
                    mLocationManager.setCurrentLocation(userCurrentLocation);

                    // Set current location into location manager
                    metaInfo.addChangeListener(mMetaInfoListener);

                    if (!mConnexionIsVerify) {
                        mConnexionIsVerify = true;
                        mMissionAccess.purgeOutdated();

                        // Notify user
                        finishConnexion(OnServerConnexionVerify.Status.VERIFY);
                        mOnServerConnexionVerify = null;
                    }
                }
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
            finishConnexion(OnServerConnexionVerify.Status.LOGIN_ERROR);
            mDatabaseHandler.release(false);
        }
    }

    private void finishConnexion(OnServerConnexionVerify.Status status) {
        mVerifyTimer.cancel();
        mVerifyTimer.purge();

        switch (status) {
            case VERIFY:
                ensureServerCompatibility(getMetaInfo());
                mOnServerConnexionVerify.connexion(status, this);
                break;
            case LOGIN_ERROR:
            default:
                mOnServerConnexionVerify.connexion(status, null);
                break;
        }
    }

    // == Private method =====================================

    /* return true if server compatibility is ensure */
    private boolean serverCompatibility(@Nullable MetaInfo metaInfo) {
        if (metaInfo != null)
            return (metaInfo.getMinimalClientVersion() <= Config.CLIENT_VERSION);
        else
            return false;
    }

    /* switch off the syncgateway synchronisation if the client version is lower than minimal client version required */
    private void ensureServerCompatibility(@Nullable MetaInfo metaInfo) {
        boolean status = serverCompatibility(metaInfo);
        mDatabaseHandler.onlineStatus(status);
        lockOffline = !status;

        // Notify user
        if (mOnServerCompatibility != null) {
            mOnServerCompatibility.serverCompatibility(status);
        }
    }
}
