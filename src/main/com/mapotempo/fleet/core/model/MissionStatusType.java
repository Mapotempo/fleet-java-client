package com.mapotempo.fleet.core.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.submodel.MissionCommand;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * MissionStatusType.
 * Read only class
 */
@DocumentBase(type = "mission_status_type")
public class MissionStatusType extends MapotempoModelBase {
    // MAPOTEMPO KEY
    public static final String LABEL = "label";
    public static final String COLOR = "color";
    public static final String COMMANDS = "commands";

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

    public ArrayList<MissionCommand> getCommands() {
        ArrayList<HashMap> hashArray = (ArrayList)getProperty(COMMANDS, new ArrayList<HashMap>());
        ArrayList<MissionCommand> res = new ArrayList<>();
        for(HashMap hm : hashArray)
        {
            res.add(new MissionCommand(hm, mDatabase));
        }
        return res;
    }
}