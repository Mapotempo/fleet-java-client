package com.mapotempo.fleet.api;

import com.couchbase.lite.Context;
import com.mapotempo.fleet.core.MapotempoFleetManager;

public class ManagerFactory {

    /**
     * TODO
     * @param context
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
        new MapotempoFleetManager(context, user, password, onServerConnexionVerify, url);
    }
}
