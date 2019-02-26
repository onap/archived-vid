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
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.model.interfaces.AaiModelWithRelationships;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Vnf implements AaiModelWithRelationships {
    @JsonProperty("vnf-id")
    private String vnfId;
    @JsonProperty("vnf-name")
    private String vnfName;
    @JsonProperty("vnf-type")
    private String vnfType;
    @JsonProperty("resource-version")
    private String resourceVersion;
    @JsonProperty("orchestration-status")
    private String orchestrationStatus;
    @JsonProperty("relationship-list")
    private RelationshipList relationshipList;


    @JsonProperty("vnf-id")
    public String getVnfId() {
        return vnfId;
    }

    @JsonProperty("vnf-id")
    public void setVnfId(String vnfId) {
        this.vnfId = vnfId;
    }

    @JsonProperty("vnf-name")
    public String getVnfName() {
        return vnfName;
    }

    @JsonProperty("vnf-name")
    public void setVnfName(String vnfName) {
        this.vnfName = vnfName;
    }

    @JsonProperty("vnf-type")
    public String getVnfType() {
        return vnfType;
    }

    @JsonProperty("vnf-type")
    public void setVnfType(String vnfType) {
        this.vnfType = vnfType;
    }

    @JsonProperty("resource-version")
    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonProperty("resource-version")
    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @JsonProperty("orchestration-status")
    public String getOrchestrationStatus() {
        return orchestrationStatus;
    }

    @JsonProperty("orchestration-status")
    public void setOrchestrationStatus(String orchestrationStatus) {
        this.orchestrationStatus = orchestrationStatus;
    }

    @Override
    @JsonProperty("relationship-list")
    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    @JsonProperty("relationship-list")
    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }

}
