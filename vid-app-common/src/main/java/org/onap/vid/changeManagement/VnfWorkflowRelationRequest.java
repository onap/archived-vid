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

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class VnfWorkflowRelationRequest {

	public VnfWorkflowRelationRequest() {
	}

	public VnfWorkflowRelationRequest(List<WorkflowsDetail> workflowsDetails) {
		this.workflowsDetails = workflowsDetails;
	}

	@JsonProperty("workflowsDetails")
	private List<WorkflowsDetail> workflowsDetails;
	
	@JsonProperty("workflowsDetails")
	public List<WorkflowsDetail> getWorkflowsDetails() {
		return workflowsDetails;
	}
	
	@JsonProperty("workflowsDetails")
	public void setWorkflowsDetails(List<WorkflowsDetail> workflowsDetails) {
		this.workflowsDetails = workflowsDetails;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		VnfWorkflowRelationRequest request = (VnfWorkflowRelationRequest) o;
		return Objects.equals(this.workflowsDetails, request.workflowsDetails);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.workflowsDetails);
	}
}
