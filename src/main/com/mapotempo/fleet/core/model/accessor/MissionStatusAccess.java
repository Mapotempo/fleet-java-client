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
import com.mapotempo.fleet.api.model.MissionInterface;
import com.mapotempo.fleet.api.model.MissionStatusInterface;
import com.mapotempo.fleet.api.model.accessor.MissionStatusAccessInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Mission;
import com.mapotempo.fleet.core.model.MissionStatus;
import com.mapotempo.fleet.core.model.MissionStatusType;

import java.util.ArrayList;
import java.util.List;

/**
 * MissionStatusAccess.
 */
public class MissionStatusAccess extends Access<MissionStatus> implements MissionStatusAccessInterface {
    public MissionStatusAccess(DatabaseHandler dbHandler) throws CoreException {
        super(MissionStatus.class, dbHandler, "date");
    }

    public MissionStatus getNew(Mission mission, MissionStatusType statusType) {
        MissionStatus res = getNew();
        res = getNew();
        res.setMission(mission);
        res.setStatusType(statusType);
        return res;
    }

    /**
     * Filter mission_status by mission_id
     *
     * @param mission
     * @return List
     */
    @Override
    public List<MissionStatusInterface> getByMission(final MissionInterface mission) {
        List<MissionStatusInterface> res = new ArrayList<>();

        Query query = mView.createQuery();
        query.setPostFilter(new Predicate<QueryRow>() {
            @Override
            public boolean apply(QueryRow queryRow) {
                MissionStatus missionStatus = new MissionStatus(queryRow.getDocument());

                if (missionStatus.getMission().equals(mission)) {
                    return true;
                }
                return false;
            }
        });

        QueryEnumerator queryEnumerator;
        try {
            queryEnumerator = query.run();
            res = new ArrayList<MissionStatusInterface>(runQuery(queryEnumerator));
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
}
