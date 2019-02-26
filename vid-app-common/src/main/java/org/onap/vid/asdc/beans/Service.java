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
/**
 * The Class Service.
 */
public class Service {

	/**
	 * The Enum DistributionStatus.
	 */
	public enum DistributionStatus {

		/** The distribution not approved. */
		DISTRIBUTION_NOT_APPROVED,

		/** The distribution approved. */
		DISTRIBUTION_APPROVED,

		/** The distributed. */
		DISTRIBUTED,

		/** The distribution rejected. */
		DISTRIBUTION_REJECTED,

		/** The destributed for tenant isolation. */
		DISTRIBUTION_COMPLETE_OK
	}

	/**
	 * The Enum LifecycleState.
	 */
	public enum LifecycleState {

		/** The not certified checkout. */
		NOT_CERTIFIED_CHECKOUT,

		/** The not certified checkin. */
		NOT_CERTIFIED_CHECKIN,

		/** The ready for certification. */
		READY_FOR_CERTIFICATION,

		/** The certification in progress. */
		CERTIFICATION_IN_PROGRESS,

		/** The certified. */
		CERTIFIED
	}

	/** The uuid. */
	private String uuid;

	/** The invariant UUID. */
	private String invariantUUID;

	/** The name. */
	private String name;

	/** The version. */
	private String version;

	/** The tosca model URL. */
	private String toscaModelURL;

	/** The category. */
	private String category;

	/** The lifecycle state. */
	private Service.LifecycleState lifecycleState;

	/** The last updater user uid. */
	private String lastUpdaterUserId;

	/** The last updater full name. */
	private String lastUpdaterFullName;

	/** The distribution status. */
	private String distributionStatus;

	/** The artifacts. */
	private Collection<Artifact> artifacts;

	/** The resources. */
	private Collection<SubResource> resources;

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Gets the invariant UUID.
	 *
	 * @return the invariant UUID
	 */
	public String getInvariantUUID() {
		return invariantUUID;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the tosca model URL.
	 *
	 * @return the tosca model URL
	 */
	public String getToscaModelURL() {
		return toscaModelURL;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Gets the lifecycle state.
	 *
	 * @return the lifecycle state
	 */
	public Service.LifecycleState getLifecycleState() {
		return lifecycleState;
	}

	/**
	 * Gets the last updater user uid.
	 *
	 * @return the last updater user uid
	 */
	public String getLastUpdaterUserId() {
		return lastUpdaterUserId;
	}

	/**
	 * Gets the last updater full name.
	 *
	 * @return the last updater full name
	 */
	public String getLastUpdaterFullName() {
		return lastUpdaterFullName;
	}

	/**
	 * Gets the distribution status.
	 *
	 * @return the distribution status
	 */
	public String getDistributionStatus() {
		return distributionStatus;
	}

	/**
	 * Gets the artifacts.
	 *
	 * @return the artifacts
	 */
	public Collection<Artifact> getArtifacts() {
		return artifacts;
	}

	/**
	 * Gets the resources.
	 *
	 * @return the resources
	 */
	public Collection<SubResource> getResources() {
		return resources;
	}

	/**
	 * Sets the uuid.
	 *
	 * @param uuid the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Sets the invariant UUID.
	 *
	 * @param invariantUUID the new invariant UUID
	 */
	public void setInvariantUUID(String invariantUUID) {
		this.invariantUUID = invariantUUID;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the version.
	 *
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Sets the tosca model URL.
	 *
	 * @param toscaModelURL the new tosca model URL
	 */
	public void setToscaModelURL(String toscaModelURL) {
		this.toscaModelURL = toscaModelURL;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Sets the lifecycle state.
	 *
	 * @param lifecycleState the new lifecycle state
	 */
	public void setLifecycleState(Service.LifecycleState lifecycleState) {
		this.lifecycleState = lifecycleState;
	}

	/**
	 * Sets the last updater user uid.
	 *
	 * @param lastUpdaterUserId the new last updater user uid
	 */
	public void set(String lastUpdaterUserId) {
		this.lastUpdaterUserId = lastUpdaterUserId;
	}

	/**
	 * Sets the last updater full name.
	 *
	 * @param lastUpdaterFullName the new last updater full name
	 */
	public void setLastUpdaterFullName(String lastUpdaterFullName) {
		this.lastUpdaterFullName = lastUpdaterFullName;
	}

	/**
	 * Sets the distribution status.
	 *
	 * @param distributionStatus the new distribution status
	 */
	public void setDistributionStatus(String distributionStatus) {
		this.distributionStatus = distributionStatus;
	}

	/**
	 * Sets the artifacts.
	 *
	 * @param artifacts the new artifacts
	 */
	public void setArtifacts(Collection<Artifact> artifacts) {
		this.artifacts = artifacts;
	}

	/**
	 * Sets the resources.
	 *
	 * @param resources the new resources
	 */
	public void setResources(Collection<SubResource> resources) {
		this.resources = resources;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return uuid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return UUID.fromString(getUuid()).hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof Service)) return false;

		final Service service = (Service) o;

		return (service.getUuid().equals(getUuid()));
	}

	public Service(String uuid, String invariantUUID, String category, String version, String name, String distributionStatus, String toscaModelURL, LifecycleState lifecycleState, Collection<Artifact> artifacts, Collection<SubResource> resources) {
		this.uuid = uuid;
		this.invariantUUID = invariantUUID;
		this.name = name;
		this.version = version;
		this.toscaModelURL = toscaModelURL;
		this.category = category;
		this.lifecycleState = lifecycleState;
		this.distributionStatus = distributionStatus;
		this.artifacts = artifacts;
		this.resources = resources;
	}

	public Service() {
	}
}
