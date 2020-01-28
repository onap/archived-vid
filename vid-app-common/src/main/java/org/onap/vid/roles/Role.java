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

public class Role {

    private EcompRole ecompRole;

    private String subscriberId;

    private String serviceType;

    private String tenant;

    private String owningEntityId;

    public Role(EcompRole ecompRole, String subscriberId, String serviceType, String tenant,
        String owningEntityId) {
        this.ecompRole = ecompRole;
        this.subscriberId = subscriberId;
        this.serviceType = serviceType;
        this.tenant = tenant;
        this.owningEntityId = owningEntityId;
    }

    public EcompRole getEcompRole() {
        return ecompRole;
    }


    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getServiceType() {
        return serviceType;
    }


    public String getTenant() {
        return tenant;
    }


    public String getOwningEntityId() {
        return owningEntityId;
    }

    public void setOwningEntityId(String owningEntityId) {
        this.owningEntityId = owningEntityId;
    }
}
