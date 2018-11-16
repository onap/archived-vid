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

import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.mso.rest.RequestDetails;

import java.util.List;
import java.util.Map;

/**
 * Created by Oren on 7/12/17.
 */
public class RoleValidator {

    private boolean disableRoles = SystemProperties.getProperty("role_management_activated").equals("false");
    private List<Role> userRoles;

    public RoleValidator(List<Role> roles) {
        this.userRoles = roles;
    }

    public boolean isSubscriberPermitted(String subscriberName) {
        if(this.disableRoles) return true;

        for (Role role : userRoles) {
            if (role.getSubscribeName().equals(subscriberName))
                return true;
        }
        return false;
    }

    public boolean isServicePermitted(String subscriberName, String serviceType) {
        if(this.disableRoles) return true;

        for (Role role : userRoles) {
            if (role.getSubscribeName().equals(subscriberName) && role.getServiceType().equals(serviceType))
                return true;
        }
        return false;
    }

    public boolean isMsoRequestValid(RequestDetails mso_request) {
        if(this.disableRoles) return true;

        try {
            String globalSubscriberIdRequested = (String) ((Map) ((Map) mso_request.getAdditionalProperties().get("requestDetails")).get("subscriberInfo")).get("globalSubscriberId");
            String serviceType = (String) ((Map) ((Map) mso_request.getAdditionalProperties().get("requestDetails")).get("requestParameters")).get("subscriptionServiceType");
            return isServicePermitted(globalSubscriberIdRequested, serviceType);
        } catch (Exception e) {
            //Until we'll get the exact information regarding the tenants and the global customer id, we'll return true on unknown requests to mso
            return true;
        }
    }

    public boolean isTenantPermitted(String globalCustomerId, String serviceType, String tenantName) {
        if(this.disableRoles) return true;

        for (Role role : userRoles) {
            if (role.getSubscribeName().equals(globalCustomerId)
                    && role.getServiceType().equals(serviceType)
                    && (role.getTenant() == null || role.getTenant().equalsIgnoreCase(tenantName))) {
                return true;
            }
        }
        return false;
    }

    void enableRoles() {
        this.disableRoles = false;
    }
}
