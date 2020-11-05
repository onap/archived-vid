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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.onap.vid.mso.rest.SubscriberInfo;

public class ServiceInstantiationRequestDetails {

    @JsonProperty("modelInfo")
    private ModelInfo modelInfo;

    @JsonProperty("owningEntity")
    private ServiceInstantiationOwningEntity owningEntity;

    @JsonProperty("subscriberInfo")
    private SubscriberInfo subscriberInfo;

    @JsonProperty("project")
    private Project project;

    @JsonProperty("requestParameters")
    private RequestParameters requestParameters;

    @JsonProperty("requestInfo")
    private RequestInfo requestInfo;

    public ServiceInstantiationRequestDetails(@JsonProperty(value = "modelInfo", required = true) ModelInfo modelInfo,
                                              @JsonProperty(value = "owningEntity", required = true) ServiceInstantiationOwningEntity owningEntity,
                                              @JsonProperty(value = "subscriberInfo", required = true) SubscriberInfo subscriberInfo,
                                              @JsonProperty(value = "project", required = true) Project project,
                                              @JsonProperty(value = "requestInfo", required = true) RequestInfo requestInfo,
                                              @JsonProperty(value = "requestParameters", required = true) RequestParameters requestParameters) {
        this.modelInfo = modelInfo;
        this.owningEntity = owningEntity;
        this.subscriberInfo = subscriberInfo;
        this.project = project;
        this.requestInfo = requestInfo;
        this.requestParameters = requestParameters;
    }

    public static class  ServiceInstantiationOwningEntity{
        public final String owningEntityId;
        public final String owningEntityName;

        public ServiceInstantiationOwningEntity(String owningEntityId, String owningEntityName) {
            this.owningEntityId = owningEntityId;
            this.owningEntityName = owningEntityName;
        }
    }

    public static class RequestInfo {

        @JsonInclude(NON_EMPTY) public final String instanceName;
        @JsonInclude(NON_EMPTY) public final String productFamilyId;
        public final String source;
        public final boolean suppressRollback;
        public final String requestorId;

        public RequestInfo(String instanceName, String productFamilyId, String source, boolean rollbackOnFailure, String requestorId) {
            this.instanceName = instanceName;
            this.productFamilyId = productFamilyId;
            this.source = source;
            this.requestorId = requestorId;
            // in the FE we are asking for "RollbackOnFailure" but to MSO we are passing the negative value "suppressRollback"
            this.suppressRollback = !rollbackOnFailure;
        }
    }

    public static class Project{
        public final String projectName;

        public Project(String projectName) {
            this.projectName = projectName;
        }
    }

    public static class RequestParameters {
        @JsonInclude(NON_NULL) public final String testApi;
        public final String subscriptionServiceType;
        public final boolean aLaCarte;
        public final List<? extends UserParamTypes> userParams;

        public RequestParameters(String subscriptionServiceType, boolean aLaCarte, List<? extends UserParamTypes> userParams) {
            this(subscriptionServiceType, aLaCarte, userParams, null);
        }

        public RequestParameters(String subscriptionServiceType, boolean aLaCarte, List<? extends UserParamTypes> userParams, String testApi) {
            this.subscriptionServiceType = subscriptionServiceType;
            this.aLaCarte = aLaCarte;
            this.userParams = userParams;
            this.testApi = testApi;
        }
    }

    public static class UserParamNameAndValue implements UserParamTypes {
        private final String name;
        private final String value;

        @JsonCreator
        public UserParamNameAndValue(
            @JsonProperty("name") String name,
            @JsonProperty("value") String value
        ) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof UserParamNameAndValue)) {
                return false;
            }
            UserParamNameAndValue that = (UserParamNameAndValue) o;
            return Objects.equals(getName(), that.getName()) &&
                Objects.equals(getValue(), that.getValue());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getValue());
        }
    }

    public static class HomingSolution implements UserParamTypes {
        private final String homingSolution;

        public HomingSolution(String homingSolution) {
            this.homingSolution = homingSolution;
        }

        @JsonProperty("Homing_Solution")
        public String getHomingSolution() {
            return homingSolution;
        }
    }

    public static class ServiceInstantiationService implements UserParamTypes {
        private final ServiceInstantiationServiceInner serviceInstantiationServiceInner;

        public ServiceInstantiationService(ModelInfo modelInfo, String instanceName, List<Map<String, String>> instanceParams, List<ServiceInstantiationVnf> vnfs, List<ServiceInstantiationPnf> pnfs) {
            serviceInstantiationServiceInner = new ServiceInstantiationServiceInner(modelInfo, instanceName, instanceParams, vnfs, pnfs);
        }

        @JsonProperty("service")
        public ServiceInstantiationServiceInner getServiceInstantiationServiceInner() {
            return serviceInstantiationServiceInner;
        }

        private static class ServiceInstantiationServiceInner implements UserParamTypes {
            public ModelInfo modelInfo = new ModelInfo();
            @JsonInclude(NON_EMPTY)
            public String instanceName;
            public List<Map<String, String>> instanceParams;
            public Resources resources;

            public ServiceInstantiationServiceInner(ModelInfo modelInfo, String instanceName, List<Map<String, String>> instanceParams, List<ServiceInstantiationVnf> vnfs, List<ServiceInstantiationPnf> pnfs) {
                this.modelInfo.setModelType(modelInfo.getModelType());
                this.modelInfo.setModelName(modelInfo.getModelName());
                this.modelInfo.setModelVersionId(modelInfo.getModelVersionId());
                this.instanceName = instanceName;
                this.instanceParams = instanceParams;
                this.resources = new Resources(vnfs, pnfs);
            }
        }
    }

    public static class ServiceInstantiationVnfList{
        public final List<ServiceInstantiationVnf> vnfs;

        public ServiceInstantiationVnfList(List<ServiceInstantiationVnf> vnfList) {
            this.vnfs = vnfList;
        }
    }

    public static class ServiceInstantiationPnfList{

        public final List<ServiceInstantiationPnf> pnfs;

        public ServiceInstantiationPnfList(List<ServiceInstantiationPnf> pnfList) {
            this.pnfs = pnfList;
        }
    }

    public static class ServiceInstantiationVnf{
        public final ModelInfo modelInfo;
        public final CloudConfiguration cloudConfiguration;
        public final Platform platform;
        public final LineOfBusiness lineOfBusiness;
        public final String productFamilyId;
        public final List<Map<String, String>>  instanceParams;
        @JsonInclude(NON_EMPTY) public final List<VfModuleMacro> vfModules;
        @JsonInclude(NON_EMPTY) public final String instanceName;

        public ServiceInstantiationVnf(ModelInfo modelInfo, CloudConfiguration cloudConfiguration, String platform, String lineOfBusiness, String productFamilyId, List<Map<String, String>>  instanceParams, List<VfModuleMacro> vfModules, String instanceName) {
            this.modelInfo = modelInfo;
            this.cloudConfiguration = cloudConfiguration;
            this.platform = new Platform(platform);
            this.lineOfBusiness = new LineOfBusiness(lineOfBusiness);
            this.productFamilyId = productFamilyId;
            this.instanceParams = instanceParams;
            this.vfModules = vfModules;
            this.instanceName = instanceName;
        }
    }

    public static class ServiceInstantiationPnf{

        public final ModelInfo modelInfo;
        public final Platform platform;
        public final LineOfBusiness lineOfBusiness;
        public final String productFamilyId;
        public final List<Map<String, String>>  instanceParams;
        @JsonInclude(NON_EMPTY) public final String instanceName;

        public ServiceInstantiationPnf(ModelInfo modelInfo, String platform, String lineOfBusiness, String productFamilyId, List<Map<String, String>>  instanceParams, String instanceName) {
            this.modelInfo = modelInfo;
            this.platform = new Platform(platform);
            this.lineOfBusiness = new LineOfBusiness(lineOfBusiness);
            this.productFamilyId = productFamilyId;
            this.instanceParams = instanceParams;
            this.instanceName = instanceName;
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

        public LineOfBusiness(String lineOfBusiness) {
            this.lineOfBusinessName = lineOfBusiness;
        }
    }
}

