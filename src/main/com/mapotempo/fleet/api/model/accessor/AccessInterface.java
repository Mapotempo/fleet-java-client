package com.mapotempo.fleet.api.model.accessor;

import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;

import java.util.List;

/**
 * AccessInterface.
 */
public interface AccessInterface<T> {

    /**
     * getNew.
     * @return return new data
     */
    public T getNew () throws CoreException;

    /**
     * get.
     * @param id
     * @return
     */
    public T get(String id) throws CoreException;

    /**
     * getAll.
     * @return
     */
    public List<T> getAll() throws CoreException;

    /**
     * addChangeListener.
     */
    public void addChangeListener(Access.ChangeListener<T> changeListener);

    /**
     * addRemoveListener.
     */
    public void addRemoveListener(Access.ChangeListener<T> changeListener);

}
