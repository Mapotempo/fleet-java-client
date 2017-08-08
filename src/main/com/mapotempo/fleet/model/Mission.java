package com.mapotempo.fleet.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.accessor.MapotempoModelBase;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.utils.DateHelper;
import com.mapotempo.fleet.model.submodel.Location;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Company.
 */
@DocumentBase(type = "mission")
public class Mission extends MapotempoModelBase {

    public Mission(Database database) {
        super(database);
    }

    public Mission(Document document) {
        super(document);
    }

    public String getName() {
        return (String)getProperty("name", "Unknow");
    }

    /*
    public void setName(String name) {
        setProperty("name", name);
    }*/

    public String getCompanyId() {
        return (String)getProperty("company_id", "No comnay id found");
    }

    public Date getDeliveryDate() {
        String dataType = (String)getProperty("delivery_date", "0");
        Date res = DateHelper.dateFromString(dataType);
        return res;
    }

    public Location getLocation() {
        Location defaultLocation = new Location(0, 0);
        Map dataType = (Map)getProperty("location", defaultLocation.toMap());
        Location res = new Location(dataType);
        return res;
    }
}
