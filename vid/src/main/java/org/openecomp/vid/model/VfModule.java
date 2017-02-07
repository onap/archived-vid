/*-
 * ============LICENSE_START=======================================================
 * VID
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

package org.openecomp.vid.model;

import org.openecomp.vid.asdc.beans.tosca.Group;

/**
 * The Class VfModule.
 */
public class VfModule {

	/** The uuid. */
	private String uuid;
	
	/** The invariant uuid. */
	private String invariantUuid;
	
	/** The description. */
	private String description;
	
	/** The name. */
	private String name;
	
	/** The version. */
	private String version;
	
	/** The volume group allowed. */
	private boolean volumeGroupAllowed;
	
	/**
	 * Instantiates a new vf module.
	 */
	public VfModule() {}
	
	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Gets the invariant uuid.
	 *
	 * @return the invariant uuid
	 */
	public String getInvariantUuid() {
		return invariantUuid;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
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
	 * Checks if is volume group allowed.
	 *
	 * @return true, if is volume group allowed
	 */
	public boolean isVolumeGroupAllowed() {
		return volumeGroupAllowed;
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
	 * Sets the invariant uuid.
	 *
	 * @param invariantUuid the new invariant uuid
	 */
	public void setInvariantUuid(String invariantUuid) {
		this.invariantUuid = invariantUuid;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
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
	 * Sets the volume group allowed.
	 *
	 * @param volumeGroupAllowed the new volume group allowed
	 */
	private void setVolumeGroupAllowed(boolean volumeGroupAllowed) {
		this.volumeGroupAllowed = volumeGroupAllowed;
	}
	
	/**
	 * Extract vf module.
	 *
	 * @param group the group
	 * @return the vf module
	 */
	public static VfModule extractVfModule(Group group) {

		final VfModule vfModule = new VfModule();
		
		vfModule.setUuid(group.getMetadata().getVfModuleModelUUID());
		vfModule.setInvariantUuid(group.getMetadata().getVfModuleModelInvariantUUID());
		vfModule.setDescription(group.getMetadata().getDescription());
		vfModule.setName(group.getMetadata().getVfModuleModelName());
		vfModule.setVersion(group.getMetadata().getVfModuleModelVersion());
		
		if (group.getProperties().containsKey("volume_group")) {
			vfModule.setVolumeGroupAllowed(Boolean.valueOf(group.getProperties().get("volume_group")));
		} else {
			vfModule.setVolumeGroupAllowed(false);
		}
		
		return vfModule;
	}
}
