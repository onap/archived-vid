/*-
 * ============LICENSE_START=======================================================
 * VID ASDC Client
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.openecomp.vid.asdc.beans;

import java.util.Collection;
/**
 * The Class SubResource.
 */
public class SubResource {

	/** The resource instance name. */
	private String resourceInstanceName;
	
	/** The resource name. */
	private String resourceName;
	
	/** The resource invariant UUID. */
	private String resourceInvariantUUID;
	
	/** The resource version. */
	private String resourceVersion;
	
	/** The resource type. */
	private String resourceType;
	
	/** The resource UUID. */
	private String resourceUUID;
	
	/** The artifacts. */
	private Collection<Artifact> artifacts;
	
	/**
	 * Gets the resource instance name.
	 *
	 * @return the resource instance name
	 */
	public String getResourceInstanceName() {
		return resourceInstanceName;
	}
	
	/**
	 * Gets the resource name.
	 *
	 * @return the resource name
	 */
	public String getResourceName() {
		return resourceName;
	}
	
	/**
	 * Gets the resource invariant UUID.
	 *
	 * @return the resource invariant UUID
	 */
	public String getResourceInvariantUUID() {
		return resourceInvariantUUID;
	}
	
	/**
	 * Gets the resource version.
	 *
	 * @return the resource version
	 */
	public String getResourceVersion() {
		return resourceVersion;
	}
	
	/**
	 * Gets the resoucre type.
	 *
	 * @return the resoucre type
	 */
	public String getResoucreType() {
		return resourceType;
	}
	
	/**
	 * Gets the resource UUID.
	 *
	 * @return the resource UUID
	 */
	public String getResourceUUID() {
		return resourceUUID;
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
	 * Sets the resource instance name.
	 *
	 * @param resourceInstanceName the new resource instance name
	 */
	public void setResourceInstanceName(String resourceInstanceName) {
		this.resourceInstanceName = resourceInstanceName;
	}
	
	/**
	 * Sets the resource name.
	 *
	 * @param resourceName the new resource name
	 */
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
	/**
	 * Sets the resource invariant UUID.
	 *
	 * @param resourceInvariantUUID the new resource invariant UUID
	 */
	public void setResourceInvariantUUID(String resourceInvariantUUID) {
		this.resourceInvariantUUID = resourceInvariantUUID;
	}
	
	/**
	 * Sets the resource version.
	 *
	 * @param resourceVersion the new resource version
	 */
	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}
	
	/**
	 * Sets the resoucre type.
	 *
	 * @param resourceType the new resoucre type
	 */
	public void setResoucreType(String resourceType) {
		this.resourceType = resourceType;
	}
	
	/**
	 * Sets the resource UUID.
	 *
	 * @param resourceUUID the new resource UUID
	 */
	public void setResourceUUID(String resourceUUID) {
		this.resourceUUID = resourceUUID;
	}
	
	/**
	 * Sets the artifacts.
	 *
	 * @param artifacts the new artifacts
	 */
	public void setArtifacts(Collection<Artifact> artifacts) {
		this.artifacts = artifacts;
	}
}
