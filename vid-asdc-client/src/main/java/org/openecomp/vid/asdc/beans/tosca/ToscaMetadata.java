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

package org.openecomp.vid.asdc.beans.tosca;

/**
 * The Class ToscaMetadata.
 */
public class ToscaMetadata {

	/** The template name. */
	private String template_name;
	
	/** The invariant UUID. */
	private String invariantUUID;
	
	/** The uuid. */
	private String uuid;
	
	/** The version. */
	private String version;
	
	/** The name. */
	private String name;
	
	/** The description. */
	private String description;
	
	/** The category. */
	private String category;
	
	/** The subcategory. */
	private String subcategory;
	
	/** The type. */
	private String type;
	
	/** The resource vendor. */
	private String resourceVendor;
	
	/** The resource vendor release. */
	private String resourceVendorRelease;
	
	/** The service ecomp naming. */
	private boolean serviceEcompNaming;
	
	/** The service homing. */
	private boolean serviceHoming;
	
	/** The vf module model name. */
	//ToscaMetadata for VF Modules
	private String vfModuleModelName;
	
	/** The vf module model invariant UUID. */
	private String vfModuleModelInvariantUUID;
	
	/** The vf module model UUID. */
	private String vfModuleModelUUID;
	
	/** The vf module model version. */
	private String vfModuleModelVersion;
	
	/**
	 * Instantiates a new tosca metadata.
	 */
	public ToscaMetadata() {}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
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
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUUID() {
		return uuid;
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
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
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
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Gets the subcategory.
	 *
	 * @return the subcategory
	 */
	public String getSubcategory() {
		return subcategory;
	}

	/**
	 * Gets the resource vendor.
	 *
	 * @return the resource vendor
	 */
	public String getResourceVendor() {
		return resourceVendor;
	}

	/**
	 * Gets the resource vendor release.
	 *
	 * @return the resource vendor release
	 */
	public String getResourceVendorRelease() {
		return resourceVendorRelease;
	}

	/**
	 * Checks if is service ecomp naming.
	 *
	 * @return true, if is service ecomp naming
	 */
	public boolean isServiceEcompNaming() {
		return serviceEcompNaming;
	}

	/**
	 * Checks if is service homing.
	 *
	 * @return true, if is service homing
	 */
	public boolean isServiceHoming() {
		return serviceHoming;
	}

	/**
	 * Sets the type.
	 *
	 * @param type the new type
	 */
	public void setType(String type) {
		this.type = type;
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
	 * Sets the uuid.
	 *
	 * @param uuid the new uuid
	 */
	public void setUUID(String uuid) {
		this.uuid = uuid;
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
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
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
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Sets the service ecomp naming.
	 *
	 * @param serviceEcompNaming the new service ecomp naming
	 */
	public void setServiceEcompNaming(boolean serviceEcompNaming) {
		this.serviceEcompNaming = serviceEcompNaming;
	}

	/**
	 * Sets the service homing.
	 *
	 * @param serviceHoming the new service homing
	 */
	public void setServiceHoming(boolean serviceHoming) {
		this.serviceHoming = serviceHoming;
	}

	/**
	 * Gets the template name.
	 *
	 * @return the template name
	 */
	public String gettemplate_name() {
		return template_name;
	}
	
	/**
	 * Sets the template name.
	 *
	 * @param template_name the new template name
	 */
	public void settemplate_name(String template_name) {
		this.template_name = template_name;
	}
	
	/**
	 * Sets the subcategory.
	 *
	 * @param subcategory the new subcategory
	 */
	public void setSubcategory(String subcategory) {
		this.subcategory = subcategory;
	}
	
	/**
	 * Sets the resource vendor.
	 *
	 * @param resourceVendor the new resource vendor
	 */
	public void setResourceVendor(String resourceVendor) {
		this.resourceVendor = resourceVendor;
	}

	/**
	 * Sets the resource vendor release.
	 *
	 * @param resourceVendorRelease the new resource vendor release
	 */
	public void setResourceVendorRelease(String resourceVendorRelease) {
		this.resourceVendorRelease = resourceVendorRelease;
	}

	/**
	 * Gets the vf module model name.
	 *
	 * @return the vf module model name
	 */
	public String getVfModuleModelName() {
		return vfModuleModelName;
	}

	/**
	 * Sets the vf module model name.
	 *
	 * @param vfModuleModelName the new vf module model name
	 */
	public void setVfModuleModelName(String vfModuleModelName) {
		this.vfModuleModelName = vfModuleModelName;
	}

	/**
	 * Gets the vf module model invariant UUID.
	 *
	 * @return the vf module model invariant UUID
	 */
	public String getVfModuleModelInvariantUUID() {
		return vfModuleModelInvariantUUID;
	}

	/**
	 * Sets the vf module model invariant UUID.
	 *
	 * @param vfModuleModelInvariantUUID the new vf module model invariant UUID
	 */
	public void setVfModuleModelInvariantUUID(String vfModuleModelInvariantUUID) {
		this.vfModuleModelInvariantUUID = vfModuleModelInvariantUUID;
	}

	/**
	 * Gets the vf module model UUID.
	 *
	 * @return the vf module model UUID
	 */
	public String getVfModuleModelUUID() {
		return vfModuleModelUUID;
	}

	/**
	 * Sets the vf module model UUID.
	 *
	 * @param vfModuleModelUUID the new vf module model UUID
	 */
	public void setVfModuleModelUUID(String vfModuleModelUUID) {
		this.vfModuleModelUUID = vfModuleModelUUID;
	}

	/**
	 * Gets the vf module model version.
	 *
	 * @return the vf module model version
	 */
	public String getVfModuleModelVersion() {
		return vfModuleModelVersion;
	}

	/**
	 * Sets the vf module model version.
	 *
	 * @param vfModuleModelVersion the new vf module model version
	 */
	public void setVfModuleModelVersion(String vfModuleModelVersion) {
		this.vfModuleModelVersion = vfModuleModelVersion;
	}
}
