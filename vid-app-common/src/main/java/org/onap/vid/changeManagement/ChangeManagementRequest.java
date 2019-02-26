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

package org.onap.vid.changeManagement;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({
    "requestDetails",
		"requestType"
})

public class ChangeManagementRequest {

	public static class MsoChangeManagementRequest {
		public static final String SOFTWARE_UPDATE = "inPlaceSoftwareUpdate";
		public static final String REPLACE = "replace";
		public static final String CONFIG_UPDATE = "applyUpdatedConfig";

	}

	public static final String VNF_IN_PLACE_SOFTWARE_UPDATE = "vnf in place software update";
	public static final String UPDATE = "update";
	public static final String REPLACE = "replace";
	public static final String CONFIG_UPDATE = "vnf config update";
	public static final String SCALE_OUT = "vnf scale out";

	@JsonProperty("requestDetails")
    private List<RequestDetails> requestDetails;

	@JsonProperty("requestType")
    private String requestType;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();
    
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

	
	
}
