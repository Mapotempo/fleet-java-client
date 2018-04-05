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

import com.mapotempo.fleet.api.ConnectionVerifyStatus;
import com.mapotempo.fleet.api.model.accessor.AccessInterface;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Company;
import com.mapotempo.fleet.core.model.MetaInfo;
import com.mapotempo.fleet.core.model.User;
import com.mapotempo.fleet.core.model.UserCurrentLocation;
import com.mapotempo.fleet.core.model.UserSettings;
import com.mapotempo.fleet.core.model.accessor.CompanyAccess;
import com.mapotempo.fleet.core.model.accessor.MetaInfoAccess;
import com.mapotempo.fleet.core.model.accessor.UserAccess;
import com.mapotempo.fleet.core.model.accessor.UserCurrentLocationAccess;
import com.mapotempo.fleet.core.model.accessor.UserSettingsAccess;
import com.mapotempo.fleet.utils.DateHelper;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class ensure that after login all minimals documents data are download.
 * The minimal documents data required are :
 * --------------------------------------------------------------
 * |       Document         |   Channel                         |
 * --------------------------------------------------------------
 * |    User                |   user:[sync gateway user name]   |
 * |    UserSettings        |   user:[sync gateway user name]   |
 * |    UserCurrentLocation |   user:[sync gateway user name]   |
 * |    Company             |   company:[company id]            |
 * |    MetaInfo            |   !                               |
 * --------------------------------------------------------------
 * Company document should be access only when the user channel are download, because [company id]
 * information require to set channel are only accessible in User.
 */
public class ConnectionRequirement {

    // == Private attribute =================================
    private ConnectionRequirement INSTANCE = this;

    private DatabaseHandler mDatabaseHandler;

    private UserAccess mUserAccess;

    private UserSettingsAccess mUserSettingsAccess;

    private UserCurrentLocationAccess mUserCurrentLocationAccess;

    private CompanyAccess mCompanyAccess;

    private MetaInfoAccess mMetaInfoAccess;

    private boolean mChannelInit = false;

    private Timer mVerifyTimer = new Timer();

    private int mVerifyCounter = 0;

    private final static int MAX_VERIFY = 15;

    private final AccessInterface.ChangeListener<User> mUserAccessListener = new AccessInterface.ChangeListener<User>() {
        @Override
        public void changed(List<User> items) {
            if (items.size() > 0) {
                if (items.size() > 1)
                    System.err.println("Warning : " + getClass().getSimpleName() + " more than one user available, return the first");

                // When user is received we can add channel
                initChannels(items.get(0));

            } else {
                System.err.println("Warning : " + getClass().getSimpleName() + "no user found");
            }
            verifyConnection();
        }
    };

    private ConnectionRequirementVerifyListener mConnectionRequirementVerifyListener;

    // == Private Interface =================================

    public interface ConnectionRequirementVerifyListener {
        void onConnectionVerify(User user, UserCurrentLocation userCurrentLocation, UserSettings userSettings, Company company, MetaInfo metaInfo);

        void onConnectionFail(ConnectionVerifyStatus code);
    }

    public ConnectionRequirement(DatabaseHandler databaseHandler,
                                 ConnectionRequirementVerifyListener connectionRequirementVerifyListener,
                                 UserAccess userAccess,
                                 UserSettingsAccess userSettingsAccess,
                                 UserCurrentLocationAccess userCurrentLocationAccess,
                                 CompanyAccess companyAccess,
                                 MetaInfoAccess metaInfoAccess) throws CoreException {
        mDatabaseHandler = databaseHandler;
        mConnectionRequirementVerifyListener = connectionRequirementVerifyListener;
        mUserAccess = userAccess;
        mUserCurrentLocationAccess = userCurrentLocationAccess;
        mUserSettingsAccess = userSettingsAccess;
        mCompanyAccess = companyAccess;
        mMetaInfoAccess = metaInfoAccess;
    }

    public void InitConnectionRequirementSequence() throws CoreException {
        // ==================================================================================================
        // == 1) - Init user channel, other channel will be set on user document reception
        // ==================================================================================================
        mDatabaseHandler.setUserChannel();
        mDatabaseHandler.restartPuller();

        // ================================================================
        // == 2) - Verify connection to ensure user document already present
        // ================================================================
        if (!verifyConnection()) {

            // ============================================================
            // == 3) - Set listener on user access to be notified reception
            // ============================================================
            mUserAccess.addChangeListener(mUserAccessListener);

            // ====================
            // == 4) - Launch timer
            // ====================
            final TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    INSTANCE.verifyConnection();
                }
            };
            mVerifyTimer.schedule(timerTask, 2000, 2000);
        }
    }

    public void stopConnectionRequirementSequence() {
        mVerifyTimer.cancel();
        mVerifyTimer.purge();
    }

    private boolean verifyConnection() {
        User user = mUserAccess.getAll().size() > 0 ? (User) mUserAccess.getAll().get(0) : null;
        UserCurrentLocation userCurrentLocation = mUserCurrentLocationAccess.getAll().size() > 0 ? (UserCurrentLocation) mUserCurrentLocationAccess.getAll().get(0) : null;
        UserSettings userSettings = mUserSettingsAccess.getAll().size() > 0 ? (UserSettings) mUserSettingsAccess.getAll().get(0) : null;
        Company company = mCompanyAccess.getAll().size() > 0 ? (Company) mCompanyAccess.getAll().get(0) : null;
        MetaInfo metaInfo = mMetaInfoAccess.getAll().size() > 0 ? (MetaInfo) mMetaInfoAccess.getAll().get(0) : null;

        mVerifyCounter++;
        if (mVerifyCounter > MAX_VERIFY) {
            stopConnectionRequirementSequence();
            ConnectionVerifyStatus code_error = findMissing(user, userCurrentLocation, userSettings, company, metaInfo);
            mConnectionRequirementVerifyListener.onConnectionFail(code_error);
        } else {
            if (user != null) {

                initChannels(user);

                // The others documents require are present
                if (userCurrentLocation != null
                        && userSettings != null
                        && metaInfo != null
                        && company != null) {
                    stopConnectionRequirementSequence();
                    mConnectionRequirementVerifyListener.onConnectionVerify(user, userCurrentLocation, userSettings, company, metaInfo);
                    mUserAccess.removeChangeListener(mUserAccessListener);
                    return true;
                }
            }
        }
        return false;
    }

    private void initChannels(User user) {
        if (!mChannelInit) {
            mDatabaseHandler.setMissionChannel(user.getSyncUser(), DateHelper.dateForChannel(-1));
            mDatabaseHandler.setMissionChannel(user.getSyncUser(), DateHelper.dateForChannel(0));
            mDatabaseHandler.setMissionChannel(user.getSyncUser(), DateHelper.dateForChannel(1));
            mDatabaseHandler.setMissionChannel(user.getSyncUser(), DateHelper.dateForChannel(2));
            mDatabaseHandler.setCompanyChannel(user.getCompanyId());
            mDatabaseHandler.setMissionStatusTypeChannel(user.getCompanyId());
            mDatabaseHandler.setMissionActionTypeChannel(user.getCompanyId());

            mChannelInit = true;
            mDatabaseHandler.restartPuller();
        }
    }

    private ConnectionVerifyStatus findMissing(User user, UserCurrentLocation userCurrentLocation, UserSettings userSettings, Company company, MetaInfo metaInfo) {
        if (user == null)
            return ConnectionVerifyStatus.SERVER_ERROR_USER_MISSING;
        else if (userCurrentLocation == null)
            return ConnectionVerifyStatus.SERVER_ERROR_USER_CURRENT_LOCATION_MISSING;
        else if (userSettings == null)
            return ConnectionVerifyStatus.SERVER_ERROR_USER_SETTING_MISSING;
        else if (company == null)
            return ConnectionVerifyStatus.SERVER_ERROR_COMPANY_MISSING;
        else if (metaInfo == null)
            return ConnectionVerifyStatus.SERVER_ERROR_META_INFO_MISSING;
        else
            return ConnectionVerifyStatus.UNKNOW_ERROR;
    }
}
