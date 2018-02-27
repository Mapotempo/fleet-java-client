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

package com.mapotempo.fleet.core.model.accessor;

import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.MetaInfo;

/**
 * w
 * MetaInfoAccess.
 */
public class MetaInfoAccess extends Access<MetaInfo> {
    public MetaInfoAccess(DatabaseHandler dbHandler) throws CoreException {
        super(MetaInfo.class, dbHandler, null);
    }
}
