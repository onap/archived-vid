package org.openecomp.vid.mso;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.changeManagement.ChangeManagementRequest;
import org.openecomp.vid.changeManagement.RequestDetailsWrapper;
import org.openecomp.vid.controller.MsoController;
import org.openecomp.vid.controller.OperationalEnvironmentController;
import org.openecomp.vid.domain.mso.RequestInfo;
import org.openecomp.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.openecomp.vid.mso.model.OperationalEnvironmentDeactivateInfo;
import org.openecomp.vid.mso.rest.OperationalEnvironment.OperationEnvironmentRequestDetails;
import org.openecomp.vid.mso.rest.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.BadRequestException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static org.openecomp.vid.controller.MsoController.*;
import static org.openecomp.vid.mso.MsoProperties.*;

public class MsoBusinessLogicImpl implements MsoBusinessLogic {

    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
    final static Pattern SOFTWARE_VERSION_PATTERN = Pattern.compile("^[A-Za-z0-9.\\-]+$");
    final static Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+$");
    private static final String ACTIVATE = "/activate";
    private static final String DEACTIVATE = "/deactivate";
    private static final String ENABLE_PORT = "/enablePort";
    private static final String DISABLE_PORT = "/disablePort";
    private final static String RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT = "operationalEnvironment";
    private final static String SOURCE_OPERATIONAL_ENVIRONMENT = "VID";
    final static private ObjectMapper objectMapper = new ObjectMapper();
    /**
     * The Mso REST client
     * This should be replaced with mso client factory.
     */
    private final MsoInterface msoClientInterface;
    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoController.class);

    @Autowired
    public MsoBusinessLogicImpl(MsoInterface msoClientInterface) {
        this.msoClientInterface = msoClientInterface;
    }

    static String validateEndpointPath(String endpointEnvVariable) {
        String endpoint = SystemProperties.getProperty(endpointEnvVariable);
        if (endpoint == null || endpoint.isEmpty()) {
            throw new RuntimeException(endpointEnvVariable + " env variable is not defined");
        }
        return endpoint;
    }

    // this function should get params from tosca and send them to instance at mso, then return success response.
    @Override
    public MsoResponseWrapper createSvcInstance(RequestDetails msoRequest) throws Exception {
        String methodName = "createSvcInstance ";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_SVC_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }

        return msoClientInterface.createSvcInstance(msoRequest, endpoint);
    }

    @Override
    public MsoResponseWrapper createVnf(RequestDetails requestDetails, String serviceInstanceId) throws Exception {
        String methodName = "createVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }

        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        return msoClientInterface.createVnf(requestDetails, vnf_endpoint);
    }

    @Override
    public MsoResponseWrapper createNwInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception {
        String methodName = "createNwInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }

        String nw_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        return msoClientInterface.createNwInstance(requestDetails, nw_endpoint);
    }

    @Override
    public MsoResponseWrapper createVolumeGroupInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception {
        String methodName = "createVolumeGroupInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }

        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnf_endpoint = vnf_endpoint.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);

        return msoClientInterface.createVolumeGroupInstance(requestDetails, vnf_endpoint);
    }

    @Override
    public MsoResponseWrapper createVfModuleInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception {
        String methodName = "createVfModuleInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }

        String partial_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        String vf_module_endpoint = partial_endpoint.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);

        return msoClientInterface.createVfModuleInstance(requestDetails, vf_module_endpoint);
    }

    @Override
    public MsoResponseWrapper createConfigurationInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception {
        String methodName = "createConfigurationInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATIONS);
        endpoint = endpoint.replace(SVC_INSTANCE_ID, serviceInstanceId);

        return msoClientInterface.createConfigurationInstance(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception {
        String methodName = "deleteSvcInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_SVC_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }

        String svc_endpoint = endpoint + "/" + serviceInstanceId;

        return msoClientInterface.deleteSvcInstance(requestDetails, svc_endpoint);
    }

    @Override
    public MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception {
        String methodName = "deleteVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }
        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnf_endpoint = vnf_endpoint + '/' + vnfInstanceId;

        return msoClientInterface.deleteVnf(requestDetails, vnf_endpoint);
    }

    @Override
    public MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId, String vfModuleId) throws Exception {
        String methodName = "deleteVfModule";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }

        String vf__modules_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId).replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);

        String delete_vf_endpoint = vf__modules_endpoint + '/' + vfModuleId;

        return msoClientInterface.deleteVfModule(requestDetails, delete_vf_endpoint);
    }

    @Override
    public MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId, String volumeGroupId) throws Exception {
        String methodName = "deleteVolumeGroupInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }

        String svc_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        String vnf_endpoint = svc_endpoint.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);
        String delete_volume_group_endpoint = vnf_endpoint + "/" + volumeGroupId;

        return msoClientInterface.deleteVolumeGroupInstance(requestDetails, delete_volume_group_endpoint);
    }

    @Override
    public MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String serviceInstanceId, String networkInstanceId) throws Exception {
        String methodName = "deleteNwInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }

        String svc_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        String delete_nw_endpoint = svc_endpoint + "/" + networkInstanceId;

        return msoClientInterface.deleteNwInstance(requestDetails, delete_nw_endpoint);
    }

    @Override
    public MsoResponseWrapper getOrchestrationRequest(String requestId) throws Exception {
        String methodName = "getOrchestrationRequest";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        MsoResponseWrapper w = null;
        try {
            String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQ);
            String path = p + "/" + requestId;

            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);

            msoClientInterface.getOrchestrationRequest(str, "", path, restObjStr);

            return MsoUtil.wrapResponse(restObjStr);

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper getOrchestrationRequests(String filterString) throws Exception {
        String methodName = "getOrchestrationRequest";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        MsoResponseWrapper w = null;
        try {
            String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQS);
            String path = p + filterString;

            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);

            msoClientInterface.getOrchestrationRequest(str, "", path, restObjStr);

            return MsoUtil.wrapResponse(restObjStr);

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public List<Request> getOrchestrationRequestsForDashboard() throws Exception {
        String methodName = "getOrchestrationRequestsForDashboard";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            String path = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQS);
            path += "filter=modelType:EQUALS:vnf";
            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);

            MsoResponseWrapper msoResponseWrapper = msoClientInterface.getOrchestrationRequestsForDashboard(str, "", path, restObjStr);
            List<RequestWrapper> allOrchestrationRequests = deserializeOrchestrationRequestsJson(msoResponseWrapper.getEntity());

            List<Request> filteredOrchestrationRequests = new ArrayList<>();
            for (RequestWrapper currentRequest : allOrchestrationRequests) {
                if ((currentRequest.getRequest() != null) && (currentRequest.getRequest().getRequestScope() == Request.RequestScope.VNF) && ((currentRequest.getRequest().getRequestType() ==
                        Request.RequestType.REPLACE_INSTANCE) || (currentRequest.getRequest().getRequestType() ==
                        Request.RequestType.UPDATE_INSTANCE))) {
                    filteredOrchestrationRequests.add(currentRequest.getRequest());
                }
            }
            return filteredOrchestrationRequests;
        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }

    }

    private List<RequestWrapper> deserializeOrchestrationRequestsJson(String orchestrationRequestsJson) throws Exception {
        String methodName = "deserializeOrchestrationRequestsJson";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);
        RequestList requestList = mapper.readValue(orchestrationRequestsJson, RequestList.class);
        return requestList.getRequestList();
    }


    @Override
    public List<Task> getManualTasksByRequestId(String originalRequestId) throws Exception {
        String methodName = "getManualTasksByRequestId";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_MAN_TASKS);
            String path = p + "?originalRequestId=" + originalRequestId;

            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);

            MsoResponseWrapper msoResponseWrapper = msoClientInterface.getManualTasksByRequestId(str, "", path, restObjStr);
            return deserializeManualTasksJson(msoResponseWrapper.getEntity());

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    private List<Task> deserializeManualTasksJson(String manualTasksJson) throws Exception {
        String methodName = "deserializeManualTasksJson";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        ObjectMapper mapper = new ObjectMapper();
        TaskList taskList = mapper.readValue(manualTasksJson, TaskList.class);
        return taskList.getTaskList();
    }


    @Override
    public MsoResponseWrapper completeManualTask(RequestDetails requestDetails, String taskId) throws Exception {
        String methodName = "completeManualTask";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        MsoResponseWrapper w = null;
        try {
            String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_MAN_TASKS);
            String path = p + "/" + taskId + "/complete";

            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);

            msoClientInterface.completeManualTask(requestDetails, str, "", path, restObjStr);

            return MsoUtil.wrapResponse(restObjStr);

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    @Override
    public MsoResponseWrapper activateServiceInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception {
        String methodName = "activateServiceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        try {
            String serviceEndpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
            String activateServicePath = serviceEndpoint + "/" + serviceInstanceId + "/activate";

            RestObject<String> restObjStr = new RestObject<>();
            String str = "";
            restObjStr.set(str);

            msoClientInterface.setServiceInstanceStatus(requestDetails, str, "", activateServicePath, restObjStr);

            return MsoUtil.wrapResponse(restObjStr);

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }


    @Override
    public MsoResponseWrapperInterface updateVnf(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception {
        String methodName = "updateVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }
        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnf_endpoint = vnf_endpoint + '/' + vnfInstanceId;
        return msoClientInterface.updateVnf(requestDetails, vnf_endpoint);
    }

    @Override
    public MsoResponseWrapperInterface replaceVnf(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception {
        String methodName = "replaceVnf";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint;
        try {
            endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_CHANGE_MANAGEMENT_INSTANCE);
        } catch (Exception exception) {
            throw exception;
        }
        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnf_endpoint = vnf_endpoint.replace(VNF_INSTANCE_ID, vnfInstanceId);
        vnf_endpoint = vnf_endpoint.replace(REQUEST_TYPE, "replace"); //No Constants file, TODO: once you create - add it.
        return msoClientInterface.replaceVnf(requestDetails, vnf_endpoint);
    }

    public RequestDetailsWrapper generateInPlaceMsoRequest(org.openecomp.vid.changeManagement.RequestDetails requestDetails) throws Exception {
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
    public MsoResponseWrapperInterface updateVnfSoftware(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception {
        String methodName = "updateVnfSoftware";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        String vnf_endpoint = generateInPlaceSoftwareUpdateUrl(serviceInstanceId, vnfInstanceId);
        RequestDetailsWrapper finalRequestDetails = generateInPlaceMsoRequest(requestDetails);
        return msoClientInterface.updateVnfSoftware(finalRequestDetails, vnf_endpoint);
    }

    private String generateInPlaceSoftwareUpdateUrl(String serviceInstanceId, String vnfInstanceId) {
        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_VNF_CHANGE_MANAGEMENT_INSTANCE);
        String vnf_endpoint = endpoint.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
        vnf_endpoint = vnf_endpoint.replace(VNF_INSTANCE_ID, vnfInstanceId);
        vnf_endpoint = vnf_endpoint.replace(REQUEST_TYPE, "inPlaceSoftwareUpdate"); //workflow name in mso is different than workflow name in vid UI
        return vnf_endpoint;
    }

    private void validateUpdateVnfSoftwarePayload(RequestDetails requestDetails) throws Exception {
        final String noValidPayloadMsg = "No valid payload in " + ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE + " request";

        if (requestDetails.getRequestParameters() == null || requestDetails.getRequestParameters().getAdditionalProperties() == null) {
            throw new BadRequestException(noValidPayloadMsg);
        }

        Object payloadRaw = requestDetails.getRequestParameters().getAdditionalProperties().get("payload");
        Map payload;

        try {
            payload = objectMapper.readValue((String) payloadRaw, Map.class);
        } catch (Exception exception) {

            throw new BadRequestException(noValidPayloadMsg);
        }
        validateUpdateVnfSoftwarePayloadProperty(payload, noValidPayloadMsg, "existing-software-version", SOFTWARE_VERSION_PATTERN);
        validateUpdateVnfSoftwarePayloadProperty(payload, noValidPayloadMsg, "new-software-version", SOFTWARE_VERSION_PATTERN);

        //if "operations-timeout" is not integer, trying to read it as String that represent a number
        if (!(payload.get("operations-timeout") instanceof Integer)) {
            validateUpdateVnfSoftwarePayloadProperty(payload, noValidPayloadMsg, "operations-timeout", NUMBER_PATTERN);
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
    public MsoResponseWrapper deleteConfiguration(
            RequestDetails requestDetails,
            String serviceInstanceId,
            String configurationId) throws Exception {

        String methodName = "deleteConfiguration";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATION_INSTANCE);
        endpoint = endpoint.replace(SVC_INSTANCE_ID, serviceInstanceId);
        endpoint = endpoint.replace(CONFIGURATION_ID, configurationId);

        return msoClientInterface.deleteConfiguration(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper setConfigurationActiveStatus(
            RequestDetails requestDetails,
            String serviceInstanceId,
            String configurationId,
            boolean isActivate) throws Exception {

        String methodName = "setConfigurationActiveStatus";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATION_INSTANCE);
        endpoint = endpoint.replace(SVC_INSTANCE_ID, serviceInstanceId);
        endpoint = endpoint.replace(CONFIGURATION_ID, configurationId);

        String isActivateState = (isActivate ? ACTIVATE : DEACTIVATE);
        endpoint = endpoint + isActivateState;

        return msoClientInterface.setConfigurationActiveStatus(requestDetails, endpoint);
    }

    @Override
    public MsoResponseWrapper setServiceInstanceStatus(RequestDetails requestDetails , String serviceInstanceId, boolean isActivate)throws Exception{
        String methodName = "setServiceInstanceStatus";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        try {
            String serviceEndpoint = validateEndpointPath(MsoProperties.MSO_REST_API_SVC_INSTANCE);
            String endpoint = serviceEndpoint + "/" + serviceInstanceId;

            String isActivateState = (isActivate ? ACTIVATE : DEACTIVATE);
            endpoint = endpoint + isActivateState;


            RestObject<String> restObjStr = new RestObject<>();
            String str = "";
            restObjStr.set(str);

            msoClientInterface.setServiceInstanceStatus(requestDetails , str, "", endpoint, restObjStr);

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
            boolean isEnable) throws Exception {
        String methodName = "setPortOnConfigurationStatus";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATION_INSTANCE);
        endpoint = endpoint.replace(SVC_INSTANCE_ID, serviceInstanceId);
        endpoint = endpoint.replace(CONFIGURATION_ID, configurationId);

        String isEnablePortStatus = (isEnable ? ENABLE_PORT : DISABLE_PORT);
        endpoint = endpoint + isEnablePortStatus;

        return msoClientInterface.setPortOnConfigurationStatus(requestDetails, endpoint);
    }


    @Override
    public RequestDetails createOperationalEnvironmentActivationRequestDetails(OperationalEnvironmentActivateInfo details) {
        RequestDetails requestDetails = new RequestDetails();
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAdditionalProperty("resourceType", RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT);
        requestInfo.setSource(SOURCE_OPERATIONAL_ENVIRONMENT);
        requestInfo.setRequestorId(details.getUserId());
        requestDetails.setRequestInfo(requestInfo);

        org.openecomp.vid.domain.mso.RelatedInstance relatedInstance = new org.openecomp.vid.domain.mso.RelatedInstance();
        relatedInstance.setAdditionalProperty("resourceType", RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT);
        relatedInstance.setInstanceId(details.getRelatedInstanceId());
        relatedInstance.setInstanceName(details.getRelatedInstanceName());
        requestDetails.setAdditionalProperty("relatedInstanceList", Collections.singletonList(ImmutableMap.of("relatedInstance", relatedInstance)));

        org.openecomp.vid.domain.mso.RequestParameters requestParameters = new org.openecomp.vid.domain.mso.RequestParameters();
        requestParameters.setAdditionalProperty("operationalEnvironmentType", "VNF");
        requestParameters.setAdditionalProperty("workloadContext", details.getWorkloadContext());
        requestParameters.setAdditionalProperty("manifest", details.getManifest());
        requestDetails.setRequestParameters(requestParameters);

        debugRequestDetails(requestDetails);

        return requestDetails;
    }

    @Override
    public String getOperationalEnvironmentActivationPath(OperationalEnvironmentActivateInfo details) {
        String path = validateEndpointPath(MSO_REST_API_OPERATIONAL_ENVIRONMENT_ACTIVATE);
        path = path.replace("<operational_environment_id>", details.getOperationalEnvironmentId());
        return path;
    }

    @Override
    public RequestDetails createOperationalEnvironmentDeactivationRequestDetails(OperationalEnvironmentDeactivateInfo details) {
        RequestDetails requestDetails = new RequestDetails();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAdditionalProperty("resourceType", RESOURCE_TYPE_OPERATIONAL_ENVIRONMENT);
        requestInfo.setSource(SOURCE_OPERATIONAL_ENVIRONMENT);
        requestInfo.setRequestorId(details.getUserId());
        requestDetails.setRequestInfo(requestInfo);

        org.openecomp.vid.domain.mso.RequestParameters requestParameters = new org.openecomp.vid.domain.mso.RequestParameters();
        requestInfo.setAdditionalProperty("operationalEnvironmentType", "VNF");
        requestDetails.setRequestParameters(requestParameters);

        debugRequestDetails(requestDetails);

        return requestDetails;
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

    private void debugRequestDetails(Object requestDetails) {
        if (logger.isDebugEnabled()) {
            String requestDetailsAsString;
            try {
                requestDetailsAsString = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(ImmutableMap.of("requestDetails", requestDetails));
            } catch (JsonProcessingException e) {
                requestDetailsAsString = "error: cannot stringify RequestDetails";
            }
            logger.debug(EELFLoggerDelegate.debugLogger, "requestDetailsAsString: {}", requestDetailsAsString);
        }
    }

    @Override
    public String getOperationalEnvironmentCreationPath() {
        String path = validateEndpointPath(MSO_REST_API_OPERATIONAL_ENVIRONMENT_CREATE);
        return path;
    }

    @Override
    public OperationEnvironmentRequestDetails convertParametersToRequestDetails(OperationalEnvironmentController.OperationalEnvironmentCreateBody input, String userId) {
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

        debugRequestDetails(requestDetails);
        return requestDetails;
    }

    @Override
    public MsoResponseWrapper removeRelationshipFromServiceInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception {
        String methodName = "removeRelationshipFromServiceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String serviceEndpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
        String removeRelationshipsPath = serviceEndpoint + "/" + serviceInstanceId + "/removeRelationships";

        return msoClientInterface.removeRelationshipFromServiceInstance(requestDetails, removeRelationshipsPath);
    }

    @Override
    public MsoResponseWrapper addRelationshipToServiceInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception {
        String methodName = "addRelationshipToServiceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String serviceEndpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
        String addRelationshipsPath = serviceEndpoint + "/" + serviceInstanceId + "/addRelationships";

        return msoClientInterface.addRelationshipToServiceInstance(requestDetails, addRelationshipsPath);
    }


}
