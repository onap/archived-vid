package org.onap.vid.changeManagement;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"modelInfo",
"cloudConfiguration",
"requestInfo",
"requestParameters",
"vnfName",
"vnfInstanceId"
})
public class RequestDetails extends org.onap.vid.mso.rest.RequestDetails{

	@JsonProperty("vnfName")
    private String vnfName;
	@JsonProperty("vnfInstanceId")
    private String vnfInstanceId;

	/** The related model list. */
	@JsonProperty("relatedInstanceList")
	public List<RelatedInstanceList> relatedInstList;

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

	@JsonGetter
	public List<RelatedInstanceList> getRelatedInstList() {
		return relatedInstList;
	}

	@JsonSetter
	public void setRelatedInstList(List<RelatedInstanceList> relatedInstList) {
		this.relatedInstList = relatedInstList;
	}

}
