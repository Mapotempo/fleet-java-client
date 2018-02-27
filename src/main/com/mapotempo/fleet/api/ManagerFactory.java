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

package com.mapotempo.fleet.api;

import com.couchbase.lite.Context;
import com.mapotempo.fleet.core.MapotempoFleetManager;

/**
 * ManagerFactory.
 * <p>{@link ManagerFactory} is the entry point for the Mapotempo Fleet Java Client.</p>
 * <p>This factory allow to get {@link MapotempoFleetManagerInterface} for the entire java application.</p>
 * <p>Three methods getManager are implemented. One synchronous method and two asynchronous methods.
 * The two asynchronous methods return the {@link MapotempoFleetManagerInterface} into
 * {@link com.mapotempo.fleet.api.MapotempoFleetManagerInterface.OnServerConnexionVerify} callback method.</p>
 */
public class ManagerFactory {

    /**
     * Try to create a connected {@link MapotempoFleetManagerInterface} asynchronously.
     * This factory method is asynchronous, and the result is return in the
     * {@link com.mapotempo.fleet.api.MapotempoFleetManagerInterface.OnServerConnexionVerify} provides.
     *
     * @param context                 The application context
     * @param user                    The user name
     * @param password                The user password
     * @param onServerConnexionVerify A {@link com.mapotempo.fleet.api.MapotempoFleetManagerInterface.OnServerConnexionVerify}
     */
    public static void getManager(Context context, String user, String password, MapotempoFleetManagerInterface.OnServerConnexionVerify onServerConnexionVerify) {
        MapotempoFleetManager mapotempoFleetManager = new MapotempoFleetManager(context, user, password, onServerConnexionVerify);
    }

    /**
     * Try to create a connected {@link MapotempoFleetManagerInterface} asynchronously.
     * This factory method is asynchronous, and the result is return in the
     * {@link com.mapotempo.fleet.api.MapotempoFleetManagerInterface.OnServerConnexionVerify} provides.
     *
     * @param context                 The application context
     * @param user                    The user name
     * @param password                The user password
     * @param onServerConnexionVerify A {@link com.mapotempo.fleet.api.MapotempoFleetManagerInterface.OnServerConnexionVerify}
     * @param url                     A custom url for syncgateway
     */
    public static void getManager(Context context, String user, String password, MapotempoFleetManagerInterface.OnServerConnexionVerify onServerConnexionVerify, String url) {
        MapotempoFleetManager mapotempoFleetManager = new MapotempoFleetManager(context, user, password, onServerConnexionVerify, url);
    }
}
