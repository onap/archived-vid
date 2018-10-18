/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 Nokia. All rights reserved.
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
package org.onap.vid.mso;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.onap.vid.changeManagement.ChangeManagementRequest;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.controllers.OperationalEnvironmentController;
import org.onap.vid.domain.mso.RequestInfo;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.onap.vid.mso.model.OperationalEnvironmentDeactivateInfo;
import org.onap.vid.mso.rest.OperationalEnvironment.OperationEnvironmentRequestDetails;
import org.onap.vid.mso.rest.*;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.togglz.core.manager.FeatureManager;

import javax.ws.rs.BadRequestException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang.StringUtils.upperCase;
import static org.onap.vid.changeManagement.ChangeManagementRequest.MsoChangeManagementRequest;
import static org.onap.vid.controllers.MsoController.*;
import static org.onap.vid.mso.MsoProperties.*;
import static org.onap.vid.properties.Features.FLAG_UNASSIGN_SERVICE;
import static org.onap.vid.utils.Logging.debugRequestDetails;

public class MsoBusinessLogicImpl implements MsoBusinessLogic {

    static final List<String> DASHBOARD_ALLOWED_TYPES = Stream.of(RequestType.REPLACE_INSTANCE,
            RequestType.UPDATE_INSTANCE,
            RequestType.APPLY_UPDATED_CONFIG,
            RequestType.IN_PLACE_SOFTWARE_UPDATE,
            RequestType.SCALE_OUT)
            .map(requestType -> requestType.toString().toUpperCase())
            .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    private static final String RESOURCE_TYPE = "resourceType";
    /**
     * The Constant dateFormat.
     */
    private static final DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
    private static final Pattern SOFTWARE_VERSION_PATTERN = Pattern.compile("^[A-Za-z0-9.\\-]+$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+$");
    private static final String ACTIVATE = "/activate";
    private static final String DEACTIVATE = "/deactivate";
    private static final String ENABLE_PORT = "/enablePort";
    private static final String DISABLE_PORT = "/disablePort";
    private static final String RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT = "operationalEnvironment";
    private static final String SOURCE_OPERATIONAL_ENVIRONMENT = "VID";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * The logger.
     */
    private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoBusinessLogicImpl.class);
    /**
     * The Mso REST client
     * This should be replaced with mso client factory.
     */
    private final MsoInterface msoClientInterface;
    FeatureManager featureManager;

    @Autowired
    public MsoBusinessLogicImpl(MsoInterface msoClientInterface, FeatureManager featureManager) {
        this.msoClientInterface = msoClientInterface;
        this.featureManager = featureManager;
    }

    public static String validateEndpointPath(String endpointEnvVariable) {
        String endpoint = SystemProperties.getProperty(endpointEnvVariable);
        if (endpoint == null || endpoint.isEmpty()) {
            throw new GenericUncheckedException(endpointEnvVariable + " env variable is not defined");
        }
        return endpoint;
    }

    // this function should get params from tosca and send them to instance at mso, then return success response.
    @Override
    public MsoResponseWrapper createSvcInstance(RequestDetails msoRequest) {
        logInvocationInDebug("createSvcInstance");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_SVC_INSTANCE);

        return msoClientInterface.createSvcInstance(msoRequest, endpoint);
    }

    @Override
    public MsoResponseWrapper createE2eSvcInstance(Object msoRequest) {
        logInvocationInDebug("createE2eSvcInstance");
        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_E2E_SVC_INSTANCE);

        return msoClientInterface.createE2eSvcInstance(msoRequest, endpoint);
    }

    @Override
    public MsoResponseWrapper createVnf(RequestDetails requestDetails, String serviceInstanceId) {
        logInvocationInDebug("createVnf");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE);

        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        return msoClientInterface.createVnf(requestDetails, vnf_endpoint);
    }

    @Override
    public MsoResponseWrapper createNwInstance(RequestDetails requestDetails, String serviceInstanceId) {
        logInvocationInDebug("createNwInstance");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);

        String nw_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        return msoClientInterface.createNwInstance(requestDetails, nw_endpoint);
    }

    @Override
    public MsoResponseWrapper createVolumeGroupInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) {
        logInvocationInDebug("createVolumeGroupInstance");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);

        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnf_endpoint = vnf_endpoint.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);

        return msoClientInterface.createVolumeGroupInstance(requestDetails, vnf_endpoint);
    }

    @Override
    public MsoResponseWrapper createVfModuleInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) {
        logInvocationInDebug("createVfModuleInstance");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);

        String partial_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        String vf_module_endpoint = partial_endpoint.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);

        return msoClientInterface.createVfModuleInstance(requestDetails, vf_module_endpoint);
    }

    @Override
    public MsoResponseWrapper scaleOutVfModuleInstance(org.onap.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) {
        logInvocationInDebug("scaleOutVfModuleInstance");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VF_MODULE_SCALE_OUT);

        String partial_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        String vf_module_endpoint = partial_endpoint.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);
        RequestDetailsWrapper wrapper = new RequestDetailsWrapper();
        requestDetails.setVnfName(null);
        requestDetails.setVnfInstanceId(null);
        wrapper.requestDetails = requestDetails;

        return msoClientInterface.scaleOutVFModuleInstance(wrapper, vf_module_endpoint);
    }

    @Override
    public MsoResponseWrapper createConfigurationInstance(org.onap.vid.mso.rest.RequestDetailsWrapper requestDetailsWrapper, String serviceInstanceId) {
        logInvocationInDebug("createConfigurationInstance");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATIONS);
        endpoint = endpoint.replace(SVC_INSTANCE_ID, serviceInstanceId);

        return msoClientInterface.createConfigurationInstance(requestDetailsWrapper, endpoint);
    }

    @Override
    public MsoResponseWrapper deleteE2eSvcInstance(Object requestDetails, String serviceInstanceId) {
        logInvocationInDebug("deleteE2eSvcInstance");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_E2E_SVC_INSTANCE) + "/" + serviceInstanceId;

        return msoClientInterface.deleteE2eSvcInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String serviceInstanceId, String serviceStatus) {
        logInvocationInDebug("deleteSvcInstance");
        String endpoint;

        if (featureManager.isActive(FLAG_UNASSIGN_SERVICE)) {
            endpoint = validateEndpointPath(MsoProperties.MSO_DELETE_OR_UNASSIGN_REST_API_SVC_INSTANCE);
            if (shouldUnassignService(serviceStatus)) {
                logger.debug(EELFLoggerDelegate.debugLogger, "unassign service");
                String svc_endpoint = endpoint + "/" + serviceInstanceId + "/unassign";
                return msoClientInterface.unassignSvcInstance(requestDetails, svc_endpoint);
            }
        } else {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_SVC_INSTANCE);
        }

        String svc_endpoint = endpoint + "/" + serviceInstanceId;
        return msoClientInterface.deleteSvcInstance(requestDetails, svc_endpoint);
    }

    private boolean shouldUnassignService(String serviceStatus) {
        return ImmutableList.of("created", "pendingdelete", "pending-delete", "assigned").contains(serviceStatus.toLowerCase());
    }

    @Override
    public MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) {
        logInvocationInDebug("deleteVnf");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnf_endpoint = vnf_endpoint + '/' + vnfInstanceId;

        return msoClientInterface.deleteVnf(requestDetails, vnf_endpoint);
    }

    @Override
    public MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId, String vfModuleId) {
        logInvocationInDebug("deleteVfModule");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
        String vf__modules_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId).replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);
        String delete_vf_endpoint = vf__modules_endpoint + '/' + vfModuleId;

        return msoClientInterface.deleteVfModule(requestDetails, delete_vf_endpoint);
    }

    @Override
    public MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId, String volumeGroupId) {
        logInvocationInDebug("deleteVolumeGroupInstance");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
        String svc_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        String vnf_endpoint = svc_endpoint.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);
        String delete_volume_group_endpoint = vnf_endpoint + "/" + volumeGroupId;

        return msoClientInterface.deleteVolumeGroupInstance(requestDetails, delete_volume_group_endpoint);
    }

    @Override
    public MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String serviceInstanceId, String networkInstanceId) {
        logInvocationInDebug("deleteNwInstance");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
        String svc_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        String delete_nw_endpoint = svc_endpoint + "/" + networkInstanceId;

        return msoClientInterface.deleteNwInstance(requestDetails, delete_nw_endpoint);
    }

    @Override
    public MsoResponseWrapper getOrchestrationRequest(String requestId) {
        String methodName = "getOrchestrationRequest";
        logInvocationInDebug(methodName);
        try {
            String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQ);
            String path = p + "/" + requestId;

            return msoClientInterface.getOrchestrationRequest(path);

        } catch (Exception e) {
            logException(methodName, e);
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper getOrchestrationRequests(String filterString) {
        String methodName = "getOrchestrationRequest";
        logInvocationInDebug(methodName);
        try {
            String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQS);
            String path = p + filterString;

            return msoClientInterface.getOrchestrationRequest(path);

        } catch (Exception e) {
            logException(methodName, e);
            throw e;
        }
    }

    @Override
    public List<Request> getOrchestrationRequestsForDashboard() {
        String methodName = "getOrchestrationRequestsForDashboard";
        logInvocationInDebug(methodName);

        List<Request> dashboardOrchestrationReqs = new ArrayList<>();
        try {
            List<RequestWrapper> vnfOrchestrationReqsWrappers = getOrchestrationRequestsByFilter("modelType", "vnf");
            dashboardOrchestrationReqs = vnfOrchestrationReqsWrappers.stream()
                    .filter(reqWrapper -> Objects.nonNull(reqWrapper.getRequest())
                            && DASHBOARD_ALLOWED_TYPES.contains(upperCase(reqWrapper.getRequest().getRequestType())))
                    .map(RequestWrapper::getRequest)
                    .collect(Collectors.toList());

            List<RequestWrapper> scaleOutOrchestrationReqWrappers = getOrchestrationRequestsByFilter("action", "scaleOut");
            List<Request> scaleoutRequests = scaleOutOrchestrationReqWrappers.stream()
                    .filter(reqWrapper -> Objects.nonNull(reqWrapper.getRequest()))
                    .map(RequestWrapper::getRequest)
                    .collect(Collectors.toList());

            dashboardOrchestrationReqs.addAll(scaleoutRequests);
        } catch (Exception e) {
            logException(methodName, e);
        }
        return dashboardOrchestrationReqs;
    }

    private String constructOrchestrationRequestFilter(String filterName, String filterValue) {
        return String.format("%sfilter=%s:EQUALS:%s",
                SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQS), filterName, filterValue);
    }

    private List<RequestWrapper> getOrchestrationRequestsByFilter(String filterName, String filterValue) {
        String orchestrationReqPath = constructOrchestrationRequestFilter(filterName, filterValue);
        RestObject<String> restObjStr = new RestObject<>();
        String str = new String();
        restObjStr.set(str);
        MsoResponseWrapper msoResponseWrapper = msoClientInterface.getOrchestrationRequestsForDashboard(str, "", orchestrationReqPath, restObjStr);
        return deserializeOrchestrationRequestsJson(msoResponseWrapper.getEntity());
    }

    private List<RequestWrapper> deserializeOrchestrationRequestsJson(String orchestrationRequestsJson) {
        logInvocationInDebug("deserializeOrchestrationRequestsJson");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        RequestList requestList;
        try {
            requestList = mapper.readValue(orchestrationRequestsJson, RequestList.class);
        } catch (IOException e) {
            throw new GenericUncheckedException(e);
        }
        return requestList.getRequestList();
    }


    @Override
    public List<Task> getManualTasksByRequestId(String originalRequestId) {
        String methodName = "getManualTasksByRequestId";
        logInvocationInDebug(methodName);

        try {
            String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_MAN_TASKS);
            String path = p + "?originalRequestId=" + originalRequestId;

            RestObject<String> restObjStr = new RestObject<>();
            String str = new String();
            restObjStr.set(str);

            MsoResponseWrapper msoResponseWrapper = msoClientInterface.getManualTasksByRequestId(str, "", path, restObjStr);
            return deserializeManualTasksJson(msoResponseWrapper.getEntity());

        } catch (Exception e) {
            logException(methodName, e);
            throw e;
        }
    }

    private List<Task> deserializeManualTasksJson(String manualTasksJson) {
        logInvocationInDebug("deserializeManualTasksJson");

        ObjectMapper mapper = new ObjectMapper();
        try {
            TaskList taskList = mapper.readValue(manualTasksJson, TaskList.class);
            return taskList.getTaskList();
        } catch (IOException e) {
            throw new GenericUncheckedException(e);
        }
    }


    @Override
    public MsoResponseWrapper completeManualTask(RequestDetails requestDetails, String taskId) {
        String methodName = "completeManualTask";
        logInvocationInDebug(methodName);
        try {
            String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_MAN_TASKS);
            String path = p + "/" + taskId + "/complete";

            RestObject<String> restObjStr = new RestObject<>();
            String str = new String();
            restObjStr.set(str);

            msoClientInterface.completeManualTask(requestDetails, str, "", path, restObjStr);

            return MsoUtil.wrapResponse(restObjStr);

        } catch (Exception e) {
            logException(methodName, e);
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper activateServiceInstance(RequestDetails requestDetails, String serviceInstanceId) {
        String methodName = "activateServiceInstance";
        logInvocationInDebug(methodName);
        try {
            String serviceEndpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
            String activateServicePath = serviceEndpoint + "/" + serviceInstanceId + ACTIVATE;

            RestObject<String> restObjStr = new RestObject<>();
            String str = "";
            restObjStr.set(str);

            msoClientInterface.setServiceInstanceStatus(requestDetails, str, "", activateServicePath, restObjStr);

            return MsoUtil.wrapResponse(restObjStr);

        } catch (Exception e) {
            logException(methodName, e);
            throw e;
        }
    }


    @Override
    public MsoResponseWrapperInterface updateVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) {
        logInvocationInDebug("updateVnf");

        String endpoint;
        endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnf_endpoint = vnf_endpoint + '/' + vnfInstanceId;
        return msoClientInterface.updateVnf(requestDetails, vnf_endpoint);
    }

    @Override
    public MsoResponseWrapperInterface replaceVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) {
        logInvocationInDebug("replaceVnf");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_CHANGE_MANAGEMENT_INSTANCE);
        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnf_endpoint = vnf_endpoint.replace(VNF_INSTANCE_ID, vnfInstanceId);
        vnf_endpoint = vnf_endpoint.replace(REQUEST_TYPE, MsoChangeManagementRequest.REPLACE);
        return msoClientInterface.replaceVnf(requestDetails, vnf_endpoint);
    }

    public RequestDetailsWrapper generateInPlaceMsoRequest(org.onap.vid.changeManagement.RequestDetails requestDetails) {
        validateUpdateVnfSoftwarePayload(requestDetails);
        RequestDetails inPlaceSoftwareUpdateRequest = new RequestDetails();
        inPlaceSoftwareUpdateRequest.setCloudConfiguration(requestDetails.getCloudConfiguration());
        inPlaceSoftwareUpdateRequest.setRequestParameters(requestDetails.getRequestParameters());
        inPlaceSoftwareUpdateRequest.setRequestInfo(requestDetails.getRequestInfo());
        RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();
        requestDetailsWrapper.requestDetails = inPlaceSoftwareUpdateRequest;
        return requestDetailsWrapper;
    }

    @Override
    public RequestDetailsWrapper generateConfigMsoRequest(org.onap.vid.changeManagement.RequestDetails requestDetails) {
        validateUpdateVnfConfig(requestDetails);
        RequestDetails configUpdateRequest = new RequestDetails();
        configUpdateRequest.setRequestParameters(requestDetails.getRequestParameters());
        configUpdateRequest.setRequestInfo(requestDetails.getRequestInfo());
        RequestDetailsWrapper requestDetailsWrapper = new RequestDetailsWrapper();
        requestDetailsWrapper.requestDetails = configUpdateRequest;
        return requestDetailsWrapper;
    }

    @Override
    public MsoResponseWrapperInterface updateVnfSoftware(org.onap.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) {
        logInvocationInDebug("updateVnfSoftware");
        String vnf_endpoint = getChangeManagementEndpoint(serviceInstanceId, vnfInstanceId, MsoChangeManagementRequest.SOFTWARE_UPDATE); //workflow name in mso is different than workflow name in vid UI
        RequestDetailsWrapper finalRequestDetails = generateInPlaceMsoRequest(requestDetails);
        return msoClientInterface.changeManagementUpdate(finalRequestDetails, vnf_endpoint);
    }

    @Override
    public MsoResponseWrapperInterface updateVnfConfig(org.onap.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) {
        logInvocationInDebug("updateVnfConfig");
        RequestDetailsWrapper finalRequestDetails = generateConfigMsoRequest(requestDetails);
        String vnf_endpoint = getChangeManagementEndpoint(serviceInstanceId, vnfInstanceId, MsoChangeManagementRequest.CONFIG_UPDATE);
        return msoClientInterface.changeManagementUpdate(finalRequestDetails, vnf_endpoint);
    }

    private String getChangeManagementEndpoint(String serviceInstanceId, String vnfInstanceId, String vnfRequestType) {
        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_CHANGE_MANAGEMENT_INSTANCE);
        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnf_endpoint = vnf_endpoint.replace(VNF_INSTANCE_ID, vnfInstanceId);
        vnf_endpoint = vnf_endpoint.replace(REQUEST_TYPE, vnfRequestType);
        return vnf_endpoint;
    }

    private Map getChangeManagementPayload(RequestDetails requestDetails, String message) {
        if (requestDetails.getRequestParameters() == null || requestDetails.getRequestParameters().getAdditionalProperties() == null) {
            throw new BadRequestException(message);
        }
        Object payloadRaw = requestDetails.getRequestParameters().getAdditionalProperties().get("payload");
        try {
            return objectMapper.readValue((String) payloadRaw, Map.class);
        } catch (Exception exception) {
            throw new BadRequestException(message);
        }
    }

    private void validateUpdateVnfSoftwarePayload(RequestDetails requestDetails) {
        final String noValidPayloadMsg = "No valid payload in " + ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE + " request";

        Map payload = getChangeManagementPayload(requestDetails, noValidPayloadMsg);
        validateUpdateVnfSoftwarePayloadProperty(payload, noValidPayloadMsg, "existing_software_version", SOFTWARE_VERSION_PATTERN);
        validateUpdateVnfSoftwarePayloadProperty(payload, noValidPayloadMsg, "new_software_version", SOFTWARE_VERSION_PATTERN);

        //if "operations_timeout" is not integer, trying to read it as String that represent a number
        if (!(payload.get("operations_timeout") instanceof Integer)) {
            validateUpdateVnfSoftwarePayloadProperty(payload, noValidPayloadMsg, "operations_timeout", NUMBER_PATTERN);
        }
    }

    private void validateUpdateVnfSoftwarePayloadProperty(Map payload, String noValidPayloadMsg, String propertyName, Pattern pattern) {
        Object forValidation = payload.get(propertyName);
        final String noValidPayloadPropertyMsg = noValidPayloadMsg + ", " + propertyName + " property is not valid";
        if (!(forValidation instanceof String)) {
            throw new BadRequestException(noValidPayloadPropertyMsg);
        }
        if (!pattern.matcher((String) forValidation).matches()) {
            throw new BadRequestException(noValidPayloadPropertyMsg);
        }
    }

    @Override
    public MsoResponseWrapper deleteConfiguration(org.onap.vid.mso.rest.RequestDetailsWrapper requestDetailsWrapper,
                                                  String serviceInstanceId,
                                                  String configurationId) {

        logInvocationInDebug("deleteConfiguration");
        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATION_INSTANCE);
        endpoint = endpoint.replace(SVC_INSTANCE_ID, serviceInstanceId);
        endpoint = endpoint.replace(CONFIGURATION_ID, configurationId);

        return msoClientInterface.deleteConfiguration(requestDetailsWrapper, endpoint);
    }

    @Override
    public MsoResponseWrapper setConfigurationActiveStatus(
            RequestDetails requestDetails,
            String serviceInstanceId,
            String configurationId,
            boolean isActivate) {

        logInvocationInDebug("setConfigurationActiveStatus");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATION_INSTANCE);
        endpoint = endpoint.replace(SVC_INSTANCE_ID, serviceInstanceId);
        endpoint = endpoint.replace(CONFIGURATION_ID, configurationId);

        String isActivateState = (isActivate ? ACTIVATE : DEACTIVATE);
        endpoint = endpoint + isActivateState;

        return msoClientInterface.setConfigurationActiveStatus(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper setServiceInstanceStatus(RequestDetails requestDetails, String serviceInstanceId, boolean isActivate) {
        logInvocationInDebug("setServiceInstanceStatus");
        String methodName = "setServiceInstanceStatus";
        try {
            String serviceEndpoint = validateEndpointPath(MsoProperties.MSO_REST_API_SVC_INSTANCE);
            String endpoint = serviceEndpoint + "/" + serviceInstanceId;

            String isActivateState = (isActivate ? ACTIVATE : DEACTIVATE);
            endpoint = endpoint + isActivateState;


            RestObject<String> restObjStr = new RestObject<>();
            String str = "";
            restObjStr.set(str);

            msoClientInterface.setServiceInstanceStatus(requestDetails, str, "", endpoint, restObjStr);

            return MsoUtil.wrapResponse(restObjStr);

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper setPortOnConfigurationStatus(
            RequestDetails requestDetails,
            String serviceInstanceId,
            String configurationId,
            boolean isEnable) {
        logInvocationInDebug("setPortOnConfigurationStatus");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATION_INSTANCE);
        endpoint = endpoint.replace(SVC_INSTANCE_ID, serviceInstanceId);
        endpoint = endpoint.replace(CONFIGURATION_ID, configurationId);

        String isEnablePortStatus = (isEnable ? ENABLE_PORT : DISABLE_PORT);
        endpoint = endpoint + isEnablePortStatus;

        return msoClientInterface.setPortOnConfigurationStatus(requestDetails, endpoint);
    }


    @Override
    public RequestDetailsWrapper<RequestDetails> createOperationalEnvironmentActivationRequestDetails(OperationalEnvironmentActivateInfo details) {
        RequestDetails requestDetails = new RequestDetails();
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAdditionalProperty(RESOURCE_TYPE, RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT);
        requestInfo.setSource(SOURCE_OPERATIONAL_ENVIRONMENT);
        requestInfo.setRequestorId(details.getUserId());
        requestDetails.setRequestInfo(requestInfo);

        org.onap.vid.domain.mso.RelatedInstance relatedInstance = new org.onap.vid.domain.mso.RelatedInstance();
        relatedInstance.setAdditionalProperty(RESOURCE_TYPE, RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT);
        relatedInstance.setInstanceId(details.getRelatedInstanceId());
        relatedInstance.setInstanceName(details.getRelatedInstanceName());
        requestDetails.setAdditionalProperty("relatedInstanceList", Collections.singletonList(ImmutableMap.of("relatedInstance", relatedInstance)));

        org.onap.vid.domain.mso.RequestParameters requestParameters = new org.onap.vid.domain.mso.RequestParameters();
        requestParameters.setUserParams(null);
        requestParameters.setAdditionalProperty("operationalEnvironmentType", "VNF");
        requestParameters.setAdditionalProperty("workloadContext", details.getWorkloadContext());
        requestParameters.setAdditionalProperty("manifest", details.getManifest());
        requestDetails.setRequestParameters(requestParameters);

        RequestDetailsWrapper<RequestDetails> requestDetailsWrapper = new RequestDetailsWrapper<>(requestDetails);

        debugRequestDetails(requestDetailsWrapper, logger);

        return requestDetailsWrapper;
    }

    @Override
    public String getOperationalEnvironmentActivationPath(OperationalEnvironmentActivateInfo details) {
        String path = validateEndpointPath(MSO_REST_API_OPERATIONAL_ENVIRONMENT_ACTIVATE);
        path = path.replace("<operational_environment_id>", details.getOperationalEnvironmentId());
        return path;
    }

    @Override
    public RequestDetailsWrapper<RequestDetails> createOperationalEnvironmentDeactivationRequestDetails(OperationalEnvironmentDeactivateInfo details) {
        RequestDetails requestDetails = new RequestDetails();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAdditionalProperty(RESOURCE_TYPE, RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT);
        requestInfo.setSource(SOURCE_OPERATIONAL_ENVIRONMENT);
        requestInfo.setRequestorId(details.getUserId());
        requestDetails.setRequestInfo(requestInfo);

        org.onap.vid.domain.mso.RequestParameters requestParameters = new org.onap.vid.domain.mso.RequestParameters();
        requestParameters.setUserParams(null);
        requestParameters.setAdditionalProperty("operationalEnvironmentType", "VNF");
        requestDetails.setRequestParameters(requestParameters);
        RequestDetailsWrapper<RequestDetails> requestDetailsWrapper = new RequestDetailsWrapper<>(requestDetails);
        debugRequestDetails(requestDetailsWrapper, logger);
        return requestDetailsWrapper;
    }

    @Override
    public String getCloudResourcesRequestsStatusPath(String requestId) {
        String path = validateEndpointPath(MSO_REST_API_CLOUD_RESOURCES_REQUEST_STATUS);
        path = path.replace("<request_id>", requestId);
        return path;
    }

    @Override
    public String getOperationalEnvironmentDeactivationPath(OperationalEnvironmentDeactivateInfo details) {
        String path = validateEndpointPath(MSO_REST_API_OPERATIONAL_ENVIRONMENT_DEACTIVATE);
        path = path.replace("<operational_environment_id>", details.getOperationalEnvironmentId());
        return path;
    }

    @Override
    public String getOperationalEnvironmentCreationPath() {
        return validateEndpointPath(MSO_REST_API_OPERATIONAL_ENVIRONMENT_CREATE);
    }


    @Override
    public RequestDetailsWrapper<OperationEnvironmentRequestDetails> convertParametersToRequestDetails(OperationalEnvironmentController.OperationalEnvironmentCreateBody input, String userId) {
        OperationEnvironmentRequestDetails.RequestInfo requestInfo = new OperationEnvironmentRequestDetails.RequestInfo(
                RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT,
                input.getInstanceName(),
                SOURCE_OPERATIONAL_ENVIRONMENT,
                userId);

        OperationEnvironmentRequestDetails.RelatedInstance relatedInstance = new OperationEnvironmentRequestDetails.RelatedInstance(
                RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT,
                input.getEcompInstanceId(),
                input.getEcompInstanceName());

        List<OperationEnvironmentRequestDetails.RelatedInstance> relatedInstanceList = Collections.singletonList((relatedInstance));

        OperationEnvironmentRequestDetails.RequestParameters requestParameters = new OperationEnvironmentRequestDetails.RequestParameters(
                input.getOperationalEnvironmentType(),
                input.getTenantContext(),
                input.getWorkloadContext());

        OperationEnvironmentRequestDetails requestDetails = new OperationEnvironmentRequestDetails(requestInfo, relatedInstanceList, requestParameters);
        RequestDetailsWrapper<OperationEnvironmentRequestDetails> requestDetailsWrapper = new RequestDetailsWrapper<>(requestDetails);
        debugRequestDetails(requestDetailsWrapper, logger);
        return requestDetailsWrapper;
    }

    @Override
    public MsoResponseWrapper removeRelationshipFromServiceInstance(RequestDetails requestDetails, String serviceInstanceId) {
        logInvocationInDebug("removeRelationshipFromServiceInstance");

        String serviceEndpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
        String removeRelationshipsPath = serviceEndpoint + "/" + serviceInstanceId + "/removeRelationships";

        return msoClientInterface.removeRelationshipFromServiceInstance(requestDetails, removeRelationshipsPath);
    }

    @Override
    public MsoResponseWrapper addRelationshipToServiceInstance(RequestDetails requestDetails, String serviceInstanceId) {
        logInvocationInDebug("addRelationshipToServiceInstance");

        String serviceEndpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
        String addRelationshipsPath = serviceEndpoint + "/" + serviceInstanceId + "/addRelationships";

        return msoClientInterface.addRelationshipToServiceInstance(requestDetails, addRelationshipsPath);
    }

    private void validateUpdateVnfConfig(RequestDetails requestDetails) {
        final String noValidPayloadMsg = "No valid payload in " + ChangeManagementRequest.CONFIG_UPDATE + " request";

        Map payload = getChangeManagementPayload(requestDetails, noValidPayloadMsg);
        validateConfigUpdateVnfPayloadProperty(payload, noValidPayloadMsg, "request-parameters");
        validateConfigUpdateVnfPayloadProperty(payload, noValidPayloadMsg, "configuration-parameters");
    }

    private void validateConfigUpdateVnfPayloadProperty(Map payload, String noValidPayloadMsg, String propertyName) {
        final String noValidPayloadPropertyMsg = noValidPayloadMsg + ", " + propertyName + " property is not valid";
        if (!payload.containsKey(propertyName)) {
            throw new BadRequestException(noValidPayloadPropertyMsg);
        }
    }

    private void logInvocationInDebug(String methodName) {
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + "  start");
    }

    private void logException(String methodName, Exception e) {
        logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
    }

    enum RequestType {

        CREATE_INSTANCE("createInstance"),
        DELETE_INSTANCE("deleteInstance"),
        REPLACE_INSTANCE("replaceInstance"),
        UPDATE_INSTANCE("updateInstance"),
        ACTIVATE_INSTANCE("activateInstance"),
        DEACTIVATE_INSTANCE("deactivateInstance"),
        APPLY_UPDATED_CONFIG("applyUpdatedConfig"),
        IN_PLACE_SOFTWARE_UPDATE("inPlaceSoftwareUpdate"),
        SCALE_OUT("scaleOut"),
        UNKNOWN("unknown"),
        NOT_PROVIDED("not provided");
        private static final Map<String, RequestType> CONSTANTS = new HashMap<>();

        static {
            for (RequestType c : values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private final String value;

        RequestType(String value) {
            this.value = value;
        }

        @JsonCreator
        public static RequestType fromValue(String value) {
            RequestType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }
    }
}