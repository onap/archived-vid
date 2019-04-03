package org.onap.vid.roles;

public class AlwaysValidRoleValidator implements RoleValidator {

    AlwaysValidRoleValidator() {
        // package visibility, only for RoleValidator's factory
    }

    @Override
    public boolean isSubscriberPermitted(String subscriberName) {
        return true;
    }

    @Override
    public boolean isServicePermitted(String subscriberName, String serviceType) {
        return true;
    }

    @Override
    public boolean isTenantPermitted(String globalCustomerId, String serviceType, String tenantName) {
        return true;
    }
}
