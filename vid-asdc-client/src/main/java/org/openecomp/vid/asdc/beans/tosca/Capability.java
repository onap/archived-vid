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

import java.util.Collection;
import java.util.Map;

/**
 * The Class Capability.
 */
public class Capability {

	/** The type. */
	private String type; //FIXME: Make an enumeration?
	
	/** The description. */
	private String description;
	
	/** The occurrences. */
	private Collection<String> occurrences; //FIXME: Make an enumeration?
	
	/** The properties. */
	private Map<String, Property> properties;
	
	/** The valid source types. */
	private Collection<String> valid_source_types; //FIXME: Make an enumeration?
	
	/**
	 * Instantiates a new capability.
	 */
	public Capability() {}
	
	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
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
	 * Gets the occurrences.
	 *
	 * @return the occurrences
	 */
	public Collection<String> getOccurrences() {
		return occurrences;
	}
	
	/**
	 * Gets the properties.
	 *
	 * @return the properties
	 */
	public Map<String, Property> getProperties() {
		return properties;
	}
	
	/**
	 * Gets the valid source types.
	 *
	 * @return the valid source types
	 */
	public Collection<String> getValid_source_types() {
		return valid_source_types;
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
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Sets the occurrences.
	 *
	 * @param occurrences the new occurrences
	 */
	public void setOccurrences(Collection<String> occurrences) {
		this.occurrences = occurrences;
	}
	
	/**
	 * Sets the properties.
	 *
	 * @param properties the properties
	 */
	public void setProperties(Map<String, Property> properties) {
		this.properties = properties;
	}
	
	/**
	 * Sets the valid source types.
	 *
	 * @param valid_source_types the new valid source types
	 */
	public void setValid_source_types(Collection<String> valid_source_types) {
		this.valid_source_types = valid_source_types;
	}
}
