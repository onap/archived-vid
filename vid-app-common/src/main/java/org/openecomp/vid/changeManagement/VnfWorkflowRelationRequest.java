package org.openecomp.vid.changeManagement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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
