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

package com.mapotempo.fleet.api.model;

import com.mapotempo.fleet.api.model.submodel.AddressInterface;
import com.mapotempo.fleet.api.model.submodel.LocationInterface;
import com.mapotempo.fleet.api.model.submodel.TimeWindowsInterface;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MissionInterface.
 */
public interface MissionInterface extends MapotempoModelBaseInterface {

    /**
     * Return the name.
     *
     * @return A name
     */
    String getName();

    /**
     * Change the mission name.
     *
     * @param name The new name
     */
    void setName(String name);

    /**
     * The company owned mission
     *
     * @return A {@link String}
     */
    String getCompanyId();

    /**
     * Get the mission date.
     *
     * @return A date
     */
    Date getDate();

    /**
     * Get the mission Eta date.
     *
     * @return
     */
    Date getEta();

    /**
     * Get the mission Eta if found Or the mission date.
     *
     * @return
     */
    Date getEtaOrDefault();

    /**
     * Get the mission location.
     *
     * @return A {@link LocationInterface}
     */
    LocationInterface getLocation();

    /**
     * Set the location.
     *
     * @param location A {@link LocationInterface}
     */
    void setLocation(LocationInterface location);

    /**
     * Get the mission address.
     *
     * @return An {@link AddressInterface}
     */
    AddressInterface getAddress();

    /**
     * Set the mission address.
     *
     * @param address An {@link AddressInterface}
     */
    void setAddress(AddressInterface address);

    /**
     * Get the mission status.
     *
     * @return A {@link MissionStatusTypeInterface}
     */
    MissionStatusTypeInterface getStatus();

    /**
     * Set the mission status.
     *
     * @param missionStatus A {@link MissionStatusTypeInterface}
     */
    void setStatus(MissionStatusTypeInterface missionStatus);

    /**
     * Get user's mission.
     *
     * @return An {@link String}
     */
    String getSyncUser();

    /**
     * Get the reference.
     *
     * @return A {@link String}
     */
    String getReference();

    /**
     * Set the reference.
     *
     * @param reference A {@link String}
     */
    void setReference(String reference);

    /**
     * Get the comment.
     *
     * @return A {@link String}
     */
    String getComment();

    /**
     * Set the comment.
     *
     * @param comment A {@link String}
     */
    void setComment(String comment);

    /**
     * Get the phone number.
     *
     * @return A {@link String}
     */
    String getPhone();

    /**
     * Set the phone number.
     *
     * @param phone A {@link String}
     */
    void setPhone(String phone);

    /**
     * Get the mission duration.
     *
     * @return An int
     */
    int getDuration();

    /**
     * Set the duration.
     *
     * @param duration An int
     */
    void setDuration(int duration);

    /**
     * Get the mission time windows.
     *
     * @return An {@link ArrayList}
     */
    List<TimeWindowsInterface> getTimeWindow();

    /**
     * Set the mission time windows.
     *
     * @param timeWindows An {@link ArrayList}
     */
    void setTimeWindow(List<TimeWindowsInterface> timeWindows);

    /**
     * Get custom data.
     *
     * @return
     */
    Map<String, String> getCustomData();

    /**
     * Set the custom data.
     *
     * @param data
     */
    void setCustomData(Map<String, String> data);

    /**
     * Set inputStream attachment
     *
     * @param inputStream
     */
    void setAttachment(String name, String contentType, InputStream inputStream);

    // ##########################
    // ##    Survey Section    ##
    // ##########################

    /**
     * Set the survey location.
     */
    LocationInterface getSurveyLocation();

    /**
     * Get the survey location.
     */
    void setSurveyLocation(LocationInterface location);

    /**
     * Delete the survey location.
     */
    void deleteSurveyLocation();

    /**
     * Set the survey address.
     */
    AddressInterface getSurveyAddress();

    /**
     * Get the survey address.
     */
    void setSurveyAddress(AddressInterface location);

    /**
     * Delete the survey address.
     */
    void deleteSurveyAddress();

}
