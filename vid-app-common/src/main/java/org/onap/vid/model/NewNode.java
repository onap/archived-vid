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

package org.onap.vid.model;

import org.onap.vid.asdc.beans.tosca.Input;

import java.util.HashMap;
import java.util.Map;


public class NewNode {
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
	
	/** The model customization uuid. */
	private String customizationUuid;
	
	/** The inputs. */
	private Map<String, Input> inputs;
	
	/** The get_input or other constructs from node template properties. */
	private Map<String, CommandProperty> commands;
	
	/** The get_input or other constructs from node template properties. */
	private Map<String, String> properties;
	/**
	 * Instantiates a new node.
	 */
	public NewNode() {
		this.commands = new HashMap<>();
		this.properties = new HashMap<>();
	}
	
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
	 * Gets the customization uuid.
	 *
	 * @return the model customization uuid
	 */
	public String getCustomizationUuid() {
		return customizationUuid;
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
	 * Gets the commands.
	 *
	 * @return the commands
	 */
	public Map<String, CommandProperty> getCommands() {
		return commands;
	}
	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	public Map<String, String> getProperties() {
		return properties;
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
	public void setCustomizationUuid(String u) {
		this.customizationUuid = u;
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
	 * Sets the commands.
	 *
	 * @param m the commands
	 */
	public void setCommands( Map<String, CommandProperty>m ) {
		commands = m;
	}
	/**
	 * Sets the properties.
	 *
	 * @param p the properties
	 */
	public void setProperties( Map<String, String>p) {
		properties = p;
	}

}
