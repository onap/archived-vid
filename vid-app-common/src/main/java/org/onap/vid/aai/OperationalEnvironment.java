package org.onap.vid.aai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.model.RelationshipList;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalEnvironment {

    private String operationalEnvironmentId;
    private String operationalEnvironmentName;
    private String operationalEnvironmentType;
    private String operationalEnvironmentStatus;
    private String tenantContext;
    private String workloadContext;
    private String resourceVersion;
    private RelationshipList relationshipList;

    public OperationalEnvironment() {
    }

    public OperationalEnvironment(String operationalEnvironmentId, String operationalEnvironmentName, String operationalEnvironmentType, String operationalEnvironmentStatus, String tenantContext, String workloadContext, String resourceVersion, RelationshipList relationshipList) {
        this.operationalEnvironmentId = operationalEnvironmentId;
        this.operationalEnvironmentName = operationalEnvironmentName;
        this.operationalEnvironmentType = operationalEnvironmentType;
        this.operationalEnvironmentStatus = operationalEnvironmentStatus;
        this.tenantContext = tenantContext;
        this.workloadContext = workloadContext;
        this.resourceVersion = resourceVersion;
        this.relationshipList = relationshipList;
    }

    public String getOperationalEnvironmentId() {
        return operationalEnvironmentId;
    }

    @JsonProperty("operational-environment-id")
    public void setOperationalEnvironmentId(String operationalEnvironmentId) {
        this.operationalEnvironmentId = operationalEnvironmentId;
    }

    public String getOperationalEnvironmentName() {
        return operationalEnvironmentName;
    }

    @JsonProperty("operational-environment-name")
    public void setOperationalEnvironmentName(String operationalEnvironmentName) {
        this.operationalEnvironmentName = operationalEnvironmentName;
    }

    public String getOperationalEnvironmentType() {
        return operationalEnvironmentType;
    }

    @JsonProperty("operational-environment-type")
    public void setOperationalEnvironmentType(String operationalEnvironmentType) {
        this.operationalEnvironmentType = operationalEnvironmentType;
    }

    public String getOperationalEnvironmentStatus() {
        return operationalEnvironmentStatus;
    }

    @JsonProperty("operational-environment-status")
    public void setOperationalEnvironmentStatus(String operationalEnvironmentStatus) {
        this.operationalEnvironmentStatus = operationalEnvironmentStatus;
    }

    public String getTenantContext() {
        return tenantContext;
    }

    @JsonProperty("tenant-context")
    public void setTenantContext(String tenantContext) {
        this.tenantContext = tenantContext;
    }

    public String getWorkloadContext() {
        return workloadContext;
    }

    @JsonProperty("workload-context")
    public void setWorkloadContext(String workloadContext) {
        this.workloadContext = workloadContext;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonProperty("resource-version")
    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    @JsonProperty("relationship-list")
    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }
}
