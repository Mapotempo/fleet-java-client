package com.mapotempo.fleet.api.model;

import com.mapotempo.fleet.core.base.MapotempoModelBase;

public interface MapotempoModelBaseInterface {

    boolean delete();

    interface ChangeListener<T extends MapotempoModelBase> {
        void changed(T item);
    }

    void addChangeListener(ChangeListener changeListener);

    void removeChangeListener(ChangeListener changeListener);

    public String getId();

    boolean save();
}