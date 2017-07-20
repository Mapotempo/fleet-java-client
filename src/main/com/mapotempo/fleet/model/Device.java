package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.model.submodel.Location;

@DocumentBase(type = "device")
public class Device extends ModelBase
{
    public Device() {
    }

    /**
     * Constructor.
     * @param name Device name
     */
    public Device(String name) {
        this.mName = name;
    }

    @FieldBase(name = "name")
    public String mName;

    /**
     * equals.
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if(obj != null)
            if(this.mName.equals(((Mission)obj).mName))
                return super.equals(obj);
        return false;
    }
}
