package org.onap.vid.changeManagement;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
    "requestDetails",
		"requestType"
})

public class ChangeManagementRequest {

	public static class MsoChangeManagementRequest {
		public final static String SOFTWARE_UPDATE = "inPlaceSoftwareUpdate";
		public static final String REPLACE = "replace";
		public final static String CONFIG_UPDATE = "applyUpdatedConfig";

	}

	public final static String VNF_IN_PLACE_SOFTWARE_UPDATE = "vnf in place software update";
	public static final String UPDATE = "update";
	public static final String REPLACE = "replace";
	public final static String CONFIG_UPDATE = "vnf config update";
	public final static String SCALE_OUT = "vnf scale out";

	@JsonProperty("requestDetails")
    private List<RequestDetails> requestDetails;

	@JsonProperty("requestType")
    private String requestType;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    
	@JsonProperty("requestDetails")
	public List<RequestDetails> getRequestDetails() {
		return requestDetails;
	}

	@JsonProperty("requestDetails")
	public void setRequestDetails(List<RequestDetails> requestDetails) {
		this.requestDetails = requestDetails;
	}

	@JsonProperty("requestType")
	public String getRequestType() {
		return requestType;
	}

	@JsonProperty("requestType")
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
	return this.additionalProperties;
	}


	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
	this.additionalProperties.put(name, value);
	}

	
	
}
