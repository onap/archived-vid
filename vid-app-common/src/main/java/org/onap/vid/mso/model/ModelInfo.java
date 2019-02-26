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

package org.onap.vid.mso.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * fields describing the SDC entity being operated on by the request
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "modelCustomizationName",
    "modelCustomizationId",
    "modelInvariantId",
    "modelVersionId",
    "modelName",
    "modelNameVersionId",
    "modelType",
    "modelVersion"
})
public class ModelInfo {

    /**
     * reference to the customized set of parameters associated with a model in a given service context
     * 
     */
    @JsonProperty("modelCustomizationName")
    private String modelCustomizationName;
    /**
     * reference to the customized set of parameters associated with a model in a given service context
     * 
     */
    @JsonProperty("modelCustomizationId")
    private String modelCustomizationId;
    /**
     * Invariant UUID for the model name, irrespective of the version, as defined in SDC--authoritative
     * 
     */
    @JsonProperty("modelInvariantId")
    private String modelInvariantId;
    /**
     * Version id for version
     * 
     */
    @JsonProperty("modelVersionId")
    private String modelVersionId;
    /**
     * name of the model as defined in SDC--not authoritative
     * 
     */
    @JsonProperty("modelName")
    private String modelName;
    /**
     * UUID for the model name and version combination as defined in SDC--authoritative
     * 
     */
    @JsonProperty("modelNameVersionId")
    private String modelNameVersionId;
    /**
     * short description of the entity being operated on
     * (Required)
     * 
     */
    @JsonProperty("modelType")
    private String modelType;
    /**
     * the version of the model as defined in SDC--not authoritative
     * 
     */
    @JsonProperty("modelVersion")
    private String modelVersion;

    /**
     * reference to the customized set of parameters associated with a model in a given service context
     * 
     * @return
     *     The modelCustomizationName
     */
    @JsonProperty("modelCustomizationName")
    public String getModelCustomizationName() {
        return modelCustomizationName;
    }

    /**
     * reference to the customized set of parameters associated with a model in a given service context
     * 
     * @param modelCustomizationName
     *     The modelCustomizationName
     */
    @JsonProperty("modelCustomizationName")
    public void setModelCustomizationName(String modelCustomizationName) {
        this.modelCustomizationName = modelCustomizationName;
    }

    /**
     * reference to the customized set of parameters associated with a model in a given service context
     * 
     * @return
     *     The modelCustomizationId
     */
    @JsonProperty("modelCustomizationId")
    public String getModelCustomizationId() {
        return modelCustomizationId;
    }

    /**
     * reference to the customized set of parameters associated with a model in a given service context
     * 
     * @param modelCustomizationId
     *     The modelCustomizationId
     */
    @JsonProperty("modelCustomizationId")
    public void setModelCustomizationId(String modelCustomizationId) {
        this.modelCustomizationId = modelCustomizationId;
    }

    /**
     * Invariant UUID for the model name, irrespective of the version, as defined in SDC--authoritative
     * 
     * @return
     *     The modelInvariantId
     */
    @JsonProperty("modelInvariantId")
    public String getModelInvariantId() {
        return modelInvariantId;
    }

    /**
     * Invariant UUID for the model name, irrespective of the version, as defined in SDC--authoritative
     * 
     * @param modelInvariantId
     *     The modelInvariantId
     */
    @JsonProperty("modelInvariantId")
    public void setModelInvariantId(String modelInvariantId) {
        this.modelInvariantId = modelInvariantId;
    }

    /**
     * Version id for version
     * 
     * @return
     *     The modelVersionId
     */
    @JsonProperty("modelVersionId")
    public String getModelVersionId() {
        return modelVersionId;
    }

    /**
     * Version id for version
     * 
     * @param modelVersionId
     *     The modelVersionId
     */
    @JsonProperty("modelVersionId")
    public void setModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    /**
     * name of the model as defined in SDC--not authoritative
     * 
     * @return
     *     The modelName
     */
    @JsonProperty("modelName")
    public String getModelName() {
        return modelName;
    }

    /**
     * name of the model as defined in SDC--not authoritative
     * 
     * @param modelName
     *     The modelName
     */
    @JsonProperty("modelName")
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    /**
     * UUID for the model name and version combination as defined in SDC--authoritative
     * 
     * @return
     *     The modelNameVersionId
     */
    @JsonProperty("modelNameVersionId")
    public String getModelNameVersionId() {
        return modelNameVersionId;
    }

    /**
     * UUID for the model name and version combination as defined in SDC--authoritative
     * 
     * @param modelNameVersionId
     *     The modelNameVersionId
     */
    @JsonProperty("modelNameVersionId")
    public void setModelNameVersionId(String modelNameVersionId) {
        this.modelNameVersionId = modelNameVersionId;
    }

    /**
     * short description of the entity being operated on
     * (Required)
     * 
     * @return
     *     The modelType
     */
    @JsonProperty("modelType")
    public String getModelType() {
        return modelType;
    }

    /**
     * short description of the entity being operated on
     * (Required)
     * 
     * @param modelType
     *     The modelType
     */
    @JsonProperty("modelType")
    public void setModelType(String modelType) {
        this.modelType = modelType;
    }

    /**
     * the version of the model as defined in SDC--not authoritative
     * 
     * @return
     *     The modelVersion
     */
    @JsonProperty("modelVersion")
    public String getModelVersion() {
        return modelVersion;
    }

    /**
     * the version of the model as defined in SDC--not authoritative
     * 
     * @param modelVersion
     *     The modelVersion
     */
    @JsonProperty("modelVersion")
    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(modelCustomizationName).append(modelCustomizationId).append(modelInvariantId).append(modelVersionId).append(modelName).append(modelNameVersionId).append(modelType).append(modelVersion).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ModelInfo)) {
            return false;
        }
        ModelInfo rhs = ((ModelInfo) other);
        return new EqualsBuilder().append(modelCustomizationName, rhs.modelCustomizationName).append(modelCustomizationId, rhs.modelCustomizationId).append(modelInvariantId, rhs.modelInvariantId).append(modelVersionId, rhs.modelVersionId).append(modelName, rhs.modelName).append(modelNameVersionId, rhs.modelNameVersionId).append(modelType, rhs.modelType).append(modelVersion, rhs.modelVersion).isEquals();
    }

}
