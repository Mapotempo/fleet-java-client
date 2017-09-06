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
        return (String) getProperty(LABEL, "Unknow");
    }

    public void setLabel(String label) {
        setProperty(LABEL, label);
    }

    public String getColor() {
        return (String) getProperty(COLOR, "FF0000");
    }

    public void setColor(String hexColor) {
        setProperty(COLOR, hexColor);
    }

    public ArrayList<MissionCommand> getCommands() {
        ArrayList<HashMap> hashArray = (ArrayList<HashMap>) getProperty(COMMANDS, new ArrayList<HashMap>());
        ArrayList<MissionCommand> res = new ArrayList<>();
        for (HashMap hm : hashArray) {
            res.add(new MissionCommand(hm, mDatabase));
        }
        return res;
    }
}