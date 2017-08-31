package com.mapotempo.fleet.core;

import com.couchbase.lite.*;
import com.couchbase.lite.auth.Authenticator;
import com.couchbase.lite.auth.AuthenticatorFactory;
import com.couchbase.lite.replicator.RemoteRequestResponseException;
import com.couchbase.lite.replicator.Replication;
import com.couchbase.lite.support.FileDirUtils;
import com.mapotempo.fleet.core.exception.CoreException;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * DatabaseHandler.
 */
public class DatabaseHandler {

    private boolean mReleaseStatus = false;

    private boolean mConnexionStatus = true;

    private Context mContext;

    private Manager mManager;

    public Database mDatabase;

    private String mDbname = "mydbname";

    private URL url = null;

    private String mUser;

    private String mPassword;

    private Replication mPusher, mPuller;

    public interface OnCatchLoginError{
        void CatchLoginError();
    }

    private OnCatchLoginError mOnCatchLoginError;

    public DatabaseHandler(String user, String password, Context context, OnCatchLoginError onCatchLoginError) throws CoreException {
        mOnCatchLoginError = onCatchLoginError;

        Handler handler = new StreamHandler(System.out, new SimpleFormatter());
        this.mPassword = password;

        this.mUser = user;
        this.mContext = context;
        try {
            this.mManager = new Manager(mContext, Manager.DEFAULT_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
            // TODO
            throw new CoreException("TODO");
        }
        mDbname = mUser + "_database";

        try {
            DatabaseOptions passwordDatabaseOption = new DatabaseOptions();
            passwordDatabaseOption.setEncryptionKey(mPassword);
            passwordDatabaseOption.setCreate(true);
            this.mDatabase = mManager.openDatabase(mDbname.toLowerCase(), passwordDatabaseOption);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            // TODO
            throw new CoreException("TODO");
        }
    }

    final private String PASSWORD = "password";

    public void initConnexion(String syncGatewayUrl) throws CoreException
    {
        // CONNEXION
        try{
            url = new URL(syncGatewayUrl);
        } catch (MalformedURLException e){
            e.printStackTrace();
            // TODO
            throw new CoreException("TODO");
        }

        // TODO
        /*
        mDatabase.addChangeListener(new Database.ChangeListener() {
            @Override
            public void changed(Database.ChangeEvent event) {
                List<DocumentChange> changes = event.getChanges();
            }
        });*/

        // Pusher and Puller sync
        mPusher = mDatabase.createPushReplication(url);
        mPusher.setContinuous(true); // Runs forever in the background
        mPusher.addChangeListener(new Replication.ChangeListener() {
            @Override
            public void changed(Replication.ChangeEvent changeEvent) {
                System.out.println("pusher changed listener " + changeEvent.getStatus());
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
                if(changeEvent.getError() != null){
                    System.out.println(changeEvent.toString());
                    if(changeEvent.getError() instanceof RemoteRequestResponseException){
                        RemoteRequestResponseException ex = (RemoteRequestResponseException)changeEvent.getError();
                        System.out.println("HTTP Error: " + ex.getCode() + ": " + ex.getMessage());
                        System.out.println("            " + ex.getUserInfo());
                        System.out.println(" hash code  " + ex.hashCode());
                        if(new Integer(401).equals(ex.getCode())) {
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

        onlineStatus(mConnexionStatus);
    }

    public void onlineStatus(boolean status) {
        mConnexionStatus = status;
        if(status) {
            mPusher.goOnline();
            mPuller.goOnline();
        } else {
            mPusher.goOffline();
            mPuller.goOffline();
        }
    }

    public boolean isOnline() {
        return mConnexionStatus;
    }

    public void printAllData()
    {
        try {
            // Let's find the documents that have conflicts so we can resolve them:
            Query query = mDatabase.createAllDocumentsQuery();
            query.setAllDocsMode(Query.AllDocsMode.ALL_DOCS);
            List<Object> res = new ArrayList<Object>();
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                String docId = row.getDocumentId();
                Document doc = mDatabase.getDocument(docId );
                System.out.println(doc.getProperties().toString());
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return;
    }

    public void restartPuller() {
        mPuller.restart();
    }

    public void setUserChannel(String userName) throws CoreException {
        if(mPuller != null) {
            List<String> channels = mPuller.getChannels();
            channels.add("user:" + userName);
            mPuller.setChannels(channels);
        } else {
            throw new CoreException("Warning : Connexion need to be configure with setConnexionParam method before channel setting");
        }
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

    public void release(boolean delete_db) {
        if(mReleaseStatus)
            return;

        if(mPusher != null) {
            mPusher.stop();
            mPusher = null;
        }

        if(mPuller != null) {
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
        // ## cette tache à couchbase.
        mDatabase.runAsync(new AsyncTask() {
            @Override
            public void run(Database database) {
                mDatabase.close();
            }
        });

        if(delete_db) {
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
}
