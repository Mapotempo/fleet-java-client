package com.mapotempo.fleet.model;

import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.FieldBase;
import com.mapotempo.fleet.model.submodel.Location;

/**
 * Track.
 */
@DocumentBase(type = "track")
public class Track {

    @FieldBase(name = "location")
    public Location mLocation;

    @FieldBase(name = "date")
    public int date;
}
