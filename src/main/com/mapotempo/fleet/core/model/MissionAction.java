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
import com.mapotempo.fleet.api.model.CompanyInterface;
import com.mapotempo.fleet.api.model.MissionActionInterface;
import com.mapotempo.fleet.api.model.MissionActionTypeInterface;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.submodel.LocationInterface;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.ModelBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.submodel.Location;
import com.mapotempo.fleet.utils.DateHelper;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * MissionAction.
 */
@DocumentBase(type = "mission_action")
public class MissionAction extends ModelBase implements MissionActionInterface {

    public Document getDocument() {
        return mDocument;
    }

    // MAPOTEMPO KEY
    public static final String COMPANY_ID = "company_id";
    public static final String MISSION_ID = "mission_id";
    public static final String MISSION_ACTION_TYPE_ID = "mission_action_type_id";
    public static final String ACTION_LOCATION = "action_location";
    private static final String DATE = "date";

    public MissionAction(Database database) {
        super(database);
    }

    public MissionAction(Document document) {
        super(document);
    }

    public MissionAction(String id, Database database) throws CoreException {
        super(id, database);
    }

    public void setCompany(CompanyInterface company) {
        setProperty(COMPANY_ID, company.getId());
    }

    public CompanyInterface getCompany(CompanyInterface company) {
        CompanyInterface res;
        String company_id = getProperty(COMPANY_ID, String.class, "");
        try {
            res = new Company(company_id, mDatabase);
        } catch (CoreException e) {
            System.out.println("WARNING : return a non saved Mission");
            Company missionStatus = new Company(mDatabase);
            res = missionStatus;
        }
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMission(MissionInterface mission) {
        setProperty(MISSION_ID, mission.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionInterface getMission() {
        MissionInterface res;
        String mission_id = getProperty(MISSION_ID, String.class, "");
        try {
            res = new Mission(mission_id, mDatabase);
        } catch (CoreException e) {
            System.out.println("WARNING : return a non saved Mission");
            Mission missionStatus = new Mission(mDatabase);
            res = missionStatus;
        }
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActionType(MissionActionTypeInterface statusType) {
        setProperty(MISSION_ACTION_TYPE_ID, statusType.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionActionTypeInterface getActionType() {
        MissionActionTypeInterface res;
        String status_id = getProperty(MISSION_ACTION_TYPE_ID, String.class, "");
        try {
            res = new MissionActionType(status_id, mDatabase);
        } catch (CoreException e) {
            System.out.println("WARNING : return a non saved MissionStatusType");
            MissionActionType missionAction = new MissionActionType(mDatabase);
            res = missionAction;
        }
        return res;
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
    public void setLocation(LocationInterface location) {
        setProperty(ACTION_LOCATION, location);
    }

    /**
     * {@inheritDoc}
     */
    public Location getLocation() {
        Map hashMap = getProperty(ACTION_LOCATION, Map.class, new HashMap());
        return new Location(hashMap, mDatabase);
    }
}
