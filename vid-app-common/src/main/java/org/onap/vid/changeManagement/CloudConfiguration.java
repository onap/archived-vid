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
"lcpCloudRegionId",
"tenantId"
})
public class CloudConfiguration {
	@JsonProperty("lcpCloudRegionId")
	private String lcpCloudRegionId;
	@JsonProperty("tenantId")
	private String tenantId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("lcpCloudRegionId")
	public String getLcpCloudRegionId() {
	return lcpCloudRegionId;
	}

	@JsonProperty("lcpCloudRegionId")
	public void setLcpCloudRegionId(String lcpCloudRegionId) {
	this.lcpCloudRegionId = lcpCloudRegionId;
	}

	@JsonProperty("tenantId")
	public String getTenantId() {
	return tenantId;
	}

	@JsonProperty("tenantId")
	public void setTenantId(String tenantId) {
	this.tenantId = tenantId;
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
