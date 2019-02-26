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

package org.onap.vid.mso.rest;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.onap.vid.exceptions.NotFoundException;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.model.ModelInfo;
import org.onap.vid.mso.model.RequestInfo;
import org.onap.vid.mso.model.RequestParameters;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * aggregates the context, configuraiton and detailed parameters associated with the request into a single structure.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "cloudConfiguration",
        "modelInfo",
        "relatedModelList",
        "requestInfo",
        "subscriberInfo",
        "requestParameters",
        "configurationParameters"
})
public class RequestDetails{



    /** The cloud configuration. */
    @JsonProperty("cloudConfiguration")
    private CloudConfiguration cloudConfiguration;

    /** The model info. */
    @JsonProperty("modelInfo")
    private ModelInfo modelInfo;

    /** The related model list. */
    @JsonProperty("relatedInstanceList")
    private List<RelatedInstanceWrapper> relatedInstanceList;

    /** The request info. */
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;

    /** The subscriber info. */
    @JsonProperty("subscriberInfo")
    private SubscriberInfo subscriberInfo;

    /** The request parameters. */
    @JsonProperty("requestParameters")
    private RequestParameters requestParameters;

    @JsonProperty("configurationParameters")
    protected List<Map<String, String>> configurationParameters;

    /** The additional properties. */
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * Gets the cloud configuration.
     *
     * @return     The cloudConfiguration
     */
    @JsonProperty("cloudConfiguration")
    public CloudConfiguration getCloudConfiguration() {
        return cloudConfiguration;
    }

    /**
     * Sets the cloud configuration.
     *
     * @param cloudConfiguration     The cloudConfiguration
     */
    @JsonProperty("cloudConfiguration")
    public void setCloudConfiguration(CloudConfiguration cloudConfiguration) {
        this.cloudConfiguration = cloudConfiguration;
    }

    /**
     * Gets the model info.
     *
     * @return     The modelInfo
     */
    @JsonProperty("modelInfo")
    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    /**
     * Sets the model info.
     *
     * @param modelInfo     The modelInfo
     */
    @JsonProperty("modelInfo")
    public void setModelInfo(ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }
    /**
     * Gets the related instance list.
     *
     * @return     The relatedInstanceList
     */
    @JsonProperty("relatedInstanceList")
    public List<RelatedInstanceWrapper> getRelatedInstanceList() {
        return relatedInstanceList;
    }

    /**
     * Sets the related model list.
     *
     * @param relatedInstanceList     The relatedInstanceList
     */
    @JsonProperty("relatedInstanceList")
    public void setRelatedInstanceList(List<RelatedInstanceWrapper> relatedInstanceList) {
        this.relatedInstanceList = relatedInstanceList;
    }

    /**
     * Gets the request info.
     *
     * @return     The requestInfo
     */
    @JsonProperty("requestInfo")
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    /**
     * Sets the request info.
     *
     * @param requestInfo     The requestInfo
     */
    @JsonProperty("requestInfo")
    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    /**
     * Gets the subscriber info.
     *
     * @return     The subscriberInfo
     */
    @JsonProperty("subscriberInfo")
    public SubscriberInfo getSubscriberInfo() {
        return subscriberInfo;
    }

    /**
     * Sets the subscriber info.
     *
     * @param subscriberInfo     The subscriberInfo
     */
    @JsonProperty("subscriberInfo")
    public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
        this.subscriberInfo = subscriberInfo;
    }

    /* (non-Javadoc)
     * @see org.onap.vid.domain.mso.RequestDetails#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /* (non-Javadoc)
     * @see org.onap.vid.domain.mso.RequestDetails#getAdditionalProperties()
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    /* (non-Javadoc)
     * @see org.onap.vid.domain.mso.RequestDetails#setAdditionalProperty(java.lang.String, java.lang.Object)
     */
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @JsonProperty("configurationParameters")
    public List<Map<String, String>> getConfigurationParameters() {
        return configurationParameters;
    }

    @JsonProperty("configurationParameters")
    public void setConfigurationParameters(List<Map<String, String>> configurationParameters) {
        this.configurationParameters = configurationParameters;
    }

    /* (non-Javadoc)
     * @see org.onap.vid.domain.mso.RequestDetails#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(cloudConfiguration)
                .append(modelInfo)
                .append(relatedInstanceList)
                .append(requestInfo)
                .append(getRequestParameters())
                .append(subscriberInfo)
                .append(additionalProperties)
                .append(configurationParameters)
                .toHashCode();
    }

    /* (non-Javadoc)
     * @see org.onap.vid.domain.mso.RequestDetails#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RequestDetails)) {
            return false;
        }
        RequestDetails rhs = ((RequestDetails) other);
        return new EqualsBuilder()
                .append(cloudConfiguration, rhs.cloudConfiguration)
                .append(modelInfo, rhs.modelInfo)
                .append(relatedInstanceList, rhs.relatedInstanceList)
                .append(requestInfo, rhs.requestInfo)
                .append(getRequestParameters(), rhs.getRequestParameters())
                .append(subscriberInfo, rhs.subscriberInfo)
                .append(additionalProperties, rhs.additionalProperties)
                .append(configurationParameters, rhs.configurationParameters)
                .isEquals();
    }

    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(RequestParameters requestParameters) {
        this.requestParameters = requestParameters;
    }

    public <T> T extractValueByPathUsingAdditionalProperties (List<String> keys, Class<T> clazz) {
        Object result = getAdditionalProperties();
        for (String key : keys) {
            if (result instanceof Map) {
                result = ((Map) result).get(key);
            }

            else {
                throw new NotFoundException("failed to find key: "+key+" in path: "+String.join("\\", keys));
            }
        }
        if (clazz.isInstance(result)) {
            return clazz.cast(result);
        }

        throw new NotFoundException(
                String.format("failed to extract value from path:%s because %s is not of type %s",
                String.join("\\", keys), String.valueOf(result) , clazz));
    }


}
