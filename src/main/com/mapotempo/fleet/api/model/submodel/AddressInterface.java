package com.mapotempo.fleet.api.model.submodel;

/**
 * AddressInterface.
 * Describe an address
 */
public interface AddressInterface {
    /**
     * Get the street name.
     *
     * @return A {@link String}
     */
    String getStreet();

    /**
     * Get the postal code.
     *
     * @return A {@link String}
     */
    String getPostalcode();

    /**
     * Get the city name.
     *
     * @return A {@link String}
     */
    String getCity();

    /**
     * Get the state.
     *
     * @return A {@link String}
     */
    String getState();

    /**
     * Get the country.
     *
     * @return A {@link String}
     */
    String getCountry();

    /**
     * Get the address detail.
     *
     * @return A {@link String}
     */
    String getDetail();
}
