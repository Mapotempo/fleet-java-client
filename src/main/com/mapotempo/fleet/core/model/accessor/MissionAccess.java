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

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Predicate;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.accessor.AccessInterface;
import com.mapotempo.fleet.api.model.accessor.MissionAccessInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.ModelBase;
import com.mapotempo.fleet.core.base.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Mission;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * MissionAccess.
 */
public class MissionAccess extends Access<Mission> implements MissionAccessInterface, AccessInterface<MissionInterface> {

    // Query hour offset
    private static final int HOUR_QUERY_OFFSET = -12;

    // Purge hour offset.
    private static final int HOUR_PURGE_OFFSET = -96;

    public MissionAccess(DatabaseHandler dbHandler) throws CoreException {
        super(Mission.class, dbHandler, "date");
    }

    @Override
    protected Query getQuery() {
        Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendar.HOUR, HOUR_QUERY_OFFSET);
        Query query = mView.createQuery();
        query.setPostFilter(new Predicate<QueryRow>() {
            @Override
            public boolean apply(QueryRow queryRow) {
                Mission mission = new Mission(queryRow.getDocument());
                if (mission.getDate().after(calendar.getTime())) {
                    return true;
                }
                return false;
            }
        });
        return query;
    }

    @Override
    public List getAllWithoutFilter() {
        Query query = mView.createQuery();
        try {
            QueryEnumerator result = query.run();
            List<Mission> items = runQuery(result);
            return items;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
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

                if (mission.getDate().after(before) && mission.getDate().before(after)) {
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

    public void purgeOutdated() {
        Date d = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);
        calendar.add(calendar.HOUR, HOUR_PURGE_OFFSET);
        final Date date = calendar.getTime();

        purges(new PurgeListener() {
            @Override
            public boolean doPurge(ModelBase data) {
                Mission mission = (Mission) data;
                if (mission.getDate().before(date))
                    return true;
                else
                    return false;
            }
        });
    }
}
