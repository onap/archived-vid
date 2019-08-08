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

import com.fasterxml.jackson.annotation.JsonInclude;
import org.onap.sdc.toscaparser.api.Property;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Input.
 */
public class Input {
	
	/** The entry schema. */
	private Input entry_schema;
  
        /** The type. */
	private String type;
	
	/** The description. */
	private String description;
	
	/** The default. */
	private Object _default;

	private InputProperties inputProperties;

	private String fromInputName;

	/** The constraints */
	private List<org.onap.sdc.toscaparser.api.elements.constraints.Constraint> constraints;
	
	/** The required field. If not set, the default is true */
	private boolean required = true;
	
	/** Details the inputs template */
	private String templateName;
	private String templateUUID;
	private String templateInvariantUUID;
	private String templateCustomizationUUID;

	public Input(org.onap.sdc.toscaparser.api.parameters.Input input, List<Property> properties){
		this.type = input.getType();
		this.description = input.getDescription();
		this._default = input.getDefault();
		this.inputProperties = new InputProperties(properties);
		this.fromInputName = input.getName();
	}

	/**
	 * Instantiates a new input.
	 */
	public Input() {
		constraints = new ArrayList<>();
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
	 * Gets the required field.
	 *
	 * @return the required field
	 */
	public boolean getRequired() {
		return required;
	}
	/**
	 * Sets the required value.
	 *
	 * @param required the new required value
	 */
	public void setRequired(boolean required) {
		this.required = required;
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
	 */
	public void setentry_schema(Input s) {
		this.entry_schema = s;
	}

	public InputProperties getInputProperties() {
		return inputProperties;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public String getFromInputName() {
		return fromInputName;
	}

	public void setInputProperties(InputProperties inputProperties) {
		this.inputProperties = inputProperties;
	}
	/**
	 * Sets the constraints.
	 *
	 * @param c the new constraints
	 */
	public void setConstraints(List<org.onap.sdc.toscaparser.api.elements.constraints.Constraint> c) {
		this.constraints = c;
	}
	/**
	 * Gets the constraints
	 *
	 * @return the constraints
	 */
	public List<org.onap.sdc.toscaparser.api.elements.constraints.Constraint> getConstraints() {
		return constraints;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "type=" + type + ",description=" + description + ",default=" + _default;
	}

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateUUID() {
        return templateUUID;
    }

    public void setTemplateUUID(String templateUUID) {
        this.templateUUID = templateUUID;
    }

    public String getTemplateInvariantUUID() {
        return templateInvariantUUID;
    }

    public void setTemplateInvariantUUID(String templateInvariantUUID) {
        this.templateInvariantUUID = templateInvariantUUID;
    }

    public String getTemplateCustomizationUUID() {
        return templateCustomizationUUID;
    }

    public void setTemplateCustomizationUUID(String templateCustomizationUUID) {
        this.templateCustomizationUUID = templateCustomizationUUID;
    }
}
