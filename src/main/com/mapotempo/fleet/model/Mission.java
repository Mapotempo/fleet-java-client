package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;
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
    public Mission(String name, Location location, Device device) {
        this.mName = name;
        this.mLocation = location;
        this.mDevice = device;
    }

    @FieldBase(name = "device", foreign = true)
    public Device mDevice;

    @FieldBase(name = "name")
    public String mName;

    @FieldBase(name = "location")
    public Location mLocation;

    @FieldBase(name = "date")
    public Date mDate;

    @FieldBase(name = "delivery_date")
    public Date mDeliveryDate;

    /**
     * equals.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj != null )
            if(this.mName.equals(((Mission)obj).mName))
                if(this.mLocation.equals(((Mission)obj).mLocation))
                    if(this.mDevice != null && ((Mission)obj).mDevice != null) {
                        if (this.mDevice.equals(((Mission) obj).mDevice))
                            return super.equals(obj);
                    }
                    else if(this.mDevice == null && ((Mission)obj).mDevice == null)
                            return true;
        return false;
    }
}
