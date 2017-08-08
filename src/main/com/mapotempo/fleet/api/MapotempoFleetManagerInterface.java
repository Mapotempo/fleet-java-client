package com.mapotempo.fleet.api;

import com.mapotempo.fleet.model.Company;
import com.mapotempo.fleet.model.User;
import com.mapotempo.fleet.model.accessor.MissionAccess;

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
    MissionAccess getMissionAccess();

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
