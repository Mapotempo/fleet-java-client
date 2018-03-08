package com.mapotempo.fleet.core.model.accessor;

import com.mapotempo.fleet.api.model.MissionActionInterface;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.core.BaseTest;
import com.mapotempo.fleet.core.DatabaseFeeder;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * MissionAccessTest.
 */
public class MissionActionTypeAccessTest extends BaseTest {

    @BeforeAll
    static void BeforeAll() throws Exception {
        iniDatabase(DatabaseFeeder.Dataset.DATASET_1);
    }

    @AfterAll
    static void AfterAll() throws Exception {
        removeDatabase();
    }

    @Test
    @DisplayName("Mission access correct order")
    void testMissionStatusAccess() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_b89d5ag45h66c00fd4a56zda5z354be");
        List<MissionActionInterface> missionStatuses = mMissionActionAccess.getByMission(mission);
        Assertions.assertEquals(1, missionStatuses.size());
        mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        missionStatuses = mMissionActionAccess.getByMission(mission);
        Assertions.assertEquals(2, missionStatuses.size());
    }
}
