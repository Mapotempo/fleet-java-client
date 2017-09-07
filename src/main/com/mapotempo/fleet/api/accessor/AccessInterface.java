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

package com.mapotempo.fleet.api.accessor;

import com.mapotempo.fleet.api.model.MapotempoModelBaseInterface;

import java.util.List;

/**
 * AccessInterface.
 */
public interface AccessInterface<T extends MapotempoModelBaseInterface> {

    interface ChangeListener<T> {
        void changed(List<T> items);
    }

    /**
     * Get a new instance of ...
     *
     * @return return new data
     */
    T getNew();

    /**
     * get.
     *
     * @param id Element id to retrieve
     * @return T element or null
     */
    T get(String id);

    /**
     * getAll.
     *
     * @return Return all element
     */
    List<T> getAll();

    /**
     * addChangeListener
     *
     * @param changeListener add a change listener
     */
    void addChangeListener(ChangeListener<T> changeListener);

    /**
     * addRemoveListener.
     *
     * @param changeListener remove the change listener
     */
    void removeChangeListener(ChangeListener<T> changeListener);

}
