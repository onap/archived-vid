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
 * The Class TopologyTemplate.
 */
public class TopologyTemplate {
	
	/** The substitution mappings. */
	private SubstitutionMappings substitution_mappings;
	
	/** The inputs. */
	private Map<String, Input> inputs;
	
	/** The node templates. */
	private Map<String, NodeTemplate> node_templates;
	
	/** The groups. */
	private Map<String, Group> groups;
	
	/**
	 * Instantiates a new topology template.
	 */
	public TopologyTemplate() {
		substitution_mappings = new SubstitutionMappings();
		inputs = new HashMap<String, Input> ();
		node_templates = new HashMap<String, NodeTemplate> ();
		groups = new HashMap<String, Group> ();
	}
	
	/**
	 * Gets the substitution mappings.
	 *
	 * @return the substitution mappings
	 */
	public SubstitutionMappings getsubstitution_mappings() {
		return this.substitution_mappings;
	}
	
	/**
	 * Sets the substitution mappings.
	 *
	 * @param substitution_mappings the new substitution mappings
	 */
	public void setsubstitution_mappings(SubstitutionMappings substitution_mappings) {
		this.substitution_mappings = substitution_mappings;
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
	 * Sets the inputs.
	 *
	 * @param inputs the inputs
	 */
	public void setInputs(Map<String, Input> inputs) {
		this.inputs = inputs;
	}
	
	/**
	 * Gets the node templates.
	 *
	 * @return the node templates
	 */
	public Map<String, NodeTemplate> getnode_templates() {
		return node_templates;
	}
	
	/**
	 * Setnode templates.
	 *
	 * @param node_templates the node templates
	 */
	public void setnode_templates(Map<String, NodeTemplate> node_templates) {
		this.node_templates = node_templates;
	}

	/**
	 * Gets the groups.
	 *
	 * @return the groups
	 */
	public Map<String, Group> getGroups() {
		return groups;
	}

	/**
	 * Sets the groups.
	 *
	 * @param groups the groups
	 */
	public void setGroups(Map<String, Group> groups) {
		this.groups = groups;
	}
}
