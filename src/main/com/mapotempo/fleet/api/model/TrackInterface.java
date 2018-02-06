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

import com.mapotempo.fleet.api.model.submodel.LocationDetailsInterface;
import com.mapotempo.fleet.api.model.submodel.LocationInterface;

import java.util.ArrayList;
import java.util.Date;

/**
 * TrackInterface.
 */
public interface TrackInterface extends MapotempoModelBaseInterface {
    /**
     * Get the owner name.
     *
     * @return The owner
     */
    String getOwnerId();

    /**
     * Set the owner name.
     */
    void setOwnerId(String owner);

    /**
     * Get the company id.
     *
     * @return The company id
     */
    String getCompanyId();

    /**
     * Set the company id.
     */
    void setCompanyId(String company_id);

    /**
     * Get the mission track.
     *
     * @return A {@link LocationDetailsInterface}
     */
    ArrayList<LocationDetailsInterface> getLocations();

    /**
     * Set the tracks.
     *
     * @param locations A {@link LocationInterface}
     */
    void setLocations(ArrayList<LocationDetailsInterface> locations);

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
}
