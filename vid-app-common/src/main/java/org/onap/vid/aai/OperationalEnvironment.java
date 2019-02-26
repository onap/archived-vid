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

package org.onap.vid.aai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.model.RelationshipList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalEnvironment {

    private String operationalEnvironmentId;
    private String operationalEnvironmentName;
    private String operationalEnvironmentType;
    private String operationalEnvironmentStatus;
    private String tenantContext;
    private String workloadContext;
    private String resourceVersion;
    private RelationshipList relationshipList;

    public OperationalEnvironment() {
    }

    public OperationalEnvironment(String operationalEnvironmentId, String operationalEnvironmentName, String operationalEnvironmentType, String operationalEnvironmentStatus, String tenantContext, String workloadContext, String resourceVersion, RelationshipList relationshipList) {
        this.operationalEnvironmentId = operationalEnvironmentId;
        this.operationalEnvironmentName = operationalEnvironmentName;
        this.operationalEnvironmentType = operationalEnvironmentType;
        this.operationalEnvironmentStatus = operationalEnvironmentStatus;
        this.tenantContext = tenantContext;
        this.workloadContext = workloadContext;
        this.resourceVersion = resourceVersion;
        this.relationshipList = relationshipList;
    }

    public String getOperationalEnvironmentId() {
        return operationalEnvironmentId;
    }

    @JsonProperty("operational-environment-id")
    public void setJsonOperationalEnvironmentId(String operationalEnvironmentId) {
        this.operationalEnvironmentId = operationalEnvironmentId;
    }

    public String getOperationalEnvironmentName() {
        return operationalEnvironmentName;
    }

    @JsonProperty("operational-environment-name")
    public void setJsonOperationalEnvironmentName(String operationalEnvironmentName) {
        this.operationalEnvironmentName = operationalEnvironmentName;
    }

    public String getOperationalEnvironmentType() {
        return operationalEnvironmentType;
    }

    @JsonProperty("operational-environment-type")
    public void setJsonOperationalEnvironmentType(String operationalEnvironmentType) {
        this.operationalEnvironmentType = operationalEnvironmentType;
    }

    public String getOperationalEnvironmentStatus() {
        return operationalEnvironmentStatus;
    }

    @JsonProperty("operational-environment-status")
    public void setJsonOperationalEnvironmentStatus(String operationalEnvironmentStatus) {
        this.operationalEnvironmentStatus = operationalEnvironmentStatus;
    }

    public String getTenantContext() {
        return tenantContext;
    }

    @JsonProperty("tenant-context")
    public void setJsonTenantContext(String tenantContext) {
        this.tenantContext = tenantContext;
    }

    public String getWorkloadContext() {
        return workloadContext;
    }

    @JsonProperty("workload-context")
    public void setJsonWorkloadContext(String workloadContext) {
        this.workloadContext = workloadContext;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonProperty("resource-version")
    public void setJsonResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    @JsonProperty("relationship-list")
    public void setJsonRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }
}
