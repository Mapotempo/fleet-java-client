/*
 * Copyright © Mapotempo, 2018
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
import com.mapotempo.fleet.api.model.UserInterface;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.ModelBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Company.
 */
@DocumentBase(type = "user")
public class User extends ModelBase implements UserInterface {

    // MAPOTEMPO KEY
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String SYNC_USER = "sync_user";
    public static final String COMPANY_ID = "company_id";
    public static final String ROLES = "roles";

    public User(Database database) {
        super(database);
    }

    public User(Document doc) {
        super(doc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSyncUser() {
        return getProperty(SYNC_USER, String.class, "Unknow");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCompanyId() {
        return getProperty(COMPANY_ID, String.class, "No company id found");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getRoles() {
        return (ArrayList<String>) getProperty(ROLES, ArrayList.class, new ArrayList<String>());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserName() {
        return getProperty(NAME, String.class, "Unknow");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserEmail() {
        return getProperty(EMAIL, String.class, "");
    }

}
