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
import com.mapotempo.fleet.api.model.TrackInterface;
import com.mapotempo.fleet.api.model.submodel.LocationDetailsInterface;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.model.submodel.LocationDetails;
import com.mapotempo.fleet.utils.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Track.
 */
@DocumentBase(type = "track")
public class Track extends MapotempoModelBase implements TrackInterface {

    private static final String LOCATIONS = "location";

    public static final String COMPANY_ID = "company_id";

    private static final String OWNER = "owner";

    private static final String DATE = "date";

    public Track(Database database) {
        super(database);
    }

    public Track(Document doc) {
        super(doc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOwnerId() {
        return getProperty(OWNER, String.class, "Unknow");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOwnerId(String owner) {
        setProperty(OWNER, owner);
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
    public void setCompanyId(String company_id) {
        setProperty(COMPANY_ID, company_id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<LocationDetailsInterface> getLocations() {
        ArrayList<HashMap> hashArray = (ArrayList<HashMap>) getProperty(LOCATIONS, ArrayList.class, new ArrayList<HashMap>());
        ArrayList<LocationDetailsInterface> res = new ArrayList<>();
        for (HashMap hm : hashArray) {
            res.add(new LocationDetails(hm, mDatabase));
        }
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLocations(ArrayList locations) {
        ArrayList<Map> hashArray = new ArrayList<>();
        for (LocationDetails loc : (ArrayList<LocationDetails>) locations) {
            hashArray.add(loc.toMap());
        }
        setProperty(LOCATIONS, hashArray);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getDate() {
        String dataType = getProperty(DATE, String.class, "0");
        return DateHelper.fromStringISO8601(dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDate(String isoDate) {
        setProperty(DATE, isoDate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDate(Date date) {
        setProperty(DATE, DateHelper.toStringISO8601(date));
    }
}
