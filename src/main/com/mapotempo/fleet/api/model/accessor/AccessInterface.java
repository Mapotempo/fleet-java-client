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

import com.mapotempo.fleet.api.model.MapotempoModelBaseInterface;

import java.util.List;

/**
 * {@link AccessInterface}
 */
public interface AccessInterface<T extends MapotempoModelBaseInterface> {

    /**
     * ChangeListener interface.
     *
     * @param <T> The template type
     */
    interface ChangeListener<T> {
        void changed(List<T> items);
    }

    /**
     * Get a new unsave instance.
     * Use {@link MapotempoModelBaseInterface#save()} on the returned instance to save it in the local database.
     * The synchronisation can fail if user aren't the create write, but the local model stay in the local storage.
     *
     * @return A new unsaved instance
     */
    T getNew();

    /**
     * Get an element by id.
     *
     * @param id element's id to retrieve
     * @return element or null
     */
    T get(String id);

    /**
     * Get all elements.
     *
     * @return Return all element or empty list
     */
    List<T> getAll();

    /**
     * Add an {@link ChangeListener} in the accessor.
     * The {@link ChangeListener#changed(List)} method will be called on data changes.
     *
     * @param changeListener add a change {@link ChangeListener}
     */
    void addChangeListener(ChangeListener<T> changeListener);

    /**
     * Remove a recorded {@link ChangeListener}.
     *
     * @param changeListener the listener to remove.
     */
    void removeChangeListener(ChangeListener<T> changeListener);

    void purgeAll();
}
