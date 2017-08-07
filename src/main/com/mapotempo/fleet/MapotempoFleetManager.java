package com.mapotempo.fleet;

import com.couchbase.lite.Context;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.model.accessor.CompanyAccess;
import com.mapotempo.fleet.model.accessor.MissionAccess;

/**
 * MapotempoFleetManager.
 */
public class MapotempoFleetManager {

    private static MapotempoFleetManager ourInstance = null;

    public static MapotempoFleetManager getInstance(Context context) {
        if(ourInstance == null)
            ourInstance = new MapotempoFleetManager(context);
        return ourInstance;
    }

    private Context mContext;

    private DatabaseHandler mDatabaseHandler;

    public CompanyAccess mCompanyAccess;

    public MissionAccess mMissionAccess;

    private MapotempoFleetManager(Context context) {
        mContext = context;
        try {
            mDatabaseHandler = new DatabaseHandler("default", mContext);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
        } catch (CoreException e) {
            e.printStackTrace();
        };
    }

    public void newConnexion(String user, String password) {
        try {
            mDatabaseHandler = new DatabaseHandler(user, mContext);
            mDatabaseHandler.setConnexionParam(password, "http://localhost:4984/db");
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
        } catch (CoreException e)
        {
            e.printStackTrace();
        }
    }

}
