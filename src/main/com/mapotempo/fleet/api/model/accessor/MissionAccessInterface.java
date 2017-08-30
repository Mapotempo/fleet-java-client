package com.mapotempo.fleet.api.model.accessor;

import com.mapotempo.fleet.core.model.Mission;

import java.util.Date;
import java.util.List;

/**
 * MissionAccessInterface.
 */
public interface MissionAccessInterface extends AccessInterface<Mission> {
    List<Mission> getByWindow(final Date before, final Date after);
}
