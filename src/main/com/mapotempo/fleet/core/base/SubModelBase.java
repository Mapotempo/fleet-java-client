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

package com.mapotempo.fleet.core.base;

import com.couchbase.lite.Database;

import java.util.Map;

/**
 * The abstract submodel base.
 */
public abstract class SubModelBase {

    protected Database mDatabase;

    public SubModelBase(Database database) {
        mDatabase = database;
        return;
    }

    public SubModelBase(Map map, Database database) {
        mDatabase = database;
        fromMap(map);
        return;
    }

    abstract protected void fromMap(Map map);

    abstract public Map<String, Object> toMap();

    protected <T> T getProperty(String key, Class<T> clazz, T def, Map source) {
        Object data;
        data = source.get(key);

        if (data == null) {
            System.err.println("WARNING : in " + getClass().getName() + " The key " + key + " is absent");
            return def;
        }
        try {
            return clazz.cast(data);
        } catch (ClassCastException e) {
            class Local {
            }
            ;
            String funcName = Local.class.getEnclosingMethod().getName();
            System.err.println("WARNING : in " + funcName + " find type : " + data.getClass().getName() + ", " + clazz.getName() + " expected");
        }
        return def;
    }
}
