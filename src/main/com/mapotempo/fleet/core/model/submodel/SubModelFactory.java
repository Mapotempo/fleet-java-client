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

package com.mapotempo.fleet.core.model.submodel;

import com.couchbase.lite.Database;
import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;
import com.mapotempo.fleet.api.model.submodel.LocationDetailsInterface;
import com.mapotempo.fleet.api.model.submodel.SubModelFactoryInterface;
import com.mapotempo.fleet.core.model.MissionStatusType;

import java.util.Date;

/**
 * SubModel factory
 */
public class SubModelFactory implements SubModelFactoryInterface {

    private Database mDatabase;

    public SubModelFactory(Database database) {
        mDatabase = database;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location CreateNewLocation(double lat, double lon) {
        Location res = new Location(lat, lon, mDatabase);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDetailsInterface CreateNewLocationDetails(double lat, double lon,
                                                             Date date,
                                                             double accuracy,
                                                             double speed,
                                                             double bearing,
                                                             double elevation,
                                                             int signalStrength,
                                                             Integer cid, Integer lac,
                                                             Integer mcc, Integer mnc) {
        LocationDetails res = new LocationDetails(lat, lon, date, accuracy, speed, bearing, elevation, signalStrength, cid, lac, mcc, mnc, mDatabase);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address CreateNewAddress(String street, String postalCode, String city, String state, String country, String detail) {
        Address res = new Address(street, postalCode, city, state, country, detail, mDatabase);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionCommand CreateNewMissionCommand(String label, MissionStatusTypeInterface missionStatusType, String group) {
        MissionCommand res = new MissionCommand(label, (MissionStatusType) missionStatusType, group, mDatabase);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TimeWindow CreateNewTimeWindow(Date start, Date end) {
        TimeWindow res = new TimeWindow(start, end, mDatabase);
        return res;
    }
}
