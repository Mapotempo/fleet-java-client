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

package com.mapotempo.fleet.api;

import com.mapotempo.fleet.api.model.accessor.MissionAccessInterface;
import com.mapotempo.fleet.api.model.accessor.MissionStatusTypeAccessInterface;
import com.mapotempo.fleet.api.model.model.submodel.SubModelFactoryInterface;
import com.mapotempo.fleet.core.model.Company;
import com.mapotempo.fleet.core.model.User;

/**
 */
public interface MapotempoFleetManagerInterface {

    /**
     * Return the company associate to the user.
     * If user havn't company or company isn't
     * syncronisated getCompany return a default
     * company.
     * Note : You can use setOnCompanyAvailable to attach a callback
     * for notify when user data is available.
     * @return the Company associated to the user
     */
    Company getCompany();

    /**
     * Return the user data associate to the user.
     * If user havn't data or data isn't
     * syncronisated getUser return null.
     * Note : You can use setOnUserAvailable to attach a callback
     * for notify when user data is available.
     * @return the User data associated to the user
     */
    User getUser();

    /**
     * onServerConnexionVerify.
     */
    interface OnServerConnexionVerify {
        enum Status {
            TIMEOUT,
            VERIFY,
            LOGIN_ERROR
        }

        /**
         *
         * @param status
         * @param mapotempoFleetManager {@code null}
         */
        void connexion(Status status, MapotempoFleetManagerInterface mapotempoFleetManager);
    }

    /**
     * todo
     * @return
     */
    MissionAccessInterface getMissionAccess();

    /**
     * todo
     * @return
     */
    MissionStatusTypeAccessInterface getMissionStatusTypeAccessInterface();

    /**
     * Return the submodel factory.
     * @return a submodel factory
     */
    SubModelFactoryInterface getSubmodelFactory();

    /**
     * todo
     */
    void onlineStatus(boolean status);

    /**
     * todo
     */
    boolean isOnline();


    /**
     * Release connexion.
     */
    void release();
}
