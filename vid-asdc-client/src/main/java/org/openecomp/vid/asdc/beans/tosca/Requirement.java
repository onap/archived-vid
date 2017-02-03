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

/**
 * The Class Requirement.
 */
public class Requirement {

	/** The occurrences. */
	private Collection<String> occurrences;
	
	/** The capability. */
	private String capability;
	
	/** The node. */
	private String node;
	
	/** The relationship. */
	private String relationship;
	
	/**
	 * Instantiates a new requirement.
	 */
	private Requirement() {}
	
	/**
	 * Gets the occurrences.
	 *
	 * @return the occurrences
	 */
	public Collection<String> getOccurrences() {
		return occurrences;
	}
	
	/**
	 * Gets the capability.
	 *
	 * @return the capability
	 */
	public String getCapability() {
		return capability;
	}
	
	/**
	 * Gets the node.
	 *
	 * @return the node
	 */
	public String getNode() {
		return node;
	}
	
	/**
	 * Gets the relationship.
	 *
	 * @return the relationship
	 */
	public String getRelationship() {
		return relationship;
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
	 * Sets the capability.
	 *
	 * @param capability the new capability
	 */
	public void setCapability(String capability) {
		this.capability = capability;
	}
	
	/**
	 * Sets the node.
	 *
	 * @param node the new node
	 */
	public void setNode(String node) {
		this.node = node;
	}
	
	/**
	 * Sets the relationship.
	 *
	 * @param relationship the new relationship
	 */
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	
	
}
