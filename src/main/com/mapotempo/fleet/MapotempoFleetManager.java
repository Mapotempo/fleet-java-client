package com.mapotempo.fleet;

import com.couchbase.lite.Context;
import com.mapotempo.fleet.api.MapotempoFleetManagerInterface;
import com.mapotempo.fleet.api.model.model.submodel.SubModelFactoryInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Company;
import com.mapotempo.fleet.core.model.User;
import com.mapotempo.fleet.core.model.accessor.CompanyAccess;
import com.mapotempo.fleet.core.model.accessor.MissionAccess;
import com.mapotempo.fleet.core.model.accessor.MissionStatusTypeAccess;
import com.mapotempo.fleet.core.model.accessor.UserAccess;
import com.mapotempo.fleet.core.model.submodel.SubModelFactory;
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

    private MissionStatusTypeAccess mMissionStatusTypeAccess;

    private boolean mConnexionIsVerify = false;

    private boolean mChannelInit = false;

    private SubModelFactoryInterface mSubModelFactoryInterface;

    private OnServerConnexionVerify mOnServerConnexionVerify;

    /** {@inheritDoc} */
    @Override
    public Company getCompany() {
        List<Company> companies = mCompanyAccess.getAll();
        if(companies.size() > 0)
            return companies.get(0);
        else
            return null;
            // return mCompanyAccess.getNew();
    }

    /** {@inheritDoc} */
    @Override
    public User getUser() {
            List<User> users = mUserAccess.getAll();
        if(users.size() > 0)
            return users.get(0);
        else
            return null;
            // return mUserAccess.getNew();
    }

    /** {@inheritDoc} */
    @Override
    public MissionAccess getMissionAccess() {
        return mMissionAccess;
    }

    /** {@inheritDoc} */
    @Override
    public MissionStatusTypeAccess getMissionStatusTypeAccessInterface() {
        return mMissionStatusTypeAccess;
    };

    /** {@inheritDoc} */
    @Override
    public SubModelFactoryInterface getSubmodelFactory() {
        return mSubModelFactoryInterface;
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

    private void initChannelsConfigurationSequence(final String userName) throws CoreException{

        mDatabaseHandler.setUserChannel(userName);


        // Ecoute des Documents User
        mUserAccess.addChangeListener(new Access.ChangeListener<User>() {
            @Override
            public void changed(List<User> items) {
                if(items.size() > 0) {
                    if(items.size() > 1)
                        System.err.println("Warning : " + getClass().getSimpleName()  + " more than one user available, return the first");

                    // When user is received we can add channel
                    if(!mChannelInit) {
                        tryToInitchannels(items.get(0));
                    }
                } else {
                    System.err.println("Warning : " + getClass().getSimpleName() + "no user found");
                }
                verityConnexion();
            }
        });
        verityConnexion();
    }

    private void verityConnexion() {
        User user = getUser();
        if(user != null) {
            if(!mChannelInit) {
                tryToInitchannels(user);
            }
            if(!mConnexionIsVerify) {
                mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.VERIFY);
            }
        }
    }

    private void tryToInitchannels(User user) {
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(-1));
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(0));
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(1));
        mDatabaseHandler.setMissionChannel(user.getUser(), DateHelper.dateForChannel(2));
        mDatabaseHandler.setCompanyChannel(user.getCompanyId());
        mDatabaseHandler.setMissionStatusTypeChannel(user.getCompanyId());
        mChannelInit = true;
        mDatabaseHandler.restartPuller();
    }

    @Override
    public void release() {
        mDatabaseHandler.release();
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
            mSubModelFactoryInterface = new SubModelFactory(mDatabaseHandler.mDatabase);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);

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
    public static MapotempoFleetManagerInterface getManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify, int timeout) {
        return new MapotempoFleetManager(context, user, password, onServerConnexionVerify);
    }

    /**
     * Connected manager.
     * @param context java context
     * @param user user login
     * @param password user password
     */
    private MapotempoFleetManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify) {
        mContext = context;
        mOnServerConnexionVerify = onServerConnexionVerify;

        try {
            mDatabaseHandler = new DatabaseHandler(user, mContext);
            mSubModelFactoryInterface = new SubModelFactory(mDatabaseHandler.mDatabase);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);

            // Set channels
            mDatabaseHandler.setConnexionParam(password, "http://localhost:4984/db");
            initChannelsConfigurationSequence(user);
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
    public static MapotempoFleetManagerInterface getManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify, String url) {
        return new MapotempoFleetManager(context, user, password, onServerConnexionVerify, url);
    }

    /**
     * Connected manager.
     * @param context java context
     * @param user user login
     * @param url server url
     * @param password user password
     */
    private MapotempoFleetManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify, String url) {
        mContext = context;
        mOnServerConnexionVerify = onServerConnexionVerify;

        try {
            mDatabaseHandler = new DatabaseHandler(user, mContext);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);

            // Set channels
            mDatabaseHandler.setConnexionParam(password, url);

            initChannelsConfigurationSequence(user);
        } catch (CoreException e) {
            e.printStackTrace();
        };
    }
}
