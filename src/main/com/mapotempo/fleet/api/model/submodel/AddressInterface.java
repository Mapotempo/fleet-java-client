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

package com.mapotempo.fleet.api.model.submodel;

/**
 * AddressInterface.
 * Describe an address
 */
public interface AddressInterface {
    /**
     * Get the street name.
     *
     * @return A {@link String}
     */
    String getStreet();

    /**
     * Get the postal code.
     *
     * @return A {@link String}
     */
    String getPostalcode();

    /**
     * Get the city name.
     *
     * @return A {@link String}
     */
    String getCity();

    /**
     * Get the state.
     *
     * @return A {@link String}
     */
    String getState();

    /**
     * Get the country.
     *
     * @return A {@link String}
     */
    String getCountry();

    /**
     * Get the address detail.
     *
     * @return A {@link String}
     */
    String getDetail();

    /**
     * Address is valid.
     */
    boolean isValid();
}
