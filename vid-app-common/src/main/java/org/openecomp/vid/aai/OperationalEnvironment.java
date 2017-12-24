package org.openecomp.vid.aai;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openecomp.vid.aai.model.Relationship;
import org.openecomp.vid.aai.model.RelationshipList;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OperationalEnvironment {

    private String operationalEnvironmentId;
    private String operationalEnvironmentName;
    private String operationalEnvironmentType;
    private String operationalEnvironmentStatus;
    private String tenantContext;
    private String workloadContext;
    private String resourceVersion;
    private List<Relationship> relationshipList;

    public OperationalEnvironment() {
    }

    public OperationalEnvironment(String operationalEnvironmentId, String operationalEnvironmentName, String operationalEnvironmentType, String operationalEnvironmentStatus, String tenantContext, String workloadContext, String resourceVersion, List<Relationship> relationshipList) {
        this.operationalEnvironmentId = operationalEnvironmentId;
        this.operationalEnvironmentName = operationalEnvironmentName;
        this.operationalEnvironmentType = operationalEnvironmentType;
        this.operationalEnvironmentStatus = operationalEnvironmentStatus;
        this.tenantContext = tenantContext;
        this.workloadContext = workloadContext;
        this.resourceVersion = resourceVersion;
        this.relationshipList = relationshipList;
    }

    @JsonProperty("operational-environment-id")
    public String getOperationalEnvironmentId() {
        return operationalEnvironmentId;
    }

    public void setOperationalEnvironmentId(String operationalEnvironmentId) {
        this.operationalEnvironmentId = operationalEnvironmentId;
    }

    @JsonProperty("operational-environment-name")
    public String getOperationalEnvironmentName() {
        return operationalEnvironmentName;
    }

    public void setOperationalEnvironmentName(String operationalEnvironmentName) {
        this.operationalEnvironmentName = operationalEnvironmentName;
    }

    @JsonProperty("operational-environment-type")
    public String getOperationalEnvironmentType() {
        return operationalEnvironmentType;
    }

    public void setOperationalEnvironmentType(String operationalEnvironmentType) {
        this.operationalEnvironmentType = operationalEnvironmentType;
    }

    @JsonProperty("operational-environment-status")
    public String getOperationalEnvironmentStatus() {
        return operationalEnvironmentStatus;
    }

    public void setOperationalEnvironmentStatus(String operationalEnvironmentStatus) {
        this.operationalEnvironmentStatus = operationalEnvironmentStatus;
    }

    @JsonProperty("tenant-context")
    public String getTenantContext() {
        return tenantContext;
    }

    public void setTenantContext(String tenantContext) {
        this.tenantContext = tenantContext;
    }

    @JsonProperty("workload-context")
    public String getWorkloadContext() {
        return workloadContext;
    }

    public void setWorkloadContext(String workloadContext) {
        this.workloadContext = workloadContext;
    }

    @JsonProperty("resource-version")
    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @JsonProperty("relationship-list")
    public List<Relationship> getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(List<Relationship> relationshipList) {
        this.relationshipList = relationshipList;
    }
}
