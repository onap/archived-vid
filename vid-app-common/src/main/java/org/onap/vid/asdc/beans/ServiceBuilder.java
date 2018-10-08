package org.onap.vid.asdc.beans;

import java.util.Collection;

public class ServiceBuilder {
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
        return new Service(uuid, invariantUUID, category, version, name, distributionStatus, toscaModelURL, lifecycleState, artifacts, resources);
    }
}


