package com.mapotempo.fleet.core;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.JavaContext;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.accessor.MissionAccess;
import com.mapotempo.fleet.core.model.accessor.MissionStatusActionAccess;
import com.mapotempo.fleet.core.model.accessor.MissionStatusTypeAccess;

import java.io.IOException;

public abstract class BaseTest {
    protected static DatabaseHandler mDatabaseHandler;

    protected static MissionAccess mMissionAccess;

    protected static MissionStatusTypeAccess mMissionStatusTypeAccess;

    protected static MissionStatusActionAccess mMissionStatusActionAccess;

    protected static DatabaseFeeder.Dataset mDataset = DatabaseFeeder.Dataset.DATASET_1;

    protected static void iniDatabase(DatabaseFeeder.Dataset dataset) throws CouchbaseLiteException, IOException, CoreException {
        System.out.println("Create the data base");
        mDatabaseHandler = new DatabaseHandler("default_abcde", "default_abcde", new JavaContext(), new DatabaseHandler.OnCatchLoginError() {
            @Override
            public void CatchLoginError() {
            }
        });
        System.out.println(" - Database successfull created");
        mMissionAccess = new MissionAccess(mDatabaseHandler);
        System.out.println(" - MissionAccess successfull created");
        mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);
        System.out.println(" - MissionStatusTypeAccess successfull created");
        mMissionStatusActionAccess = new MissionStatusActionAccess(mDatabaseHandler);
        System.out.println(" - MissionStatusActionAccess successfull created");
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