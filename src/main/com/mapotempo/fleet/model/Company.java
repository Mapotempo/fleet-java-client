package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.model.submodel.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Company.
 */
@DocumentBase(type = "company")
public class Company extends ModelBase
{
    public Company() {
    }

    @FieldBase(name = "name")
    public String mName;

    @FieldBase(name = "location")
    public Location mLocation;

    @FieldBase(name = "owners")
    public ArrayList<String> owners;
}
