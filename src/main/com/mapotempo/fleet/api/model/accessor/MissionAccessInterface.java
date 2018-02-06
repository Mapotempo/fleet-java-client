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

import com.mapotempo.fleet.api.model.MissionInterface;

import java.util.Date;
import java.util.List;

/**
 * The {@link MissionInterface} accessor.
 * An accessor provide actions on data as well as a notification mechanism.
 */
public interface MissionAccessInterface extends AccessInterface<MissionInterface> {

    /**
     * @return The specific {@link MissionInterface}
     */
    @Override
    MissionInterface get(String id);

    /**
     * {@inheritDoc}
     *
     * @return a new {@link MissionInterface} unsaved
     */
    @Override
    MissionInterface getNew();

    /**
     * @return The list of all with filter {@link MissionInterface}
     */
    @Override
    List<MissionInterface> getAll();

    /**
     * @return The list of all {@link MissionInterface}
     */
    List<MissionInterface> getAllWithoutFilter();

    /**
     * Get all elements.
     *
     * @param before the before {@link Date}
     * @param after  the after {@link Date}
     * @return Return all element or empty list
     */
    List<MissionInterface> getByWindow(final Date before, final Date after);

    /**
     * {@inheritDoc}
     */
    @Override
    void addChangeListener(ChangeListener<MissionInterface> changeListener);

    /**
     * {@inheritDoc}
     */
    @Override
    void removeChangeListener(ChangeListener<MissionInterface> changeListener);
}
