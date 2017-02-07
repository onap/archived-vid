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

package org.openecomp.vid.mso.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
//import javax.annotation.Generated;

import org.openecomp.vid.domain.mso.CloudConfiguration;
import org.openecomp.vid.domain.mso.ModelInfo;
import org.openecomp.vid.domain.mso.RequestInfo;
import org.openecomp.vid.domain.mso.RequestParameters;
import org.openecomp.vid.domain.mso.SubscriberInfo;
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
 * aggregates the context, configuraiton and detailed parameters associated with the request into a single structure
 * 
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
public class RequestDetails extends org.openecomp.vid.domain.mso.RequestDetails {

    @JsonProperty("cloudConfiguration")
    private CloudConfiguration cloudConfiguration;
    @JsonProperty("modelInfo")
    private ModelInfo modelInfo;
    @JsonProperty("relatedModelList")
    private List<RelatedModel> relatedModelList;
    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;
    @JsonProperty("subscriberInfo")
    private SubscriberInfo subscriberInfo;
    @JsonProperty("requestParameters")
    private RequestParameters requestParameters;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The cloudConfiguration
     */
    @JsonProperty("cloudConfiguration")
    public CloudConfiguration getCloudConfiguration() {
        return cloudConfiguration;
    }

    /**
     * 
     * @param cloudConfiguration
     *     The cloudConfiguration
     */
    @JsonProperty("cloudConfiguration")
    public void setCloudConfiguration(CloudConfiguration cloudConfiguration) {
        this.cloudConfiguration = cloudConfiguration;
    }

    /**
     * 
     * @return
     *     The modelInfo
     */
    @JsonProperty("modelInfo")
    public ModelInfo getModelInfo() {
        return modelInfo;
    }

    /**
     * 
     * @param modelInfo
     *     The modelInfo
     */
    @JsonProperty("modelInfo")
    public void setModelInfo(ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    /**
     * 
     * @return
     *     The relatedModelList
     */
    @JsonProperty("relatedModelList")
    public List<RelatedModel> getRelatedModelList() {
        return relatedModelList;
    }

    /**
     * 
     * @param relatedModelList
     *     The relatedModelList
     */
    @JsonProperty("relatedModelList")
    public void setRelatedModelList( List<RelatedModel> relatedModelList) {
        this.relatedModelList = relatedModelList;
    }

    /**
     * 
     * @return
     *     The requestInfo
     */
    @JsonProperty("requestInfo")
    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    /**
     * 
     * @param requestInfo
     *     The requestInfo
     */
    @JsonProperty("requestInfo")
    public void setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }
    /**
     * 
     * @return
     *     The subscriberInfo
     */
    @JsonProperty("subscriberInfo")
    public SubscriberInfo getSubscriberInfo() {
        return subscriberInfo;
    }

    /**
     * 
     * @param subscriberInfo
     *     The subscriberInfo
     */
    @JsonProperty("subscriberInfo")
    public void setSubscriberInfo(SubscriberInfo subscriberInfo) {
        this.subscriberInfo = subscriberInfo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(cloudConfiguration).append(modelInfo).append(relatedModelList).append(requestInfo).append(getRequestParameters()).append(subscriberInfo).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RequestDetails) == false) {
            return false;
        }
        RequestDetails rhs = ((RequestDetails) other);
        return new EqualsBuilder().append(cloudConfiguration, rhs.cloudConfiguration).append(modelInfo, rhs.modelInfo).append(relatedModelList, rhs.relatedModelList).append(requestInfo, rhs.requestInfo).append(getRequestParameters(), rhs.getRequestParameters()).append(subscriberInfo, rhs.subscriberInfo).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
