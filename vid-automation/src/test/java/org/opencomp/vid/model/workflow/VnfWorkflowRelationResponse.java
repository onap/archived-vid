package org.opencomp.vid.model.workflow;


import java.util.List;

public class VnfWorkflowRelationResponse extends ListOfErrorsResponse {

	public VnfWorkflowRelationResponse() {
	}

	public VnfWorkflowRelationResponse(List<String> errors) {
		super(errors);
	}
}
