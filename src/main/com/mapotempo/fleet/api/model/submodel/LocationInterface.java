package com.mapotempo.fleet.api.model.submodel;

/**
 * LocationInterface.
 * Describe a location
 */
public interface LocationInterface {
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
     * Location is valide.
     * Check if lon and lat aren't null
     *
     * @return boolean verity
     */
    boolean isValide();

}
