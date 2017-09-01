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

package com.mapotempo.fleet.api.model.model.submodel;

import com.mapotempo.fleet.core.model.MissionStatusType;
import com.mapotempo.fleet.core.model.submodel.Address;
import com.mapotempo.fleet.core.model.submodel.Location;
import com.mapotempo.fleet.core.model.submodel.MissionCommand;
import com.mapotempo.fleet.core.model.submodel.TimeWindow;

import java.util.Date;

public interface SubModelFactoryInterface {

    /**
     * Create a new allocated location.
     * @param lat latitude
     * @param lon longitude
     * @return a new allocated Location
     */
    Location CreateNewLocation(double lat, double lon);

    /**
     * Create a new allocated address.
     * @param street street
     * @param postalCode postalcode
     * @param city city
     * @param state state
     * @param country country
     * @param detail detail
     * @return a new allocated Address
     */
    Address CreateNewAddress(String street, String postalCode, String city, String state, String country, String detail);

    /**
     * Create a new allocated mission.
     * @param label label
     * @param missionStatusType missionStatusType
     * @param group group
     * @return a new allocated MissionCommand
     */
    MissionCommand CreateNewMissionCommand(String label, MissionStatusType missionStatusType, String group);

    /**
     * Create a new allocated TimeWindow
     * @param start windows start
     * @param end windows end
     * @return a new allocated TimeWindows
     */
    TimeWindow CreateNewTimeWindow(Date start, Date end);
}
