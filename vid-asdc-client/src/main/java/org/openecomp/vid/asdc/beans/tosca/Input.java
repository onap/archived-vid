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

// TODO: Auto-generated Javadoc
/**
 * The Class Input.
 */
public class Input {

	/** The type. */
	private String type;
	
	/** The description. */
	private String description;
	
	/** The default. */
	private Object _default;
	
	/** The entry schema. */
	private Input entry_schema;
	
	/**
	 * Instantiates a new input.
	 */
	public Input() {}
	
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
	 * Gets the default.
	 *
	 * @return the default
	 */
	public Object getDefault() {
		return _default;
	}
	
	/**
	 * Sets the default.
	 *
	 * @param _default the new default
	 */
	public void setDefault(Object _default) {
		this._default = _default;
	}
	
	/**
	 * Gets the entry schema.
	 *
	 * @return the entry schema
	 */
	public Input getentry_schema() {
		return entry_schema;
	}
	
	/**
	 * Sets the entry schema.
	 *
	 * @param entry_schema the new entry schema
	 */
	public void setentry_schema(Input entry_schema) {
		this.entry_schema = entry_schema;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "type=" + type + ",description=" + description + ",default=" + _default;
	}
}
