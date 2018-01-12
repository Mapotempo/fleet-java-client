package com.mapotempo.fleet.api.model.submodel;

import java.util.Date;

/**
 * LocationDetailsInterface.
 * Describe a dated location
 */
public interface LocationDetailsInterface extends LocationInterface {

    /**
     * Get the Date.
     *
     * @return A Date
     */
    Date getDate();
}
