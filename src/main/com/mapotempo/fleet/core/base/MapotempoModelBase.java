/*
 * Copyright © Mapotempo, 2017
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

package com.mapotempo.fleet.core.base;

import com.couchbase.lite.*;
import com.mapotempo.fleet.core.exception.CoreException;

import java.util.*;

/**
 * MapotempoModelBase.
 */
abstract public class MapotempoModelBase {

    protected boolean readOnly = false;

    private MapotempoModelBase INSTANCE = this;

    private UnsavedRevision updateDocument;
    private HashMap<String, Boolean> readInUpdateDocument = new HashMap<>();

    protected Document mDocument;

    protected Database mDatabase;


    // TODO
    private Document.ChangeListener mConflictSolver = new Document.ChangeListener() {
        @Override
        public void changed(Document.ChangeEvent event) {
            System.out.println("isConflict           :" + event.getChange().isConflict());
            System.out.println("getRevisionId        :" + event.getChange().getRevisionId());
            System.out.println("isDeletion           :" + event.getChange().isDeletion());
            System.out.println("isCurrentRevision    :" + event.getChange().isCurrentRevision());
            System.out.println("getWinningRevisionID :" + event.getChange().getWinningRevisionID());
            System.out.println("getAddedRevision     :" + event.getChange().getAddedRevision());
            System.out.println("toString             :" + event.getChange().toString());
            System.out.println("-----------------------------------------------------------------------");
        }
    };

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
        DocumentBase documentAnnotation = getClass().getAnnotation(DocumentBase.class);

        mDatabase = database;
        mDocument = mDatabase.getDocument(documentAnnotation.type() + "_" + UUID.randomUUID().toString());
        updateDocument = mDocument.createRevision();
        Map map = new HashMap();
        map.put(documentAnnotation.type_field(), documentAnnotation.type());
        updateDocument.setProperties(map);
    }

    public MapotempoModelBase(Document doc) {
        mDocument = doc;
        mDatabase = mDocument.getDatabase();
        updateDocument = mDocument.createRevision();
    }

    // USE THIS WITH CAUTION FOR THE MOMENT !
    public MapotempoModelBase(String id, Database database) throws CoreException {
        mDatabase = database;
        // getExistingDocument return null value if document isn't found in database,
        // maybe i could use getDocument, i don't know for the moment, sorry =/ !
        mDocument = mDatabase.getExistingDocument(id);

        // throw an exception for the moment !
        if(mDocument == null) {
            throw new CoreException(getClass().getName() + " : document id " + id + " not found in database " + mDatabase.getName());
        }
        updateDocument = mDocument.createRevision();
    }

    public boolean delete() {
        try {
            return mDocument.delete();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addChangeListener(ChangeListener changeListener) {
        if(mChangeListenerList.size() == 0)
            mDocument.addChangeListener(mDocumentChangeListener);
        mChangeListenerList.add(changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        if(mChangeListenerList.size() == 0)
            mDocument.removeChangeListener(mDocumentChangeListener);
        mChangeListenerList.remove(changeListener);
    }

    public String getId() {
        return mDocument.getId();
    }

    public String getRef() {
        return mDocument.getCurrentRevision().getId();
    }

    public boolean save() {
        if(!readOnly) {
            try {
                updateDocument.save();
                // Create the new futur revision
                updateDocument = mDocument.createRevision();
                readInUpdateDocument.clear();
                return true;
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            DocumentBase documentAnnotation = getClass().getAnnotation(DocumentBase.class);
            System.out.println("warning: property of " + documentAnnotation.type() + " " + mDocument.getId() +
                    " can't be set, model is defined as read only");
            return false;
        }
    }

    protected void setProperty(String key, Object value) {
        if(!readOnly) {
            Map mapMerge = new HashMap();
            Map properties= updateDocument.getProperties();
            mapMerge.putAll(properties);
            mapMerge.put(key, value);
            updateDocument.setProperties(mapMerge);
            readInUpdateDocument.put(key, true);
        } else {
            DocumentBase documentAnnotation = getClass().getAnnotation(DocumentBase.class);
            System.out.println("warning: property of " + documentAnnotation.type() + " " + mDocument.getId() +
                    " can't be set, model is defined as read only");
        }
    }

    protected Object getProperty(String key, Object def) {
        Object data;
        if(readInUpdateDocument.get(key) == null)
            data= mDocument.getProperty(key);
        else
            data= updateDocument.getProperty(key);

        if(data == null) {
            System.err.println("WARNING : in " + getClass().getName() + " The key " + key + " is absent from document " + mDocument.getId());
            data = def;
        }
        return data;
    }
}
