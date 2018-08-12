package vid.automation.test.model;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;

/**
 * Created by Oren on 7/16/17.
 */
public class User {
    public Credentials credentials;
    public ArrayList<String> subscriberNames;
    public ArrayList<String> serviceTypes;
    public ArrayList<String> tenants;
    public ArrayList<String> roles;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("credentials", credentials)
                .add("subscriberNames", subscriberNames)
                .add("serviceTypes", serviceTypes)
                .add("tenants", tenants)
                .add("roles", roles)
                .toString();
    }
}
