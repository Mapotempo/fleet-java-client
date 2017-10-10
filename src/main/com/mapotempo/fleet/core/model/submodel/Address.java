/*
 * Copyright © Mapotempo, 2017
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

    @Override
    public void fromMap(Map map) {
        mStreet = map.get(STREET).toString();
        mPostalCode = map.get(POSTALCODE).toString();
        mCity = map.get(CITY).toString();
        mState = map.get(STATE).toString();
        mCountry = map.get(COUNTRY).toString();
        mDetail = map.get(DETAIL).toString();
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> res = new HashMap<>();
        res.put(STREET, mStreet);
        res.put(POSTALCODE, mPostalCode);
        res.put(CITY, mCity);
        res.put(STATE, mState);
        res.put(COUNTRY, mCountry);
        res.put(DETAIL, mDetail);
        return res;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getStreet() {
        return mStreet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPostalcode() {
        return mPostalCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCity() {
        return mCity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return mState;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCountry() {
        return mCountry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDetail() {
        return mDetail;
    }

    public boolean equals(Object obj) {
        if (super.equals(obj))
            return true;
        Address cmp = (Address) obj;
        if (mStreet.equals(cmp.mStreet))
            if (mPostalCode.equals(cmp.mPostalCode))
                if (mCity.equals(cmp.mCity))
                    if (mState.equals(cmp.mState))
                        if (mDetail.equals(cmp.mDetail))
                            return true;
        return false;
    }

    @Override
    public String toString() {
        return mStreet + " " + mCity + ", " + mPostalCode + " " + mState + " ";
    }
}
