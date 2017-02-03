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

import java.util.Map;
import java.util.UUID;

import org.openecomp.vid.asdc.beans.tosca.Input;
import org.openecomp.vid.asdc.beans.tosca.NodeTemplate;

/**
 * The Class VNF.
 */
public class VNF {

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
	
	/** The model customization name. */
	private String modelCustomizationName;
	
	/** The inputs. */
	private Map<String, Input> inputs;
	
	/** The vf modules. */
	private Map<UUID, VfModule> vfModules;
	
	/** The volume groups. */
	private Map<UUID, VolumeGroup> volumeGroups;
	
	/**
	 * Instantiates a new vnf.
	 */
	public VNF() {}
	
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
	 * Gets the model customization name.
	 *
	 * @return the model customization name
	 */
	public String getModelCustomizationName() {
		return modelCustomizationName;
	}

	/**
	 * Gets the inputs.
	 *
	 * @return the inputs
	 */
	public Map<String, Input> getInputs() {
		return inputs;
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
	 * Gets the vf modules.
	 *
	 * @return the vf modules
	 */
	public Map<UUID, VfModule> getVfModules() {
		return vfModules;
	}

	/**
	 * Sets the vf modules.
	 *
	 * @param vfModules the vf modules
	 */
	public void setVfModules(Map<UUID, VfModule> vfModules) {
		this.vfModules = vfModules;
	}

	/**
	 * Gets the volume groups.
	 *
	 * @return the volume groups
	 */
	public Map<UUID, VolumeGroup> getVolumeGroups() {
		return volumeGroups;
	}

	/**
	 * Sets the volume groups.
	 *
	 * @param volumeGroups the volume groups
	 */
	public void setVolumeGroups(Map<UUID, VolumeGroup> volumeGroups) {
		this.volumeGroups = volumeGroups;
	}

	/**
	 * Sets the inputs.
	 *
	 * @param inputs the inputs
	 */
	public void setInputs(Map<String, Input> inputs) {
		this.inputs = inputs;
	}

	/**
	 * Extract vnf.
	 *
	 * @param modelCustomizationName the model customization name
	 * @param nodeTemplate the node template
	 * @return the vnf
	 */
	public static VNF extractVnf(String modelCustomizationName, NodeTemplate nodeTemplate) {
		
		final VNF vnf = new VNF();
		
		vnf.setUuid(nodeTemplate.getMetadata().getUUID());
		vnf.setInvariantUuid(nodeTemplate.getMetadata().getInvariantUUID());
		vnf.setDescription(nodeTemplate.getMetadata().getDescription());
		vnf.setName(nodeTemplate.getMetadata().getName());
		vnf.setModelCustomizationName(modelCustomizationName);
		vnf.setVersion(nodeTemplate.getMetadata().getVersion());
		
		return vnf;
	}

	/**
	 * Sets the model customization name.
	 *
	 * @param modelCustomizationName the new model customization name
	 */
	private void setModelCustomizationName(String modelCustomizationName) {
		this.modelCustomizationName = modelCustomizationName;
	}
}
