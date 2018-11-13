package org.onap.vid.roles;

import org.onap.vid.mso.rest.RequestDetails;

import java.util.List;
import java.util.Map;

/**
 * Created by Oren on 7/12/17.
 */
public class RoleValidator {

    private List<Role> userRoles;

    public RoleValidator(List<Role> roles) {
        this.userRoles = roles;
    }

    public boolean isSubscriberPermitted(String subscriberName) {
        for (Role role : userRoles) {
            if (role.getSubscribeName().equals(subscriberName))
                return true;
        }
        return false;
    }

    public boolean isServicePermitted(String subscriberName, String serviceType) {
        for (Role role : userRoles) {
            if (role.getSubscribeName().equals(subscriberName) && role.getServiceType().equals(serviceType))
                return true;
        }
        return false;
    }

    public boolean isMsoRequestValid(RequestDetails mso_request) {
        try {
            String globalSubscriberIdRequested = (String) ((Map) ((Map) mso_request.getAdditionalProperties().get("requestDetails")).get("subscriberInfo")).get("globalSubscriberId");
            String serviceType = (String) ((Map) ((Map) mso_request.getAdditionalProperties().get("requestDetails")).get("requestParameters")).get("subscriptionServiceType");
            return isServicePermitted(globalSubscriberIdRequested, serviceType);
        } catch (Exception e) {
            //Until we'll get the exact information regarding the tenants and the global customer id, we'll return true on unknown requests to mso
            return true;
        }
//        return false;
    }

    public boolean isTenantPermitted(String globalCustomerId, String serviceType, String tenantName) {
        for (Role role : userRoles) {
            if (role.getSubscribeName().equals(globalCustomerId)
                    && role.getServiceType().equals(serviceType)
                    && (role.getTenant() == null || role.getTenant().equalsIgnoreCase(tenantName))) {
                return true;
            }
        }
        return false;
    }
}
