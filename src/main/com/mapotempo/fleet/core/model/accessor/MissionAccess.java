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

package com.mapotempo.fleet.core.model.accessor;

import com.couchbase.lite.*;
import com.mapotempo.fleet.api.accessor.AccessInterface;
import com.mapotempo.fleet.api.accessor.MissionAccessInterface;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Mission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * MissionAccess.
 */
public class MissionAccess extends Access<Mission> implements MissionAccessInterface, AccessInterface<MissionInterface> {

    public MissionAccess(DatabaseHandler dbHandler) throws CoreException {
        super(Mission.class, dbHandler, "name");
    }

    /**
     * Filter missions trough a time window
     *
     * @param before Start fetching from this date
     * @param after  End fetching from this date
     * @return List
     */
    @Override
    public List<MissionInterface> getByWindow(final Date before, final Date after) {
        List<MissionInterface> res = new ArrayList<>();

        Query query = mView.createQuery();
        query.setPostFilter(new Predicate<QueryRow>() {
            @Override
            public boolean apply(QueryRow queryRow) {
                Mission mission = new Mission(queryRow.getDocument());

                if (mission.getDeliveryDate().after(before) && mission.getDeliveryDate().before(after)) {
                    return true;
                }
                return false;
            }
        });

        QueryEnumerator queryEnumerator;
        try {
            queryEnumerator = query.run();
            res = new ArrayList<MissionInterface>(runQuery(queryEnumerator));
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
}
