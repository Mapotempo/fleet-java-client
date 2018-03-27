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

package com.mapotempo.fleet.core.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;
import com.mapotempo.fleet.api.model.submodel.AddressInterface;
import com.mapotempo.fleet.api.model.submodel.LocationInterface;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.ModelBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.submodel.Address;
import com.mapotempo.fleet.core.model.submodel.Location;
import com.mapotempo.fleet.core.model.submodel.TimeWindow;
import com.mapotempo.fleet.utils.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Company.
 */
@DocumentBase(type = "mission")
public class Mission extends ModelBase implements MissionInterface {

    // MAPOTEMPO KEY
    private static final String NAME = "name";
    private static final String COMPANY_ID = "company_id";
    private static final String DATE = "date";
    private static final String ETA = "eta";
    private static final String LOCATION = "location";
    private static final String ADDRESS = "address";
    private static final String SYNC_USER = "sync_user";
    private static final String MISSION_STATUS_TYPE_ID = "mission_status_type_id";
    private static final String REFERENCE = "reference";
    private static final String COMMENT = "comment";
    private static final String PHONE = "phone";
    private static final String DURATION = "duration";
    private static final String TIME_WINDOWS = "time_windows";
    private static final String CUSTOM_DATA = "custom_data";
    private static final String SURVEY_LOCATION = "survey_location";
    private static final String SURVEY_ADDRESS = "survey_address";

    public Mission(Database database) {
        super(database);
    }

    public Mission(Document document) {
        super(document);
    }

    public Mission(String id, Database database) throws CoreException {
        super(id, database);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return getProperty(NAME, String.class, "Unknow");
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
        return getProperty(COMPANY_ID, String.class, "No company id");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate() {
        String dataType = getProperty(DATE, String.class, "0");
        return DateHelper.fromStringISO8601(dataType);
    }

    /**
     * {@inheritDoc}
     */
    public Date getEta() {
        String eta = getProperty(ETA, String.class, "0");
        return DateHelper.fromStringISO8601(eta);
    }

    /**
     * {@inheritDoc}
     */
    public Date getEtaOrDefault() {
        String eta = getProperty(ETA, String.class, null);
        if (eta != null)
            return DateHelper.fromStringISO8601(eta);

        String dataType = getProperty(DATE, String.class, "0");
        return DateHelper.fromStringISO8601(dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Location getLocation() {
        Location defaultLocation = new Location(0., 0., mDatabase);
        Map dataType = getProperty(LOCATION, Map.class, defaultLocation.toMap());
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
        Address defaultAddress = new Address("Unknow", "Unknow", "Unknow", "Unknow", "Unknow", "", mDatabase);
        Map dataType = getProperty(ADDRESS, Map.class, defaultAddress.toMap());
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
        String status_id = getProperty(MISSION_STATUS_TYPE_ID, String.class, "0");
        try {
            return new MissionStatusType(status_id, mDatabase);
        } catch (CoreException e) {
            //e.printStackTrace();
            System.err.println("WARNING : return a non saved MissionStatusType");
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
    public String getSyncUser() {
        return getProperty(SYNC_USER, String.class, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReference() {
        return getProperty(REFERENCE, String.class, "");
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
        return (String) getProperty(COMMENT, String.class, "");
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
        return getProperty(PHONE, String.class, "");
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
        return getProperty(DURATION, Integer.class, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDuration(int duration) {
        setProperty(DURATION, duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List getTimeWindow() {
        ArrayList<Map> mapArray = (ArrayList<Map>) getProperty(TIME_WINDOWS, List.class, new ArrayList<HashMap>());
        List<TimeWindow> res = new ArrayList<>();
        for (Map hm : mapArray)
            res.add(new TimeWindow(hm, mDatabase));
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTimeWindow(List timeWindows) {
        List<Map> hashArray = new ArrayList<>();
        for (TimeWindow tm : (List<TimeWindow>) timeWindows)
            hashArray.add(tm.toMap());
        setProperty(TIME_WINDOWS, hashArray);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getCustomData() {
        Map<String, String> default_hash = new HashMap<String, String>();
        Map<String, String> res = (Map<String, String>) getProperty(CUSTOM_DATA, Map.class, default_hash);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCustomData(Map<String, String> data) {
        setProperty(CUSTOM_DATA, data);
    }

    // ##########################
    // ##    Survey Section    ##
    // ##########################

    /**
     * {@inheritDoc}
     */
    @Override
    public Location getSurveyLocation() {
        Map dataType = getProperty(SURVEY_LOCATION, Map.class, new HashMap());
        return new Location(dataType, mDatabase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSurveyLocation(LocationInterface location) {
        setProperty(SURVEY_LOCATION, ((Location) location).toMap());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address getSurveyAddress() {
        Map dataType = getProperty(SURVEY_ADDRESS, Map.class, new HashMap());
        return new Address(dataType, mDatabase);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSurveyAddress(AddressInterface address) {
        setProperty(SURVEY_ADDRESS, ((Address) address).toMap());
    }
}
