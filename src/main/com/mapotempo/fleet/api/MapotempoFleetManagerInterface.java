package com.mapotempo.fleet.api;

import com.mapotempo.fleet.api.model.model.accessor.MissionAccessInterface;
import com.mapotempo.fleet.model.Company;
import com.mapotempo.fleet.model.User;

/**
 * MapotempoFleetManagerInterface.
 */
public interface MapotempoFleetManagerInterface {

    /**
     * todo
     * @return
     */
    Company getCompany();

    /**
     * todo
     * @return
     */
    User getUser();

    /**
     * todo
     * @return
     */
    MissionAccessInterface getMissionAccess();

    /**
     * todo
     * @return
     */
    boolean connexion(boolean status);

    /**
     * todo
     */
    boolean getConnexionStatus();
}
