package com.mapotempo.fleet.core.model.accessor;

import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.BaseTest;
import com.mapotempo.fleet.core.DatabaseFeeder;
import com.mapotempo.fleet.core.model.Mission;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * MissionAccessTest.
 */
public class MissionAccessTest extends BaseTest {

    @BeforeAll
    static void BeforeAll() throws Exception {
        iniDatabase(DatabaseFeeder.Dataset.DATASET_1);
    }

    @AfterAll
    static void AfterAll() throws Exception {
        removeDatabase();
    }

    @Test
    @DisplayName("Mission access right order")
    void testMissionAccess() throws Exception {
        List<Mission> missions = mMissionAccess.getAllWithoutFilter(); // Without filter, to avoid having to update the input data
        List<Mission> expectedMissions = new ArrayList<>();
        Document doc = mDatabaseHandler.mDatabase.getDocument("mission_b89d5ag45h66c00fd4a56zda5z354be");
        expectedMissions.add(new Mission(doc));
        doc = mDatabaseHandler.mDatabase.getDocument("mission_de20ef854f96c00fe46089d16f0554be");
        expectedMissions.add(new Mission(doc));
        Assertions.assertTrue(missions.equals(expectedMissions));
    }
}
