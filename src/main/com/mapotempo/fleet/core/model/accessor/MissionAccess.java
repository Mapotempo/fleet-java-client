package com.mapotempo.fleet.core.model.accessor;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Predicate;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;

import com.mapotempo.fleet.api.model.accessor.MissionAccessInterface;
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
public class MissionAccess extends Access<Mission> implements MissionAccessInterface {

    public MissionAccess(DatabaseHandler dbHandler) throws CoreException {
        super(Mission.class, dbHandler, "name");
    }


    /**
     * Filter missions trough a time window
     * @param before Start fetching from this date
     * @param after End fetching from this date
     * @return List<Mission>
     */
    public List<Mission> getByWindow(final Date before, final Date after) {
        List<Mission> res = new ArrayList<>();

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
            res = runQuery(queryEnumerator);
        } catch(CouchbaseLiteException e) {
            e.printStackTrace();
        } finally {
            return res;
        }
    }
}
