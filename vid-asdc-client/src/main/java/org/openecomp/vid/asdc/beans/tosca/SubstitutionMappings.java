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

import java.util.HashMap;
import java.util.Map;

/**
 * The Class SubstitutionMappings.
 */
public class SubstitutionMappings {

	/** The node type. */
	private String node_type;
	
	/** The capabilities. */
	private Map<String, Capability> capabilities;
	
	/** The requirements. */
	private Map<String, Requirement> requirements;
	
	/**
	 * Instantiates a new substitution mappings.
	 */
	public SubstitutionMappings() {
		capabilities = new HashMap<String, Capability> ();
		requirements = new HashMap<String, Requirement> ();
	}
	
	/**
	 * Gets the node type.
	 *
	 * @return the node type
	 */
	public String getnode_type() {
		return node_type;
	}
	
	/**
	 * Sets the node type.
	 *
	 * @param node_type the new node type
	 */
	public void setnode_type(String node_type) {
		this.node_type = node_type;
	}

	/**
	 * Gets the capabilities.
	 *
	 * @return the capabilities
	 */
	public Map<String, Capability> getCapabilities() {
		return capabilities;
	}

	/**
	 * Sets the capabilities.
	 *
	 * @param capabilities the capabilities
	 */
	public void setCapabilities(Map<String, Capability> capabilities) {
		this.capabilities = capabilities;
	}

	/**
	 * Gets the requirements.
	 *
	 * @return the requirements
	 */
	public Map<String, Requirement> getRequirements() {
		return requirements;
	}

	/**
	 * Sets the requirements.
	 *
	 * @param requirements the requirements
	 */
	public void setRequirements(Map<String, Requirement> requirements) {
		this.requirements = requirements;
	}
}
