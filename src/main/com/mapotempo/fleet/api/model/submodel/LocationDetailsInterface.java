package com.mapotempo.fleet.api.model.submodel;

import java.util.Date;

/**
 * LocationDetailsInterface.
 * Describe a dated location
 */
public interface LocationDetailsInterface {
    /**
     * Get the longitude.
     *
     * @return A double
     */
    Double getLon();

    /**
     * Get the latitude.
     *
     * @return A double
     */
    Double getLat();

    /**
     * Get the Date.
     *
     * @return A Date
     */
    Date getDate();
}
