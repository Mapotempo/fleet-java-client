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
import com.mapotempo.fleet.api.model.UserPreferenceInterface;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.ModelBase;

/**
 * UserSettings.
 */
@DocumentBase(type = "user_settings")
public class UserSettings extends ModelBase implements UserPreferenceInterface {

    // MAPOTEMPO KEY
    public static final String SYNC_USER = "sync_user";
    public static final String COMPANY_ID = "company_id";
    public static final String NIGHT_MODE = "night_mode";

    public UserSettings(Database database) {
        super(database);
    }

    public UserSettings(Document doc) {
        super(doc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUser() {
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
    public Boolean getBoolPreference(Preference preference) {
        return getProperty(preference.toString(), Boolean.class, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setBoolPreference(Preference preference, Boolean status) {
        setProperty(preference.toString(), status);
    }

    /**
     * {@inheritDoc}
     */
    public NightModePreference getNightModePreference() {
        String nightMode = getProperty(NIGHT_MODE, String.class, "automatic");
        return NightModePreference.fromString(nightMode);
    }

    /**
     * {@inheritDoc}
     */
    public void setNightModePreference(NightModePreference status) {
        setProperty(NIGHT_MODE, status);
    }
}
