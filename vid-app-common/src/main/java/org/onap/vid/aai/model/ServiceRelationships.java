package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceRelationships {
	
	public String serviceInstanceId;

	public String serviceInstanceName;

	public String serviceType;

	public String serviceRole;

	public String environmentContext;

	public String workloadContext;

	public String modelInvariantId;

	public String modelVersionId;

	public String resourceVersion;

	public String orchestrationStatus;

	public RelationshipList relationshipList;


	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	@JsonProperty("service-instance-id")
	public void setJsonServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public String getServiceInstanceName() {
		return serviceInstanceName;
	}

	@JsonProperty("service-instance-name")
	public void setJsonServiceInstanceName(String serviceInstanceName) {
		this.serviceInstanceName = serviceInstanceName;
	}

	public String getModelInvariantId() {
		return modelInvariantId;
	}

	public String getServiceType() {
		return serviceType;
	}

	@JsonProperty("service-type")
	public void setJsonServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceRole() {
		return serviceRole;
	}

	@JsonProperty("service-role")
	public void setJsonServiceRole(String serviceRole) {
		this.serviceRole = serviceRole;
	}

	public String getEnvironmentContext() {
		return environmentContext;
	}

	@JsonProperty("environment-context")
	public void setJsonEnvironmentContext(String environmentContext) {
		this.environmentContext = environmentContext;
	}

	public String getWorkloadContext() {
		return workloadContext;
	}

	@JsonProperty("workload-context")
	public void setJsonWorkloadContext(String workloadContext) {
		this.workloadContext = workloadContext;
	}

	@JsonProperty("model-invariant-id")
	public void setJsonModelInvariantId(String modelInvariantId) {
		this.modelInvariantId = modelInvariantId;
	}

	public String getModelVersionId() {
		return modelVersionId;
	}

	@JsonProperty("model-version-id")
	public void setJsonModelVersionId(String modelVersionId) {
		this.modelVersionId = modelVersionId;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	@JsonProperty("resource-version")
	public void setJsonResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	public String getOrchestrationStatus() {
		return orchestrationStatus;
	}

	@JsonProperty("orchestration-status")
	public void setJsonOrchestrationStatus(String orchestrationStatus) {
		this.orchestrationStatus = orchestrationStatus;
	}

	public RelationshipList getRelationshipList() {
		return relationshipList;
	}

	@JsonProperty("relationship-list")
	public void setJsonRelationshipList(RelationshipList relationshipList) {
		this.relationshipList = relationshipList;
	}



	
	

}
