package com.mapotempo.fleet.core.model.accessor;

import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.MissionStatusInterface;
import com.mapotempo.fleet.core.BaseTest;
import com.mapotempo.fleet.core.DatabaseFeeder;
import org.junit.jupiter.api.*;

import java.util.List;

/**
 * MissionAccessTest.
 */
public class MissionStatusAccessTest extends BaseTest {

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
    void testMissionStatusAccess() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        List<MissionStatusInterface> missionStatuses = mMissionStatusAccess.getByMission(mission);
        Assertions.assertEquals(missionStatuses.size(), 2);
        mission = mMissionAccess.get("mission_b89d5ag45h66c00fd4a56zda5z354be");
        missionStatuses = mMissionStatusAccess.getByMission(mission);
        Assertions.assertEquals(missionStatuses.size(), 1);
    }
}
