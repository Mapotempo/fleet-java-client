package com.mapotempo.fleet.core.model.accessor;

import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Company;

/**
 * CompanyAccess.
 */
public class CompanyAccess extends Access<Company> {
    public CompanyAccess(DatabaseHandler dbHandler) throws CoreException {
        super(Company.class, dbHandler);
    }
}
