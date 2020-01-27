package org.onap.vid.roles;


public class RoleValidatorByOwningEntity implements RoleValidator{

    public boolean isOwningEntityIdPermitted(String owningEntityId){
        return false;
    }

    @Override
    public boolean isSubscriberPermitted(String subscriberName) {
        return false;
    }

    @Override
    public boolean isServicePermitted(String subscriberName, String serviceType) {
        return false;
    }

    @Override
    public boolean isTenantPermitted(String globalCustomerId, String serviceType, String tenantName) {
        return false;
    }
}
