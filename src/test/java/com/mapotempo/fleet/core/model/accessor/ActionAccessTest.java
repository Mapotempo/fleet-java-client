package com.mapotempo.fleet.core.model.accessor;

import com.mapotempo.fleet.api.model.MissionStatusActionInterface;
import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;
import com.mapotempo.fleet.core.BaseTest;
import com.mapotempo.fleet.core.DatabaseFeeder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * MissionAccessTest.
 */
public class ActionAccessTest extends BaseTest {

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
    void testLocation() throws Exception {
        MissionStatusTypeInterface missionStatusType = mMissionStatusTypeAccess.get("status_completed:2ba5b8eadce2f0035adzadaz456456daz");
        List<MissionStatusActionInterface> missionStatusActions = mMissionStatusActionAccess.getByPrevious(missionStatusType);
        // TODO
        for (MissionStatusActionInterface action : missionStatusActions) {
            System.out.println(action.getGroup());
            System.out.println(action.getLabel());
        }
        missionStatusType = mMissionStatusTypeAccess.get("status_pending:2ba5b8ea56d2dazadzadadza645dzadaz");
        missionStatusActions = mMissionStatusActionAccess.getByPrevious(missionStatusType);
        for (MissionStatusActionInterface action : missionStatusActions) {
            System.out.println(action.getGroup());
            System.out.println(action.getLabel());
        }
    }
}
