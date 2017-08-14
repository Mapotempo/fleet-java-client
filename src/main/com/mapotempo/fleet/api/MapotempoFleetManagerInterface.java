package com.mapotempo.fleet.api;

import com.mapotempo.fleet.api.model.accessor.MissionAccessInterface;
import com.mapotempo.fleet.core.model.Company;
import com.mapotempo.fleet.core.model.User;

/**
 * MapotempoFleetManagerInterface.
 */
public interface MapotempoFleetManagerInterface {

    /**
     * Return the company associate to the user.
     * If user havn't company or company isn't
     * syncronisated getCompany return a default
     * company.
     * @return the Company associated to the user
     */
    Company getCompany();

    /**
     * Return the user data associate to the user.
     * If user havn't data or data isn't
     * syncronisated getUser return default
     * user.
     * @return the User data associated to the user
     */
    User getUser();

    /**
     * todo
     * @return
     */
    MissionAccessInterface getMissionAccess();

    /**
     * todo
     */
    void onlineStatus(boolean status);

    /**
     * todo
     */
    boolean isOnline();
}
