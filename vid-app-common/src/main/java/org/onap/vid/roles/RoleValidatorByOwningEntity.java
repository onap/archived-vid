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
import org.apache.commons.lang3.StringUtils;

public class RoleValidatorByOwningEntity implements RoleValidator{

    private final List<Role> userRoles;

    RoleValidatorByOwningEntity(List<Role> roles) {
        this.userRoles = roles;
    }

    private boolean isOwningEntityIdPermitted(String owningEntityId) {
        if (StringUtils.isEmpty(owningEntityId)) {
            return false;
        }

        return userRoles.stream().anyMatch(userRole ->
            StringUtils.equals(userRole.getOwningEntityId(), owningEntityId)
        );
    }

    @Override
    public boolean isSubscriberPermitted(String subscriberId) {
        return false;
    }

    @Override
    public boolean isServicePermitted(WithPermissionProperties permissionProperties) {
        if (permissionProperties instanceof WithPermissionPropertiesOwningEntity) {
            String owningEntityId = ((WithPermissionPropertiesOwningEntity) permissionProperties).getOwningEntityId();
            return isOwningEntityIdPermitted(owningEntityId);
        } else {
            return false;
        }
    }

    @Override
    public boolean isTenantPermitted(String subscriberId, String serviceType, String tenantName) {
        return false;
    }
}
