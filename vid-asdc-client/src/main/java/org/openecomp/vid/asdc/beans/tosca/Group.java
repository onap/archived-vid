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
 * The Class Group.
 */
public class Group {

	/** The type. */
	private String type;
	
	/** The members. */
	private Collection<String> members;
	
	/** The metadata. */
	private ToscaMetadata metadata;
	
	/** The vf module type. */
	private String vf_module_type;
	
	/** The properties. */
	private Map<String, String> properties;
	
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
	 * Gets the members.
	 *
	 * @return the members
	 */
	public Collection<String> getMembers() {
		return members;
	}
	
	/**
	 * Sets the members.
	 *
	 * @param members the new members
	 */
	public void setMembers(Collection<String> members) {
		this.members = members;
	}
	
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
	 * Gets the vf module type.
	 *
	 * @return the vf module type
	 */
	public String getvf_module_type() {
		return vf_module_type;
	}
	
	/**
	 * Sets the vf module type.
	 *
	 * @param vf_module_type the new vf module type
	 */
	public void setvf_module_type(String vf_module_type) {
		this.vf_module_type = vf_module_type;
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
	 * Sets the properties.
	 *
	 * @param properties the properties
	 */
	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
