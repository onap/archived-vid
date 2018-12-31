package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("model-version-id")
    public void setJsonModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    public String getModelName() {
        return modelName;
    }

    @JsonProperty("model-name")
    public void setJsonModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    @JsonProperty("model-version")
    public void setJsonModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getDistributionStatus() {
        return distributionStatus;
    }

    @JsonProperty("distribution-status")
    public void setJsonDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonProperty("resource-version")
    public void setJsonResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    @JsonProperty("model-description")
    public void setJsonModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

}
