package com.mapotempo.fleet.core.model.accessor;

import com.mapotempo.fleet.api.model.accessor.MissionStatusAccessInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.MissionStatusType;

/**
 * MissionStatusTypeAccess.
 */
public class MissionStatusTypeAccess extends Access<MissionStatusType> implements MissionStatusAccessInterface {
    public MissionStatusTypeAccess(DatabaseHandler dbHandler) throws CoreException {
        super(MissionStatusType.class, dbHandler);
    }

}
