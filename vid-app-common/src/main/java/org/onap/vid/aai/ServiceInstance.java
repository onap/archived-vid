package org.onap.vid.aai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceInstance {
	
	@JsonProperty("service-instance-id")
	public String serviceInstanceId;

	@JsonProperty("service-instance-name")
	public String serviceInstanceName;

	@JsonProperty("persona-model-id")
	public String personaModelId;

	@JsonProperty("persona-model-version")
	public String personaModelVersion;

	@JsonProperty("resource-version")
	public String resourceVersion;

	@JsonProperty("orchestration-status")
	public String orchestrationStatus;

	@JsonProperty("model-invariant-id")
	public String modelInvariantId;

	@JsonProperty("model-version-id")
	public String modelVersionId;

}
