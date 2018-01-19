package com.mapotempo.fleet.core.utils;

import com.couchbase.lite.CouchbaseLiteException;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.UserCurrentLocation;
import com.mapotempo.fleet.core.model.submodel.LocationDetails;
import com.mapotempo.fleet.utils.LocationManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class DateHelperTest {
    static int TIMEOUT = 10000; // Time configuration for location manager
    static UserCurrentLocation mUserCurrentLocation = Mockito.mock(UserCurrentLocation.class);
    static LocationManager mLocationManager;

    @BeforeEach
    void BeforeAll() throws CoreException, CouchbaseLiteException, IOException {
        mLocationManager = new LocationManager(mUserCurrentLocation, TIMEOUT);
    }

    @DisplayName("Only one save action should be call during the laps time * 3")
    @Test
    void test() throws InterruptedException {
        LocationDetails locationDetails1 = Mockito.mock(LocationDetails.class);
        mLocationManager.updateLocation(locationDetails1);
        LocationDetails locationDetails2 = Mockito.mock(LocationDetails.class);
        mLocationManager.updateLocation(locationDetails2);
        LocationDetails locationDetails3 = Mockito.mock(LocationDetails.class);
        mLocationManager.updateLocation(locationDetails3);

        Mockito.verify(mUserCurrentLocation, Mockito
                .after(TIMEOUT)
                .times(1))
                .save();
    }

    @DisplayName("Location with best accuracy are save")
    @Test
    void test2() throws InterruptedException {
        LocationDetails locationDetails1 = Mockito.mock(LocationDetails.class);
        Mockito.when(locationDetails1.getmAccuracy()).thenReturn(30.);
        mLocationManager.updateLocation(locationDetails1);

        LocationDetails locationDetails2 = Mockito.mock(LocationDetails.class);
        Mockito.when(locationDetails2.getmAccuracy()).thenReturn(20.);
        mLocationManager.updateLocation(locationDetails2);

        LocationDetails locationDetails3 = Mockito.mock(LocationDetails.class);
        Mockito.when(locationDetails3.getmAccuracy()).thenReturn(40.);
        mLocationManager.updateLocation(locationDetails3);

        Assertions.assertEquals((Double) 20., mLocationManager.getNextAvailableLocation().getmAccuracy());
    }
}
