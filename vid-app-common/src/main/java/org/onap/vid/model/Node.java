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

package org.onap.vid.model;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.asdc.beans.tosca.Input;
import org.onap.vid.asdc.beans.tosca.NodeTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
/**
 * The Class Node.
 */
public class Node implements MinimalNode {
	/** The Constant LOG. */
	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(Node.class);

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
	private Map<String, Input> inputs = new HashMap<>();

	/** The get_input or other constructs from node template properties. */
	private Map<String, CommandProperty> commands;

	/** The get_input or other constructs from node template properties. */
	private Map<String, String> properties;

	/** Type from Metadata */
	private String type;
	/**
	 * Instantiates a new node.
	 */
	public Node() {
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


	/**
	 * @return metadata type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Set metadata type
	 *
	 * @param type e.g. VF/CP/SERVICE_PROXY
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Extract node.
	 *
	 * @param nodeTemplate the node template
	 * @return the node
	 */
	public void extractNode (NodeTemplate nodeTemplate) {
		
		String methodName = "extractNode";
		
		setUuid(nodeTemplate.getMetadata().getUUID());
		setInvariantUuid(nodeTemplate.getMetadata().getInvariantUUID());
		setDescription(nodeTemplate.getMetadata().getDescription());
		setName(nodeTemplate.getMetadata().getName());
		setVersion(nodeTemplate.getMetadata().getVersion());
		// add customizationUUID
		setCustomizationUuid(nodeTemplate.getMetadata().getCustomizationUUID());

		try {
			// nodeTemplate.getProperties() map of String->Object
			for (Entry<String, Object> entrySet : nodeTemplate.getProperties().entrySet()) {
				LOG.debug(EELFLoggerDelegate.debugLogger, methodName + " node template property: " + entrySet.getKey());
				if ( entrySet.getValue() != null ) {
					readStringAndCommandsProperties(entrySet);
				}
			}
		}
		catch ( Exception e ) {
			LOG.error(EELFLoggerDelegate.errorLogger, methodName + " Unable to parse node properties: e=" +
					e.toString());
		}
	}

	private void readStringAndCommandsProperties(Entry<String, Object> entrySet) {
		String key = entrySet.getKey();
		String methodName = "readStringAndCommandsProperties";
		LOG.debug(EELFLoggerDelegate.debugLogger, methodName + "  property: " +
				key + "=" + entrySet.getValue());
		Class<?> c = entrySet.getValue().getClass();
		if ( c.getName().equalsIgnoreCase(String.class.getName())) {
            getProperties().put (key, (String) entrySet.getValue());
        }
        else {
            Class<?>[] interfaces = entrySet.getValue().getClass().getInterfaces();

            for(Class<?> ifc: interfaces ) {
                if ( ifc.getName().equalsIgnoreCase(Map.class.getName()) ) {
                    readGetInputAsCommands(entrySet, key);

                }
            }

        }
	}

    private void readGetInputAsCommands(Entry<String, Object> entrySet, String key) {
        // only extract get_input for now
        @SuppressWarnings("unchecked")
        HashMap<String,String> v = (HashMap<String,String>) entrySet.getValue();
        for (Entry<String, String> entry : v.entrySet()) {
            // only include get_input for now
            if ( ModelConstants.GET_INPUT_TAG.equalsIgnoreCase ( entry.getKey() ) ) {
                CommandProperty cp = new CommandProperty();
                cp.setCommand(entry.getKey());
                cp.setInputName(entry.getValue());
                cp.setDisplayName(key);
                getCommands().put(key,cp);
            }
        }
    }

}
