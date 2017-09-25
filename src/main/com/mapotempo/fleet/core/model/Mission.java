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

package com.mapotempo.fleet.core.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;
import com.mapotempo.fleet.api.model.submodel.AddressInterface;
import com.mapotempo.fleet.api.model.submodel.LocationInterface;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.submodel.Address;
import com.mapotempo.fleet.core.model.submodel.Location;
import com.mapotempo.fleet.core.model.submodel.TimeWindow;
import com.mapotempo.fleet.utils.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Company.
 */
@DocumentBase(type = "mission")
public class Mission extends MapotempoModelBase implements MissionInterface {

    // MAPOTEMPO KEY
    private static final String NAME = "name";
    private static final String COMPANY_ID = "company_id";
    private static final String DATE = "date";
    private static final String LOCATION = "location";
    private static final String ADDRESS = "address";
    private static final String OWNERS = "owners";
    private static final String MISSION_STATUS_TYPE_ID = "mission_status_type_id";
    private static final String REFERENCE = "reference";
    private static final String COMMENT = "comment";
    private static final String PHONE = "phone";
    private static final String DURATION = "duration";
    private static final String TIME_WINDOWS = "time_windows";
    private static final String CUSTOM_DATA = "custom_data";

    public Mission(Database database) {
        super(database);
    }

    public Mission(Document document) {
        super(document);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return (String) getProperty(NAME, "Unknow");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
        setProperty(NAME, name);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getCompanyId() {
        return (String) getProperty(COMPANY_ID, "No company id found");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCompanyId(String companyId) {
        setProperty(COMPANY_ID, companyId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate() {
        String dataType = (String) getProperty(DATE, "0");
        return DateHelper.fromStringISO8601(dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDate(String isoDate) {
        setProperty(DATE, isoDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDate(Date date) {
        setProperty(DATE, DateHelper.toStringISO8601(date));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location getLocation() {
        Location defaultLocation = new Location(0, 0, mDatabase);
        Map dataType = (Map) getProperty(LOCATION, defaultLocation.toMap());
        return new Location(dataType, mDatabase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(LocationInterface location) {
        setProperty(LOCATION, ((Location) location).toMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address getAddress() {
        Address defaultAddress = new Address("", "", "", "", "", "", mDatabase);
        Map dataType = (Map) getProperty(ADDRESS, defaultAddress.toMap());
        return new Address(dataType, mDatabase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAddress(AddressInterface address) {
        setProperty(ADDRESS, ((Address) address).toMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionStatusTypeInterface getStatus() {
        String status_id = (String) getProperty(MISSION_STATUS_TYPE_ID, "0");
        try {
            return new MissionStatusType(status_id, mDatabase);
        } catch (CoreException e) {
            e.printStackTrace();
            System.out.println("WARNING : return a non saved MissionStatusType");
            MissionStatusType missionStatus = new MissionStatusType(mDatabase);
            missionStatus.setLabel(status_id);
            return missionStatus;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStatus(MissionStatusTypeInterface missionStatus) {
        setProperty(MISSION_STATUS_TYPE_ID, missionStatus.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<String> getOwners() {
        return (ArrayList<String>) getProperty(OWNERS, new ArrayList<String>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOwners(ArrayList<String> owners) {
        setProperty(OWNERS, owners);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReference() {
        return (String) getProperty(REFERENCE, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setReference(String reference) {
        setProperty(REFERENCE, reference);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComment() {
        return (String) getProperty(COMMENT, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComment(String comment) {
        setProperty(COMMENT, comment);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPhone() {
        return (String) getProperty(PHONE, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPhone(String phone) {
        setProperty(PHONE, phone);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDuration() {
        return (int) getProperty(DURATION, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDuration(int duration) {
        getProperty(DURATION, duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList getTimeWindow() {
        ArrayList<HashMap> hashArray = (ArrayList<HashMap>) getProperty(TIME_WINDOWS, new ArrayList<HashMap>());
        ArrayList<TimeWindow> res = new ArrayList<>();
        for (HashMap hm : hashArray)
            res.add(new TimeWindow(hm, mDatabase));
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimeWindow(ArrayList timeWindows) {
        ArrayList<TimeWindow> hashArray = new ArrayList<>();
        for (TimeWindow tm : (ArrayList<TimeWindow>) timeWindows)
            hashArray.add(new TimeWindow(tm.toMap(), mDatabase));
        setProperty(TIME_WINDOWS, hashArray);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<String, String> getCustomData() {
        HashMap<String, String> default_hash = new HashMap<String, String>();
        HashMap<String, String> res = (HashMap<String, String>) getProperty(CUSTOM_DATA, default_hash);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCustomData(HashMap<String, String> data) {
        setProperty(CUSTOM_DATA, data);
    }
}
