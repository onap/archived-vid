/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 Nokia.
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

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.model.RelationshipList;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class OperationalEnvironment {

    private final String operationalEnvironmentId;
    private final String operationalEnvironmentName;
    private final String operationalEnvironmentType;
    private final String operationalEnvironmentStatus;
    private final String tenantContext;
    private final String workloadContext;
    private final String resourceVersion;
    private final RelationshipList relationshipList;

    public static OperationalEnvironmentBuilder builder() {
        return new OperationalEnvironmentBuilder();
    }

    @JsonCreator
    OperationalEnvironment(
        @JsonProperty(value = "operational-environment-id", access = WRITE_ONLY) String operationalEnvironmentId,
        @JsonProperty(value = "operational-environment-name", access = WRITE_ONLY) String operationalEnvironmentName,
        @JsonProperty(value = "operational-environment-type", access = WRITE_ONLY) String operationalEnvironmentType,
        @JsonProperty(value = "operational-environment-status", access = WRITE_ONLY) String operationalEnvironmentStatus,
        @JsonProperty(value = "tenant-context", access = WRITE_ONLY) String tenantContext,
        @JsonProperty(value = "workload-context", access = WRITE_ONLY) String workloadContext,
        @JsonProperty(value = "resource-version", access = WRITE_ONLY) String resourceVersion,
        @JsonProperty(value = "relationship-list", access = WRITE_ONLY) RelationshipList relationshipList) {
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

    public String getOperationalEnvironmentName() {
        return operationalEnvironmentName;
    }

    public String getOperationalEnvironmentType() {
        return operationalEnvironmentType;
    }

    public String getOperationalEnvironmentStatus() {
        return operationalEnvironmentStatus;
    }

    public String getTenantContext() {
        return tenantContext;
    }

    public String getWorkloadContext() {
        return workloadContext;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public static class OperationalEnvironmentBuilder {

        private String operationalEnvironmentId;
        private String operationalEnvironmentName;
        private String operationalEnvironmentType;
        private String operationalEnvironmentStatus;
        private String tenantContext;
        private String workloadContext;
        private String resourceVersion;

        private RelationshipList relationshipList;

        public OperationalEnvironmentBuilder withOperationalEnvironmentId(
            String operationalEnvironmentId) {
            this.operationalEnvironmentId = operationalEnvironmentId;
            return this;
        }

        public OperationalEnvironmentBuilder withOperationalEnvironmentName(
            String operationalEnvironmentName) {
            this.operationalEnvironmentName = operationalEnvironmentName;
            return this;
        }

        public OperationalEnvironmentBuilder withOperationalEnvironmentType(
            String operationalEnvironmentType) {
            this.operationalEnvironmentType = operationalEnvironmentType;
            return this;
        }

        public OperationalEnvironmentBuilder withOperationalEnvironmentStatus(
            String operationalEnvironmentStatus) {
            this.operationalEnvironmentStatus = operationalEnvironmentStatus;
            return this;
        }

        public OperationalEnvironmentBuilder withTenantContext(String tenantContext) {
            this.tenantContext = tenantContext;
            return this;
        }

        public OperationalEnvironmentBuilder withWorkloadContext(String workloadContext) {
            this.workloadContext = workloadContext;
            return this;
        }

        public OperationalEnvironmentBuilder withResourceVersion(String resourceVersion) {
            this.resourceVersion = resourceVersion;
            return this;
        }

        public OperationalEnvironmentBuilder withRelationshipList(
            RelationshipList relationshipList) {
            this.relationshipList = relationshipList;
            return this;
        }

        public OperationalEnvironment build() {
            return new OperationalEnvironment(operationalEnvironmentId,
                operationalEnvironmentName,
                operationalEnvironmentType,
                operationalEnvironmentStatus,
                tenantContext,
                workloadContext,
                resourceVersion,
                relationshipList);
        }

    }

}
