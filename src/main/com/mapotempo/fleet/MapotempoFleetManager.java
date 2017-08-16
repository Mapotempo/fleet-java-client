package com.mapotempo.fleet;

import com.couchbase.lite.Context;
import com.mapotempo.fleet.api.MapotempoFleetManagerInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Company;
import com.mapotempo.fleet.core.model.User;
import com.mapotempo.fleet.core.model.accessor.CompanyAccess;
import com.mapotempo.fleet.core.model.accessor.MissionAccess;
import com.mapotempo.fleet.core.model.accessor.UserAccess;
import com.mapotempo.fleet.core.utils.DateHelper;

import java.util.List;

/**
 * {@inheritDoc}
 */
public class MapotempoFleetManager implements MapotempoFleetManagerInterface {

    private Context mContext;

    private DatabaseHandler mDatabaseHandler;

    private CompanyAccess mCompanyAccess;

    private UserAccess mUserAccess;

    private MissionAccess mMissionAccess;


    /** {@inheritDoc} */
    @Override
    public Company getCompany() {
        List<Company> companies = mCompanyAccess.getAll();
        if(companies.size() > 0)
            return companies.get(0);
        else
            return null;
    //        return mCompanyAccess.getNew();
    }

    private OnCompanyAvailable mOnCompanyAvailable;

    /** {@inheritDoc} */

    @Override
    public void setOnCompanyAvailable(OnCompanyAvailable onCompanyAvailable) {
        mOnCompanyAvailable = onCompanyAvailable;
    }

    /** {@inheritDoc} */
    @Override
    public void clearOnCompanyAvailable() {
        mOnCompanyAvailable = null;
    }

    /** {@inheritDoc} */
    @Override
    public User getUser() {
            List<User> users = mUserAccess.getAll();
        if(users.size() > 0)
            return users.get(0);
        else
            return null;
    //        return mUserAccess.getNew();
    }

    private OnUserAvailable mOnUserAvailable;

    /** {@inheritDoc} */
    @Override
    public void setOnUserAvailable(OnUserAvailable onUserAvailable) {
        mOnUserAvailable = onUserAvailable;
    }

    /** {@inheritDoc} */
    @Override
    public void clearOnUserAvailable() {
        mOnUserAvailable = null;
    }

    /** {@inheritDoc} */
    @Override
    public MissionAccess getMissionAccess() {
        return mMissionAccess;
    }

    /** {@inheritDoc} */
    @Override
    public void onlineStatus(boolean status) {
        mDatabaseHandler.onlineStatus(status);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isOnline() {
        return mDatabaseHandler.isOnline();
    }

    private void channelsConfiguration(final String user) {
        mDatabaseHandler.setUserChannel(user);
        mUserAccess.addChangeListener(new Access.ChangeListener<User>() {
            @Override
            public void changed(List<User> items) {
                if(mOnUserAvailable != null) {
                    if(items.size() > 0)
                    {
                        mOnUserAvailable.userAvailable(items.get(0));
                        if(items.size() > 1)
                            System.err.println("Warning : " + getClass().getSimpleName()  + " more than one user available, return the first");
                    }
                    else
                        System.err.println("Warning : " + getClass().getSimpleName()  + "no user found");
                }
            }
        });

        mDatabaseHandler.setMissionChannel(user, DateHelper.dateForChannel(-1));
        mDatabaseHandler.setMissionChannel(user, DateHelper.dateForChannel(0));
        mDatabaseHandler.setMissionChannel(user, DateHelper.dateForChannel(1));
        mDatabaseHandler.setMissionChannel(user, DateHelper.dateForChannel(2));
    }

    @Override
    public void close() {
        mDatabaseHandler.close();
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

            // Set channels
            channelsConfiguration(user);
        } catch (CoreException e) {
            e.printStackTrace();
        };
    }

    /**
     * todo
     * @param context
     * @param user
     * @param url server url
     * @param password
     * @return return a {@link MapotempoFleetManagerInterface}
     */
    public static MapotempoFleetManagerInterface getManager(Context context, String user, String password, String url) {
        return new MapotempoFleetManager(context, user, password, url);
    }

    /**
     * Connected manager.
     * @param context java context
     * @param user user login
     * @param url server url
     * @param password user password
     */
    private MapotempoFleetManager(Context context, String user, String password, String url) {
        mContext = context;
        try {
            mDatabaseHandler = new DatabaseHandler(user, mContext);
            mDatabaseHandler.setConnexionParam(password, url);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);

            // Set channels
            channelsConfiguration(user);
        } catch (CoreException e) {
            e.printStackTrace();
        };
    }
}
