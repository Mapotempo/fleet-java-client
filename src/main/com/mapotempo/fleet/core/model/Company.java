package com.mapotempo.fleet.core.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.model.submodel.Location;

import java.util.Map;

/**
 * Company.
 */
@DocumentBase(type = "company")
public class Company extends MapotempoModelBase {

    public Company(Database database) {
        super(database);
    }

    public Company(Document doc) {
        super(doc);
    }

    public String getName() {
        return (String) getProperty("name", "unknow");
    }

    public Location getLocation() {
        Location defaultLocation = new Location(0, 0);
        Map dataType = (Map)getProperty("location", defaultLocation.toMap());
        Location res = new Location(dataType);
        return res;
    }
}