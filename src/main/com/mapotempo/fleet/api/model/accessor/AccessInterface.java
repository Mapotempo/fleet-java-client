package com.mapotempo.fleet.api.model.accessor;

import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.exception.CoreException;

import java.util.List;

/**
 * AccessInterface.
 */
public interface AccessInterface<T extends MapotempoModelBase> {

    /**
     * getNew.
     * @return return new data
     */
    T getNew ();

    /**
     * get.
     * @param id
     * @return
     */
    T get(String id);

    /**
     * getAll.
     * @return
     */
    List<T> getAll();

    /**
     * addChangeListener.
     */
    void addChangeListener(Access.ChangeListener<T> changeListener);

    /**
     * addRemoveListener.
     */
    void removeChangeListener(Access.ChangeListener<T> changeListener);

}
