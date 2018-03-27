/*
 * Copyright © Mapotempo, 2018
 *
 * This file is part of Mapotempo.
 *
 * Mapotempo is free software. You can redistribute it and/or
 * modify since you respect the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Mapotempo is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Licenses for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Mapotempo. If not, see:
 * <http://www.gnu.org/licenses/agpl.html>
 */

package com.mapotempo.fleet.api.model.accessor;

import com.mapotempo.fleet.api.model.CompanyInterface;
import com.mapotempo.fleet.api.model.MissionActionInterface;
import com.mapotempo.fleet.api.model.MissionActionTypeInterface;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;
import com.mapotempo.fleet.api.model.submodel.LocationInterface;

import javax.annotation.Nullable;
import java.util.List;

/**
 * The {@link MissionStatusTypeInterface} accessor.
 * An accessor provide actions on data as well as a notification mechanism.
 */
public interface MissionActionAccessInterface extends AccessInterface<MissionActionInterface> {

    /**
     * @return The specific {@link MissionActionInterface}
     */
    @Override
    MissionActionInterface get(String id);

    /**
     * @return The list of all {@link MissionActionTypeInterface}
     */
    @Override
    List<MissionActionInterface> getAll();

    /**
     * {@inheritDoc}
     */
    @Override
    void addChangeListener(ChangeListener<MissionActionInterface> changeListener);

    /**
     * {@inheritDoc}
     */
    @Override
    void removeChangeListener(ChangeListener<MissionActionInterface> changeListener);

    /**
     * Create a new mission action
     *
     * @param company    The company owner
     * @param mission    The mission owner
     * @param actionType The action type
     * @param location   The location of the action (Optional)
     * @return a new {@link MissionActionInterface} unsaved
     */
    MissionActionInterface create(CompanyInterface company,
                                  MissionInterface mission,
                                  MissionActionTypeInterface actionType,
                                  @Nullable LocationInterface location);

    /**
     * Filter mission_status by mission_id
     *
     * @param mission
     * @return List
     */
    List<MissionActionInterface> getByMission(final MissionInterface mission);
}
