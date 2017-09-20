package org.openecomp.vid.changeManagement;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"source",
"suppressRollback",
"requestorId"
})
public class RequestInfo {
	@JsonProperty("source")
	private String source;
	@JsonProperty("suppressRollback")
	private Boolean suppressRollback;
	@JsonProperty("requestorId")
	private String requestorId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("source")
	public String getSource() {
	return source;
	}

	@JsonProperty("source")
	public void setSource(String source) {
	this.source = source;
	}

	@JsonProperty("suppressRollback")
	public Boolean getSuppressRollback() {
	return suppressRollback;
	}

	@JsonProperty("suppressRollback")
	public void setSuppressRollback(Boolean suppressRollback) {
	this.suppressRollback = suppressRollback;
	}

	@JsonProperty("requestorId")
	public String getRequestorId() {
	return requestorId;
	}

	@JsonProperty("requestorId")
	public void setRequestorId(String requestorId) {
	this.requestorId = requestorId;
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
