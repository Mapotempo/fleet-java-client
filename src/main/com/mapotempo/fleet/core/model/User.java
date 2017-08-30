package com.mapotempo.fleet.core.model;

import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.mapotempo.fleet.core.base.MapotempoModelBase;
import com.mapotempo.fleet.core.base.DocumentBase;

import java.util.ArrayList;
import java.util.List;
/**
 * Company.
 */
@DocumentBase(type = "user")
public class User extends MapotempoModelBase {

    // MAPOTEMPO KEY
    public static final String USER = "user";
    public static final String COMPANY_ID = "company_id";
    public static final String ROLES = "roles";

    public User(Database database) {
        super(database);
    }

    public User(Document doc) {
        super(doc);
    }

    public String getUser() {
        return (String)getProperty(USER, "Unknow");
    }

    public String getCompanyId() {
        return (String)getProperty(COMPANY_ID, "No company id found");
    }

    public List<String> getRoles() {
        return (ArrayList<String>)getProperty(ROLES, new ArrayList<String>());
    }
}
