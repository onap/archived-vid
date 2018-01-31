package org.onap.vid.changeManagement;

import java.util.List;

public class GetWorkflowsResponse {
	private List<String> workflows;

	public GetWorkflowsResponse() {
	}

	public GetWorkflowsResponse(List<String> workflows) {
		this.workflows = workflows;
	}

	public List<String> getWorkflows() {
		return workflows;
	}

	public void setWorkflows(List<String> workflows) {
		this.workflows = workflows;
	}
	

}
