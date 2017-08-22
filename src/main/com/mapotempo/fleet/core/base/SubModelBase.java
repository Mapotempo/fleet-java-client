package com.mapotempo.fleet.core.base;

import com.couchbase.lite.Database;

import java.util.Map;

/** The abstract submodel base.*/
public abstract class SubModelBase {

    protected Database mDatabase;

    public SubModelBase(Database database) {
        mDatabase = database;
        return;
    }

    public SubModelBase(Map map, Database database) {
        mDatabase = database;
        fromMap(map);
        return;
    }

    abstract protected void fromMap(Map map);

    abstract public Map<String, String> toMap();
}
