package org.openecomp.vid.mso;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import org.openecomp.vid.controller.MsoController;
import org.openecomp.vid.domain.mso.RequestInfo;
import org.openecomp.vid.domain.mso.RequestParameters;
import org.openecomp.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.openecomp.vid.mso.model.OperationalEnvironmentDeactivateInfo;
import org.openecomp.vid.mso.rest.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.openecomp.vid.controller.MsoController.*;
import static org.openecomp.vid.mso.MsoProperties.MSO_REST_API_CLOUD_RESOURCES_REQUEST_STATUS;
import static org.openecomp.vid.mso.MsoProperties.MSO_REST_API_OPERATIONAL_ENVIRONMENT_ACTIVATE;
import static org.openecomp.vid.mso.MsoProperties.MSO_REST_API_OPERATIONAL_ENVIRONMENT_CREATE;
import static org.openecomp.vid.mso.MsoProperties.MSO_REST_API_OPERATIONAL_ENVIRONMENT_DEACTIVATE;

/**
 * Created by pickjonathan on 19/06/2017.
 */
public class MsoBusinessLogic {

    private static final String ACTIVATE = "/activate";
    private static final String DEACTIVATE = "/deactivate";
    private static final String ENABLE_PORT = "/enablePort";
    private static final String DISABLE_PORT = "/disablePort";

    /**
     * The Mso REST client
     * This should be replaced with mso client factory.
     */
    private MsoInterface msoClientInterface;

    /**
     * The logger.
     */
    EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoController.class);

    /**
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

    public MsoBusinessLogic() {
        msoClientInterface = MsoRestInterfaceFactory.getInstance();
    }

    // this function should get params from tosca and send them to instance at mso, then return success response.
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

    public MsoResponseWrapper createVfModuleInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception{
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

    public MsoResponseWrapper createConfigurationInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception{
        String methodName = "createConfigurationInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        String endpoint = validateEndpointPath(MsoProperties.MSO_REST_API_CONFIGURATIONS);
        endpoint = endpoint.replace(SVC_INSTANCE_ID, serviceInstanceId);

        return msoClientInterface.createConfigurationInstance(requestDetails, endpoint);
    }

    public MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception{
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

    public MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception{
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

    public MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId, String vfModuleId) throws Exception{
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

    public MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId, String volumeGroupId)throws Exception{
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

    public MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String serviceInstanceId, String networkInstanceId) throws Exception{
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

    public MsoResponseWrapper getOrchestrationRequest(String requestId)throws Exception{
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

    public MsoResponseWrapper getOrchestrationRequests(String filterString)throws Exception{
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

    public List<Request> getOrchestrationRequestsForDashboard()throws Exception{
        String methodName = "getOrchestrationRequestsForDashboard";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        try {
            String path = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQS);
            path += "filter=modelType:EQUALS:vnf";
            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);

            MsoResponseWrapper msoResponseWrapper =  msoClientInterface.getOrchestrationRequestsForDashboard(str, "", path, restObjStr);
            List<RequestWrapper> allOrchestrationRequests = deserializeOrchestrationRequestsJson(msoResponseWrapper.getEntity());

            List<Request> filteredOrchestrationRequests = new ArrayList<>();
            for (RequestWrapper currentRequest:allOrchestrationRequests){
                if ((currentRequest.getRequest() != null) && (currentRequest.getRequest().getRequestScope() == Request.RequestScope.VNF) && ((currentRequest.getRequest().getRequestType() ==
                        Request.RequestType.REPLACE_INSTANCE)||(currentRequest.getRequest().getRequestType() ==
                        Request.RequestType.UPDATE_INSTANCE) )) {
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
        RequestList requestList = mapper.readValue(orchestrationRequestsJson , RequestList.class);
        return requestList.getRequestList();
    }


    public List<Task> getManualTasksByRequestId(String originalRequestId)throws Exception{
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

    private List<Task> deserializeManualTasksJson(String manualTasksJson) throws Exception{
        String methodName = "deserializeManualTasksJson";
        logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");

        ObjectMapper mapper = new ObjectMapper();
        TaskList taskList = mapper.readValue(manualTasksJson , TaskList.class);
        return taskList.getTaskList();
    }


    public MsoResponseWrapper completeManualTask(RequestDetails requestDetails , String taskId)throws Exception{
        String methodName = "completeManualTask";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        MsoResponseWrapper w = null;
        try {
            String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_MAN_TASKS);
            String path = p + "/" + taskId + "/complete";

            RestObject<String> restObjStr = new RestObject<String>();
            String str = new String();
            restObjStr.set(str);

            msoClientInterface.completeManualTask(requestDetails , str, "", path, restObjStr);

            return MsoUtil.wrapResponse(restObjStr);

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }

    public MsoResponseWrapper activateServiceInstance(RequestDetails requestDetails , String serviceInstanceId)throws Exception{
        String methodName = "activateServiceInstance";
        logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
        try {
            String serviceEndpoint = SystemProperties.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
            String activateServicePath = serviceEndpoint + "/" + serviceInstanceId  + "/activate";

            RestObject<String> restObjStr = new RestObject<>();
            String str = "";
            restObjStr.set(str);

            msoClientInterface.activateServiceInstance(requestDetails , str, "", activateServicePath, restObjStr);

            return MsoUtil.wrapResponse(restObjStr);

        } catch (Exception e) {
            logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
            throw e;
        }
    }




    public static String validateEndpointPath(String endpointEnvVariable) {
        String endpoint = SystemProperties.getProperty(endpointEnvVariable);
        if (endpoint == null || endpoint.isEmpty()) {
            throw new RuntimeException(endpointEnvVariable + " env variable is not defined");
        }
        return endpoint;
    }

	public MsoResponseWrapper updateVnf(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception {
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
	
	public MsoResponseWrapper replaceVnf(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception {
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


    public RequestDetails createOperationalEnvironmentActivationRequestDetails(OperationalEnvironmentActivateInfo details) {
        RequestDetails requestDetails = new RequestDetails();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAdditionalProperty("resourceType", "operationalEnvironment");
        requestInfo.setSource("VID");
        requestInfo.setRequestorId(details.getUserId());
        requestDetails.setRequestInfo(requestInfo);

        org.openecomp.vid.domain.mso.RelatedInstance relatedInstance = new org.openecomp.vid.domain.mso.RelatedInstance();
        relatedInstance.setAdditionalProperty("resourceType", "operationalEnvironment");
        relatedInstance.setInstanceId(details.getRelatedInstanceId());
        relatedInstance.setInstanceName(details.getRelatedInstanceName());
        requestDetails.setAdditionalProperty("relatedInstanceList", Collections.singletonList(ImmutableMap.of("relatedInstance", relatedInstance)));

        RequestParameters requestParameters = new RequestParameters();
        requestParameters.setAdditionalProperty("operationalEnvironmentType", "VNF");
        requestParameters.setAdditionalProperty("workloadContext", details.getWorkloadContext());
        requestParameters.setAdditionalProperty("manifest", details.getManifest());
        requestDetails.setRequestParameters(requestParameters);

        debugRequestDetails(requestDetails);

        return requestDetails;
    }

    public String getOperationalEnvironmentActivationPath(OperationalEnvironmentActivateInfo details) {
        String path = validateEndpointPath(MSO_REST_API_OPERATIONAL_ENVIRONMENT_ACTIVATE);
        path = path.replace("<operational_environment_id>", details.getOperationalEnvironmentId());
        return path;
    }

    public RequestDetails createOperationalEnvironmentDeactivationRequestDetails(OperationalEnvironmentDeactivateInfo details) {
        RequestDetails requestDetails = new RequestDetails();

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setAdditionalProperty("resourceType", "operationalEnvironment");
        requestInfo.setSource("VID");
        requestInfo.setRequestorId(details.getUserId());
        requestDetails.setRequestInfo(requestInfo);

        RequestParameters requestParameters = new RequestParameters();
        requestInfo.setAdditionalProperty("operationalEnvironmentType", "VNF");
        requestDetails.setRequestParameters(requestParameters);

        debugRequestDetails(requestDetails);

        return requestDetails;
    }

    public String getCloudResourcesRequestsStatusPath(String requestId) {
        String path = validateEndpointPath(MSO_REST_API_CLOUD_RESOURCES_REQUEST_STATUS);
        path = path.replace("<request_id>", requestId);
        return path;
    }

    public String getOperationalEnvironmentDeactivationPath(OperationalEnvironmentDeactivateInfo details) {
        String path = validateEndpointPath(MSO_REST_API_OPERATIONAL_ENVIRONMENT_DEACTIVATE);
        path = path.replace("<operational_environment_id>", details.getOperationalEnvironmentId());
        return path;
    }

    private void debugRequestDetails(RequestDetails requestDetails) {
        if (logger.isDebugEnabled()) {
            String requestDetailsAsString;
            try {
                requestDetailsAsString = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(ImmutableMap.of("requestDetails", requestDetails));
            } catch (JsonProcessingException e) {
                requestDetailsAsString = "error: cannot stringify ActivationRequestDetails";
            }
            logger.debug(EELFLoggerDelegate.debugLogger, "requestDetailsAsString: {}", requestDetailsAsString);
        }
    }

    public String getOperationalEnvironmentCreationPath() {
        String path = validateEndpointPath(MSO_REST_API_OPERATIONAL_ENVIRONMENT_CREATE);
        return path;
    }



}
