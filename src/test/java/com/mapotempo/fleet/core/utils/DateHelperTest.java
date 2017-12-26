package com.mapotempo.fleet.core.utils;

import com.couchbase.lite.CouchbaseLiteException;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.UserCurrentLocation;
import com.mapotempo.fleet.core.model.submodel.LocationDetails;
import com.mapotempo.fleet.utils.LocationManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class DateHelperTest {
    static int TIMEOUT = 400;
    static UserCurrentLocation mUserCurrentLocation = Mockito.mock(UserCurrentLocation.class);
    static LocationManager mLocationManager;

    @BeforeEach
    void BeforeAll() throws CoreException, CouchbaseLiteException, IOException {
        mLocationManager = new LocationManager(mUserCurrentLocation, TIMEOUT);
    }

    /**
     * Only one save action should be call during the laps time * 3.
     *
     * @throws InterruptedException
     */
    @Test
    void test() throws InterruptedException {
        LocationDetails locationDetails1 = Mockito.mock(LocationDetails.class);
        mLocationManager.updateLocation(locationDetails1);
        LocationDetails locationDetails2 = Mockito.mock(LocationDetails.class);
        mLocationManager.updateLocation(locationDetails2);
        LocationDetails locationDetails3 = Mockito.mock(LocationDetails.class);
        mLocationManager.updateLocation(locationDetails3);
        Thread.sleep(TIMEOUT * 4);
        Mockito.verify(mUserCurrentLocation).save();
    }
}
