package com.mapotempo.fleet.model.submodel;

import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.SubModelBase;

import java.util.HashMap;
import java.util.Map;

/**
 * Address.
 */
public class Address extends SubModelBase
{
    public Address(Map map, DatabaseHandler databaseHandler) {
        super(map, databaseHandler);
    }

    public Address(String street, String postalcode, String city, String state, String country, String detail) {
        this.street = street;
        this.postalcode = postalcode;
        this.city = city;
        this.state = state;
        this.country = country;
        this.detail = detail;
    }

    @Override
    public void fromMap(Map map) {
        this.street = map.get("street").toString();
        this.postalcode = map.get("postalcode").toString();
        this.city = map.get("city").toString();
        this.state = map.get("state").toString();
        this.country = map.get("country").toString();
        this.detail = map.get("detail").toString();
    }

    @Override
    public Map<String, String> toMap() {
        HashMap<String, String> res = new HashMap<>();
        res.put("street", street);
        res.put("postalcode", postalcode);
        res.put("city", city);
        res.put("state", state);
        res.put("country", country);
        res.put("detail", detail);
        return res;
    }

    private String street;
    private String postalcode;
    private String city;
    private String state;
    private String country;
    private String detail;


    @Override
    public boolean equals(Object obj) {
        if(obj != null)
            if(this.street == ((Address)obj).street)
                if(this.postalcode == ((Address)obj).postalcode)
                    if(this.city == ((Address)obj).city)
                        if(this.state == ((Address)obj).state)
                            if(this.country == ((Address)obj).country)
                                if(this.detail == ((Address)obj).detail)
                                    return true;
        return false;
    }

    @Override
    public String toString() {
        return "address : \n" + street + "\n" + postalcode + "\n" + city  + "\n" + state + "\n" + country +"\n" + detail;
    }
}

