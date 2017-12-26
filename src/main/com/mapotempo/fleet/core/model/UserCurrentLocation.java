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
import com.mapotempo.fleet.api.model.CurrentLocationInterface;
import com.mapotempo.fleet.api.model.submodel.LocationDetailsInterface;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.ModelBase;
import com.mapotempo.fleet.core.model.submodel.LocationDetails;

import java.util.HashMap;

/**
 * UserCurrentLocation.
 */
@DocumentBase(type = "user_current_location")
public class UserCurrentLocation extends ModelBase implements CurrentLocationInterface {

    private static final String LOCATION_DETAIL = "location_detail";

    public static final String COMPANY_ID = "company_id";

    private static final String SYNC_USER = "sync_user";

    public UserCurrentLocation(Database database) {
        super(database);
    }

    public UserCurrentLocation(Document doc) {
        super(doc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOwnerId() {
        return getProperty(SYNC_USER, String.class, "Unknow");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCompanyId() {
        return getProperty(COMPANY_ID, String.class, "Unknow");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationDetailsInterface getLocation() {
        HashMap map = getProperty(LOCATION_DETAIL, HashMap.class, new HashMap());
        LocationDetailsInterface res = new LocationDetails(map, mDatabase);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocation(LocationDetailsInterface location) {
        setProperty(LOCATION_DETAIL, ((LocationDetails) location).toMap());
    }
}
