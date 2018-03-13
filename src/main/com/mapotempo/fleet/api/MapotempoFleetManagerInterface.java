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

package com.mapotempo.fleet.api;

import com.mapotempo.fleet.api.model.CompanyInterface;
import com.mapotempo.fleet.api.model.UserInterface;
import com.mapotempo.fleet.api.model.UserPreferenceInterface;
import com.mapotempo.fleet.api.model.accessor.MissionAccessInterface;
import com.mapotempo.fleet.api.model.accessor.MissionStatusAccessInterface;
import com.mapotempo.fleet.api.model.accessor.MissionStatusActionAccessInterface;
import com.mapotempo.fleet.api.model.accessor.MissionStatusTypeAccessInterface;
import com.mapotempo.fleet.api.model.accessor.TrackAccessInterface;
import com.mapotempo.fleet.api.model.submodel.LocationDetailsInterface;
import com.mapotempo.fleet.api.model.submodel.MetaInfoInterface;
import com.mapotempo.fleet.api.model.submodel.SubModelFactoryInterface;

/**
 * MapotempoFleetManagerInterface is a database model access and synchronisation server manager.
 */
public interface MapotempoFleetManagerInterface {

    /**
     * MetaInfoInterface.
     * Return the {@link MetaInfoInterface}
     *
     * @return {@link MetaInfoInterface}
     */
    MetaInfoInterface getMetaInfo();

    /**
     * Return the company associate to the user.
     * If user have no company or company isn't
     * synchronised getCompany return a default
     * company.
     *
     * @return the Company data associated to the user
     */
    CompanyInterface getCompany();

    /**
     * Return the user data associate to the user
     * If user have no data or data isn't
     * synchronised getUser return null.
     *
     * @return the User data associated to the user
     */
    UserInterface getUser();

    /**
     * Return the user preference data associate to the user
     * If user have no data or data isn't
     * synchronised getUser return a default document.
     *
     * @return the User data associated to the user
     */
    UserPreferenceInterface getUserPreference();

    /**
     * Get the currentLocationDetails data associate to the user.
     */
    LocationDetailsInterface getCurrentLocationDetails();

    /**
     * Set the currentLocationDetails data associate to the user.
     */
    void setCurrentLocationDetails(LocationDetailsInterface locationDetailsInterface);

    /**
     * ConnectionVerifyListener.
     * Interface use to call back when the connection to the dabatase is ensure.
     * Enumerator {@link ConnectionVerifyStatus} return the status connection
     * <ul>
     * <li>VERIFY</li>
     * <li>LOGIN_ERROR</li>
     * </ul>
     */
    interface ConnectionVerifyListener {
        /**
         * connection.
         *
         * @param status                The connection status
         * @param mapotempoFleetManager {@code null}
         */
        void onServerConnectionVerify(ConnectionVerifyStatus status, MapotempoFleetManagerInterface mapotempoFleetManager);
    }

    /**
     * MissionAccessInterface.
     * Return the {@link MissionAccessInterface} or null
     *
     * @return {@link MissionAccessInterface}
     */
    MissionAccessInterface getMissionAccess();

    /**
     * MissionStatusAccessInterface.
     * Return the {@link MissionStatusAccessInterface}
     *
     * @return MissionStatusAccessInterface
     */
    MissionStatusAccessInterface getMissionStatusAccessInterface();

    /**
     * MissionStatusTypeAccessInterface.
     * Return the {@link MissionStatusTypeAccessInterface}
     *
     * @return MissionStatusTypeAccessInterface
     */
    MissionStatusTypeAccessInterface getMissionStatusTypeAccessInterface();

    /**
     * MissionStatusTypeAccessInterface.
     * Return the {@link MissionStatusActionAccessInterface}
     *
     * @return MissionStatusActionAccessInterface
     */

    public MissionStatusActionAccessInterface getMissionStatusActionAccessInterface();

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
     * Release connection.
     */
    void release();

    /**
     * Return the client server compatibility.
     * If compatibility is false the synchronisation switch off automatically
     * User application need to update this library or use a lower server version
     */
    boolean serverCompatibility();

    interface OnServerCompatibility {
        /**
         * Return the client server compatibility.
         * If compatibility is false the synchronisation switch off automatically
         * User application need to update this library or use a lower server version
         */
        void serverCompatibility(boolean compatibility);
    }

    /**
     * Attache a callback to be notify if server version up during app running.
     */
    void addOnServerCompatibilityChange(OnServerCompatibility onServerCompatibility);

}
