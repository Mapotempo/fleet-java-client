package com.mapotempo.fleet.core.model.submodel;

import com.couchbase.lite.Database;
import com.mapotempo.fleet.api.model.model.submodel.SubModelFactoryInterface;
import com.mapotempo.fleet.core.model.MissionStatusType;

import java.util.Date;

/**
 * SubModel factory
 */
public class SubModelFactory implements SubModelFactoryInterface {

    private Database mDatabase;

    public SubModelFactory(Database database) {
        mDatabase = database;
    }

    /** {@inheritDoc} */
    @Override
    public Location CreateNewLocation(double lat, double lon) {
        Location res = new Location(lat, lon, mDatabase);
        return res;
    }

    /** {@inheritDoc} */
    @Override
    public Address CreateNewAddress(String street, String postalCode, String city, String state, String country, String detail) {
        Address res = new Address(street, postalCode, city, state, country, detail, mDatabase);
        return res;
    }

    /** {@inheritDoc} */
    @Override
    public MissionCommand CreateNewMissionCommand(String label, MissionStatusType missionStatusType, String group) {
        MissionCommand res = new MissionCommand(label, missionStatusType, group, mDatabase);
        return res;
    }

    /** {@inheritDoc} */
    @Override
    public TimeWindow CreateNewTimeWindow(Date start, Date end) {
        TimeWindow res = new TimeWindow(start, end, mDatabase);
        return res;
    }
}
