package com.mapotempo.fleet.api.model.submodel;

import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;

public interface MissionCommandInterface {
    MissionStatusTypeInterface getMissionStatusType();

    String getGroup();

    String getLabel();
}
