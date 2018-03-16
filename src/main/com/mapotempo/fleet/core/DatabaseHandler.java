/*
 * Copyright © Mapotempo, 2018
 *
 * This file is part of Mapotempo.
 *
 * Mapotempo is free software. You can redistribute it and/or
 * modify since you respect the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 *
 * Mapotempo is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the Licenses for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Mapotempo. If not, see:
 * <http://www.gnu.org/licenses/agpl.html>
 */

package com.mapotempo.fleet.core;

import com.couchbase.lite.AsyncTask;
import com.couchbase.lite.Attachment;
import com.couchbase.lite.Context;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseOptions;
import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.Manager;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.SavedRevision;
import com.couchbase.lite.TransactionalTask;
import com.couchbase.lite.UnsavedRevision;
import com.couchbase.lite.auth.Authenticator;
import com.couchbase.lite.auth.AuthenticatorFactory;
import com.couchbase.lite.replicator.RemoteRequestResponseException;
import com.couchbase.lite.replicator.Replication;
import com.couchbase.lite.support.FileDirUtils;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.utils.HashHelper;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * DatabaseHandler.
 */
public class DatabaseHandler {

    private boolean mReleaseStatus = false;

    private boolean mConnectionStatus = true;

    private Context mContext;

    private Manager mManager;

    public Database mDatabase;

    private String mDbname;

    private URL url = null;

    private String mUser;

    private String mPassword;

    private Replication mPusher, mPuller;

    private String mSyncGatewayUrl;

    public interface OnCatchLoginError {
        void CatchLoginError();
    }

    final private OnCatchLoginError mOnCatchLoginError;

    public DatabaseHandler(String user, String password, Context context, String syncGatewayUrl, OnCatchLoginError onCatchLoginError) throws CoreException {
        mContext = context;
        mOnCatchLoginError = onCatchLoginError;

        // FIXME
        mUser = user;
        mPassword = password;

        mSyncGatewayUrl = syncGatewayUrl;

        try {
            mManager = new Manager(mContext, Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CoreException("Error : Manager can't be created");
        }

        mDbname = databaseNameGenerator(mUser, mSyncGatewayUrl);

        try {
            DatabaseOptions passwordDatabaseOption = new DatabaseOptions();
            passwordDatabaseOption.setEncryptionKey(mPassword);
            passwordDatabaseOption.setCreate(true);
            mDatabase = mManager.openDatabase(mDbname.toLowerCase(), passwordDatabaseOption);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            throw new CoreException("Error : Can't open bdd");
        }

        initConnection();
    }

    // CONNECTION
    public void initConnection() throws CoreException {
        try {
            url = new URL(mSyncGatewayUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new CoreException("Error : Invalide url connection");
        }

        // Pusher and Puller sync
        mPusher = mDatabase.createPushReplication(url);
        mPusher.setContinuous(true); // Runs forever in the background
        mPusher.addChangeListener(new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent changeEvent) {
                System.out.println("pusher changed listener " + changeEvent.getStatus());
                Replication.ReplicationStatus a = changeEvent.getStatus();
                if (changeEvent.getError() != null) {
                    System.out.println("--------------------------------");
                    System.out.println("changeEvent.toString() >>>>>>>>> " + changeEvent.toString());
                    System.out.println("changeEvent.getError() >>>>>>>>> " + changeEvent.getError());

                    if (changeEvent.getError() instanceof RemoteRequestResponseException) {
                        RemoteRequestResponseException ex = (RemoteRequestResponseException) changeEvent.getError();
                        System.out.println("HTTP Error: " + ex.getCode() + ": " + ex.getMessage());
                        System.out.println("            " + ex.getUserInfo());
                        System.out.println(" hash code  " + ex.hashCode());
                        if (new Integer(403).equals(ex.getCode())) {
                            System.out.println("403 !!");
                            mOnCatchLoginError.CatchLoginError();
                        }
                    }
                    System.out.println("--------------------------------");
                }
            }
        });

        mPuller = mDatabase.createPullReplication(url);
        mPuller.setContinuous(true); // Runs forever in the background
        mPuller.addChangeListener(new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent changeEvent) {
                Replication.ReplicationStatus a = changeEvent.getStatus();
                System.out.println("puller changed listener " + changeEvent.getStatus());
                System.out.println("> *************");
                if (changeEvent.getError() != null) {
                    System.out.println(changeEvent.toString());
                    if (changeEvent.getError() instanceof RemoteRequestResponseException) {
                        RemoteRequestResponseException ex = (RemoteRequestResponseException) changeEvent.getError();
                        System.out.println("HTTP Error: " + ex.getCode() + ": " + ex.getMessage());
                        System.out.println("            " + ex.getUserInfo());
                        System.out.println(" hash code  " + ex.hashCode());
                        if (new Integer(401).equals(ex.getCode())) {
                            System.out.println("401 !!");
                            mOnCatchLoginError.CatchLoginError();
                        }
                    }
                }
                System.out.println("< *************");
            }
        });

        // USER AUTH
        Authenticator authenticator = AuthenticatorFactory.createBasicAuthenticator(mUser, mPassword);
        mPusher.setAuthenticator(authenticator);
        mPuller.setAuthenticator(authenticator);

        // Start synchronisation
        mPusher.start();
        mPuller.start();

        onlineStatus(mConnectionStatus);

        startConflictLiveQuery();

        setPublicChannel();

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        for (String c : mPuller.getChannels())
            System.out.println(c);
    }

    // FIXME replace goOnline/goOffline => start/stop
    public void onlineStatus(boolean status) {
        mConnectionStatus = status;
        if (status) {
            mPusher.start();
            mPuller.start();
        } else {
            mPusher.stop();
            mPuller.stop();
        }
    }

    public boolean isOnline() {
        return mConnectionStatus;
    }

    public void printAllData() {
        try {
            // Let's find the documents that have conflicts so we can resolve them:
            Query query = mDatabase.createAllDocumentsQuery();
            query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
            List<Object> res = new ArrayList<Object>();
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                String docId = row.getDocumentId();
                Document doc = mDatabase.getDocument(docId);
                System.out.println("----------------------------");
                System.out.println("id : " + doc.getId());
                for (SavedRevision savedRevision : doc.getConflictingRevisions())
                    System.out.println("    - conflict : " + savedRevision.getProperties().get("_rev"));
                System.out.println("    - " + doc.getProperties().toString());

            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return;
    }

    public void restartPuller() {
        mPuller.restart();
    }

    public void setPublicChannel() {
        List<String> channels = mPuller.getChannels();
        channels.add("!");
        mPuller.setChannels(channels);
    }

    public void setUserChannel() {
        List<String> channels = mPuller.getChannels();
        channels.add("user:" + mUser);
        mPuller.setChannels(channels);
    }

    public void setCompanyChannel(String companyId) {
        List<String> channels = mPuller.getChannels();
        channels.add("company:" + companyId);
        mPuller.setChannels(channels);
    }

    public void setMissionChannel(String userName, String date) {
        List<String> channels = mPuller.getChannels();
        channels.add("mission:" + userName + ":" + date);
        mPuller.setChannels(channels);
    }

    public void setMissionStatusTypeChannel(String company_id) {
        List<String> channels = mPuller.getChannels();
        channels.add("mission_status_type:" + company_id);
        mPuller.setChannels(channels);
    }

    public void setMissionActionTypeChannel(String company_id) {
        List<String> channels = mPuller.getChannels();
        channels.add("mission_action_type:" + company_id);
        mPuller.setChannels(channels);
    }

    public void release(boolean delete_db) {
        if (mReleaseStatus)
            return;

        if (mPusher != null) {
            mPusher.stop();
            mPusher = null;
        }

        if (mPuller != null) {
            mPuller.stop();
            mPuller = null;
        }

        // ###############################################################
        // ## FIXME Trick
        // ## Ce 'trick' permet d'effectuer la fermeture de la base
        // ## dans un autre thread. En effet la fermeture d'une base
        // ## qui a reçu un 401 sur l'un de ses replicator (puller, pusher)
        // ## prend un timeout de 60s à la fermeture de celle ci. Pour ne
        // ## pas bloquer le thread ui de l'utilisateur nous déléguons donc
        // ## cette tache à couchbase via la methode runAsync de Database.
        // ## (cette methode n'est pas censé servir sur android, voir la doc ...)
        mDatabase.runAsync(new AsyncTask() {
            @Override
            public void run(Database database) {
                mDatabase.close();
            }
        });

        if (delete_db) {
            File databaseDirectory = mManager.getDirectory();
            if (databaseDirectory != null) {
                File databaseFile = new File(databaseDirectory, mDbname + ".cblite2"); // Or ".cblite"...
                if (databaseFile.exists()) {
                    FileDirUtils.deleteRecursive(databaseFile);
                }
            }
        }
        // # FIXME Trick
        // ###############################################################

        mDatabase = null;

        mManager.close();
        mManager = null;
        mReleaseStatus = true;
    }

    private String databaseNameGenerator(String userName, String url) {
        try {
            // Database name must be unique for a username and specific url.
            return "database_" + userName + HashHelper.sha256(url).substring(0, 10);
        } catch (CoreException e) {
            throw new RuntimeException("Error during databaseNameGenerator");
        }
    }


    private void startConflictLiveQuery() {
        System.out.println(">>>>>>>>>>>>>>>>>> startConflictLiveQuery");

        LiveQuery conflictsLiveQuery = mDatabase.createAllDocumentsQuery().toLiveQuery();
        conflictsLiveQuery.setAllDocsMode(Query.AllDocsMode.ONLY_CONFLICTS);
        conflictsLiveQuery.addChangeListener(new LiveQuery.ChangeListener() {
            @Override
            public void changed(LiveQuery.ChangeEvent event) {
                resolveConflicts(event.getRows());
            }
        });
        conflictsLiveQuery.start();
    }

    private void resolveConflicts(QueryEnumerator rows) {
        for (QueryRow row : rows) {
            List<SavedRevision> revs = row.getConflictingRevisions();
            if (revs.size() > 1) {
                SavedRevision defaultWinning = revs.get(0);
                Map<String, Object> props = defaultWinning.getUserProperties();
                Attachment image = defaultWinning.getAttachment("image");
                resolveConflicts(revs, props, image);
            }
        }
    }

    private void resolveConflicts(final List<SavedRevision> revs, final Map<String, Object> desiredProps, final Attachment desiredImage) {
        mDatabase.runInTransaction(new TransactionalTask() {
            @Override
            public boolean run() {
                int i = 0;
                for (SavedRevision rev : revs) {
                    UnsavedRevision newRev = rev.createRevision(); // Create new revision
                    if (i == 0) { // That's the current/winning revision
                        newRev.setUserProperties(desiredProps);
                        if (desiredImage != null) {
                            try {
                                newRev.setAttachment("image", "image/jpg", desiredImage.getContent());
                            } catch (CouchbaseLiteException e) {
                                e.printStackTrace();
                            }
                        }
                    } else { // That's a conflicting revision, delete it
                        newRev.setIsDeletion(true);
                    }

                    try {
                        newRev.save(true); // Persist the new revision
                    } catch (CouchbaseLiteException e) {
                        e.printStackTrace();
                        return false;
                    }
                    i++;
                }
                return true;
            }
        });
    }
}
