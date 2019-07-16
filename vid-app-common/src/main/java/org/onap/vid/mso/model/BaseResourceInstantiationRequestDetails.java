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

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;

@JsonInclude(NON_NULL)
public class BaseResourceInstantiationRequestDetails {

    @JsonProperty("modelInfo")
    protected ModelInfo modelInfo;

    @JsonProperty("cloudConfiguration")
    protected CloudConfiguration cloudConfiguration;

    @JsonProperty("requestInfo")
    protected RequestInfo requestInfo;

    @JsonProperty("platform")
    protected Platform platform;

    @JsonProperty("lineOfBusiness")
    protected LineOfBusiness lineOfBusiness;

    @JsonProperty("relatedInstanceList")
    protected List<RelatedInstance> relatedInstanceList;

    @JsonProperty("requestParameters")
    protected RequestParameters requestParameters;

    public BaseResourceInstantiationRequestDetails(@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                                   @JsonProperty(value = "cloudConfiguration", required = true) CloudConfiguration cloudConfiguration,
                                                   @JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
                                                   @JsonProperty(value = "platform", required = true) Platform platform,
                                                   @JsonProperty(value = "lineOfBusiness", required = true) LineOfBusiness lineOfBusiness,
                                                   @JsonProperty(value = "relatedInstanceList", required = true) List<RelatedInstance> relatedInstanceList,
                                                   @JsonProperty(value = "requestParameters", required = true) RequestParameters requestParameters)
    {
        this.modelInfo = modelInfo;
        this.cloudConfiguration = cloudConfiguration;
        this.requestInfo = requestInfo;
        this.platform = platform;
        this.lineOfBusiness = lineOfBusiness;
        this.relatedInstanceList = relatedInstanceList;
        this.requestParameters = requestParameters;
    }

    public BaseResourceInstantiationRequestDetails(@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                                   @JsonProperty(value = "cloudConfiguration", required = true) CloudConfiguration cloudConfiguration,
                                                   @JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
                                                   @JsonProperty(value = "relatedInstanceList", required = true) List<RelatedInstance> relatedInstanceList,
                                                   @JsonProperty(value = "requestParameters", required = true) RequestParameters requestParameters)
    {
        this.modelInfo = modelInfo;
        this.cloudConfiguration = cloudConfiguration;
        this.requestInfo = requestInfo;
        this.relatedInstanceList = relatedInstanceList;
        this.requestParameters = requestParameters;
    }

    public static class RequestInfo {

        @JsonInclude(NON_EMPTY) public final String instanceName;
        @JsonInclude(NON_EMPTY) public final String productFamilyId;
        public final String source;
        @JsonInclude(NON_NULL) public final Boolean suppressRollback;
        public final String requestorId;

        public RequestInfo(String instanceName, String productFamilyId, String source, Boolean rollbackOnFailure, String requestorId) {
            this.instanceName = instanceName;
            this.productFamilyId = productFamilyId;
            this.source = source;
            this.requestorId = requestorId;
            // in the FE we are asking for "RollbackOnFailure" but to MSO we are passing the negative value "suppressRollback"
            this.suppressRollback = rollbackOnFailure != null ? (!rollbackOnFailure) : null;
        }
    }

    public static class Project{
        public final String projectName;

        public Project(String projectName) {
            this.projectName = projectName;
        }
    }

    public static class Platform{
        public final String platformName;

        public Platform(String platformName) {
            this.platformName = platformName;
        }
    }

    public static class LineOfBusiness{
        public final String lineOfBusinessName;

        private LineOfBusiness(String lineOfBusiness) {
            this.lineOfBusinessName = lineOfBusiness;
        }

        public static LineOfBusiness of(String lineOfBusiness) {
            return lineOfBusiness==null ? null : new LineOfBusiness(lineOfBusiness);
        }
    }

    @JsonTypeName("relatedInstance")
    @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
    public static class RelatedInstance{
        public ModelInfo modelInfo;
        @JsonInclude(NON_NULL) public String instanceId; //TODO ask Eylon - is this needed, and if yes, for other fields as well?
        @JsonInclude(NON_NULL) public String instanceName;

        public RelatedInstance (@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                @JsonProperty (value = "instanceId", required = true) String instanceId){
            this.modelInfo = modelInfo;
            this.instanceId = instanceId;
        }

        public RelatedInstance (@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                @JsonProperty (value = "instanceId", required = true) String instanceId,
                                @JsonProperty (value = "instanceName", required = true) String instanceName){
            this.modelInfo = modelInfo;
            this.instanceId = instanceId;
            this.instanceName = instanceName;
        }
    }

    public static class RequestParameters {
        public final List<? extends UserParamTypes> userParams;

        @JsonInclude(NON_NULL) public final String testApi;
        public RequestParameters(List<? extends UserParamTypes> userParams, String testApi) {
            this.userParams = userParams;
            this.testApi = testApi;
        }

        public List<? extends UserParamTypes> getUserParams() {
            return userParams;
        }
        public String getTestApi() {
            return testApi;
        }
    }
}

