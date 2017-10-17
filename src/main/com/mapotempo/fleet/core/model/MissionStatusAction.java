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
import com.mapotempo.fleet.api.model.MissionStatusActionInterface;
import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.ModelBase;
import com.mapotempo.fleet.core.exception.CoreException;

/**
 * MissionStatusAction.
 * Read only class
 * <p>
 * <p>
 * <p>
 * {
 * "type": "mission_status_action",
 * "_id": "status_action-XXXX"
 * "company_id": "company_XXXXX_XXXXX_XXXX_XXXXX",
 * "previous_mission_status_type_id": "status_pending"
 * "next_mission_status_type_id": "status_completed"
 * "label": "To pending",
 * "group": "default"
 * }
 */
@DocumentBase(type = "mission_status_action")
public class MissionStatusAction extends ModelBase implements MissionStatusActionInterface {
    // MAPOTEMPO KEY
    public static final String LABEL = "label";
    public static final String GROUP = "group";
    public static final String PREVIOUS_STATUS_TYPE_ID = "previous_mission_status_type_id";
    public static final String NEXT_STATUS_TYPE_ID = "next_mission_status_type_id";

    public MissionStatusAction(Database database) {
        super(database);
    }

    public MissionStatusAction(Document document) {
        super(document);
    }

    public MissionStatusAction(String id, Database database) throws CoreException {
        super(id, database);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return getProperty(LABEL, String.class, "Unknow");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroup() {
        return getProperty(GROUP, String.class, "default");
    }

    @Override
    public MissionStatusTypeInterface getPreviousStatus() {
        MissionStatusTypeInterface res;
        String status_id = getProperty(PREVIOUS_STATUS_TYPE_ID, String.class, "");
        try {
            res = new MissionStatusType(status_id, mDatabase);
        } catch (CoreException e) {
            //e.printStackTrace();
            System.out.println("WARNING : return a non saved MissionStatusType");
            MissionStatusType missionStatus = new MissionStatusType(mDatabase);
            missionStatus.setLabel(status_id);
            res = missionStatus;
        }
        return res;
    }

    @Override
    public MissionStatusTypeInterface getNextStatus() {
        MissionStatusTypeInterface res;
        String status_id = getProperty(NEXT_STATUS_TYPE_ID, String.class, "");
        try {
            res = new MissionStatusType(status_id, mDatabase);
        } catch (CoreException e) {
            //e.printStackTrace();
            System.out.println("WARNING : return a non saved MissionStatusType");
            MissionStatusType missionStatus = new MissionStatusType(mDatabase);
            missionStatus.setLabel(status_id);
            res = missionStatus;
        }
        return res;
    }
}