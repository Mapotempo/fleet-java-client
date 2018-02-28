/*
 * Copyright © Mapotempo, 2018
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
import com.mapotempo.fleet.api.model.submodel.MetaInfoInterface;
import com.mapotempo.fleet.core.base.DocumentBase;
import com.mapotempo.fleet.core.base.ModelBase;

@DocumentBase(type = "meta_info")
public class MetaInfo extends ModelBase implements MetaInfoInterface {

    private static final String SERVER_VERSION = "server_version";
    private static final String MINIMAL_CLIENT_VERSION = "minimal_client_version";

    public MetaInfo(Database database) {
        super(database);
    }

    public MetaInfo(Document doc) {
        super(doc);
    }

    public Integer getServerVersion() {
        return getProperty(SERVER_VERSION, Integer.class, -1);
    }

    public Integer getMinimalClientVersion() {
        return getProperty(MINIMAL_CLIENT_VERSION, Integer.class, -1);
    }
}
