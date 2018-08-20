package org.onap.vid.model.mso;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"modelInfo",
"cloudConfiguration",
"requestInfo",
"requestParameters",
"vnfName",
"vnfInstanceId"
})
public class ChangeManagementRequestDetails extends RequestDetails{

	@JsonProperty("vnfName")
    private String vnfName;
	@JsonProperty("vnfInstanceId")
    private String vnfInstanceId;

	@JsonProperty("vnfName")
	public String getVnfName() {
		return vnfName;
	}

	@JsonProperty("vnfName")
	public void setVnfName(String vnfName) {
		this.vnfName = vnfName;
	}
	@JsonProperty("vnfInstanceId")
	public String getVnfInstanceId() {
		return vnfInstanceId;
	}

	@JsonProperty("vnfInstanceId")
	public void setVnfInstanceId(String vnfInstanceId) {
		this.vnfInstanceId = vnfInstanceId;
	}
}
