package org.onap.vid.model.mso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    public void setOperationalEnvironmentId(String operationalEnvironmentId) {
        this.operationalEnvironmentId = operationalEnvironmentId;
    }

    public String getOperationalEnvironmentName() {
        return operationalEnvironmentName;
    }

    public void setOperationalEnvironmentName(String operationalEnvironmentName) {
        this.operationalEnvironmentName = operationalEnvironmentName;
    }

    public String getOperationalEnvironmentType() {
        return operationalEnvironmentType;
    }

    public void setOperationalEnvironmentType(String operationalEnvironmentType) {
        this.operationalEnvironmentType = operationalEnvironmentType;
    }

    public String getOperationalEnvironmentStatus() {
        return operationalEnvironmentStatus;
    }

    public void setOperationalEnvironmentStatus(String operationalEnvironmentStatus) {
        this.operationalEnvironmentStatus = operationalEnvironmentStatus;
    }

    public String getTenantContext() {
        return tenantContext;
    }

    public void setTenantContext(String tenantContext) {
        this.tenantContext = tenantContext;
    }

    public String getWorkloadContext() {
        return workloadContext;
    }

    public void setWorkloadContext(String workloadContext) {
        this.workloadContext = workloadContext;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }
}
