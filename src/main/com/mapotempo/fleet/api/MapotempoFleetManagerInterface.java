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

import com.mapotempo.fleet.api.model.CompanyInterface;
import com.mapotempo.fleet.api.model.UserInterface;
import com.mapotempo.fleet.api.model.accessor.MissionAccessInterface;
import com.mapotempo.fleet.api.model.accessor.MissionStatusTypeAccessInterface;
import com.mapotempo.fleet.api.model.accessor.TrackAccessInterface;
import com.mapotempo.fleet.api.model.submodel.SubModelFactoryInterface;

/**
 * MapotempoFleetManagerInterface is a database model access and synchronisation server manager.
 */
public interface MapotempoFleetManagerInterface {

    /**
     * Return the company associate to the user.
     * If user havn't company or company isn't
     * syncronisated getCompany return a default
     * company.
     * Note : You can use setOnCompanyAvailable to attach a callback
     * for notify when user data is available.
     *
     * @return the Company data associated to the user
     */
    CompanyInterface getCompany();

    /**
     * Return the user data associate to the user.
     * If user havn't data or data isn't
     * syncronisated getUser return null.
     * Note : You can use setOnUserAvailable to attach a callback
     * for notify when user data is available.
     *
     * @return the User data associated to the user
     */
    UserInterface getUser();

    /**
     * OnServerConnexionVerify.
     * Interface use to call back when the connexion to the dabatase is ensure.
     * Enumerator {@link Status} return the status connexion
     * <ul>
     * <li>VERIFY</li>
     * <li>LOGIN_ERROR</li>
     * </ul>
     */
    interface OnServerConnexionVerify {
        enum Status {
            VERIFY,
            LOGIN_ERROR
        }

        /**
         * connexion.
         *
         * @param status                The connexion status
         * @param mapotempoFleetManager {@code null}
         */
        void connexion(Status status, MapotempoFleetManagerInterface mapotempoFleetManager);
    }

    /**
     * MissionAccessInterface.
     * Return the {@link MissionAccessInterface} or null
     *
     * @return {@link MissionAccessInterface}
     */
    MissionAccessInterface getMissionAccess();

    /**
     * MissionStatusTypeAccessInterface.
     * Return the {@link MissionStatusTypeAccessInterface}
     *
     * @return MissionStatusTypeAccessInterface
     */
    MissionStatusTypeAccessInterface getMissionStatusTypeAccessInterface();

    /**
     * TrackAccessInterface.
     * Return the {@link TrackAccessInterface} or null
     *
     * @return {@link TrackAccessInterface}
     */
    TrackAccessInterface getTrackAccess();

    /**
     * Return the submodel factory.
     *
     * @return a submodel factory
     */
    SubModelFactoryInterface getSubmodelFactory();

    /**
     * Set the synchronisation status.
     *
     * @param status new status
     */
    void onlineStatus(boolean status);

    /**
     * Return the synchronisation status.
     *
     * @return the synchronisation status
     */
    boolean isOnline();


    /**
     * Release connexion.
     */
    void release();
}
