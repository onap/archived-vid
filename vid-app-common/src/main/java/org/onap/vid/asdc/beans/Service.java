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

package org.onap.vid.asdc.beans;

import java.util.Collection;
import java.util.UUID;

public class Service {

    public enum DistributionStatus {

    DISTRIBUTION_NOT_APPROVED,

    DISTRIBUTION_APPROVED,

    DISTRIBUTED,

    DISTRIBUTION_REJECTED,

    DISTRIBUTION_COMPLETE_OK
    }

    public enum LifecycleState {

        NOT_CERTIFIED_CHECKOUT,

        NOT_CERTIFIED_CHECKIN,

        READY_FOR_CERTIFICATION,

        CERTIFICATION_IN_PROGRESS,

        CERTIFIED
    }

    private String uuid;

    private String invariantUUID;

    private String name;

    private String version;

    private String toscaModelURL;

    private String category;

    private Service.LifecycleState lifecycleState;

    private String lastUpdaterUserId;

    private String lastUpdaterFullName;

    private String distributionStatus;

    private Collection<Artifact> artifacts;

    private Collection<SubResource> resources;

    private String orchestrationType;

    private Boolean isTemplateExists;
    
    
    public static class ServiceBuilder {
       private String uuid;
       private String invariantUUID;
       private String name;
       private String version;
       private String toscaModelURL;
       private String category;
       private Service.LifecycleState lifecycleState;
       private String distributionStatus;
       private Collection<Artifact> artifacts;
       private Collection<SubResource> resources;
        private String orchestrationType;

        public ServiceBuilder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public ServiceBuilder setInvariantUUID(String invariantUUID) {
            this.invariantUUID = invariantUUID;
            return this;
        }

        public ServiceBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ServiceBuilder setVersion(String version) {
            this.version = version;
            return this;
        }

        public ServiceBuilder setToscaModelURL(String toscaModelURL) {
            this.toscaModelURL = toscaModelURL;
            return this;
        }

        public ServiceBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public ServiceBuilder setLifecycleState(Service.LifecycleState lifecycleState) {
            this.lifecycleState = lifecycleState;
            return this;
        }

        public ServiceBuilder setDistributionStatus(String distributionStatus) {
            this.distributionStatus = distributionStatus;
            return this;
        }

        public ServiceBuilder setArtifacts(Collection<Artifact> artifacts) {
            this.artifacts = artifacts;
            return this;
        }

        public ServiceBuilder setResources(Collection<SubResource> resources) {
            this.resources = resources;
            return this;
        }

        public ServiceBuilder setOrchestrationType(String orchestrationType) {
            this.orchestrationType = orchestrationType;
            return this;
        }

        public Service build() {
            return new Service(this);
        }
    }
    

    public String getUuid() {
        return uuid;
    }

    public String getInvariantUUID() {
        return invariantUUID;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getToscaModelURL() {
        return toscaModelURL;
    }

    public String getCategory() {
        return category;
    }

    public Service.LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    public String getLastUpdaterUserId() {
        return lastUpdaterUserId;
    }

    public String getLastUpdaterFullName() {
        return lastUpdaterFullName;
    }

    public String getDistributionStatus() {
        return distributionStatus;
    }

    public Collection<Artifact> getArtifacts() {
        return artifacts;
    }

    public Collection<SubResource> getResources() {
        return resources;
    }

    public String getOrchestrationType() {
        return orchestrationType;
    }

    public Boolean getTemplateExists() { return isTemplateExists; }


    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setInvariantUUID(String invariantUUID) {
        this.invariantUUID = invariantUUID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setToscaModelURL(String toscaModelURL) {
        this.toscaModelURL = toscaModelURL;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLifecycleState(Service.LifecycleState lifecycleState) {
        this.lifecycleState = lifecycleState;
    }

    public void set(String lastUpdaterUserId) {
        this.lastUpdaterUserId = lastUpdaterUserId;
    }

    public void setLastUpdaterFullName(String lastUpdaterFullName) {
        this.lastUpdaterFullName = lastUpdaterFullName;
    }

    public void setDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public void setArtifacts(Collection<Artifact> artifacts) {
        this.artifacts = artifacts;
    }

    public void setResources(Collection<SubResource> resources) {
        this.resources = resources;
    }

    public void setOrchestrationType(String orchestrationType) {
        this.orchestrationType = orchestrationType;
    }

    public void setTemplateExists(Boolean templateExists) { isTemplateExists = templateExists; }

    @Override
    public String toString() {
        return uuid;
    }

    @Override
    public int hashCode() {
        return UUID.fromString(getUuid()).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Service))
            return false;

        final Service service = (Service) o;

        return (service.getUuid().equals(getUuid()));
    }

    public Service() {}

    public Service(ServiceBuilder serviceBuilder) {

        this.uuid = serviceBuilder.uuid;
        this.invariantUUID = serviceBuilder.invariantUUID;
        this.name = serviceBuilder.name;
        this.version = serviceBuilder.version;
        this.toscaModelURL = serviceBuilder.toscaModelURL;
        this.category = serviceBuilder.category;
        this.lifecycleState = serviceBuilder.lifecycleState;
        this.distributionStatus = serviceBuilder.distributionStatus;
        this.artifacts = serviceBuilder.artifacts;
        this.resources = serviceBuilder.resources;
        this.orchestrationType = serviceBuilder.orchestrationType;
    }
}
