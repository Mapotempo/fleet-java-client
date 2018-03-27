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
import com.mapotempo.fleet.api.model.CompanyInterface;
import com.mapotempo.fleet.api.model.MissionActionInterface;
import com.mapotempo.fleet.api.model.MissionActionTypeInterface;
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.accessor.MissionActionAccessInterface;
import com.mapotempo.fleet.api.model.submodel.LocationInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.MissionAction;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MissionActionAccess.
 */
public class MissionActionAccess extends Access<MissionAction> implements MissionActionAccessInterface {
    public MissionActionAccess(DatabaseHandler dbHandler) throws CoreException {
        super(MissionAction.class, dbHandler, "date");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MissionAction create(CompanyInterface company,
                                MissionInterface mission,
                                MissionActionTypeInterface actionType,
                                Date date,
                                @Nullable LocationInterface location) {
        MissionAction res = getNew();
        res.setCompany(company);
        res.setMission(mission);
        res.setActionType(actionType);
        res.setDate(date);

        if (location != null)
            res.setLocation(location);

        res.save();
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MissionActionInterface> getByMission(final MissionInterface mission) {
        List<MissionActionInterface> res = new ArrayList<>();

        Query query = mView.createQuery();
        query.setPostFilter(new Predicate<QueryRow>() {
            @Override
            public boolean apply(QueryRow queryRow) {
                MissionAction missionStatus = new MissionAction(queryRow.getDocument());

                if (missionStatus.getMission().equals(mission)) {
                    return true;
                }
                return false;
            }
        });

        QueryEnumerator queryEnumerator;
        try {
            queryEnumerator = query.run();
            res = new ArrayList<MissionActionInterface>(runQuery(queryEnumerator));
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
}
