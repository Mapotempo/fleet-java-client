package com.mapotempo.fleet.core.model;

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

    public String getUser() {
        return (String)getProperty("user", "Unknow");
    }

    public String getCompanyId() {
        return (String)getProperty("company_id", "No company id found");
    }
}
