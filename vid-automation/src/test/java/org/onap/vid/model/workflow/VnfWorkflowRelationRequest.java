package org.onap.vid.model.workflow;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

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
	
	

}
