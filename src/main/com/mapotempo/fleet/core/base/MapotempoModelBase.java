package com.mapotempo.fleet.core.base;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.UnsavedRevision;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * MapotempoModelBase.
 */
abstract public class MapotempoModelBase {

    private UnsavedRevision updateDocument;

    protected Document mDocument;

    protected Database mDatabase;

    public MapotempoModelBase(Database database) {
        mDatabase = database;
        mDocument = mDatabase.getDocument(UUID.randomUUID().toString());
    }

    public MapotempoModelBase(Document doc) {
        // Check Document type
        mDocument = doc;
        mDatabase = mDocument.getDatabase();
        updateDocument = mDocument.createRevision();
    }

    public String getId() {
        return mDocument.getId();
    }

    public String getRef() {
        return mDocument.getCurrentRevision().getId();
    }

    protected Object getProperty(String key, Object def) {
        Object data= mDocument.getProperty(key);
        if(data == null) {
            System.err.println("The key " + key + "is absent from document " + mDocument.getId());
            data = def;
        }
        return data;
    }

    protected boolean setProperty(String key, Object value) {

        try {
            Map mapMerge = new HashMap();
            Map properties= mDocument.getProperties();
            mapMerge.putAll(properties);
            mapMerge.put(key, value);
            mDocument.putProperties(mapMerge);
            return true;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Maybe propage an exception
    public boolean save() {
        try {
            updateDocument.save();
            return true;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return false;
        }
    }
}
