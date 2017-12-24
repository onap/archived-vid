package org.openecomp.vid.mso;

import org.openecomp.vid.changeManagement.RequestDetailsWrapper;
import org.openecomp.vid.controller.OperationalEnvironmentController;
import org.openecomp.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.openecomp.vid.mso.model.OperationalEnvironmentDeactivateInfo;
import org.openecomp.vid.mso.rest.OperationalEnvironment.OperationEnvironmentRequestDetails;
import org.openecomp.vid.mso.rest.Request;
import org.openecomp.vid.mso.rest.RequestDetails;
import org.openecomp.vid.mso.rest.Task;

import java.util.List;

public interface MsoBusinessLogic {

    // this function should get params from tosca and send them to instance at mso, then return success response.
    MsoResponseWrapper createSvcInstance(RequestDetails msoRequest) throws Exception;

    MsoResponseWrapper createVnf(RequestDetails requestDetails, String serviceInstanceId) throws Exception;

    MsoResponseWrapper createNwInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception;

    MsoResponseWrapper createVolumeGroupInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception;

    MsoResponseWrapper createVfModuleInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception;

    MsoResponseWrapper createConfigurationInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception;

    MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception;

    MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception;

    MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId, String vfModuleId) throws Exception;

    MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId, String volumeGroupId)throws Exception;

    MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String serviceInstanceId, String networkInstanceId) throws Exception;

    MsoResponseWrapper getOrchestrationRequest(String requestId)throws Exception;

    MsoResponseWrapper getOrchestrationRequests(String filterString)throws Exception;

    List<Request> getOrchestrationRequestsForDashboard()throws Exception;

    List<Task> getManualTasksByRequestId(String originalRequestId)throws Exception;

    MsoResponseWrapper completeManualTask(RequestDetails requestDetails, String taskId)throws Exception;

    MsoResponseWrapper activateServiceInstance(RequestDetails requestDetails, String serviceInstanceId)throws Exception;

    MsoResponseWrapperInterface updateVnf(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception;

    MsoResponseWrapperInterface replaceVnf(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception;

    MsoResponseWrapperInterface updateVnfSoftware(org.openecomp.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId) throws Exception;

    MsoResponseWrapper deleteConfiguration(
            RequestDetails requestDetails,
            String serviceInstanceId,
            String configurationId) throws Exception;

    MsoResponseWrapper setConfigurationActiveStatus(
            RequestDetails requestDetails,
            String serviceInstanceId,
            String configurationId,
            boolean isActivate) throws Exception;

    MsoResponseWrapper setPortOnConfigurationStatus(
            RequestDetails requestDetails,
            String serviceInstanceId,
            String configurationId,
            boolean isEnable) throws Exception;

    RequestDetails createOperationalEnvironmentActivationRequestDetails(OperationalEnvironmentActivateInfo details);

    String getOperationalEnvironmentActivationPath(OperationalEnvironmentActivateInfo details);

    RequestDetails createOperationalEnvironmentDeactivationRequestDetails(OperationalEnvironmentDeactivateInfo details);

    String getCloudResourcesRequestsStatusPath(String requestId);

    String getOperationalEnvironmentDeactivationPath(OperationalEnvironmentDeactivateInfo details);

    String getOperationalEnvironmentCreationPath();

    OperationEnvironmentRequestDetails convertParametersToRequestDetails(OperationalEnvironmentController.OperationalEnvironmentCreateBody input, String userId);

    MsoResponseWrapper removeRelationshipFromServiceInstance(RequestDetails requestDetails, String serviceInstanceId) throws Exception;

    MsoResponseWrapper addRelationshipToServiceInstance(RequestDetails requestDetails, String serviceInstanceId) throws  Exception;

    MsoResponseWrapper setServiceInstanceStatus(RequestDetails requestDetails , String serviceInstanceId, boolean isActivate)throws Exception;

    RequestDetailsWrapper generateInPlaceMsoRequest(org.openecomp.vid.changeManagement.RequestDetails requestDetails) throws Exception;
}
