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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import java.util.Objects;

public class WorkflowsDetail {

	public WorkflowsDetail() {
	}

	public WorkflowsDetail(VnfDetails vnfDetails, String workflowName) {
		this.vnfDetails = vnfDetails;
		this.workflowName = workflowName;
	}

	@JsonProperty("vnfDetails")
	private VnfDetails vnfDetails;
	
	@JsonProperty("workflowName")
	private String workflowName;

	@JsonProperty("vnfDetails")
	public VnfDetails getVnfDetails() {
		return vnfDetails;
	}
	@JsonProperty("vnfDetails")
	public void setVnfDetails(VnfDetails vnfDetails) {
		this.vnfDetails = vnfDetails;
	}
	@JsonProperty("workflowName")
	public String getWorkflowName() {
		return workflowName;
	}
	@JsonProperty("workflowName")
	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	@Override
	public String toString() {
		return vnfDetails +
				", workflowName='" + workflowName;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		WorkflowsDetail detail = (WorkflowsDetail) o;
		return Objects.equals(this.vnfDetails, detail.vnfDetails) &&
			Objects.equals(this.workflowName, detail.workflowName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.vnfDetails, this.workflowName);
	}
}
