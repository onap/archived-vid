package org.openecomp.vid.changeManagement;

import org.openecomp.vid.model.ListOfErrorsResponse;

import java.util.List;

public class VnfWorkflowRelationResponse extends ListOfErrorsResponse {

	public VnfWorkflowRelationResponse() {
	}

	public VnfWorkflowRelationResponse(List<String> errors) {
		super(errors);
	}
}
