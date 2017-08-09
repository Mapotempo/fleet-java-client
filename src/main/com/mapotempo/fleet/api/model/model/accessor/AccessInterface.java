package com.mapotempo.fleet.api.model.model.accessor;

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

}
