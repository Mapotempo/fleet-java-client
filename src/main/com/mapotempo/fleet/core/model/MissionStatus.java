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
import com.mapotempo.fleet.api.model.MissionStatusInterface;
import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.ModelBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.utils.DateHelper;

import java.util.Date;

/**
 * MissionStatus.
 */
@DocumentBase(type = "mission_status")
public class MissionStatus extends ModelBase implements MissionStatusInterface {
    // MAPOTEMPO KEY
    public static final String MISSION_ID = "mission_id";
    public static final String MISSION_STATUS_TYPE_ID = "mission_status_type_id";
    private static final String DATE = "date";

    public MissionStatus(Database database) {
        super(database);
    }

    public MissionStatus(Document document) {
        super(document);
    }

    public MissionStatus(String id, Database database) throws CoreException {
        super(id, database);
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
    public void setStatusType(MissionStatusTypeInterface statusType) {
        setProperty(MISSION_STATUS_TYPE_ID, statusType.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionStatusTypeInterface getStatusType() {
        MissionStatusTypeInterface res;
        String status_id = getProperty(MISSION_STATUS_TYPE_ID, String.class, "");
        try {
            res = new MissionStatusType(status_id, mDatabase);
        } catch (CoreException e) {
            System.out.println("WARNING : return a non saved MissionStatusType");
            MissionStatusType missionStatus = new MissionStatusType(mDatabase);
            missionStatus.setLabel(status_id);
            res = missionStatus;
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
}
