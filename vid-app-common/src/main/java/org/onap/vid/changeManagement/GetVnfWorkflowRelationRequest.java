package org.onap.vid.changeManagement;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetVnfWorkflowRelationRequest {

	public GetVnfWorkflowRelationRequest() {
	}

	public GetVnfWorkflowRelationRequest(List<VnfDetails> vnfsDetails) {
		this.vnfsDetails = vnfsDetails;
	}

	@JsonProperty("vnfsDetails")
	private List<VnfDetails>  vnfsDetails;
	
	@JsonProperty("vnfsDetails")
	public List<VnfDetails> getVnfDetails() {
		return vnfsDetails;
	}
	
	@JsonProperty("vnfsDetails")
	public void setVnfDetails(List<VnfDetails> vnfDetails) {
		this.vnfsDetails = vnfDetails;
	}
}
