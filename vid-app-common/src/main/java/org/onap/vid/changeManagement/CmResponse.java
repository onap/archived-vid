package org.onap.vid.changeManagement;

public class CmResponse {
	
	public String orchestratorRequestId;
	public String serviceInstanceId;
	public String vnfInstanceId;
	public String vnfName;
	
	public CmResponse(String vnfName){
		this.orchestratorRequestId = "Request Id";
		this.serviceInstanceId = "Service instance Id";
		this.vnfInstanceId = "Vnf instance Id";
		this.vnfName = vnfName;
	}

}
