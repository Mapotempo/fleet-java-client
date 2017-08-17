package com.mapotempo.fleet.core.model.submodel;

import com.mapotempo.fleet.core.base.SubModelBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Address.
 */
public class Address extends SubModelBase
{
    // MAPOTEMPO KEY
    public static final String STREET = "mStreet";
    public static final String POSTALCODE = "mPostalcode";
    public static final String CITY = "mCity";
    public static final String STATE = "mState";
    public static final String COUNTRY = "mCountry";
    public static final String DETAIL = "mDetail";

    private String mStreet;
    private String mPostalCode;
    private String mCity;
    private String mState;
    private String mCountry;
    private String mDetail;

    public Address(Map map) {
        super(map);
    }

    public Address(String street, String postalCode, String city, String state, String country, String detail) {
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
        return "address : \n" + mStreet + "\n" + mPostalCode + "\n" + mCity + "\n" + mState + "\n" + mCountry +"\n" + mDetail;
    }
}

