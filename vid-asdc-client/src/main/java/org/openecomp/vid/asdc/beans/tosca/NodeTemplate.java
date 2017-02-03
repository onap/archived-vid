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

import java.util.Map;

/**
 * The Class NodeTemplate.
 */
public class NodeTemplate {

	/** The type. */
	private String type;
	
	/** The metadata. */
	private ToscaMetadata metadata;
	
	/** The properties. */
	private Map<String, Object> properties; //HEAT?
	
	/** The requirements. */
	private Object requirements;
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
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
	 * Gets the metadata.
	 *
	 * @return the metadata
	 */
	public ToscaMetadata getMetadata() {
		return metadata;
	}
	
	/**
	 * Sets the metadata.
	 *
	 * @param metadata the new metadata
	 */
	public void setMetadata(ToscaMetadata metadata) {
		this.metadata = metadata;
	}
	
	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	/**
	 * Sets the properties.
	 *
	 * @param properties the properties
	 */
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	
	/**
	 * Gets the requirements.
	 *
	 * @return the requirements
	 */
	public Object getRequirements() {
		return requirements;
	}
	
	/**
	 * Sets the requirements.
	 *
	 * @param requirements the new requirements
	 */
	public void setRequirements(Object requirements) {
		this.requirements = requirements;
	}
}
