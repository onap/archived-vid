/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright © 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright 2018 Nokia
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

package org.onap.vid.changeManagement;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@JsonPropertyOrder({
    "requestDetails",
		"requestType"
})

public class ChangeManagementRequest {

	public static class MsoChangeManagementRequest {
		public final static String SOFTWARE_UPDATE = "inPlaceSoftwareUpdate";
		public static final String REPLACE = "replace";
		public final static String CONFIG_UPDATE = "applyUpdatedConfig";

	}

	public final static String VNF_IN_PLACE_SOFTWARE_UPDATE = "vnf in place software update";
	public static final String UPDATE = "update";
	public static final String REPLACE = "replace";
	public final static String CONFIG_UPDATE = "vnf config update";
	public final static String SCALE_OUT = "vnf scale out";

	@JsonProperty("requestDetails")
    private List<RequestDetails> requestDetails;

	@JsonProperty("requestType")
    private String requestType;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
	@JsonProperty("requestDetails")
	public List<RequestDetails> getRequestDetails() {
		return requestDetails;
	}

	@JsonProperty("requestDetails")
	public void setRequestDetails(List<RequestDetails> requestDetails) {
		this.requestDetails = requestDetails;
	}

	@JsonProperty("requestType")
	public String getRequestType() {
		return requestType;
	}

	@JsonProperty("requestType")
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}


	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || this.getClass() != other.getClass()) {
			return false;
		}
		ChangeManagementRequest request = (ChangeManagementRequest) other;

		return new EqualsBuilder()
			.append(getRequestDetails(), request.getRequestDetails())
			.append(getRequestType(), request.getRequestType())
			.append(getAdditionalProperties(), request.getAdditionalProperties())
			.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
			.append(getRequestDetails())
			.append(getRequestType())
			.append(getAdditionalProperties())
			.toHashCode();
	}
}