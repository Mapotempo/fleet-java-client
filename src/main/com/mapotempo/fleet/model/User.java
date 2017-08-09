package com.mapotempo.fleet.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.base.DocumentBase;

/**
 * Company.
 */
@DocumentBase(type = "user")
public class User extends MapotempoModelBase {

    public User(Database database) {
        super(database);
    }

    public User(Document doc) {
        super(doc);
    }

    public String getName() {
        return mDocument.getProperty("user").toString();
    }

    public String getCompanyId() {
        return mDocument.getProperty("company_id").toString();
    }
}
