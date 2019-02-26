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
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"source",
"suppressRollback",
"requestorId"
})
public class RequestInfo {
	@JsonProperty("source")
	private String source;
	@JsonProperty("suppressRollback")
	private Boolean suppressRollback;
	@JsonProperty("requestorId")
	private String requestorId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("source")
	public String getSource() {
	return source;
	}

	@JsonProperty("source")
	public void setSource(String source) {
	this.source = source;
	}

	@JsonProperty("suppressRollback")
	public Boolean getSuppressRollback() {
	return suppressRollback;
	}

	@JsonProperty("suppressRollback")
	public void setSuppressRollback(Boolean suppressRollback) {
	this.suppressRollback = suppressRollback;
	}

	@JsonProperty("requestorId")
	public String getRequestorId() {
	return requestorId;
	}

	@JsonProperty("requestorId")
	public void setRequestorId(String requestorId) {
	this.requestorId = requestorId;
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
