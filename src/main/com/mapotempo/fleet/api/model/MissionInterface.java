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

package com.mapotempo.fleet.api.model;

import com.mapotempo.fleet.core.model.submodel.Address;
import com.mapotempo.fleet.core.model.submodel.Location;
import com.mapotempo.fleet.core.model.submodel.TimeWindow;

import java.util.ArrayList;
import java.util.Date;

/**
 * MissionInterface.
 */
public interface MissionInterface extends MapotempoModelBaseInterface {

    String getName();

    void setName(String name);

    String getCompanyId();

    void setCompanyId(String companyId);

    Date getDeliveryDate();

    void setDeliveryDate(String isoDate);

    void setDeliveryDate(Date date);

    Location getLocation();

    void setLocation(Location location);

    Address getAddress();

    void setAddress(Address address);

    MissionStatusTypeInterface getStatus();

    void setStatus(MissionStatusTypeInterface missionStatus);

    ArrayList<String> getOwners();

    void setOwners(ArrayList<String> owners);

    String getReference();

    void setReference(String reference);

    String getComment();

    void setComment(String comment);

    String getPhone();

    void setPhone(String phone);

    int getDuration();

    void setDuration(int duration);

    ArrayList<TimeWindow> getTimeWindow();

    void setTimeWindow(ArrayList<TimeWindow> timeWindows);
}
