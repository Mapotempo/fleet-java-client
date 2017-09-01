/*
 * Copyright © Mapotempo, 2017
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

package com.mapotempo.fleet.core.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.model.submodel.Location;

import java.util.Map;

/**
 * Company.
 */
@DocumentBase(type = "company")
public class Company extends MapotempoModelBase {

    // MAPOTEMPO KEY
    public static final String NAME = "name";
    public static final String COMPANY_ID = "company_id";

    public Company(Database database) {
        super(database);
    }

    public Company(Document doc) {
        super(doc);
    }

    public String getName() {
        return (String) getProperty(NAME, "unknow");
    }

    public Location getLocation() {
        Location defaultLocation = new Location(0, 0, mDatabase);
        Map dataType = (Map)getProperty(COMPANY_ID, defaultLocation.toMap());
        Location res = new Location(dataType, mDatabase);
        return res;
    }
}
