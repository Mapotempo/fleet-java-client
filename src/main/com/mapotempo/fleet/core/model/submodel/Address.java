/*
 * Copyright © Mapotempo, 2018
 *
 * This file is part of Mapotempo.
 *
 * Mapotempo is free software. You can redistribute it and/or
 * modify since you respect the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Mapotempo is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Licenses for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Mapotempo. If not, see:
 * <http://www.gnu.org/licenses/agpl.html>
 */

package com.mapotempo.fleet.core.model.submodel;

import com.couchbase.lite.Database;
import com.mapotempo.fleet.api.model.submodel.AddressInterface;
import com.mapotempo.fleet.core.base.SubModelBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Address.
 */
public class Address extends SubModelBase implements AddressInterface {
    // MAPOTEMPO KEY
    private static final String STREET = "street";
    private static final String POSTALCODE = "postalcode";
    private static final String CITY = "city";
    private static final String STATE = "state";
    private static final String COUNTRY = "country";
    private static final String DETAIL = "detail";

    private String mStreet;
    private String mPostalCode;
    private String mCity;
    private String mState;
    private String mCountry;
    private String mDetail;

    /**
     * Location.
     *
     * @param map      map
     * @param database database
     */
    public Address(Map map, Database database) {
        super(map, database);
    }

    /**
     * Location.
     *
     * @param street     street
     * @param postalCode postalCode
     * @param city       city
     * @param state      state
     * @param country    country
     * @param detail     detail
     * @param database   database
     */
    public Address(String street, String postalCode, String city, String state, String country, String detail, Database database) {
        super(database);
        mStreet = street;
        mPostalCode = postalCode;
        mCity = city;
        mState = state;
        mCountry = country;
        mDetail = detail;
    }

    private String stringPicker(String string) {
        if (string == null)
            return string;
        return string.trim().isEmpty() ? null : string.trim();
    }

    @Override
    public void fromMap(Map map) {
        mStreet = getProperty(STREET, String.class, null, map);
        mPostalCode = getProperty(POSTALCODE, String.class, null, map);
        mCity = getProperty(CITY, String.class, null, map);
        mState = getProperty(STATE, String.class, null, map);
        mCountry = getProperty(COUNTRY, String.class, null, map);
        mDetail = getProperty(DETAIL, String.class, null, map);
    }

    @Override
    public Map<String, Object> toMap() {
        HashMap<String, Object> res = new HashMap<>();
        res.put(STREET, stringPicker(mStreet));
        res.put(POSTALCODE, stringPicker(mPostalCode));
        res.put(CITY, stringPicker(mCity));
        res.put(STATE, stringPicker(mState));
        res.put(COUNTRY, stringPicker(mCountry));
        res.put(DETAIL, stringPicker(mDetail));
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getStreet() {
        return mStreet != null ? mStreet : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPostalcode() {
        return mPostalCode != null ? mPostalCode : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCity() {
        return mCity != null ? mCity : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return mState != null ? mState : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCountry() {
        return mCountry != null ? mCountry : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDetail() {
        return mDetail != null ? mDetail : "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        Map<String, Object> myMap = toMap();

        for (String element : myMap.keySet()) {
            if (myMap.get(element) != null && !((String) myMap.get(element)).isEmpty())
                return true;
        }

        return false;
    }

    public boolean equals(Object obj) {
        if (super.equals(obj))
            return true;
        Address cmp = (Address) obj;

        return (mStreet.equals(cmp.mStreet) &&
                mPostalCode.equals(cmp.mPostalCode) &&
                mCity.equals(cmp.mCity) &&
                mState.equals(cmp.mState) &&
                mDetail.equals(cmp.mDetail));
    }

    @Override
    public String toString() {
        return mStreet + " " + mCity + ", " + mPostalCode + " " + mState + " ";
    }
}
