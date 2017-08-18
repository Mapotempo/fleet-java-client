package com.mapotempo.fleet.api;

import com.mapotempo.fleet.api.model.accessor.MissionAccessInterface;
import com.mapotempo.fleet.api.model.accessor.MissionStatusTypeAccessInterface;
import com.mapotempo.fleet.core.model.Company;
import com.mapotempo.fleet.core.model.MissionStatusType;
import com.mapotempo.fleet.core.model.User;
import com.mapotempo.fleet.core.model.accessor.MissionStatusTypeAccess;

/**
 * MapotempoFleetManager is the entry point for the mapotempo fleet java client.
 */
public interface MapotempoFleetManagerInterface {

    /**
     * Return the company associate to the user.
     * If user havn't company or company isn't
     * syncronisated getCompany return a default
     * company.
     * Note : You can use setOnCompanyAvailable to attach a callback
     * for notify when user data is available.
     * @return the Company associated to the user
     */
    Company getCompany();

    /**
     * OnCompanyAvailable.
     */
    interface OnCompanyAvailable {
        void companyAvailable(Company company);
    }

    /**
     * Set a callback to be notify on available company.
     * @param onCompanyAvailable interface to implement
     */
    void setOnCompanyAvailable(OnCompanyAvailable onCompanyAvailable);

    /**
     * Clear the callback.
     */
    void clearOnCompanyAvailable();

    /**
     * Return the user data associate to the user.
     * If user havn't data or data isn't
     * syncronisated getUser return null.
     * Note : You can use setOnUserAvailable to attach a callback
     * for notify when user data is available.
     * @return the User data associated to the user
     */
    User getUser();

    /**
     * OnUserAvailable.
     */
    interface OnUserAvailable {
        void userAvailable(User user);
    }

    /**
     * Set a callback to be notify on available user.
     * @param onUserAvailable interface to user
     */
    void setOnUserAvailable(OnUserAvailable onUserAvailable);

    /**
     * Clear the callback.
     */
    void clearOnUserAvailable();

    /**
     * todo
     * @return
     */
    MissionAccessInterface getMissionAccess();

    /**
     * todo
     * @return
     */
    MissionStatusTypeAccessInterface getMissionStatusTypeAccessInterface();

    /**
     * todo
     */
    void onlineStatus(boolean status);

    /**
     * todo
     */
    boolean isOnline();


    /**
     * Close connexion.
     */
    void close();
}
