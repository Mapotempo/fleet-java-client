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
        void changed(T item);
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