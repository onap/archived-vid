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

package org.onap.vid.mso.rest.OperationalEnvironment;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.List;

public class OperationEnvironmentRequestDetails {
    private final RequestInfo requestInfo;
    private final List<RelatedInstance> relatedInstanceList;
    private final RequestParameters requestParameters;

    public OperationEnvironmentRequestDetails(@JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
                                              @JsonProperty(value = "relatedInstanceList", required = true) List<RelatedInstance> relatedInstanceList,
                                              @JsonProperty(value = "requestParameters", required = true) RequestParameters requestParameters) {
        this.requestInfo = requestInfo;
        this.relatedInstanceList = relatedInstanceList;
        this.requestParameters = requestParameters;
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public List<RelatedInstance> getRelatedInstanceList() {
        return relatedInstanceList;
    }

    public RequestParameters getRequestParameters() {
        return requestParameters;
    }

    public static class RequestInfo {
        private final String resourceType;
        private final String instanceName;
        private final String source;
        private final String requestorId;

        public RequestInfo(@JsonProperty(value = "resourceType", required = true) String resourceType,
                           @JsonProperty(value = "instanceName", required = true) String instanceName,
                           @JsonProperty(value = "source", required = true) String source,
                           @JsonProperty(value = "requestorId", required = true) String requestorId) {
            this.resourceType = resourceType;
            this.instanceName = instanceName;
            this.source = source;
            this.requestorId = requestorId;
        }

        public String getResourceType() {
            return resourceType;
        }

        public String getInstanceName() {
            return instanceName;
        }

        public String getSource() {
            return source;
        }

        public String getRequestorId() {
            return requestorId;
        }
    }

    public static class RequestParameters {
        private final String operationalEnvironmentType;
        private final String tenantContext;
        private final String workloadContext;

        public RequestParameters(@JsonProperty(value = "operationalEnvironmentType", required = true) String operationalEnvironmentType,
                                 @JsonProperty(value = "tenantContext", required = true) String tenantContext,
                                 @JsonProperty(value = "workloadContext", required = true) String workloadContext) {
            this.operationalEnvironmentType = operationalEnvironmentType;
            this.tenantContext = tenantContext;
            this.workloadContext = workloadContext;
        }
        public String getOperationalEnvironmentType() {
            return operationalEnvironmentType;
        }

        public String getTenantContext() {
            return tenantContext;
        }

        public String getWorkloadContext() {
            return workloadContext;
        }
    }

    @JsonTypeName("relatedInstance")
    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
    public static class RelatedInstance {
        private final String resourceType;
        private final String instanceId;
        private final String instanceName;

        public String getResourceType() {
            return resourceType;
        }

        public String getInstanceId() {
            return instanceId;
        }

        public String getInstanceName() {
            return instanceName;
        }

        public RelatedInstance(@JsonProperty(value = "instanceName", required = true) String resourceType,
                               @JsonProperty(value = "instanceId", required = true) String instanceId,
                               @JsonProperty(value = "instanceName", required = true) String instanceName) {
            this.resourceType = resourceType;
            this.instanceId = instanceId;
            this.instanceName = instanceName;
        }
    }
}
