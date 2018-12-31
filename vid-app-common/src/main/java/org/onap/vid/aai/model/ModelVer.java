package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelVer {

    private String modelVersionId;
    private String modelName;
    private String modelVersion;
    private String distributionStatus;
    private String resourceVersion;
    private String modelDescription;



    public String getModelVersionId() {
        return modelVersionId;
    }

    @JsonAlias("model-version-id")
    public void setModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    public String getModelName() {
        return modelName;
    }

    @JsonAlias("model-name")
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    @JsonAlias("model-version")
    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getDistributionStatus() {
        return distributionStatus;
    }

    @JsonAlias("distribution-status")
    public void setDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonAlias("resource-version")
    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    @JsonAlias("model-description")
    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

}
