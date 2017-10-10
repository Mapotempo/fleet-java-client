package com.mapotempo.fleet.core;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.JavaContext;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;
import com.mapotempo.fleet.api.model.submodel.AddressInterface;
import com.mapotempo.fleet.api.model.submodel.LocationInterface;
import com.mapotempo.fleet.api.model.submodel.MissionCommandInterface;
import com.mapotempo.fleet.api.model.submodel.TimeWindowsInterface;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.accessor.MissionAccess;
import com.mapotempo.fleet.core.model.accessor.MissionStatusTypeAccess;
import com.mapotempo.fleet.core.model.submodel.Address;
import com.mapotempo.fleet.core.model.submodel.Location;
import com.mapotempo.fleet.core.model.submodel.MissionCommand;
import com.mapotempo.fleet.core.model.submodel.TimeWindow;
import json.DatabaseFeeder;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MissionTest.
 */
class MissionTest {

    private static DatabaseHandler mDatabaseHandler;

    private static MissionAccess mMissionAccess;

    private static MissionStatusTypeAccess mMissionStatusTypeAccess;

    @BeforeAll
    static void BeforeAll() throws CoreException, CouchbaseLiteException, IOException {
        System.out.println("Create the data base");
        mDatabaseHandler = new DatabaseHandler("default_abcde", "default_abcde", new JavaContext(), new DatabaseHandler.OnCatchLoginError() {
            @Override
            public void CatchLoginError() {
            }
        });
        System.out.println(" - Database successfull created");
        System.out.println("Create MissionAccess");
        mMissionAccess = new MissionAccess(mDatabaseHandler);
        System.out.println(" - MissionAccess successfull created");
        System.out.println("Create MissionStatusTypeAccess");
        mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);
        System.out.println(" - MissionStatusTypeAccess successfull created");
        System.out.println("Feed the database");
        DatabaseFeeder.Feed(mDatabaseHandler.mDatabase);
        System.out.println(" - Database successfull feeded");
    }

    @AfterAll
    static void AfterAll() throws CouchbaseLiteException, IOException {
        //  DatabaseFeeder.Clear(mDatabaseHandler.mDatabase);
        mDatabaseHandler.mDatabase.delete();
    }

    @BeforeEach
    void BeforeEach() throws CouchbaseLiteException, IOException {
    }

    @AfterEach
    void AfterEach() throws CouchbaseLiteException, IOException {
    }

    @Test
    @DisplayName("get mission company_id")
    void testGetCompanyId() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        Assertions.assertTrue(mission.getCompanyId().equals("company_dazd456dza456daz234d4a6daz"));
    }

    @Test
    @DisplayName("get mission name")
    void testGetName() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        Assertions.assertTrue(mission.getName().equals("Mission-48"));
    }

    @Test
    @DisplayName("get mission date")
    void testGetDate() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        Assertions.assertTrue(mission.getDate().getTime() == 1503506636150L);
    }

    @Test
    @DisplayName("get owners")
    void testGetOwners() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        List<String> expectedOwners = new ArrayList<>();
        expectedOwners.add("user_login");
        ArrayList<String> owners = mission.getOwners();
        Assertions.assertTrue(owners.equals(expectedOwners));
    }

    @Test
    @DisplayName("get address")
    void testGetAddress() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        AddressInterface expectedAddress = new Address("9 Rue André Darbon", "33000", "Bordeaux", "Gironde", "France", "Pépinière éco-créative", mDatabaseHandler.mDatabase);
        AddressInterface address = mission.getAddress();
        Assertions.assertTrue(address.equals(expectedAddress));
    }

    @Test
    @DisplayName("get comment")
    void testGetComment() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        Assertions.assertTrue(mission.getComment().equals("blablabla."));
    }

    @Test
    @DisplayName("get location")
    void testGetLocation() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        LocationInterface expectedLocation = new Location(-0.5680988, 44.8547927, mDatabaseHandler.mDatabase);
        LocationInterface location = mission.getLocation();
        Assertions.assertTrue(location.equals(expectedLocation));
    }

    @Test
    @DisplayName("get phone")
    void testGetPhone() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        Assertions.assertTrue(mission.getPhone().equals("0600000001"));
    }

    @Test
    @DisplayName("get reference")
    void testGetReference() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        Assertions.assertTrue(mission.getReference().equals("ABCDEF"));
    }

    @Test
    @DisplayName("get duration")
    void testGetDuration() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        Assertions.assertTrue(mission.getDuration() == 240);
    }

    @Test
    @DisplayName("get status")
    void testGetStatus() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        Boolean res = true;
        MissionStatusTypeInterface mst = mission.getStatus();
        if (!mst.getColor().equals("009900"))
            res = false;
        List<MissionCommandInterface> commands = new ArrayList<>();
        commands.add(new MissionCommand("To Pending", mMissionStatusTypeAccess.get("status_pending:2ba5b8ea56d2dazadzadadza645dzadaz"), "default", mDatabaseHandler.mDatabase));
        commands.add(new MissionCommand("To Uncompleted", mMissionStatusTypeAccess.get("status_uncompleted:2ba5b8ed756az4d3azd4132az876j455d"), "default", mDatabaseHandler.mDatabase));
        if (!mst.getCommands().equals(commands))
            res = false;
        if (!mst.getLabel().equals("Completed"))
            res = false;
        Assertions.assertTrue(res);
    }

    @Test
    @DisplayName("get time windows")
    void testGetTimeWindows() throws Exception {
        MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
        List<TimeWindowsInterface> timeWindows = mission.getTimeWindow();
        List<TimeWindowsInterface> expectedTimeWindows = new ArrayList<>();
        expectedTimeWindows.add(new TimeWindow(new Date(1503468000000L), new Date(1503482400000L), mDatabaseHandler.mDatabase));
        expectedTimeWindows.add(new TimeWindow(new Date(1503486000000L), new Date(1503500400000L), mDatabaseHandler.mDatabase));
        Assertions.assertTrue(timeWindows.equals(expectedTimeWindows));
    }
}
