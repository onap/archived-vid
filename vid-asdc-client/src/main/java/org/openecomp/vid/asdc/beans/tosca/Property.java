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

/**
 * The Class Property.
 */
public class Property {

	/** The type. */
	private String type;
	
	/** The description. */
	private String description;
	
	/** The entry schema. */
	private Schema entry_schema;
	
	/** The default. */
	private String _default;
	
	/** The required. */
	private boolean required;
	
	/**
	 * Instantiates a new property.
	 */
	private Property() {}
	
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
	 * Gets the entry schema.
	 *
	 * @return the entry schema
	 */
	public Schema getEntry_schema() {
		return entry_schema;
	}

	/**
	 * Gets the default.
	 *
	 * @return the default
	 */
	public String get_default() {
		return _default;
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
	 * Sets the entry schema.
	 *
	 * @param entry_schema the new entry schema
	 */
	public void setEntry_schema(Schema entry_schema) {
		this.entry_schema = entry_schema;
	}

	/**
	 * Sets the default.
	 *
	 * @param _default the new default
	 */
	public void set_default(String _default) {
		this._default = _default;
	}

	/**
	 * Gets the default.
	 *
	 * @return the default
	 */
	public String getDefault() {
		return _default;
	}
	
	/**
	 * Checks if is required.
	 *
	 * @return true, if is required
	 */
	public boolean isRequired() {
		return required;
	}
	
	/**
	 * Sets the default.
	 *
	 * @param _default the new default
	 */
	public void setDefault(String _default) {
		this._default = _default;
	}
	
	/**
	 * Sets the required.
	 *
	 * @param required the new required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	
}
