/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

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
    private String orchestrationType;



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

    public String getOrchestrationType() {
        return orchestrationType;
    }

    @JsonAlias("orchestration-type")
    public void setOrchestrationType(String orchestrationType) {
        this.orchestrationType = orchestrationType;
    }
}
