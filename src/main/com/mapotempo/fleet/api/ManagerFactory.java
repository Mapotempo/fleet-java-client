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

package com.mapotempo.fleet.api;


import com.couchbase.lite.Context;
import com.mapotempo.fleet.core.MapotempoFleetManager;

/**
 * <h1>ManagerFactory</h1>
 * <p>{@link ManagerFactory} is the entry point for the mapotempo fleet java client.</p>
 */
public class ManagerFactory {

    /**
     * A default no connected manager.
     * @param context Application context
     * @return return a {@link MapotempoFleetManagerInterface}
     */
    public static MapotempoFleetManagerInterface getDefaultManager(Context context) {
        return new MapotempoFleetManager(context);
    }

    /**
     * TODO
     * @param context
     * @param user
     * @param password
     * @param onServerConnexionVerify
     */
    public static void getManager(Context context, String user, String password, MapotempoFleetManagerInterface.OnServerConnexionVerify onServerConnexionVerify) {
        MapotempoFleetManager mapotempoFleetManager = new MapotempoFleetManager(context, user, password, onServerConnexionVerify);
    }

    /**
     * TODO
     * @param context
     * @param user
     * @param password
     * @param onServerConnexionVerify
     * @param url
     */
    public static void getManager(Context context, String user, String password, MapotempoFleetManagerInterface.OnServerConnexionVerify onServerConnexionVerify, String url) {
        MapotempoFleetManager mapotempoFleetManager = new MapotempoFleetManager(context, user, password, onServerConnexionVerify, url);
    }
}
