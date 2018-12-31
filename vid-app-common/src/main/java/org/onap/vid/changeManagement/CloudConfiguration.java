package org.onap.vid.changeManagement;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"lcpCloudRegionId",
"tenantId"
})
public class CloudConfiguration {
	@JsonProperty("lcpCloudRegionId")
	private String lcpCloudRegionId;
	@JsonProperty("tenantId")
	private String tenantId;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("lcpCloudRegionId")
	public String getLcpCloudRegionId() {
	return lcpCloudRegionId;
	}

	@JsonProperty("lcpCloudRegionId")
	public void setLcpCloudRegionId(String lcpCloudRegionId) {
	this.lcpCloudRegionId = lcpCloudRegionId;
	}

	@JsonProperty("tenantId")
	public String getTenantId() {
	return tenantId;
	}

	@JsonProperty("tenantId")
	public void setTenantId(String tenantId) {
	this.tenantId = tenantId;
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
