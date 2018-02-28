package com.mapotempo.fleet.core.utils;

import com.couchbase.lite.CouchbaseLiteException;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.UserCurrentLocation;
import com.mapotempo.fleet.core.model.submodel.LocationDetails;
import com.mapotempo.fleet.core.LocationManager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

public class LocationManagerTest {
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
        // Distance = 120
        LocationDetails locationDetails1 = Mockito.mock(LocationDetails.class);
        Mockito.when(locationDetails1.getLat()).thenReturn(44.85011876769996);
        Mockito.when(locationDetails1.getLon()).thenReturn(-0.5435764789581299);
        Mockito.when(locationDetails1.getmAccuracy()).thenReturn(10.);
        mLocationManager.updateLocation(locationDetails1);

        LocationDetails locationDetails2 = Mockito.mock(LocationDetails.class);
        Mockito.when(locationDetails2.getLat()).thenReturn(44.850818538656945);
        Mockito.when(locationDetails2.getLon()).thenReturn(-0.5423963069915771);
        Mockito.when(locationDetails2.getmAccuracy()).thenReturn(200.);
        mLocationManager.updateLocation(locationDetails2);

        Assertions.assertEquals((Double) 10., mLocationManager.getNextAvailableLocation().getmAccuracy());

        Mockito.when(locationDetails2.getmAccuracy()).thenReturn(5.);
        mLocationManager.updateLocation(locationDetails2);

        Assertions.assertEquals((Double) 5., mLocationManager.getNextAvailableLocation().getmAccuracy());
    }
}
