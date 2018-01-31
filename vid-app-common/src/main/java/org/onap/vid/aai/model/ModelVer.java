package org.onap.vid.aai.model;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelVer {

    @JsonProperty("model-version-id")
    private String modelVersionId;
    @JsonProperty("model-name")
    private String modelName;
    @JsonProperty("model-version")
    private String modelVersion;
    @JsonProperty("distribution-status")
    private String distributionStatus;
    @JsonProperty("resource-version")
    private String resourceVersion;
    @JsonProperty("model-description")
    private String modelDescription;



    @JsonProperty("model-version-id")
    public String getModelVersionId() {
        return modelVersionId;
    }

    @JsonProperty("model-version-id")
    public void setModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    @JsonProperty("model-name")
    public String getModelName() {
        return modelName;
    }

    @JsonProperty("model-name")
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @JsonProperty("model-version")
    public String getModelVersion() {
        return modelVersion;
    }

    @JsonProperty("model-version")
    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    @JsonProperty("distribution-status")
    public String getDistributionStatus() {
        return distributionStatus;
    }

    @JsonProperty("distribution-status")
    public void setDistributionStatus(String distributionStatus) {
        this.distributionStatus = distributionStatus;
    }

    @JsonProperty("resource-version")
    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonProperty("resource-version")
    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @JsonProperty("model-description")
    public String getModelDescription() {
        return modelDescription;
    }

    @JsonProperty("model-description")
    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

}
