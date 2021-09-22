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

package org.onap.vid.aai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerSpecificServiceInstance {
	
	@JsonProperty("service-instance-id")
	public String serviceInstanceId;

	@JsonProperty("service-instance-name")
	public String serviceInstanceName;

	@JsonProperty("orchestration-status")
	public String orchestrationStatus;

	@JsonProperty("model-invariant-id")
	public String modelInvariantId;

	@JsonProperty("model-version-id")
	public String modelVersionId;

	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public String getServiceInstanceName() {
		return serviceInstanceName;
	}

	public void setServiceInstanceName(String serviceInstanceName) {
		this.serviceInstanceName = serviceInstanceName;
	}

	public String getOrchestrationStatus() {
		return orchestrationStatus;
	}

	public void setOrchestrationStatus(String orchestrationStatus) {
		this.orchestrationStatus = orchestrationStatus;
	}

	public String getModelInvariantId() {
		return modelInvariantId;
	}

	public void setModelInvariantId(String modelInvariantId) {
		this.modelInvariantId = modelInvariantId;
	}

	public String getModelVersionId() {
		return modelVersionId;
	}

	public void setModelVersionId(String modelVersionId) {
		this.modelVersionId = modelVersionId;
	}

	public String getSelfLink() {
		return selfLink;
	}

	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
	}

	public String getServiceRole() {
		return serviceRole;
	}

	public void setServiceRole(String serviceRole) {
		this.serviceRole = serviceRole;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getEnvironmentContext() {
		return environmentContext;
	}

	public void setEnvironmentContext(String environmentContext) {
		this.environmentContext = environmentContext;
	}

	public String getWorkloadContext() {
		return workloadContext;
	}

	public void setWorkloadContext(String workloadContext) {
		this.workloadContext = workloadContext;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	@JsonProperty("selflink")
	public String selfLink;

	@JsonProperty("service-role")
	public String serviceRole;

	@JsonProperty("service-type")
	public String serviceType;

	@JsonProperty("environment-context")
	public String environmentContext;

	@JsonProperty("workload-context")
	public String workloadContext;

	@JsonProperty("resource-version")
	public String resourceVersion;

}
