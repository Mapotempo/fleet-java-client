package com.mapotempo.fleet.core.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.submodel.Address;
import com.mapotempo.fleet.core.model.submodel.Location;
import com.mapotempo.fleet.core.utils.DateHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Company.
 */
@DocumentBase(type = "mission")
public class Mission extends MapotempoModelBase {

    // MAPOTEMPO KEY
    public static final String NAME = "name";
    public static final String COMPANY_ID = "company_id";
    public static final String DELIVERY_DATE = "delivery_date";
    public static final String LOCATION = "location";
    public static final String ADDRESS = "address";
    public static final String OWNERS = "owners";
    public static final String MISSION_STATUS_TYPE_ID = "mission_status_type_id";

    public Mission(Database database) {
        super(database);
    }

    public Mission(Document document) {
        super(document);
    }

    public String getName() {
        return (String)getProperty(NAME, "Unknow");
    }

    public void setName(String name) {
        setProperty(NAME, name);
    }

    public String getCompanyId() {
        return (String)getProperty(COMPANY_ID, "No company id found");
    }

    public void setCompanyId(String companyId) {
        setProperty(COMPANY_ID, companyId);
    }

    public Date getDeliveryDate() {
        String dataType = (String)getProperty(DELIVERY_DATE, "0");
        Date res = DateHelper.fromStringISO8601(dataType);
        return res;
    }

    public void setDeliveryDate(String isoDate) {
        setProperty(DELIVERY_DATE, isoDate);
    }

    public void setDeliveryDate(Date date) {
        setProperty(DELIVERY_DATE, DateHelper.toStringISO8601(date));
    }

    public Location getLocation() {
        Location defaultLocation = new Location(0, 0);
        Map dataType = (Map)getProperty(LOCATION, defaultLocation.toMap());
        Location res = new Location(dataType);
        return res;
    }

    public void setLocation(Location location) {
        setProperty(LOCATION, location.toMap());
    }

    public Address getAddress() {
        Address defaultAddress = new Address("", "", "", "", "", "");
        Map dataType = (Map)getProperty(ADDRESS, defaultAddress.toMap());
        Address res = new Address(dataType);
        return res;
    }

    public void setAddress(Address address) {
        setProperty(ADDRESS, address.toMap());
    }

    public MissionStatusType getStatus() {
        String status_id = (String)getProperty(MISSION_STATUS_TYPE_ID, "0");
        try {
            MissionStatusType defaultStatus = new MissionStatusType(status_id, mDatabase);
            return defaultStatus;
        } catch (CoreException e) {
            e.printStackTrace();
            System.out.println("WARNING : return a non saved MissionStatusType");
            MissionStatusType missionStatus = new MissionStatusType(mDatabase);
            missionStatus.setLabel(status_id);
            return missionStatus;
        }
    }

    public void setStatus(MissionStatusType missionStatus) {
        setProperty(MISSION_STATUS_TYPE_ID, missionStatus.getId());
    }


    public ArrayList<String> getOwners() {
        return  (ArrayList<String>)getProperty(OWNERS, new ArrayList<String>());
    }

    public void setOwners(ArrayList<String> owners) {
        setProperty(OWNERS, owners);
    }
}
