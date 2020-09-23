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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * fields describing the status of a request
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "percentProgress",
    "requestState",
    "statusMessage",
    "timestamp",
    "wasRolledBack"
})
public class RequestStatus {

    public RequestStatus() {}

    public RequestStatus(String requestState, String statusMessage, String timestamp) {
        this.requestState = requestState;
        this.statusMessage = statusMessage;
        this.timestamp = timestamp;
    }
    public RequestStatus(String requestState, String statusMessage, String timestamp, String flowStatus) {
        this.requestState = requestState;
        this.statusMessage = statusMessage;
        this.timestamp = timestamp;
        this.flowStatus = flowStatus;
    }

    /**
      * short description of the flow status
      * (Required)
      *
      */
    @JsonProperty("flowStatus")
    private String flowStatus;


    /**
     * percentage complete estimate from 0 to 100
     * 
     */
    @JsonProperty("percentProgress")
    private Double percentProgress;
    /**
     * short description of the instantiation state
     * (Required)
     * 
     */
    @JsonProperty("requestState")
    private String requestState;
    /**
     * additional descriptive information about the status
     * 
     */
    @JsonProperty("statusMessage")
    private String statusMessage;
    /**
     * GMT Datetime the requestStatus was created e.g.: Wed, 15 Oct 2014 13:01:52 GMT
     * (Required)
     * 
     */
    @JsonProperty("timestamp")
    private String timestamp;
    /**
     * true or false boolean indicating whether the request was rolled back
     * 
     */
    @JsonProperty("wasRolledBack")
    private Boolean wasRolledBack;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    /**
     * percentage complete estimate from 0 to 100
     * 
     * @return
     *     The percentProgress
     */
    @JsonProperty("percentProgress")
    public Double getPercentProgress() {
        return percentProgress;
    }

    /**
     * percentage complete estimate from 0 to 100
     * 
     * @param percentProgress
     *     The percentProgress
     */
    @JsonProperty("percentProgress")
    public void setPercentProgress(Double percentProgress) {
        this.percentProgress = percentProgress;
    }

    /**
     * short description of the instantiation state
     * (Required)
     * 
     * @return
     *     The requestState
     */
    @JsonProperty("requestState")
    public String getRequestState() {
        return requestState;
    }

    /**
     * short description of the instantiation state
     * (Required)
     * 
     * @param requestState
     *     The requestState
     */
    @JsonProperty("requestState")
    public void setRequestState(String requestState) {
        this.requestState = requestState;
    }

    /**
     * additional descriptive information about the status
     * 
     * @return
     *     The statusMessage
     */
    @JsonProperty("statusMessage")
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * additional descriptive information about the status
     * 
     * @param statusMessage
     *     The statusMessage
     */
    @JsonProperty("statusMessage")
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    /**
     * GMT Datetime the requestStatus was created e.g.: Wed, 15 Oct 2014 13:01:52 GMT
     * (Required)
     * 
     * @return
     *     The timestamp
     */
    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * GMT Datetime the requestStatus was created e.g.: Wed, 15 Oct 2014 13:01:52 GMT
     * (Required)
     * 
     * @param timestamp
     *     The timestamp
     */
    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * true or false boolean indicating whether the request was rolled back
     * 
     * @return
     *     The wasRolledBack
     */
    @JsonProperty("wasRolledBack")
    public Boolean getWasRolledBack() {
        return wasRolledBack;
    }

    /**
     * true or false boolean indicating whether the request was rolled back
     * 
     * @param wasRolledBack
     *     The wasRolledBack
     */
    @JsonProperty("wasRolledBack")
    public void setWasRolledBack(Boolean wasRolledBack) {
        this.wasRolledBack = wasRolledBack;
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

    /**
      * additional descriptive information about the status
      *
      * @return
      *     The flowStatus
      */
    @JsonProperty("flowStatus")
    public String getFlowStatus() {
        return flowStatus;
    }

    /**
      * additional descriptive information about the status
      *
      * @param flowStatus
      *     The flowStatus
      */
    @JsonProperty("flowStatus")
    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(percentProgress).append(requestState).append(statusMessage).append(timestamp).append(wasRolledBack).append(additionalProperties).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RequestStatus)) {
            return false;
        }
        RequestStatus rhs = ((RequestStatus) other);
        return new EqualsBuilder().append(percentProgress, rhs.percentProgress).append(requestState, rhs.requestState).append(statusMessage, rhs.statusMessage).append(timestamp, rhs.timestamp).append(wasRolledBack, rhs.wasRolledBack).append(flowStatus, rhs.flowStatus).append(additionalProperties, rhs.additionalProperties).isEquals();
    }

}
