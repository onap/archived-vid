/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2019 IBM.
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

package org.onap.vid.asdc.beans.tosca;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class SubstitutionMappings.
 */
public class SubstitutionMappings {

	/** The node type. */
	private String nodeType;
	
	/** The capabilities. */
	private Map<String, Object> capabilities;
	
	/** The requirements. */
	private Map<String, Object> requirements;
	
	/**
	 * Instantiates a new substitution mappings.
	 */
	public SubstitutionMappings() {
		capabilities = new HashMap<> ();
		requirements = new HashMap<> ();
	}
	
	/**
	 * Gets the node type.
	 *
	 * @return the node type
	 */
	public String getnode_type() {
		return nodeType;
	}
	
	/**
	 * Sets the node type.
	 *
	 * @param node_type the new node type
	 */
	public void setnode_type(String nodeType) {
		this.nodeType = nodeType;
	}

	/**
	 * Gets the capabilities.
	 *
	 * @return the capabilities
	 */
	public Map<String, Object> getCapabilities() {
		return capabilities;
	}

	/**
	 * Sets the capabilities.
	 *
	 * @param capabilities the capabilities
	 */
	public void setCapabilities(Map<String, Object> capabilities) {
		this.capabilities = capabilities;
	}

	/**
	 * Gets the requirements.
	 *
	 * @return the requirements
	 */
	public Map<String, Object> getRequirements() {
		return requirements;
	}

	/**
	 * Sets the requirements.
	 *
	 * @param requirements the requirements
	 */
	public void setRequirements(Map<String, Object> requirements) {
		this.requirements = requirements;
	}
}
