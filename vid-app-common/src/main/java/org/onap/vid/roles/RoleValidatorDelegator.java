package org.onap.vid.roles;

import java.util.List;

public class RoleValidatorDelegator {

    RoleValidatorBySubscriberAndServiceType roleValidatorBySubscriberAndServiceType;

    public RoleValidatorDelegator(List<Role> roles){
        roleValidatorBySubscriberAndServiceType = new RoleValidatorBySubscriberAndServiceType(roles); //       roleProvider.getUserRoles(request)
    }

    boolean isServicePermitted(String subscriberName, String serviceType){
        return roleValidatorBySubscriberAndServiceType.isServicePermitted(subscriberName, serviceType);
    }



}
