package com.mapotempo.fleet.core.model.accessor;

import com.couchbase.lite.Predicate;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryRow;
import com.mapotempo.fleet.api.model.accessor.MissionAccessInterface;
import com.mapotempo.fleet.core.DatabaseHandler;
import com.mapotempo.fleet.core.accessor.Access;
import com.mapotempo.fleet.core.exception.CoreException;
import com.mapotempo.fleet.core.model.Mission;

import java.util.ArrayList;
import java.util.List;

/**
 * MissionAccess.
 */
public class MissionAccess extends Access<Mission> implements MissionAccessInterface {
    public MissionAccess(DatabaseHandler dbHandler) throws CoreException {
        super(Mission.class, dbHandler);
    }

    /*
    // Query filter example
    public List<Mission> getAllDataName(final String name)
    {
        List<Mission> res = new ArrayList<>();
        System.out.println(mView.getDocumentType());

        Query query = mView.createQuery();
        System.out.println("DEBUG");
        query.setPostFilter(new Predicate<QueryRow>() {
            @Override
            public boolean apply(QueryRow queryRow) {
                System.out.println(queryRow.getValue());
                Object doc_name = queryRow.getDocument().getProperty("name");
                if(doc_name != null && doc_name.equals(name))
                    return true;
                return false;
            }
        });
        System.out.println("DEBUG");
        return runQuery(query);
    }*/
}
