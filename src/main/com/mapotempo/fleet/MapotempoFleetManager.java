package com.mapotempo.fleet;

import com.couchbase.lite.Context;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.model.accessor.CompanyAccess;
import com.mapotempo.fleet.model.accessor.MissionAccess;
import com.mapotempo.fleet.model.accessor.UserAccess;

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

    public DatabaseHandler mDatabaseHandler;

    public CompanyAccess mCompanyAccess;
    public UserAccess mUserAccess;
    public MissionAccess mMissionAccess;

    private MapotempoFleetManager(Context context) {
        mContext = context;
        try {
            mDatabaseHandler = new DatabaseHandler("default", mContext);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
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
            mUserAccess = new UserAccess(mDatabaseHandler);
        } catch (CoreException e)
        {
            e.printStackTrace();
        }
    }
}
