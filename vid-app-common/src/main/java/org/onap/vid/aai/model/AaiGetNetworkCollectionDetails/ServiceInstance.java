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

package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.model.interfaces.AaiModelWithRelationships;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceInstance implements AaiModelWithRelationships {

    private final String serviceInstanceId;
    private final String serviceInstanceName;
    private final String resourceVersion;
    private final RelationshipList relationshipList;

    public ServiceInstance(
            @JsonProperty("service-instance-id") String serviceInstanceId,
            @JsonProperty("service-instance-name") String serviceInstanceName,
            @JsonProperty("resource-version") String resourceVersion,
            @JsonProperty("relationship-list") RelationshipList relationshipList
    ) {
        this.serviceInstanceId = serviceInstanceId;
        this.serviceInstanceName = serviceInstanceName;
        this.resourceVersion = resourceVersion;
        this.relationshipList = relationshipList;
    }

    @JsonProperty("service-instance-id")
    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    @JsonProperty("service-instance-name")
    @JsonInclude(NON_NULL)
    public String getServiceInstanceName() {
        return serviceInstanceName;
    }

    @JsonProperty("resource-version")
    public String getResourceVersion() {
        return resourceVersion;
    }

    @Override
    @JsonProperty("relationship-list")
    public RelationshipList getRelationshipList() {
        return relationshipList;
    }
}
