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

package com.mapotempo.fleet.core.base.accessor;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.View;
import com.mapotempo.fleet.api.model.MapotempoModelBaseInterface;
import com.mapotempo.fleet.api.model.accessor.AccessInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.ModelBase;
import com.mapotempo.fleet.core.exception.CoreException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Access.
 */
public class Access<T extends ModelBase & MapotempoModelBaseInterface> {

    private DatabaseHandler mDatabaseHandler;

    private Class<T> mClazz;

    protected View mView;

    protected LiveQuery mLiveQuery;

    private DocumentBase mDocumentAnnotation;

    private Constructor<T> mConstructorFromDocument;

    private Constructor<T> mConstructorFromDatabase;

    protected List<AccessInterface.ChangeListener> mChangeListenerList;

    public Access(Class<T> clazz, DatabaseHandler dbHandler, final String sortField) throws CoreException {
        mChangeListenerList = new ArrayList<>();

        mClazz = clazz;

        // TODO ERROR MESSAGE !!
        try {
            mConstructorFromDocument = mClazz.getConstructor(Document.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new CoreException("In Class : " + mClazz.getName() + " wrong definition of constructor define.");
        }

        try {
            mConstructorFromDatabase = mClazz.getConstructor(Database.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new CoreException("In Class : " + mClazz.getName() + " wrong definition of constructor define.");
        }


        mDatabaseHandler = dbHandler;

        mClazz = clazz;

        mDocumentAnnotation = mClazz.getAnnotation(DocumentBase.class);

        // TODO complete
        if (mDocumentAnnotation == null)
            throw new CoreException("TODO Exception");

        mView = mDatabaseHandler.mDatabase.getView(mClazz.getSimpleName());
        mView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {
                String _sortField = "_id";
                if (sortField != null)
                    _sortField = sortField;

                Object type_found = document.get(mDocumentAnnotation.type_field());
                if (type_found != null && type_found.toString().equals(mDocumentAnnotation.type()))
                    emitter.emit(document.get(_sortField), document);
            }
        }, "2");

        mLiveQuery = getQuery().toLiveQuery();
        mLiveQuery.start();
        mLiveQuery.addChangeListener(new LiveQuery.ChangeListener() {
            @Override
            public void changed(LiveQuery.ChangeEvent event) {
                List<T> item = runQuery(event.getRows());
                for (AccessInterface.ChangeListener changeListener : mChangeListenerList) {
                    changeListener.changed(item);
                }
                //mLiveQuery.start();
            }
        });
    }

    public void purgeAll() {
        Query query = mView.createQuery();
        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                Document doc = row.getDocument();
                doc.purge();
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    public T getNew() {
        T res = null;
        try {
            res = mConstructorFromDatabase.newInstance(mDatabaseHandler.mDatabase);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    //@Override
    public T get(String id) {
        Document doc = mDatabaseHandler.mDatabase.getExistingDocument(id);
        if (doc != null)
            return getInstance(doc);
        return null;
    }

    /**
     * getAll.
     * Type view filter
     *
     * @return all data T in a list
     */
    //@Override
    public List getAll() {
        Query query = getQuery();
        try {
            QueryEnumerator result = query.run();
            List<T> items = runQuery(result);
            return items;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    //@Override
    public void addChangeListener(AccessInterface.ChangeListener changeListener) {
        mChangeListenerList.add(changeListener);
    }

    //@Override
    public void removeChangeListener(AccessInterface.ChangeListener changeListener) {
        mChangeListenerList.remove(changeListener);
    }

    // Override this method to change default filter.
    protected Query getQuery() {
        return mView.createQuery();
    }

    /**
     * runQuery.
     *
     * @param result the query row enumerator result
     * @return a list of T
     **/
    protected List<T> runQuery(QueryEnumerator result) {
        List<T> res = new ArrayList();
        for (Iterator<QueryRow> it = result; it.hasNext(); ) {
            QueryRow row = it.next();
            Document doc = row.getDocument();
            T data = getInstance(doc);
            if (data != null)
                res.add(data);
        }

        return res;
    }

    private T getInstance(Document document) {

        T res = null;
        try {
            res = mConstructorFromDocument.newInstance(document);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }

    protected interface PurgeListener {
        boolean doPurge(ModelBase data);
    }

    protected void purges(PurgeListener purgeListener) {
        // Get all data
        Query query = mView.createQuery();
        try {
            QueryEnumerator result = query.run();
            for (Iterator<QueryRow> it = result; it.hasNext(); ) {
                QueryRow row = it.next();
                Document doc = row.getDocument();
                T data = getInstance(doc);
                if (purgeListener.doPurge(data)) {
                    doc.purge();
                }
            }
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }
}
