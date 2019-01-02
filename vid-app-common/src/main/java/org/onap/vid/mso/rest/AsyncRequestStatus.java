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


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Represent response for: GET orchestrationRequests
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsyncRequestStatus  {

    public Request request;

    public AsyncRequestStatus(Request request) {
        this.request = request;
    }

    public AsyncRequestStatus() {

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Request {

        public Request(RequestStatus requestStatus) {
            this.requestStatus = requestStatus;
        }

        public Request() {
        }

        public String requestId;
        public String requestScope;

        /**
         * The instance ids.
         */
        public InstanceReferences instanceReferences;


        /**
         * The request details.
         */
        public RequestDetails requestDetails;

        /**
         * The request status.
         */
        public RequestStatus requestStatus;

        /**
         * The time of start.
         */
        public String startTime;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InstanceReferences {

        public String serviceInstanceId;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequestDetails {

        public RequestInfo requestInfo;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequestInfo {

        public String instanceName;
    }



}
