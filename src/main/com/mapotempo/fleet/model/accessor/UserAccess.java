package com.mapotempo.fleet.model.accessor;

import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.model.User;

/**
 * UserAccess.
 */
public class UserAccess extends Access<User> {
    public UserAccess(DatabaseHandler dbHandler) throws CoreException {
        super(User.class, dbHandler);
    }
}
