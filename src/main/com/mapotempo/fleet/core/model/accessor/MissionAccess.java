package com.mapotempo.fleet.core.model.accessor;

import com.mapotempo.fleet.api.model.accessor.MissionAccessInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Mission;

/**
 * MissionAccess.
 */
public class MissionAccess extends Access<Mission> implements MissionAccessInterface {
    public MissionAccess(DatabaseHandler dbHandler) throws CoreException {
        super(Mission.class, dbHandler);
    }
}
