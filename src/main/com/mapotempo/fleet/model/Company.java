package com.mapotempo.fleet.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.model.submodel.Location;

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
        return mDocument.getProperty("name").toString();
    }

    public Location getLocation() {
        Map dataType = (Map)mDocument.getProperty("location");
        Location res = new Location(dataType);
        return res;
    }
}
