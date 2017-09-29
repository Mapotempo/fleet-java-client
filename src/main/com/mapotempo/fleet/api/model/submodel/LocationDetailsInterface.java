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
    double getLon();

    /**
     * Get the latitude.
     *
     * @return A double
     */
    double getLat();

    /**
     * Get the Date.
     *
     * @return A Date
     */
    Date getDate();
}
