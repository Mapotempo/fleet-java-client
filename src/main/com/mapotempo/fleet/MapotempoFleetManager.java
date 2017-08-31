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
    }

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
                verifyConnexion();
            }
        });

        verifyConnexion();
    }

    private void verifyConnexion() {
        User user = getUser();
        if(user != null) {
            if(!mChannelInit) {
                tryToInitchannels(user);
            }

            if(!mConnexionIsVerify) {
                mConnexionIsVerify = true;
                mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.VERIFY, this);
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
        // Release ask by user, we don't delete database
        mDatabaseHandler.release(false);
    }

    /**
     * todo
     * @param context
     * @return return a {@link MapotempoFleetManagerInterface}
     */
    public static MapotempoFleetManagerInterface getDefaultManager(Context context) {
        return new MapotempoFleetManager(context);
    }

    private DatabaseHandler.OnCatchLoginError onCatchLoginError = new DatabaseHandler.OnCatchLoginError() {
        @Override
        public void CatchLoginError() {
            mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.LOGIN_ERROR, null);
            mDatabaseHandler.release(true);
        }
    };

    /**
     * Default manager.
     * @param context java context
     */
    private MapotempoFleetManager(Context context) {
        mContext = context;
        try {
            mDatabaseHandler = new DatabaseHandler("default_abcde", "default_abcde", mContext, onCatchLoginError);
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
     * TODO
     * @param context
     * @param user
     * @param password
     * @param onServerConnexionVerify
     */
    public static void getManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify) {
        MapotempoFleetManager mapotempoFleetManager = new MapotempoFleetManager(context, user, password, onServerConnexionVerify);
    }

    /**
     * TODO
     * @param context
     * @param user
     * @param password
     * @param onServerConnexionVerify
     */
    private MapotempoFleetManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify) {
        mContext = context;
        mOnServerConnexionVerify = onServerConnexionVerify;

        try {
            mDatabaseHandler = new DatabaseHandler(user, password, mContext, onCatchLoginError);
            mSubModelFactoryInterface = new SubModelFactory(mDatabaseHandler.mDatabase);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);

            // Set channels
            mDatabaseHandler.initConnexion("http://localhost:4984/db");
            initChannelsConfigurationSequence(user);
        } catch (CoreException e) {
            mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.LOGIN_ERROR, null);
            e.printStackTrace();
        };
    }

    /**
     * TODO
     * @param context
     * @param user
     * @param password
     * @param onServerConnexionVerify
     * @param url
     */
    public static void getManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify, String url) {
        new MapotempoFleetManager(context, user, password, onServerConnexionVerify);
    }

    /**
     * TODO
     * @param context
     * @param user
     * @param password
     * @param onServerConnexionVerify
     * @param url
     */
    private MapotempoFleetManager(Context context, String user, String password, OnServerConnexionVerify onServerConnexionVerify, String url) {
        mContext = context;
        mOnServerConnexionVerify = onServerConnexionVerify;

        try {
            mDatabaseHandler = new DatabaseHandler(user, password, mContext, onCatchLoginError);
            mMissionAccess = new MissionAccess(mDatabaseHandler);
            mCompanyAccess = new CompanyAccess(mDatabaseHandler);
            mUserAccess = new UserAccess(mDatabaseHandler);
            mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);

            // Set channels
            mDatabaseHandler.initConnexion(url);

            initChannelsConfigurationSequence(user);
        } catch (CoreException e) {
            mOnServerConnexionVerify.connexion(OnServerConnexionVerify.Status.LOGIN_ERROR, null);
            e.printStackTrace();
        }
    }
}
