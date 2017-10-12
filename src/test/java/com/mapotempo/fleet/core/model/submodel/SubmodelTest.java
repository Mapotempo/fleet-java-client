package com.mapotempo.fleet.core.model.submodel;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.JavaContext;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.utils.DateHelper;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * SubmodelTest.
 */
@DisplayName("SubmodelTest")
class SubmodelTest {

    static DatabaseHandler mDatabaseHandler;

    static Map<String, Object> mMap;

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
        System.out.println("Remove the database");
        mDatabaseHandler.mDatabase.delete();
        System.out.println(" - Database successfull removed");
    }

    @BeforeEach
    void BeforeEach() {
        mMap = new HashMap<>();
    }

    @AfterEach
    void AfterEach() throws CouchbaseLiteException, IOException, InterruptedException {
        mMap = null;
    }

    @Nested
    @DisplayName("Address")
    class AddressTest {
        @Test
        @DisplayName("Full Field")
        void testAddressFullField() throws Exception {
            mMap.put("street", "test-0");
            mMap.put("postalcode", "test-1");
            mMap.put("city", "test-2");
            mMap.put("state", "test-3");
            mMap.put("country", "test-4");
            mMap.put("detail", "test-5");
            Address address = new Address(mMap, mDatabaseHandler.mDatabase);
            Assertions.assertEquals(address.getStreet(), "test-0");
            Assertions.assertEquals(address.getPostalcode(), "test-1");
            Assertions.assertEquals(address.getCity(), "test-2");
            Assertions.assertEquals(address.getState(), "test-3");
            Assertions.assertEquals(address.getCountry(), "test-4");
            Assertions.assertEquals(address.getDetail(), "test-5");
        }

        @Test
        @DisplayName("Empty Field")
        void testAddressEmptyField() throws Exception {
            Location location = new Location(mMap, mDatabaseHandler.mDatabase);
            Assertions.assertNotEquals(location.getLat(), 45.);
            Assertions.assertEquals(location.getLat(), 0.);
        }

        @Test
        @DisplayName("Field Type Error")
        void testAddressFieldTypeError() throws Exception {
            mMap.put("street", 1);
            mMap.put("postalcode", 2);
            mMap.put("city", 2);
            mMap.put("state", 3);
            mMap.put("country", 4);
            mMap.put("detail", 5);
            Address address = new Address(mMap, mDatabaseHandler.mDatabase);
            Assertions.assertEquals(address.getStreet(), "");
            Assertions.assertEquals(address.getPostalcode(), "");
            Assertions.assertEquals(address.getCity(), "");
            Assertions.assertEquals(address.getState(), "");
            Assertions.assertEquals(address.getCountry(), "");
            Assertions.assertEquals(address.getDetail(), "");
        }
    }

    @Nested
    @DisplayName("Location")
    class LocationTest {
        @Test
        @DisplayName("Full Field")
        void testLocationFullField() throws Exception {
            mMap.put("lon", new Double(-0.53));
            mMap.put("lat", new Double(45.));
            Location location = new Location(mMap, mDatabaseHandler.mDatabase);
            Assertions.assertEquals(location.getLon(), -0.53);
            Assertions.assertEquals(location.getLat(), 45.);
            Assertions.assertTrue(true);
        }

        @Test
        @DisplayName("Empty Field")
        void testLocationLon() throws Exception {
            Location location = new Location(mMap, mDatabaseHandler.mDatabase);
            Assertions.assertNotEquals(location.getLon(), -0.53);
            Assertions.assertEquals(location.getLon(), 0.);
        }

        @Test
        @DisplayName("Field Type Error")
        void testLocationFieldTypeError() throws Exception {
            mMap.put("lon", "-0.53");
            mMap.put("lat", "45.");
            Location location = new Location(mMap, mDatabaseHandler.mDatabase);
            Assertions.assertNotEquals(location.getLat(), 45.);
            Assertions.assertEquals(location.getLat(), 0.);
        }
    }

    @Nested
    @DisplayName("Location Details")
    class LocationDetailsTest {
        @Test
        @DisplayName("Full Field")
        void testLocationFullField() throws Exception {
            Date d = new Date();
            mMap.put("lon", -0.53);
            mMap.put("lat", 45.);
            mMap.put("time", DateHelper.toStringISO8601(d));
            mMap.put("accuracy", 45.);
            mMap.put("speed", 0.1);
            mMap.put("bearing", 0.2);
            mMap.put("altitude", 0.3);
            mMap.put("signal_strength", 1);
            mMap.put("cid", 1);
            mMap.put("lac", 2);
            mMap.put("mcc", 3);
            mMap.put("mnc", 4);
            LocationDetails locationDetails = new LocationDetails(mMap, mDatabaseHandler.mDatabase);
            Assertions.assertEquals(locationDetails.getLon(), -0.53);
            Assertions.assertEquals(locationDetails.getLat(), 45.);
            Assertions.assertEquals(locationDetails.getDate(), d);
            Assertions.assertEquals(locationDetails.getmAccuracy(), (Double) 45.);
            Assertions.assertEquals(locationDetails.getSpeed(), (Double) 0.1);
            Assertions.assertEquals(locationDetails.getBearing(), (Double) 0.2);
            Assertions.assertEquals(locationDetails.getAltitude(), (Double) 0.3);
            Assertions.assertEquals(locationDetails.getSignalStrength(), (Integer) 1);
            Assertions.assertEquals(locationDetails.getCid(), (Integer) 1);
            Assertions.assertEquals(locationDetails.getLac(), (Integer) 2);
            Assertions.assertEquals(locationDetails.getMcc(), (Integer) 3);
            Assertions.assertEquals(locationDetails.getMnc(), (Integer) 4);
        }

        @Test
        @DisplayName("Empty Field")
        void testLocationLon() throws Exception {
            Location location = new Location(mMap, mDatabaseHandler.mDatabase);
            Assertions.assertNotEquals(location.getLon(), -0.53);
            Assertions.assertEquals(location.getLon(), 0.);
        }

        @Test
        @DisplayName("Field Type Error")
        void testLocationFieldTypeError() throws Exception {
            Date d = new Date();
            mMap.put("lon", "");
            mMap.put("lat", "");
            mMap.put("time", "");
            mMap.put("accuracy", "");
            mMap.put("speed", "");
            mMap.put("bearing", "");
            mMap.put("altitude", "");
            mMap.put("signal_strength", "");
            mMap.put("cid", "");
            mMap.put("lac", "");
            mMap.put("mcc", "");
            mMap.put("mnc", "");
            LocationDetails locationDetails = new LocationDetails(mMap, mDatabaseHandler.mDatabase);
            Assertions.assertEquals(locationDetails.getLon(), 0.);
            Assertions.assertEquals(locationDetails.getLat(), 0.);
            Assertions.assertEquals(locationDetails.getDate(), new Date(0));
            Assertions.assertEquals(locationDetails.getmAccuracy(), (Double) 0.);
            Assertions.assertEquals(locationDetails.getSpeed(), (Double) 0.);
            Assertions.assertEquals(locationDetails.getBearing(), (Double) 0.);
            Assertions.assertEquals(locationDetails.getAltitude(), (Double) 0.);
            Assertions.assertEquals(locationDetails.getSignalStrength(), (Integer) 0);
            Assertions.assertEquals(locationDetails.getCid(), (Integer) (-1));
            Assertions.assertEquals(locationDetails.getLac(), (Integer) (-1));
            Assertions.assertEquals(locationDetails.getMcc(), (Integer) (-1));
            Assertions.assertEquals(locationDetails.getMnc(), (Integer) (-1));
        }
    }
}