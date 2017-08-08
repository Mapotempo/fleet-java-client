package com.mapotempo.fleet.core.accessor_TO_DELETE;

import com.couchbase.lite.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.exception.CoreException;

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
      //  databaseHandler = new DatabaseHandler(DB_NAME, DB_NAME, "http://localhost:4985/db", new JavaContext());
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


        // Test
//        assertEquals(expected, actual);
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
