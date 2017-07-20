package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.model.submodel.Location;

/**
 * Company.
 */
@DocumentBase(type = "company")
public class Company {

    @FieldBase(name = "name")
    public String mName;

    @FieldBase(name = "location")
    public Location mLocation;

}
