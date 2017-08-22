package com.mapotempo.fleet.core.model.submodel;

import com.couchbase.lite.Database;
import com.mapotempo.fleet.core.base.SubModelBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Address.
 */
public class Address extends SubModelBase
{
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
     * @param map map
     * @param database database
     */
    public Address(Map map, Database database) {
        super(map, database);
    }

    /**
     * Location.
     * @param street street
     * @param postalCode postalCode
     * @param city city
     * @param state state
     * @param country country
     * @param detail detail
     * @param database database
     */
    public Address(String street, String postalCode, String city, String state, String country, String detail, Database database) {
        super(database);
        this.mStreet = street;
        this.mPostalCode = postalCode;
        this.mCity = city;
        this.mState = state;
        this.mCountry = country;
        this.mDetail = detail;
    }

    @Override
    public void fromMap(Map map) {
        this.mStreet = map.get(STREET).toString();
        this.mPostalCode = map.get(POSTALCODE).toString();
        this.mCity = map.get(CITY).toString();
        this.mState = map.get(STATE).toString();
        this.mCountry = map.get(COUNTRY).toString();
        this.mDetail = map.get(DETAIL).toString();
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

    public String getStreet() {
        return mStreet;
    }

    public String getPostalcode() {
        return mPostalCode;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public String getCountry() {
        return mCountry;
    }

    public String getDetail() {
        return mDetail;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null)
            if(this.mStreet == ((Address)obj).mStreet)
                if(this.mPostalCode == ((Address)obj).mPostalCode)
                    if(this.mCity == ((Address)obj).mCity)
                        if(this.mState == ((Address)obj).mState)
                            if(this.mCountry == ((Address)obj).mCountry)
                                if(this.mDetail == ((Address)obj).mDetail)
                                    return true;
        return false;
    }

    @Override
    public String toString() {
        return mStreet + " " + mCity + ", " + mPostalCode + " " +  mState + " ";
    }
}
