package org.onap.vid.changeManagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

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
}
