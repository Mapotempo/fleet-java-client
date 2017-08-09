package com.mapotempo.fleet.model.accessor;

import com.mapotempo.fleet.api.model.model.accessor.MissionAccessInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.model.Mission;

/**
 * MissionAccess.
 */
public class MissionAccess extends Access<Mission> implements MissionAccessInterface {
    public MissionAccess(DatabaseHandler dbHandler) throws CoreException {
        super(Mission.class, dbHandler);
    }
}
