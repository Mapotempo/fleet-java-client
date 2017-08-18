package com.mapotempo.fleet.core.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.exception.CoreException;

/**
 * MissionStatusType.
 */
@DocumentBase(type = "mission_status_type")
public class MissionStatusType extends MapotempoModelBase {

    // MAPOTEMPO KEY
    public static final String LABEL = "label";
    public static final String COLOR = "color";

    public MissionStatusType(Database database) {
        super(database);
    }

    public MissionStatusType(Document document) {
        super(document);
    }

    public MissionStatusType(String id, Database database) throws CoreException {
        super(id, database);
    }

    public String getLabel() {
        return (String)getProperty(LABEL, "Unknow");
    }

    public void setLabel(String label) {
        setProperty(LABEL, label);
    }

    public String getColor() {
        return (String)getProperty(COLOR, "FF0000");
    }

    public void setColor(String hexColor) {
        setProperty(COLOR, hexColor);
    }
}