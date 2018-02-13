package com.mapotempo.fleet.core;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.JavaContext;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.accessor.MissionAccess;
import com.mapotempo.fleet.core.model.accessor.MissionStatusTypeAccess;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.io.IOException;

public abstract class BaseNestedTest {
    protected DatabaseHandler mDatabaseHandler;

    protected MissionAccess mMissionAccess;

    protected MissionStatusTypeAccess mMissionStatusTypeAccess;

    protected abstract DatabaseFeeder.Dataset getDataset();

    @BeforeAll
    void BeforeAll() throws CoreException, CouchbaseLiteException, IOException {
        System.out.println("Create the data base");
        mDatabaseHandler = new DatabaseHandler("default_abcde", "default_abcde", new JavaContext(), "localhost", new DatabaseHandler.OnCatchLoginError() {
            @Override
            public void CatchLoginError() {
            }
        });
        System.out.println(" - Database successfull created");
        mMissionAccess = new MissionAccess(mDatabaseHandler);
        System.out.println(" - MissionAccess successfull created");
        mMissionStatusTypeAccess = new MissionStatusTypeAccess(mDatabaseHandler);
        System.out.println(" - MissionStatusTypeAccess successfull created");
        DatabaseFeeder.Feed(mDatabaseHandler.mDatabase, getDataset());
        System.out.println(" - Database successfull feeded");
    }

    @AfterAll
    void AfterAll() throws CouchbaseLiteException, IOException, InterruptedException {
        // Wait one second for async task
        Thread.sleep(1000);
        System.out.println("Remove the database");
        mDatabaseHandler.mDatabase.delete();
        System.out.println(" - Database successfull removed");
    }
}
