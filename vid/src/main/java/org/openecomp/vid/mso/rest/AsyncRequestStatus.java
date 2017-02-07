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

//import java.util.HashMap;
//import java.util.Map;
//import javax.annotation.Generated;

import org.openecomp.vid.domain.mso.InstanceIds;
import org.openecomp.vid.domain.mso.RequestStatus;
//import com.fasterxml.jackson.annotation.JsonAnyGetter;
//import com.fasterxml.jackson.annotation.JsonAnySetter;
//import com.fasterxml.jackson.annotation.JsonCreator;
//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
//import com.fasterxml.jackson.annotation.JsonPropertyOrder;
//import com.fasterxml.jackson.annotation.JsonValue;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * request structure
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)

public class AsyncRequestStatus extends org.openecomp.vid.domain.mso.AsyncRequestStatus {

    
    private InstanceIds instanceIds;
   
    private RequestStatus requestStatus;

    public InstanceIds getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(InstanceIds instanceIds) {
        this.instanceIds = instanceIds;
    }

    
    /**
     * 
     * (Required)
     * 
     * @return
     *     The requestStatus
     */
    @JsonProperty("requestStatus")
    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    /**
     * 
     * (Required)
     * 
     * @param requestStatus
     *     The requestStatus
     */
    @JsonProperty("requestStatus")
    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(getCorrelator()).append(getFinishTime()).append(instanceIds).append(getRequestId()).append(getRequestScope()).append(getRequestStatus()).append(getRequestType()).append(getStartTime()).append(getAdditionalProperties()).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AsyncRequestStatus) == false) {
            return false;
        }
        AsyncRequestStatus rhs = ((AsyncRequestStatus) other);
        return new EqualsBuilder().append(getCorrelator(), rhs.getCorrelator()).append(getFinishTime(), rhs.getFinishTime()).append(instanceIds, rhs.instanceIds).append(getRequestId(), rhs.getRequestId()).append(getRequestScope(), rhs.getRequestScope()).append(getRequestStatus(), rhs.getRequestStatus()).append(getRequestType(), rhs.getRequestType()).append(getStartTime(), rhs.getStartTime()).append(getAdditionalProperties(), rhs.getAdditionalProperties()).isEquals();
    }
}
