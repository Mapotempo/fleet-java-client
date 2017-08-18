package com.mapotempo.fleet.core.model.accessor;

import com.mapotempo.fleet.api.model.accessor.MissionStatusTypeAccessInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.MissionStatusType;

/**
 * MissionStatusTypeAccess.
 */
public class MissionStatusTypeAccess extends Access<MissionStatusType> implements MissionStatusTypeAccessInterface {
    public MissionStatusTypeAccess(DatabaseHandler dbHandler) throws CoreException {
        super(MissionStatusType.class, dbHandler);
    }

}
