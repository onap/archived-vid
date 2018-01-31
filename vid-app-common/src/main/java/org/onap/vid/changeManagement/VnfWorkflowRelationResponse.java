package org.onap.vid.changeManagement;

import org.onap.vid.model.ListOfErrorsResponse;

import java.util.List;

public class VnfWorkflowRelationResponse extends ListOfErrorsResponse {

	public VnfWorkflowRelationResponse() {
	}

	public VnfWorkflowRelationResponse(List<String> errors) {
		super(errors);
	}
}
