package com.mapotempo.fleet.core.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.model.submodel.Address;
import com.mapotempo.fleet.core.utils.DateHelper;
import com.mapotempo.fleet.core.model.submodel.Location;

import java.util.Date;
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

    public void setName(String name) {
        setProperty("name", name);
    }

    public String getCompanyId() {
        return (String)getProperty("company_id", "No comnay id found");
    }

    public void setCompanyId(String companyId) {
        setProperty("company_id", companyId);
    }

    public Date getDeliveryDate() {
        String dataType = (String)getProperty("delivery_date", "0");
        Date res = DateHelper.fromStringISO8601(dataType);
        return res;
    }

    public void setDeliveryDate(String isoDate) {
        setProperty("delivery_date", isoDate);
    }

    public void setDeliveryDate(Date date) {
        setProperty("delivery_date", DateHelper.toStringISO8601(date));
    }

    public Location getLocation() {
        Location defaultLocation = new Location(0, 0);
        Map dataType = (Map)getProperty("location", defaultLocation.toMap());
        Location res = new Location(dataType);
        return res;
    }

    public void setLocation(Location location) {
        setProperty("location", location.toMap());
    }

    public Address getAddress() {
        Address defaultAddress = new Address("", "", "", "", "", "");
        Map dataType = (Map)getProperty("address", defaultAddress.toMap());
        Address res = new Address(dataType);
        return res;
    }

    public void setAddress(Address address) {
        setProperty("address", address.toMap());
    }
}
