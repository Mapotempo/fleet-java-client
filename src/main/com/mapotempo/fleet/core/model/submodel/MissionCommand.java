package com.mapotempo.fleet.core.model.submodel;

import com.couchbase.lite.Database;
import com.mapotempo.fleet.core.base.SubModelBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.MissionStatusType;

import java.util.HashMap;
import java.util.Map;

public class MissionCommand extends SubModelBase {
    // MAPOTEMPO KEY
    public static final String LABEL = "label";
    public static final String MISSION_STATUS_TYPE_ID = "mission_status_type_id";
    public static final String GROUP = "group";

    private String mLabel;
    private MissionStatusType mMissionStatusType;
    private String mGroup;

    /**
     * MissionCommand.
     * @param map map
     * @param database database
     */
    public MissionCommand(Map map, Database database) {
        super(map, database);
    }

    /**
     * MissionCommand.
     * @param label label
     * @param missionStatusType missionStatusType
     * @param group group
     * @param database database
     */
    public MissionCommand(String label, MissionStatusType missionStatusType, String group, Database database) {
        super(database);
        this.mLabel = label;
        this.mMissionStatusType = missionStatusType;
        this.mGroup = group;
    }

    public MissionStatusType getMissionStatusType() {
        return mMissionStatusType;
    }

    public String getGroup() {
        return mGroup;
    }

    public String getLabel() {
        return mLabel;
    }

    @Override
    public void fromMap(Map map) {
        this.mLabel = map.get(LABEL).toString();
        this.mGroup = map.get(GROUP).toString();
        String status_id = map.get(MISSION_STATUS_TYPE_ID).toString();;
        try {
            MissionStatusType statusType = new MissionStatusType(status_id, mDatabase);
            mMissionStatusType = statusType;
        } catch (CoreException e) {
            e.printStackTrace();
            System.out.println("WARNING : return a non saved MissionStatusType");
            MissionStatusType missionStatus = new MissionStatusType(mDatabase);
            missionStatus.setLabel(status_id);
            this.mMissionStatusType = missionStatus;
        }
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> res = new HashMap<>();
        res.put(LABEL, mLabel);
        res.put(GROUP, mGroup);
        res.put(MISSION_STATUS_TYPE_ID, mMissionStatusType.getId());
        return res;
    }
}
