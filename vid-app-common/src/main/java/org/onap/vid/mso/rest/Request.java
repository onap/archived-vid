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

//import java.util.HashMap;
//import java.util.Map;
//import javax.annotation.Generated;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.onap.vid.domain.mso.InstanceIds;
import org.onap.vid.domain.mso.RequestStatus;
//import com.fasterxml.jackson.annotation.JsonAnyGetter;
//import com.fasterxml.jackson.annotation.JsonAnySetter;
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.builder.EqualsBuilder;
//import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * request structure.
 */
public class Request extends org.onap.vid.domain.mso.Request {

 
    /** The instance ids. */
    private InstanceIds instanceIds;
    
    /** The request details. */
    private RequestDetails requestDetails;
    
    /** The request status. */
    private RequestStatus requestStatus;
 
    
    /* (non-Javadoc)
     * @see org.openecomp.vid.domain.mso.Request#getInstanceIds()
     */
    @JsonProperty("instanceIds")
    public InstanceIds getInstanceIds() {
        return instanceIds;
    }

    /**
     * Sets the instance ids.
     *
     * @param instanceIds     The instanceIds
     */
    @JsonProperty("instanceIds")
    public void setInstanceIds(InstanceIds instanceIds) {
        this.instanceIds = instanceIds;
    }

    /**
     * (Required).
     *
     * @return     The requestDetails
     */
    @JsonProperty("requestDetails")
    public RequestDetails getRequestDetails() {
        return requestDetails;
    }

    /**
     * (Required).
     *
     * @param requestDetails     The requestDetails
     */
    @JsonProperty("requestDetails")
    public void setRequestDetails(RequestDetails requestDetails) {
        this.requestDetails = requestDetails;
    }

    
    /**
     * Gets the request status.
     *
     * @return     The requestStatus
     */
    @JsonProperty("requestStatus")
    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    /**
     * Sets the request status.
     *
     * @param requestStatus     The requestStatus
     */
    @JsonProperty("requestStatus")
    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

  
    /* (non-Javadoc)
     * @see org.openecomp.vid.domain.mso.Request#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
    
    /* (non-Javadoc)
     * @see org.openecomp.vid.domain.mso.Request#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Request) == false) {
            return false;
        }
        Request rhs = ((Request) other);
        return new EqualsBuilder().append(getFinishTime(), rhs.getFinishTime()).append(getInstanceIds(), rhs.getInstanceIds()).append(getRequestDetails(), rhs.getRequestDetails()).append(getRequestId(), rhs.getRequestId()).append(getRequestScope(), rhs.getRequestScope()).append(getRequestStatus(), rhs.getRequestStatus()).append(getRequestType(), rhs.getRequestType()).append(getStartTime(), rhs.getStartTime()).append(getAdditionalProperties(), rhs.getAdditionalProperties()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getFinishTime()).append(getInstanceIds()).append(getRequestDetails()).append(getRequestId()).append(getRequestScope()).append(getRequestStatus()).append(getRequestType()).append(getStartTime()).append(getAdditionalProperties()).toHashCode();
    }
}
