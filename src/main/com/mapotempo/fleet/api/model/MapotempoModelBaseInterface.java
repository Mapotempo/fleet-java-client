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

package com.mapotempo.fleet.api.model;

public interface MapotempoModelBaseInterface {

    /**
     * Delete the model from local storage and server storage if user permission are correct.
     *
     * @return true if correctly delete
     */
    boolean delete();

    /**
     * ChangeListener interface.
     *
     * @param <T> The template type
     */
    interface ChangeListener<T extends MapotempoModelBaseInterface> {
        void changed(T item, boolean onDeletion);
    }

    /**
     * Add an {@link ChangeListener} on the model.
     * The {@link ChangeListener#changed(MapotempoModelBaseInterface)} method will be called on data changes.
     *
     * @param changeListener add a change {@link ChangeListener}
     */
    void addChangeListener(ChangeListener changeListener);

    /**
     * Remove a recorded {@link ChangeListener}.
     *
     * @param changeListener the listener to remove.
     */
    void removeChangeListener(ChangeListener changeListener);

    /**
     * Get the id.
     *
     * @return id
     */
    String getId();

    /**
     * Save instance.
     * You can call this after model modification to save in the local storage, if user are correct permission the
     * modification will be apply on the server.
     *
     * @return true if correctly save
     */
    boolean save();
}
