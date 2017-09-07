package com.mapotempo.fleet.api.model.submodel;

import java.util.Date;

/**
 * TimeWindowsInterface.
 * Describe a time windows
 */
public interface TimeWindowsInterface {
    /**
     * The start time windows.
     *
     * @return A {@link Date}
     */
    Date getStart();

    /**
     * The end time windows.
     *
     * @return A {@link Date}
     */
    Date getEnd();
}
