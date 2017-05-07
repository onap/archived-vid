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
import javax.annotation.Generated;
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
 * modelInfo and optional instanceId and instanceName for a model related to the modelInfo being operated on.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "instanceName",
    "instanceId",
    "modelInfo"
})
public class RelatedInstance extends org.openecomp.vid.domain.mso.RelatedInstance{


    /** The model info. */
    @JsonProperty("modelInfo")
    private org.openecomp.vid.domain.mso.ModelInfo modelInfo;
    
    /** The additional properties. */
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * (Required).
     *
     * @return     The modelInfo
     */
    @JsonProperty("modelInfo")
    public org.openecomp.vid.domain.mso.ModelInfo getModelInfo() {
        return modelInfo;
    }

    /**
     * (Required).
     *
     * @param modelInfo     The modelInfo
     */
    @JsonProperty("modelInfo")
    public void setModelInfo(org.openecomp.vid.domain.mso.ModelInfo modelInfo) {
        this.modelInfo = modelInfo;
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.domain.mso.RelatedInstance#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.domain.mso.RelatedInstance#getAdditionalProperties()
     */
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.domain.mso.RelatedInstance#setAdditionalProperty(java.lang.String, java.lang.Object)
     */
    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.domain.mso.RelatedInstance#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getInstanceName()).append(getInstanceId()).append(modelInfo).append(additionalProperties).toHashCode();
    }

    /* (non-Javadoc)
     * @see org.openecomp.vid.domain.mso.RelatedInstance#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RelatedInstance) == false) {
            return false;
        }
        RelatedInstance rhs = ((RelatedInstance) other);
        return new EqualsBuilder().append(getInstanceName(), rhs.getInstanceName()).append(getInstanceId(), rhs.getInstanceId()).append(modelInfo, rhs.getModelInfo()).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
