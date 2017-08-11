package com.mapotempo.fleet.core.base;

import com.couchbase.lite.*;
import com.mapotempo.fleet.core.accessor.Access;

import java.util.*;

/**
 * MapotempoModelBase.
 */
abstract public class MapotempoModelBase {

    MapotempoModelBase INSTANCE = this;

    private UnsavedRevision updateDocument;

    protected Document mDocument;

    protected Database mDatabase;

    private Document.ChangeListener mDocumentChangeListener = new Document.ChangeListener() {
        @Override
        public void changed(Document.ChangeEvent event) {
            for(ChangeListener changeListener : mChangeListenerList) {
                changeListener.changed(INSTANCE);
            }
        }
    };

    public interface ChangeListener<T extends MapotempoModelBase> {
        void changed(T item);
    }

    private List<ChangeListener> mChangeListenerList = new ArrayList<>();

    public MapotempoModelBase(Database database) {
        mDatabase = database;
        mDocument = mDatabase.getDocument(UUID.randomUUID().toString());
        updateDocument = mDocument.createRevision();
        // Listener never is never remove, it will be remove with Document deallocation
        mDocument.addChangeListener(mDocumentChangeListener);
    }

    public MapotempoModelBase(Document doc) {
        // Check Document type
        mDocument = doc;
        mDatabase = mDocument.getDatabase();
        updateDocument = mDocument.createRevision();
        // Listener never is never remove, it will be remove with Document deallocation
        mDocument.addChangeListener(mDocumentChangeListener);
    }

    public void addChangeListener(ChangeListener changeListener) {
        mChangeListenerList.add(changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        mChangeListenerList.remove(changeListener);
    }

    public String getId() {
        return mDocument.getId();
    }

    public String getRef() {
        return mDocument.getCurrentRevision().getId();
    }

    public boolean save() {
        try {
            updateDocument.save();
            // Create the new futur revision
            updateDocument = mDocument.createRevision();
            return true;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void setProperty(String key, Object value) {
        Map mapMerge = new HashMap();
        Map properties= updateDocument.getProperties();
        mapMerge.putAll(properties);
        mapMerge.put(key, value);
        updateDocument.setProperties(mapMerge);
    }

    protected Object getProperty(String key, Object def) {
        Object data= mDocument.getProperty(key);
        if(data == null) {
            System.err.println("WARNING : in " + getClass().getName() + " The key " + key + " is absent from document " + mDocument.getId());
            data = def;
        }
        return data;
    }
}
