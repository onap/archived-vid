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

/**
 * The Class VolumeGroup.
 */
public class VolumeGroup extends org.onap.vid.model.Group {



	/**
	 * Instantiates a new volume group.
	 */
	public VolumeGroup() {}

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */

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
		volumeGroup.setProperties(VolumeGroup.extractPropertiesForGroup(group));
		return volumeGroup;
	}

}
