/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.onap.vid.asdc.beans.tosca.Input;

import java.util.Map;
import java.util.UUID;

/**
 * The Class Service.
 */
public class Service {

	/** The uuid. */
	private String uuid;

	/** The invariant uuid. */
	private String invariantUuid;

	/** The name. */
	private String name;

	/** The version. */
	private String version;

	/** The tosca model URL. */
	private String toscaModelURL;

	/** The category. */
	private String category;

	/** The Service Type. */
	private String serviceType;

	/** The Service Role */
	private String serviceRole;

	/** The description. */
	private String description;

	/** The service ecomp naming flag */
	private String serviceEcompNaming;

	private String instantiationType;


	/** The inputs. */
	private Map<String, Input> inputs;

	private VidNotions vidNotions;

	/**
	 * Gets the uuid.
	 *
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * Gets the invariant uuid.
	 *
	 * @return the invariant uuid
	 */
	public String getInvariantUuid() {
		return invariantUuid;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Gets the tosca model URL.
	 *
	 * @return the tosca model URL
	 */
	public String getToscaModelURL() {
		return toscaModelURL;
	}

	/**
	 * Gets the category.
	 *
	 * @return the category
	 */
	public String getCategory() {
		return category;
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
	 * Gets the inputs.
	 *
	 * @return the inputs
	 */
	public Map<String, Input> getInputs() {
		return inputs;
	}
	/**
	 * Get the serviceEcompNaming value
	 *
	 * @return serviceEcompNaming
	 */
	public String getServiceEcompNaming() {
		return serviceEcompNaming;
	}


	public String getInstantiationType() { return instantiationType; }

	public void setInstantiationType(String instantiationType) { this.instantiationType = instantiationType; }
	/**
	 * Sets the uuid.
	 *
	 * @param uuid the new uuid
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	/**
	 * Sets the invariant uuid.
	 *
	 * @param invariantUuid the new invariant uuid
	 */
	public void setInvariantUuid(String invariantUuid) {
		this.invariantUuid = invariantUuid;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the version.
	 *
	 * @param version the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Sets the tosca model URL.
	 *
	 * @param toscaModelURL the new tosca model URL
	 */
	public void setToscaModelURL(String toscaModelURL) {
		this.toscaModelURL = toscaModelURL;
	}

	/**
	 * Sets the category.
	 *
	 * @param category the new category
	 */
	public void setCategory(String category) {
		this.category = category;
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
	 * Sets the inputs.
	 *
	 * @param inputs the inputs
	 */
	public void setInputs(Map<String, Input> inputs) {
		this.inputs = inputs;
	}
	/**
	 * Sets the service ecomp naming.
	 *
	 * @param serviceEcompNaming the new service ecomp naming
	 */
	public void setServiceEcompNaming(String serviceEcompNaming) {
		this.serviceEcompNaming = serviceEcompNaming;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return UUID.fromString(getUuid()).hashCode();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this)
            return true;
		if (!(o instanceof Service))
            return false;

		final Service service = (Service) o;

		return (service.getUuid().equals(getUuid()));
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceRole() {
		return serviceRole;
	}

	public void setServiceRole(String serviceRole) {
		this.serviceRole = serviceRole;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	public VidNotions getVidNotions() {
		return vidNotions;
	}

	public void setVidNotions(VidNotions vidNotions) {
		this.vidNotions = vidNotions;
	}
}
