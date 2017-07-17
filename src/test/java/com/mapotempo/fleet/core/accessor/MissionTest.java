package com.mapotempo.fleet.core.accessor;

import com.couchbase.lite.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.model.Mission;
import com.mapotempo.fleet.model.submodel.Location;
import org.junit.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


import static junit.framework.TestCase.assertEquals;

/**
 * MissionTest.
 */
public class MissionTest {

    private static String DB_NAME = "database_test";

    private static DatabaseHandler databaseHandler;

    private static Integer id_doc = 0;

    @BeforeClass
    public static void setUpDataBase() throws CoreException, CouchbaseLiteException, IOException {
        System.out.println("----------");
        System.out.println("Prepare data base");
        databaseHandler = new DatabaseHandler(DB_NAME, DB_NAME, "http://localhost:4985/db", new JavaContext());
    }

    @AfterClass
    public static void clearDataBase() throws CouchbaseLiteException, IOException {
        databaseHandler.mDatabase.delete();
    }

    @Before
    public void beforeAll() {
    }

    @After
    public void afterAll() {
    }

    @Test
    public void testMissionFromDocumentWithoutDevice() throws Exception {
        // Actual data
        String jsonRes =
                "{\n" +
                        "  \"location\": {\n" +
                        "    \"lat\": 45,\n" +
                        "    \"lon\": 2\n" +
                        "  },\n" +
                        "  \"name\": \"super_man\",\n" +
                        "  \"type\": \"mission\",\n" +
                        "  \"_rev\": \"2-3d0fa5baddfe4d42cfdae9e0d847b9e4\",\n" +
                        "  \"_id\": \"Mission_401aea24-423b-4b75-8aa0-378953c84d15\"\n" +
                        "}";
        Map<String, Object> mapRes = new ObjectMapper().readValue(jsonRes, HashMap.class);
        Document document = databaseHandler.mDatabase.getDocument("Mission_401aea24-423b-4b75-8aa0-378953c84d15");
        UnsavedRevision update = document.createRevision();
        update.setProperties(mapRes);
        update.save();
        Factory<Mission> missionFactory =  new Factory<>(Mission.class, databaseHandler);
        Mission actual = missionFactory.getInstance(document);

        // Expected data
        Mission expected = new Mission();
        expected.mId = "Mission_401aea24-423b-4b75-8aa0-378953c84d15";
        expected.mLocation = new Location(45, 2);
        expected.mName = "super_man";
        expected.mDevice = null;

        // Test
        assertEquals(expected, actual);
    }

    @Test
    public void testMissionFromDocument() throws Exception {
        // TODO
    }

    @Test
    public void testDocumentFromMission() throws Exception {
        // TODO
        assertEquals(2, 2);
    }
}
