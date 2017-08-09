package com.mapotempo.fleet;

import com.couchbase.lite.Context;
import com.mapotempo.fleet.api.MapotempoFleetManagerInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.model.Company;
import com.mapotempo.fleet.model.User;
import com.mapotempo.fleet.model.accessor.CompanyAccess;
import com.mapotempo.fleet.model.accessor.MissionAccess;
import com.mapotempo.fleet.model.accessor.UserAccess;

import java.util.List;

/**
 * MapotempoFleetManager.
 */
public class MapotempoFleetManager implements MapotempoFleetManagerInterface {

    private Context mContext;

    private DatabaseHandler mDatabaseHandler;

    private CompanyAccess mCompanyAccess;

    private UserAccess mUserAccess;

    private MissionAccess mMissionAccess;

    /**
     * {@inheritDoc}
     */
    @Override
    public Company getCompany() {
        try {
            List<Company> companies = mCompanyAccess.getAll();
            if(companies.size() > 0)
                return companies.get(0);
            else
                return mCompanyAccess.getNew();
        } catch (CoreException e) {
            e.printStackTrace();
            return mCompanyAccess.getNew();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public User getUser() {
        try {
            List<User> users = mUserAccess.getAll();
            if(users.size() > 0)
                return users.get(0);
            else
                return mUserAccess.getNew();
        } catch (CoreException e) {
            e.printStackTrace();
            return mUserAccess.getNew();
        }    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionAccess getMissionAccess() {
        return mMissionAccess;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean connexion(boolean status) {
        // TODO
        System.err.println("connexion not implemented");
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getConnexionStatus() {
        // TODO
        System.err.println("connexion not implemented");
        return false;
    }

    /**
     * todo
     * @param context
     * @return return a {@link MapotempoFleetManagerInterface}
     */
    public static MapotempoFleetManagerInterface getDefaultManager(Context context) {
        return new MapotempoFleetManager(context);
    }

    /**
     * Default manager.
     * @param context java context
     */
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

    /**
     * todo
     * @param context
     * @param user
     * @param password
     * @return return a {@link MapotempoFleetManagerInterface}
     */
    public static MapotempoFleetManagerInterface getManager(Context context, String user, String password) {
        return new MapotempoFleetManager(context, user, password);
    }

    /**
     * Connected manager.
     * @param context java context
     * @param user user login
     * @param password user password
     */
    private MapotempoFleetManager(Context context, String user, String password) {
        mContext = context;
        try {
            mDatabaseHandler = new DatabaseHandler(user, mContext);
            mDatabaseHandler.setConnexionParam(password, "http://localhost:4984/db");
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
        } catch (CoreException e) {
            e.printStackTrace();
        };
    }
}
