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
import java.util.LinkedList;
import java.util.Map;

/**
 * The Class ToscaModel.
 */
public class ToscaModel {

	/** The tosca definitions version. */
	private String tosca_definitions_version;
	
	/** The description. */
	private String description;
	
	/** The metadata. */
	private ToscaMetadata metadata;
	
	/** The topology template. */
	private TopologyTemplate topology_template;
	
	/** The imports. */
	private Collection<Map<String, Map<String, String>>> imports;
	
	/** The node types. */
	private Map<String, Object> node_types;
	
	/**
	 * Instantiates a new tosca model.
	 */
	public ToscaModel() {
		metadata = new ToscaMetadata();
		topology_template = new TopologyTemplate();
		imports = new LinkedList<Map<String, Map<String, String>>> ();
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
	 * Gets the tosca definitions version.
	 *
	 * @return the tosca definitions version
	 */
	public String gettosca_definitions_version() {
		return tosca_definitions_version;
	}
	
	/**
	 * Sets the tosca definitions version.
	 *
	 * @param tosca_definitions_version the new tosca definitions version
	 */
	public void settosca_definitions_version(String tosca_definitions_version) {
		this.tosca_definitions_version = tosca_definitions_version;
	}
	
	/**
	 * Gets the topology template.
	 *
	 * @return the topology template
	 */
	public TopologyTemplate gettopology_template() {
		return topology_template;
	}
	
	/**
	 * Sets the topology template.
	 *
	 * @param topology_template the new topology template
	 */
	public void settopology_template(TopologyTemplate topology_template) {
		this.topology_template = topology_template;
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
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the imports.
	 *
	 * @return the imports
	 */
	public Collection<Map<String, Map<String, String>>> getImports() {
		return imports;
	}

	/**
	 * Sets the imports.
	 *
	 * @param imports the imports
	 */
	public void setImports(Collection<Map<String, Map<String, String>>> imports) {
		this.imports = imports;
	}
	
	/**
	 * Gets the node types.
	 *
	 * @return the node types
	 */
	public Map<String, Object> getnode_types() {
		return node_types;
	}
	
	/**
	 * Setnode types.
	 *
	 * @param node_types the node types
	 */
	public void setnode_types(Map<String, Object> node_types) { 
		this.node_types = node_types;
	}
}
