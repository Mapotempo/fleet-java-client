package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.model.submodel.Address;
import com.mapotempo.fleet.model.submodel.Location;

import java.util.ArrayList;
import java.util.Date;

@DocumentBase(type = "mission")
public class Mission extends ModelBase {
    public Mission() {
    }

    /**
     * Constructor.
     * @param name Mission name
     * @param location Mission Location
     */
    public Mission(String name, Address address, Location location, Company company, Date deliveryDate) {
        this.mName = name;
        this.mLocation = location;
        this.mCompany = company;
        this.address = address;
        this.mDeliveryDate = deliveryDate;
    }

    @FieldBase(name = "device", foreign = true)
    public Company mCompany;

    @FieldBase(name = "name")
    public String mName = "unknow mission";

    @FieldBase(name = "address")
    public Address address = new Address("", "", "", "", "", "");

    @FieldBase(name = "location")
    public Location mLocation = new Location(0, 0);

    @FieldBase(name = "delivery_date")
    public Date mDeliveryDate = new Date();

    @FieldBase(name = "owners")
    public ArrayList<String> mOwner = new ArrayList<>();

    /**
     * equals.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj != null )
            if(this.mName.equals(((Mission)obj).mName))
                if((this.mLocation == null && ((Mission)obj).mLocation == null) || this.mLocation.equals(((Mission)obj).mLocation))
                    if(this.mCompany != null && ((Mission)obj).mCompany != null) {
                        if (this.mCompany.equals(((Mission) obj).mCompany))
                            return super.equals(obj);
                    }
                    else if(this.mCompany == null && ((Mission)obj).mCompany == null)
                            return true;
        return false;
    }
}
