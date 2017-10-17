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
import com.mapotempo.fleet.api.model.MissionStatusActionInterface;
import com.mapotempo.fleet.api.model.MissionStatusTypeInterface;
import com.mapotempo.fleet.api.model.accessor.MissionStatusActionAccessInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.base.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.MissionStatusAction;

import java.util.ArrayList;
import java.util.List;

/**
 * MissionStatusActionAccess.
 */
public class MissionStatusActionAccess extends Access<MissionStatusAction> implements MissionStatusActionAccessInterface {
    public MissionStatusActionAccess(DatabaseHandler dbHandler) throws CoreException {
        super(MissionStatusAction.class, dbHandler, null);
    }

    /**
     * Filter mission_status_action by previous mission_status_type_id
     *
     * @param missionStatusType
     * @return List
     */
    @Override
    public List<MissionStatusActionInterface> getByPrevious(final MissionStatusTypeInterface missionStatusType) {
        List<MissionStatusActionInterface> res = new ArrayList<>();

        Query query = mView.createQuery();
        query.setPostFilter(new Predicate<QueryRow>() {
            @Override
            public boolean apply(QueryRow queryRow) {
                MissionStatusAction action = new MissionStatusAction(queryRow.getDocument());

                if (action.getPreviousStatus().equals(missionStatusType)) {
                    return true;
                }
                return false;
            }
        });

        QueryEnumerator queryEnumerator;
        try {
            queryEnumerator = query.run();
            res = new ArrayList<MissionStatusActionInterface>(runQuery(queryEnumerator));
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
}
