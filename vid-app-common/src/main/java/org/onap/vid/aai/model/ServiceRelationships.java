package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

	@JsonAlias("service-instance-id")
	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public String getServiceInstanceName() {
		return serviceInstanceName;
	}

	@JsonAlias("service-instance-name")
	public void setServiceInstanceName(String serviceInstanceName) {
		this.serviceInstanceName = serviceInstanceName;
	}

	public String getModelInvariantId() {
		return modelInvariantId;
	}

	public String getServiceType() {
		return serviceType;
	}

	@JsonAlias("service-type")
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServiceRole() {
		return serviceRole;
	}

	@JsonAlias("service-role")
	public void setServiceRole(String serviceRole) {
		this.serviceRole = serviceRole;
	}

	public String getEnvironmentContext() {
		return environmentContext;
	}

	@JsonAlias("environment-context")
	public void setEnvironmentContext(String environmentContext) {
		this.environmentContext = environmentContext;
	}

	public String getWorkloadContext() {
		return workloadContext;
	}

	@JsonAlias("workload-context")
	public void setWorkloadContext(String workloadContext) {
		this.workloadContext = workloadContext;
	}

	@JsonAlias("model-invariant-id")
	public void setModelInvariantId(String modelInvariantId) {
		this.modelInvariantId = modelInvariantId;
	}

	public String getModelVersionId() {
		return modelVersionId;
	}

	@JsonAlias("model-version-id")
	public void setModelVersionId(String modelVersionId) {
		this.modelVersionId = modelVersionId;
	}

	public String getResourceVersion() {
		return resourceVersion;
	}

	@JsonAlias("resource-version")
	public void setResourceVersion(String resourceVersion) {
		this.resourceVersion = resourceVersion;
	}

	public String getOrchestrationStatus() {
		return orchestrationStatus;
	}

	@JsonAlias("orchestration-status")
	public void setOrchestrationStatus(String orchestrationStatus) {
		this.orchestrationStatus = orchestrationStatus;
	}

	public RelationshipList getRelationshipList() {
		return relationshipList;
	}

	@JsonAlias("relationship-list")
	public void setRelationshipList(RelationshipList relationshipList) {
		this.relationshipList = relationshipList;
	}



	
	

}
