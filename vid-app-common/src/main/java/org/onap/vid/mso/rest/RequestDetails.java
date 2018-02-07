/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import org.onap.vid.domain.mso.*;
import org.onap.vid.domain.mso.SubscriberInfo;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


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
        "requestParameters"
})
public class RequestDetails{

    /** The cloud configuration. */
    @JsonProperty("cloudConfiguration")
    private CloudConfiguration cloudConfiguration;

    /** The model info. */
    @JsonProperty("modelInfo")
    private ModelInfo modelInfo;

    /** The related model list. */
    @JsonProperty("relatedModelList")
    private List<RelatedModel> relatedInstanceList;

    /** The request info. */
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;

    /** The subscriber info. */
    @JsonProperty("subscriberInfo")
    private SubscriberInfo subscriberInfo;

    /** The request parameters. */
    @JsonProperty("requestParameters")
    private RequestParameters requestParameters;

    /** The additional properties. */
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
    public List<RelatedModel> getRelatedInstanceList() {
        return relatedInstanceList;
    }

    /**
     * Sets the related model list.
     *
     * @param relatedInstanceList     The relatedInstanceList
     */
    @JsonProperty("relatedInstanceList")
    public void setRelatedInstanceList( List<RelatedModel> relatedInstanceList) {
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

    /* (non-Javadoc)
     * @see org.onap.vid.domain.mso.RequestDetails#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(cloudConfiguration).append(modelInfo).append(relatedInstanceList).append(requestInfo).append(getRequestParameters()).append(subscriberInfo).append(additionalProperties).toHashCode();
    }

    /* (non-Javadoc)
     * @see org.onap.vid.domain.mso.RequestDetails#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RequestDetails) == false) {
            return false;
        }
        RequestDetails rhs = ((RequestDetails) other);
        return new EqualsBuilder().append(cloudConfiguration, rhs.cloudConfiguration).append(modelInfo, rhs.modelInfo).append(relatedInstanceList, rhs.relatedInstanceList).append(requestInfo, rhs.requestInfo).append(getRequestParameters(), rhs.getRequestParameters()).append(subscriberInfo, rhs.subscriberInfo).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    public void setRequestParameters(RequestParameters requestParameters) {
        this.requestParameters = requestParameters;
    }
}
