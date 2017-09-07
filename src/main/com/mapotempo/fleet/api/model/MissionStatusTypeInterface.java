/*
 * Copyright © Mapotempo, 2017
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

package com.mapotempo.fleet.api.model;

import com.mapotempo.fleet.api.model.submodel.MissionCommandInterface;

import java.util.ArrayList;

/**
 * A MissionStatusTypeInterface describe an actual mission status.
 * Every MissionStatusTypeInterface correspond to a specific status describe by their company.
 */
public interface MissionStatusTypeInterface extends MapotempoModelBaseInterface {

    /**
     * Get the status label.
     *
     * @return A {@link String}
     */
    String getLabel();

    /**
     * Set the status label.
     *
     * @param label A {@link String}
     */
    void setLabel(String label);

    /**
     * Get the status color to display.
     *
     * @return An hexa {@link String}
     */
    String getColor();

    /**
     * Set the color.
     *
     * @param hexColor A hexa {@link String}
     */
    void setColor(String hexColor);

    /**
     * The next commands can be apply on this status.
     *
     * @return A {@link MissionStatusTypeInterface}
     */
    ArrayList<MissionCommandInterface> getCommands();
}
