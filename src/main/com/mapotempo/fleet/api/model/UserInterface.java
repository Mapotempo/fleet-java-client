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

import java.util.List;

/**
 * UserInterface.
 */
public interface UserInterface extends MapotempoModelBaseInterface {

    /**
     * Get the user login/name.
     *
     * @return A {@link String}
     */
    String getUser();

    /**
     * Get the user company.
     *
     * @return A {@link String}
     */
    String getCompanyId();

    /**
     * Get all the user roles.
     *
     * @return A {@link List} of roles.
     */
    List<String> getRoles();
}
