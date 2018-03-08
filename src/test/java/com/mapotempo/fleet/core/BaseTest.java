package com.mapotempo.fleet.core;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.JavaContext;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.accessor.MissionAccess;
import com.mapotempo.fleet.core.model.accessor.MissionActionAccess;
import com.mapotempo.fleet.core.model.accessor.MissionActionTypeAccess;
import com.mapotempo.fleet.core.model.accessor.MissionStatusTypeAccess;

import java.io.IOException;

public abstract class BaseTest {
    protected static DatabaseHandler mDatabaseHandler;

    protected static MissionAccess mMissionAccess;

    protected static MissionActionAccess mMissionActionAccess;

    protected static MissionStatusTypeAccess mMissionStatusTypeAccess;

    protected static MissionActionTypeAccess mMissionActionTypeAccess;

    protected static DatabaseFeeder.Dataset mDataset = DatabaseFeeder.Dataset.DATASET_1;

    protected static void iniDatabase(DatabaseFeeder.Dataset dataset) throws CouchbaseLiteException, IOException, CoreException {
        System.out.println("Create the data base");
        mDatabaseHandler = new DatabaseHandler("default_abcde", "default_abcde", new JavaContext(), "localhost", new DatabaseHandler.OnCatchLoginError() {
            @Override
            public void CatchLoginError() {
            }
        });
        System.out.println(" - Database successfull created");
        mMissionAccess = new MissionAccess(mDatabaseHandler);
        System.out.println(" - MissionAccess successfull created");
        mMissionActionAccess = new MissionActionAccess(mDatabaseHandler);
        System.out.println(" - MissionActionAccess successfull created");
        mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);
        System.out.println(" - MissionStatusTypeAccess successfull created");
        mMissionActionTypeAccess = new MissionActionTypeAccess(mDatabaseHandler);
        System.out.println(" - MissionActionTypeAccess successfull created");
        DatabaseFeeder.Feed(mDatabaseHandler.mDatabase, mDataset);
        System.out.println(" - Database successfull feeded");
    }

    protected static void removeDatabase() throws CouchbaseLiteException, InterruptedException {
        // Wait one second for async task
        Thread.sleep(1000);
        System.out.println("Remove the database");
        mDatabaseHandler.mDatabase.delete();
        System.out.println(" - Database successfull removed");
    }
}
