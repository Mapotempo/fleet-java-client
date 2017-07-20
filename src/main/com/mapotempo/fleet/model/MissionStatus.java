package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;

import java.util.Date;

/**
 * MissionStatus.
 */
@DocumentBase(type = "mission_status")
public class MissionStatus extends ModelBase {

    @FieldBase(name = "mission", foreign = true)
    public Mission mMission;

    @FieldBase(name = "status")
    public Status mStatus;

    @FieldBase(name = "date")
    public Date mDate;

    public enum Status {
        IN_PROGRESS,
        DELIVERED,
        UNKNOW
    }
}


