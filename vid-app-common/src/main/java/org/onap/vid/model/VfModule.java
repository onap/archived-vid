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

import org.onap.vid.asdc.beans.tosca.Group;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class VfModule.
 */
public class VfModule extends org.onap.vid.model.Group {

	/** The Constant LOG. */
	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VfModule.class);
	
	/** The Constant dateFormat. */
	static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	public static final String VOLUME_GROUP = "volume_group";

	/** The volume group allowed. */
	private boolean volumeGroupAllowed;

	/**
	 * Checks if is volume group allowed.
	 *
	 * @return true, if is volume group allowed
	 */
	public boolean isVolumeGroupAllowed() {
		return volumeGroupAllowed;
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
			vfModule.setProperties(VfModule.extractPropertiesForGroup(group));

			if (group.getProperties().containsKey(VOLUME_GROUP)) {
				if (group.getProperties().get(VOLUME_GROUP) != null) {
				
					Class<?> c = group.getProperties().get(VOLUME_GROUP).getClass();
					LOG.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + methodName + " class name=" +
							c.getName());
					
					if ( c.getName().equalsIgnoreCase(Boolean.class.getName()) ) {
						Boolean b = (Boolean)group.getProperties().get(VOLUME_GROUP);
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
			return vfModule;
	}


}
