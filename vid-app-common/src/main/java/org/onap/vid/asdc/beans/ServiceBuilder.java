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

public class ServiceBuilder {
    String uuid;
    String invariantUUID;
    String name;
    String version;
    String toscaModelURL;
    String category;
    Service.LifecycleState lifecycleState;
    String distributionStatus;
    Collection<Artifact> artifacts;
    Collection<SubResource> resources;

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

    public Service build() {
        return new Service(this);
    }
}


