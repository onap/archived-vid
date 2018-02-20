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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.onap.vid.asdc.beans.tosca.Group;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

/**
 * The Class VfModule.
 */
public class VfModule {

	/** The Constant LOG. */
	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VfModule.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	/** The uuid. */
	private String uuid;
	
	/** The invariant uuid. */
	private String invariantUuid;
	
	/** The customization uuid. */
	private String customizationUuid;
	
	/** The description. */
	private String description;
	
	/** The name. */
	private String name;
	
	/** The version. */
	private String version;
	
	/** The volume group allowed. */
	private boolean volumeGroupAllowed;
	
	/** The get_input or other constructs for VF Module. */
	private Map<String, CommandProperty> commands;
	
	/** The model customization name. */
	private String modelCustomizationName;
	
	/**
	 * Instantiates a new vf module.
	 */
	public VfModule() {
		commands = new HashMap<String, CommandProperty>();
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
	 * Gets the customization uuid.
	 *
	 * @return the invariant uuid
	 */
	public String getCustomizationUuid() {
		return customizationUuid;
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
	 * Gets the commands.
	 *
	 * @return the commands
	 */
	public Map<String, CommandProperty> getCommands() {
		return commands;
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
	 * Sets the customization uuid.
	 *
	 * @param customizationUuid the new customization uuid
	 */
	public void setCustomizationUuid(String customizationUuid) {
		this.customizationUuid = customizationUuid;
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
	public void setVolumeGroupAllowed(boolean volumeGroupAllowed) {
		this.volumeGroupAllowed = volumeGroupAllowed;
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
	 * Sets the model customization name.
	 *
	 * @param modelCustomizationName the new model customization name
	 */
	public void setModelCustomizationName(String modelCustomizationName) {
		this.modelCustomizationName = modelCustomizationName;
	}
	/**
	 * Extract vf module.
	 *
	 * @param group the group
	 * @return the vf module
	 */
	public static  VfModule extractVfModule(String modelCustomizationName, Group group) {
		
		String methodName = "extractVfModule";

		final VfModule vfModule = new VfModule();
		
		try {
			vfModule.setUuid(group.getMetadata().getVfModuleModelUUID());
			vfModule.setInvariantUuid(group.getMetadata().getVfModuleModelInvariantUUID());
			vfModule.setDescription(group.getMetadata().getDescription());
			vfModule.setName(group.getMetadata().getVfModuleModelName());
			vfModule.setVersion(group.getMetadata().getVfModuleModelVersion());
			vfModule.setCustomizationUuid(group.getMetadata().getVfModuleModelCustomizationUUID());
			vfModule.setModelCustomizationName (modelCustomizationName);
			
			if (group.getProperties().containsKey("volume_group")) {
				if (group.getProperties().get("volume_group") != null) {
				
					Class<?> c = group.getProperties().get("volume_group").getClass();
					LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " class name=" + 
							c.getName());
					
					if ( c.getName().equalsIgnoreCase(Boolean.class.getName()) ) {
						Boolean b = (Boolean)group.getProperties().get("volume_group");
						vfModule.setVolumeGroupAllowed( b.booleanValue() );
					}
				}
			} else {
				vfModule.setVolumeGroupAllowed(false);
			}
		}
		catch ( Exception e ) {
			LOG.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + methodName + " Unable to parse VF Module from group: e=" + 
					e.toString());
		}
		/* Not extracting other types of properties for 1702
		 try {
			
			for (Entry<String, Object> e : group.getProperties().entrySet()) {
				
				String k = e.getKey();
				if ( e.getValue() != null ) {
					LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " property: " + 
							k + "=" + e.getValue());
					LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " V class name: " +
							 e.getValue().getClass().getName());
					 Class<?>[] interfaces = e.getValue().getClass().getInterfaces();
					 
					 for(Class<?> ifc: interfaces ){
						 LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " ifc name: " +
								 ifc.getName());
				     }
					 
					// only extract get_input for now
					for (Entry<String, String> entry : v.entrySet()) {
						// only include get_input for now
						if ( ModelConstants.GET_INPUT_TAG.equalsIgnoreCase ( entry.getKey() ) ) {
							CommandProperty cp = new CommandProperty();
							cp.setDisplayName(entry.getValue());
							cp.setCommand(entry.getKey());
							cp.setInputName(k);
							(vfModule.getCommands()).put(k,cp);
						}
					} 
				}
			}
		}
		catch ( Exception e ) {
			LOG.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + methodName + " Unable to parse VF Module properties: e=" + 
					e.toString());
		}*/
		return vfModule;
	}
}
