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

package com.mapotempo.fleet.api.model.submodel;

import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;

import java.util.Date;

/**
 * This factory can be use to create sub-modele.
 */
public interface SubModelFactoryInterface {

    /**
     * Create a new allocated location.
     *
     * @param lat latitude
     * @param lon longitude
     * @return a new allocated Location
     */
    LocationInterface CreateNewLocation(double lat, double lon);

    /**
     * Create a new allocated location.
     *
     * @param lat            lattide
     * @param lon            longitude
     * @param date           date
     * @param accuracy       accuracy
     * @param speed          speed
     * @param bearing        bearing
     * @param elevation      elevation
     * @param signalStrength signal strength
     * @param cid            cell id
     * @param lac            location area code
     * @param mcc            mobile country code
     * @param mnc            mobile network code
     */
    LocationDetailsInterface CreateNewLocationDetails(double lat,
                                                      double lon,
                                                      Date date,
                                                      double accuracy,
                                                      double speed,
                                                      double bearing,
                                                      double elevation,
                                                      int signalStrength,
                                                      Integer cid,
                                                      Integer lac,
                                                      Integer mcc,
                                                      Integer mnc);

    /**
     * Create a new allocated address.
     *
     * @param street     street
     * @param postalCode postalcode
     * @param city       city
     * @param state      state
     * @param country    country
     * @param detail     detail
     * @return a new allocated Address
     */
    AddressInterface CreateNewAddress(String street,
                                      String postalCode,
                                      String city,
                                      String state,
                                      String country,
                                      String detail);

    /**
     * Create a new allocated mission.
     *
     * @param label             label
     * @param missionStatusType missionStatusType
     * @param group             group
     * @return a new allocated MissionCommand
     */
    MissionCommandInterface CreateNewMissionCommand(String label,
                                                    MissionStatusTypeInterface missionStatusType,
                                                    String group);

    /**
     * Create a new allocated TimeWindow
     *
     * @param start windows start
     * @param end   windows end
     * @return a new allocated TimeWindows
     */
    TimeWindowsInterface CreateNewTimeWindow(Date start, Date end);
}
