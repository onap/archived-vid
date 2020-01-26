/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.roles;

import java.util.List;
import java.util.Map;
import org.onap.vid.mso.rest.RequestDetails;

public class RoleValidatorBySubscriberAndServiceType implements RoleValidator {

    private final List<Role> userRoles;

    RoleValidatorBySubscriberAndServiceType(List<Role> roles) {
        this.userRoles = roles;
    }

    @Override
    public boolean isSubscriberPermitted(String subscriberName) {
        for (Role role : userRoles) {
            if (role.getSubscribeName().equals(subscriberName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isServicePermitted(String subscriberName, String serviceType) {
        for (Role role : userRoles) {
            if (role.getSubscribeName().equals(subscriberName) && role.getServiceType().equals(serviceType)) {
                return true;
            }
        }
        return false;
    }

    @Override
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

    boolean isMsoRequestValid(RequestDetails msoRequest) {
        try {
            String globalSubscriberIdRequested = (String) ((Map) ((Map) msoRequest.getAdditionalProperties()
                .get("requestDetails")).get("subscriberInfo")).get("globalSubscriberId");
            String serviceType = (String) ((Map) ((Map) msoRequest.getAdditionalProperties().get("requestDetails"))
                .get("requestParameters")).get("subscriptionServiceType");
            return isServicePermitted(globalSubscriberIdRequested, serviceType);
        } catch (Exception e) {
            //Until we'll get the exact information regarding the tenants and the global customer id, we'll return true on unknown requests to mso
            return true;
        }
    }

}
