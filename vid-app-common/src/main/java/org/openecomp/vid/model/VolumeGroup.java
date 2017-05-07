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
 * The Class VolumeGroup.
 */
public class VolumeGroup {

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
	
	/** The customization uuid. */
	private String customizationUuid;
	
	/** The customization uuid. */
	private String modelCustomizationName;
	/**
	 * Instantiates a new volume group.
	 */
	public VolumeGroup() {}

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * Gets the customization uuid.
	 *
	 * @return the customization uuid
	 */
	public String getCustomizationUuid() {
		return customizationUuid;
	}
	/**
	 * Gets the customization name.
	 *
	 * @return the customization name
	 */
	public String getModelCustomizationName() {
		return modelCustomizationName;
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
	 * Sets the customization uuid.
	 *
	 * @param u the new customization uuid
	 */
	private void setCustomizationUuid(String u) {
		this.customizationUuid = u;
		
	}
	/**
	 * Sets the customization name.
	 *
	 * @param u the new customization name
	 */
	private void setModelCustomizationName(String u) {
		this.modelCustomizationName = u;
		
	}
	/**
	 * Extract volume group.
	 *
	 * @param group the group
	 * @return the volume group
	 */
	public static VolumeGroup extractVolumeGroup(String modelCustomizationName, Group group) {
		final VolumeGroup volumeGroup = new VolumeGroup();
		
		volumeGroup.setUuid(group.getMetadata().getVfModuleModelUUID());
		volumeGroup.setInvariantUuid(group.getMetadata().getVfModuleModelInvariantUUID());
		volumeGroup.setDescription(group.getMetadata().getDescription());
		volumeGroup.setName(group.getMetadata().getVfModuleModelName());
		volumeGroup.setVersion(group.getMetadata().getVfModuleModelVersion());
		volumeGroup.setCustomizationUuid(group.getMetadata().getVfModuleModelCustomizationUUID());
		volumeGroup.setModelCustomizationName(modelCustomizationName);
		return volumeGroup;
	}

}
