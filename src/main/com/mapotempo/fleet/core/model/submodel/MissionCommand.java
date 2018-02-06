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
import com.mapotempo.fleet.api.model.submodel.MissionCommandInterface;
import com.mapotempo.fleet.core.base.SubModelBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.MissionStatusType;

import java.util.HashMap;
import java.util.Map;

public class MissionCommand extends SubModelBase implements MissionCommandInterface {
    // MAPOTEMPO KEY
    private static final String LABEL = "label";
    private static final String MISSION_STATUS_TYPE_ID = "mission_status_type_id";
    private static final String GROUP = "group";

    private String mLabel;
    private MissionStatusType mMissionStatusType;
    private String mGroup;

    /**
     * MissionCommand.
     *
     * @param map      map
     * @param database database
     */
    public MissionCommand(Map map, Database database) {
        super(map, database);
    }

    /**
     * MissionCommand.
     *
     * @param label             label
     * @param missionStatusType missionStatusType
     * @param group             group
     * @param database          database
     */
    public MissionCommand(String label, MissionStatusType missionStatusType, String group, Database database) {
        super(database);
        mLabel = label;
        mMissionStatusType = missionStatusType;
        mGroup = group;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionStatusType getMissionStatusType() {
        return mMissionStatusType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getGroup() {
        return mGroup;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel() {
        return mLabel;
    }

    @Override
    public void fromMap(Map map) {
        mLabel = getProperty(LABEL, String.class, "", map);
        mGroup = getProperty(GROUP, String.class, "", map);
        String status_id = getProperty(MISSION_STATUS_TYPE_ID, String.class, "", map);
        try {
            mMissionStatusType = new MissionStatusType(status_id, mDatabase);
        } catch (CoreException e) {
            //e.printStackTrace();
            System.out.println("WARNING : return a non saved MissionStatusType");
            MissionStatusType missionStatus = new MissionStatusType(mDatabase);
            missionStatus.setLabel(status_id);
            mMissionStatusType = missionStatus;
        }
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put(LABEL, mLabel);
        res.put(GROUP, mGroup);
        res.put(MISSION_STATUS_TYPE_ID, mMissionStatusType.getId());
        return res;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            MissionCommand tmp = (MissionCommand) obj;
            if (mLabel.equals(tmp.mLabel))
                if (mGroup.equals(tmp.mGroup))
                    if (mMissionStatusType.getId().equals(tmp.getMissionStatusType().getId()))
                        return true;
        }
        return false;
    }
}
