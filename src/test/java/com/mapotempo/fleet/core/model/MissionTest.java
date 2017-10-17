package com.mapotempo.fleet.core.model;

import com.mapotempo.fleet.api.model.MapotempoModelBaseInterface;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.submodel.AddressInterface;
import com.mapotempo.fleet.api.model.submodel.LocationInterface;
import com.mapotempo.fleet.api.model.submodel.TimeWindowsInterface;
import com.mapotempo.fleet.core.BaseNestedTest;
import com.mapotempo.fleet.core.DatabaseFeeder;
import com.mapotempo.fleet.core.model.submodel.Address;
import com.mapotempo.fleet.core.model.submodel.Location;
import com.mapotempo.fleet.core.model.submodel.TimeWindow;
import org.junit.jupiter.api.*;

import java.util.*;

/**
 * MissionTest.
 */
@DisplayName("Mission")
class MissionTest {
    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Get Test")
    class GetTest extends BaseNestedTest {
        @Override
        public DatabaseFeeder.Dataset getDataset() {
            return DatabaseFeeder.Dataset.DATASET_1;
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
            String expectedSyncUser = "user_login";
            String syncUser = mission.getSyncUser();
            Assertions.assertTrue(syncUser.equals(expectedSyncUser));
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
        @Disabled
        void testGetStatus() throws Exception {
            /*MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
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
            Assertions.assertTrue(res);*/
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

        @Test
        @DisplayName("get custom data")
        void testGetCustomData() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Map<String, String> customData = mission.getCustomData();
            Map<String, String> expectedCustomData = new HashMap<>();
            expectedCustomData.put("test", "test");
            Assertions.assertTrue(customData.equals(expectedCustomData));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Set Test")
    class SetTest extends BaseNestedTest {
        @Override
        public DatabaseFeeder.Dataset getDataset() {
            return DatabaseFeeder.Dataset.DATASET_1;
        }

        @Test
        @DisplayName("name modify after save")
        void testSaveName() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            String new_name = "new_name";
            mission.setName(new_name);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getName().equals(new_name));
        }

        @Test
        @DisplayName("can't modify date modify save")
        void testUnsaveName() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            String new_name = "test_name";
            mission.setName(new_name);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getName().equals(new_name));
        }

        @Test
        @DisplayName("date modify after save")
        void testSaveDate() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Date date = new Date();
            mission.setDate(date);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getDate().equals(date));
        }

        @Test
        @DisplayName("can't modify date without save")
        void testUnsaveDate() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Date date = new Date();
            mission.setDate(date);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getDate().equals(date));
        }

        @Test
        @DisplayName("address modified after save")
        void testSaveAddress() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Address address = new Address("abcd", "78000", "Test", "Gironde", "France", "Pépinière éco-créative", mDatabaseHandler.mDatabase);
            mission.setAddress(address);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getAddress().equals(address));
        }

        @Test
        @DisplayName("can't modify address without save")
        void testUnsaveAddress() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Date date = new Date();
            mission.setDate(date);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getDate().equals(date));
        }

        @Test
        @DisplayName("comment modified after save")
        void testSaveComment() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            String comment = "abcdef";
            mission.setComment(comment);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getComment().equals(comment));
        }

        @Test
        @DisplayName("can't modify comment without save")
        void testUnsaveComment() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            String comment = "abcdef";
            mission.setComment(comment);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getComment().equals(comment));
        }

        @Test
        @DisplayName("location modified after save")
        void testSaveLocation() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Location location = new Location(5., 10., mDatabaseHandler.mDatabase);
            mission.setLocation(location);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getLocation().equals(location));
        }

        @Test
        @DisplayName("can't modify location without save")
        void testUnsaveLocation() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Location location = new Location(15., 28., mDatabaseHandler.mDatabase);
            mission.setLocation(location);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getLocation().equals(location));
        }

        @Test
        @DisplayName("phone modified after save")
        void testSavePhone() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            String phone = "0553565251";
            mission.setPhone(phone);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getPhone().equals(phone));
        }

        @Test
        @DisplayName("can't modify phone without save")
        void testUnsavePhone() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            String phone = "0553565251";
            mission.setPhone(phone);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getPhone().equals(phone));
        }

        @Test
        @DisplayName("reference modified after save")
        void testSaveReference() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            String reference = "gdzalpazjfpoaz";
            mission.setReference(reference);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getReference().equals(reference));
        }

        @Test
        @DisplayName("can't modify reference without save")
        void testUnsaveReference() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            String reference = "dzadazdaz";
            mission.setReference(reference);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getReference().equals(reference));
        }

        @Test
        @DisplayName("duration modified after save")
        void testSaveDuration() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            int duration = 12;
            mission.setDuration(duration);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getDuration() == duration);
        }

        @Test
        @DisplayName("can't modify duration without save")
        void testUnsaveDuration() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            int duration = 15;
            mission.setDuration(duration);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getDuration() == duration);
        }

        @Test
        @DisplayName("custom data modified after save")
        void testSaveMissionStatus() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            MissionStatusType mst = mMissionStatusTypeAccess.get("status_pending:2ba5b8ea56d2dazadzadadza645dzadaz");
            mission.setStatus(mst);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getStatus().getId().equals(mst.getId()));
        }

        @Test
        @DisplayName("can't modify custom data without save")
        void testUnsaveMissionStatus() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            MissionStatusType mst = mMissionStatusTypeAccess.get("status_uncompleted:2ba5b8ed756az4d3azd4132az876j455d");
            mission.setStatus(mst);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getStatus().getId().equals(mst.getId()));
        }

        @Test
        @DisplayName("time_windows modified after save")
        void testSaveTimeWindows() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            List<TimeWindowsInterface> timeWindows = new ArrayList<>();
            timeWindows.add(new TimeWindow(new Date(1503468000000L), new Date(1503482400000L), mDatabaseHandler.mDatabase));
            timeWindows.add(new TimeWindow(new Date(1503486000000L), new Date(1503500400000L), mDatabaseHandler.mDatabase));
            mission.setTimeWindow(timeWindows);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getTimeWindow().equals(timeWindows));
        }

        @Test
        @DisplayName("can't modify time_windows without save")
        void testUnsaveTimeWindows() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            List<TimeWindowsInterface> timeWindows = new ArrayList<>();
            timeWindows.add(new TimeWindow(new Date(1503468004586L), new Date(15034824005896L), mDatabaseHandler.mDatabase));
            timeWindows.add(new TimeWindow(new Date(1503486005969L), new Date(15035004009789L), mDatabaseHandler.mDatabase));
            mission.setTimeWindow(timeWindows);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getTimeWindow().equals(timeWindows));
        }

        @Test
        @DisplayName("custom data modified after save")
        void testSaveCustomData() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Map<String, String> customData = new HashMap<>();
            customData.put("supertest", "test");
            mission.setCustomData(customData);
            mission.save();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission.getCustomData().equals(customData));
        }

        @Test
        @DisplayName("can't modify custom data without save")
        void testUnsaveCustomData() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Map<String, String> customData = new HashMap<>();
            customData.put("machin", "bidule");
            mission.setCustomData(customData);
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertFalse(mission.getCustomData().equals(customData));
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Callback Test")
    class CallbackTest extends BaseNestedTest {
        @Override
        public DatabaseFeeder.Dataset getDataset() {
            return DatabaseFeeder.Dataset.DATASET_1;
        }

        boolean check = false;

        @Test
        @DisplayName("callback addChangeListener test")
        void testCallack() throws Exception {
            check = false;
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            mission.addChangeListener(new MapotempoModelBaseInterface.ChangeListener() {
                @Override
                public void changed(MapotempoModelBaseInterface item, boolean onDeletion) {
                    check = true;
                }
            });
            mission.setName("toto");
            mission.save();
            Thread.sleep(20);
            Assertions.assertTrue(check);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Create, Delete Test")
    class CreateDeleteTest extends BaseNestedTest {
        @Override
        public DatabaseFeeder.Dataset getDataset() {
            return DatabaseFeeder.Dataset.DATASET_1;
        }

        @Test
        @DisplayName("create test")
        void testCreate() throws Exception {
            MissionInterface mission = mMissionAccess.getNew();
            mission.setName("toto");
            mission.setPhone("0602023565");
            mission.save();
            mission = mMissionAccess.get(mission.getId());
            Assertions.assertTrue(mission != null);
        }

        @Test
        @DisplayName("delete test")
        void testDelete() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            mission.delete();
            mission = mMissionAccess.get("mission_de20ef854f96c00fe46089d16f0554be");
            Assertions.assertTrue(mission == null);
        }
    }

    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @DisplayName("Uncompleted couchbase document Test")
    class UncompletedDocumentTest extends BaseNestedTest {
        @Override
        public DatabaseFeeder.Dataset getDataset() {
            return DatabaseFeeder.Dataset.DATASET_2;
        }

        @Test
        @DisplayName("missing type name")
        void testMissingName() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            Assertions.assertTrue(mission.getName().equals("Unknow"));
        }

        @Test
        @DisplayName("missing type time")
        void testMissingTime() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            Assertions.assertTrue(mission.getDate().getTime() == 0);
        }

        @Test
        @DisplayName("missing type time windows")
        void testMissingTimeWindows() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            //Assertions.assertTrue(mission.getStatus();
            Assertions.assertTrue(mission.getTimeWindow().equals(new ArrayList<>()));
        }

        @Test
        @DisplayName("missing type location")
        void testMissingLocation() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            Assertions.assertTrue(mission.getLocation().equals(new Location(0., 0., mDatabaseHandler.mDatabase)));
        }

        @Test
        @DisplayName("missing type address")
        void testMissingAddress() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            System.out.println(mission.getAddress().getStreet());
            Assertions.assertTrue(mission.getAddress().equals(new Address("Unknow", "Unknow", "Unknow", "Unknow", "Unknow", "", mDatabaseHandler.mDatabase)));
        }

        @Test
        @DisplayName("missing type owners")
        void testMissingOwners() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            Assertions.assertTrue(mission.getSyncUser().equals(""));
        }

        @Test
        @DisplayName("missing type custom data")
        void testMissingCustomData() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            Assertions.assertTrue(mission.getCustomData().equals(new HashMap<String, String>()));
        }

        @Test
        @DisplayName("missing type company id")
        void testMissingCustomId() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            Assertions.assertTrue(mission.getCompanyId().equals("No company id"));
        }

        @Test
        @DisplayName("missing type duration")
        void testMissingDuration() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            Assertions.assertTrue(mission.getDuration() == 0);
        }

        @Test
        @DisplayName("missing type reference")
        void testMissingReference() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            Assertions.assertTrue(mission.getReference().equals(""));
        }

        @Test
        @DisplayName("missing type phone")
        void testMissingPhone() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            Assertions.assertTrue(mission.getPhone().equals(""));
        }

        @Test
        @DisplayName("missing type comment")
        void testMissingComment() throws Exception {
            MissionInterface mission = mMissionAccess.get("mission_de20ef8dazdazdazdazd45d6az74d68az746f0554be");
            Assertions.assertTrue(mission.getComment().equals(""));
        }
    }
}