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
import com.mapotempo.fleet.api.ConnectionVerifyStatus;
import com.mapotempo.fleet.api.MapotempoFleetManagerInterface;
import com.mapotempo.fleet.api.model.MapotempoModelBaseInterface;
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
import com.mapotempo.fleet.core.model.accessor.MissionActionAccess;
import com.mapotempo.fleet.core.model.accessor.MissionActionTypeAccess;
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

    private MissionActionAccess mMissionActionAccess;

    private MissionStatusTypeAccess mMissionStatusTypeAccess;

    private MissionActionTypeAccess mMissionActionTypeAccess;

    private UserCurrentLocationAccess mUserCurrentLocationAccess;

    private SubModelFactoryInterface mSubModelFactoryInterface;

    // == Location ==========================================

    private static int LOCATION_TIMEOUT = 30000; // Location timeout in ms

    private LocationManager mLocationManager = new LocationManager(null, LOCATION_TIMEOUT);


    // == Connection sequence ================================

    private ConnectionRequirement mConnectionRequirement;


    private boolean mLockOffline = false;

    // == Listener ==========================================

    private ConnectionVerifyListener mOnServerConnectionVerify;

    private OnServerCompatibility mOnServerCompatibility;

    private ConnectionRequirement.ConnectionRequirementVerifyListener mConnectionRequirementVerifyListener = new ConnectionRequirement.ConnectionRequirementVerifyListener() {
        public void onConnectionVerify(User user, UserCurrentLocation userCurrentLocation, UserSettings userSettings, Company company, MetaInfo metaInfo) {
            // Set current location into location manager
            mLocationManager.setCurrentLocation(userCurrentLocation);

            // Set current location into location manager
            metaInfo.addChangeListener(mMetaInfoListener);

            mMissionAccess.purgeOutdated();

            // Synchronise all mission present in the database, use getAllWithoutFilter instead of getAll.
            List<Mission> missions = mMissionAccess.getAllWithoutFilter();
            for (Mission mission : missions) {
                mDatabaseHandler.setMissionChannel(user.getSyncUser(), DateHelper.dateForChannel(mission.getDate()));

            }

            // Call only one time
            if (mOnServerConnectionVerify != null) {
                mOnServerConnectionVerify.onServerConnectionVerify(ConnectionVerifyStatus.VERIFY, INSTANCE);
                mOnServerConnectionVerify = null;
            }
        }

        public void onConnectionFail(ConnectionVerifyStatus code) {
            mConnectionRequirement.stopConnectionRequirementSequence();
            mDatabaseHandler.onlineStatus(false);
            mDatabaseHandler.release(true);
            mOnServerConnectionVerify.onServerConnectionVerify(code, null);
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
            mConnectionRequirement.stopConnectionRequirementSequence();
            mDatabaseHandler.onlineStatus(false);
            mDatabaseHandler.release(true);
            mOnServerConnectionVerify.onServerConnectionVerify(ConnectionVerifyStatus.LOGIN_ERROR, null);
        }
    };

    // == Constructor =======================================

    /**
     * TODO
     *
     * @param context                  context
     * @param user                     user login
     * @param password                 password
     * @param onServerConnectionVerify callback
     */
    public MapotempoFleetManager(Context context, String user, String password, ConnectionVerifyListener onServerConnectionVerify) {
        InitMapotempoFleetManager(context, user, password, onServerConnectionVerify, "http://localhost:4984/db");
    }

    /**
     * TODO
     *
     * @param context                  context
     * @param user                     user login
     * @param password                 password
     * @param onServerConnectionVerify callback
     * @param url                      server
     */
    public MapotempoFleetManager(Context context, String user, String password, ConnectionVerifyListener onServerConnectionVerify, String url) {
        InitMapotempoFleetManager(context, user, password, onServerConnectionVerify, url);
    }

    private void InitMapotempoFleetManager(Context context, String user, String password, ConnectionVerifyListener onServerConnectionVerify, String url) {
        mContext = context;
        mOnServerConnectionVerify = onServerConnectionVerify;
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
            mMissionActionAccess = new MissionActionAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);
            mMissionActionTypeAccess = new MissionActionTypeAccess(mDatabaseHandler);
            mUserCurrentLocationAccess = new UserCurrentLocationAccess(mDatabaseHandler);

            mConnectionRequirement = new ConnectionRequirement(mDatabaseHandler,
                    mConnectionRequirementVerifyListener,
                    mUserAccess,
                    mUserSettingsAccess,
                    mUserCurrentLocationAccess,
                    mCompanyAccess,
                    mMetaInfoAccess);

            mConnectionRequirement.InitConnectionRequirementSequence();

        } catch (CoreException e) {
            if (mConnectionRequirement != null)
                mConnectionRequirement.stopConnectionRequirementSequence();
            mOnServerConnectionVerify.onServerConnectionVerify(ConnectionVerifyStatus.LOGIN_ERROR, null);
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
    public MissionActionAccess getMissionActionAccessInterface() {
        return mMissionActionAccess;
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
    public MissionActionTypeAccess getMissionActionTypeAccessInterface() {
        return mMissionActionTypeAccess;
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
        if (!mLockOffline) {
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
        mLockOffline = !status;

        // Notify user
        if (mOnServerCompatibility != null) {
            mOnServerCompatibility.serverCompatibility(status);
        }
    }
}
