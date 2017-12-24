package org.openecomp.vid.aai.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServiceRelationships {
	
	@JsonProperty("service-instance-id")
	public String serviceInstanceId;

	@JsonProperty("service-instance-name")
	public String serviceInstanceName;

	@JsonProperty("service-type")
	public String serviceType;

	@JsonProperty("service-role")
	public String serviceRole;

	@JsonProperty("environment-context")
	public String environmentContext;

	@JsonProperty("workload-context")
	public String workloadContext;

	@JsonProperty("model-invariant-id")
	public String modelInvariantId;
	
	@JsonProperty("model-version-id")
	public String modelVersionId;

	@JsonProperty("resource-version")
	public String resourceVersion;
	
	@JsonProperty("orchestration-status")
	public String orchestrationStatus;

	@JsonProperty("relationship-list")
	public RelationshipList relationshipList;


	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public String getServiceInstanceName() {
		return serviceInstanceName;
	}

	public void setServiceInstanceName(String serviceInstanceName) {
		this.serviceInstanceName = serviceInstanceName;
	}

	public String getModelInvariantId() {
		return modelInvariantId;
	}

	public void setModelInvariantId(String modelInvariantId) {
		this.modelInvariantId = modelInvariantId;
	}

	public String getModelVersionId() {
		return modelVersionId;
	}

	public void setModelVersionId(String modelVersionId) {
		this.modelVersionId = modelVersionId;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	public String getOrchestrationStatus() {
		return orchestrationStatus;
	}

	public void setOrchestrationStatus(String orchestrationStatus) {
		this.orchestrationStatus = orchestrationStatus;
	}

	public RelationshipList getRelationshipList() {
		return relationshipList;
	}

	public void setRelationshipList(RelationshipList relationshipList) {
		this.relationshipList = relationshipList;
	}



	
	

}
