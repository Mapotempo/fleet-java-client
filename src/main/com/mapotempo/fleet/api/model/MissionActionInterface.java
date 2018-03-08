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

import java.io.InputStream;
import java.util.Date;

/**
 * A MissionAction link to a Mission with StatusType and date.
 */
public interface MissionActionInterface extends MapotempoModelBaseInterface {

    /**
     * Get Mission Owner
     */
    MissionInterface getMission();

    /**
     * Set Mission Owner
     */
    void setMission(MissionInterface mission);

    /**
     * Get Mission Action Type
     */
    MissionActionTypeInterface getActionType();

    /**
     * Set Mission Action Type
     */
    void setActionType(MissionActionTypeInterface statusType);

    /**
     * Get the mission date.
     *
     * @return A date
     */
    Date getDate();

    /**
     * Set the mission date by iso8601 date {@link String}.
     *
     * @param isoDate An iso {@link String}
     */
    void setDate(String isoDate);

    /**
     * Set the mission date.
     *
     * @param date A {@link Date}
     */
    void setDate(Date date);

    /**
     * Set inputStream attachment
     *
     * @param inputStream
     */
    void setAttachment(String name, String contentType, InputStream inputStream);

    /**
     * Get inputStream attachment
     */
    InputStream getAttachment(String name);
}
