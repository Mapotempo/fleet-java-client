package com.mapotempo.fleet.core;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.JavaContext;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.submodel.Location;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * LocationTest.
 */
@DisplayName("LocationTest")
class LocationTest {

    static DatabaseHandler mDatabaseHandler;

    static Map<String, Object> mLocationMap;

    @BeforeAll
    static void BeforeAll() throws CoreException, CouchbaseLiteException, IOException {
        System.out.println("Create the data base");
        mDatabaseHandler = new DatabaseHandler("default_abcde", "default_abcde", new JavaContext(), new DatabaseHandler.OnCatchLoginError() {
            @Override
            public void CatchLoginError() {
            }
        });
        System.out.println(" - Database successfull created");
    }

    @AfterAll
    static void AfterAll() throws CoreException, CouchbaseLiteException, IOException {
        // Wait one second for async task
        System.out.println("Remove the database");
        mDatabaseHandler.mDatabase.delete();
        System.out.println(" - Database successfull removed");
    }

    @BeforeEach
    void BeforeEach() {
        mLocationMap = new HashMap<>();
        mLocationMap.put("lon", new Double(-0.53));
        mLocationMap.put("lat", new Double(45.));
    }

    @AfterEach
    void AfterEach() throws CouchbaseLiteException, IOException, InterruptedException {
        mLocationMap = null;
    }

    @Test
    @DisplayName("Location Full")
    void testLocation() throws Exception {
        Location location = new Location(mLocationMap, mDatabaseHandler.mDatabase);
        Assertions.assertEquals(location.getLon(), -0.53);
        Assertions.assertEquals(location.getLat(), 45.);
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Location Lon is String value")
    void testLocationLon() throws Exception {
        mLocationMap.put("lon", "-0.53");
        Location location = new Location(mLocationMap, mDatabaseHandler.mDatabase);
        Assertions.assertNotEquals(location.getLon(), -0.53);
        Assertions.assertEquals(location.getLon(), 0.);
    }

    @Test
    @DisplayName("Location Lon is String value")
    void testLocationLat() throws Exception {
        mLocationMap.put("lat", "45.");
        Location location = new Location(mLocationMap, mDatabaseHandler.mDatabase);
        Assertions.assertNotEquals(location.getLat(), 45.);
        Assertions.assertEquals(location.getLat(), 0.);
    }
}