/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright 2019 Nokia
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

import java.util.List;
import java.util.UUID;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.changeManagement.WorkflowRequestDetail;
import org.onap.vid.controller.OperationalEnvironmentController;
import org.onap.vid.model.SOWorkflowList;
import org.onap.vid.model.SoftDeleteRequest;
import org.onap.vid.mso.model.OperationalEnvironmentActivateInfo;
import org.onap.vid.mso.model.OperationalEnvironmentDeactivateInfo;
import org.onap.vid.mso.rest.OperationalEnvironment.OperationEnvironmentRequestDetails;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.mso.rest.Task;
import org.onap.vid.services.ProbeInterface;

public interface MsoBusinessLogic extends ProbeInterface {

    // this function should get params from tosca and send them to instance at mso, then return success response.
    MsoResponseWrapper createSvcInstance(RequestDetails msoRequest);

    MsoResponseWrapper createE2eSvcInstance(Object msoRequest);

    MsoResponseWrapper deleteE2eSvcInstance(Object requestDetails, String serviceInstanceId);

    MsoResponseWrapper createVnf(RequestDetails requestDetails, String serviceInstanceId);

    MsoResponseWrapper createNwInstance(RequestDetails requestDetails, String serviceInstanceId);

    MsoResponseWrapper createVolumeGroupInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId);

    MsoResponseWrapper createVfModuleInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId);

    MsoResponseWrapper scaleOutVfModuleInstance(org.onap.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId);

    MsoResponseWrapper invokeVnfWorkflow(WorkflowRequestDetail request, String userId, UUID serviceInstanceId, UUID vnfInstanceId, UUID workflow_UUID);

    MsoResponseWrapper createConfigurationInstance(org.onap.vid.mso.rest.RequestDetailsWrapper requestDetailsWrapper, String serviceInstanceId);

    MsoResponseWrapper deleteSvcInstance(RequestDetails requestDetails, String serviceInstanceId, String serviceStatus);

    MsoResponseWrapper deleteVnf(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId);

    MsoResponseWrapper deleteVfModule(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId, String vfModuleId);

    MsoResponseWrapper deleteVolumeGroupInstance(RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId, String volumeGroupId);

    MsoResponseWrapper deleteNwInstance(RequestDetails requestDetails, String serviceInstanceId, String networkInstanceId);

    MsoResponseWrapper getOrchestrationRequest(String requestId);

    MsoResponseWrapper getOrchestrationRequests(String filterString);

    List<Request> getOrchestrationRequestsForDashboard();

    List<Task> getManualTasksByRequestId(String originalRequestId);

    MsoResponseWrapper completeManualTask(RequestDetails requestDetails, String taskId);

    MsoResponseWrapper activateServiceInstance(RequestDetails requestDetails, String serviceInstanceId);

    MsoResponseWrapperInterface updateVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId);

    MsoResponseWrapperInterface replaceVnf(org.onap.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId);

    MsoResponseWrapperInterface updateVnfSoftware(org.onap.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId);

    MsoResponseWrapperInterface updateVnfConfig(org.onap.vid.changeManagement.RequestDetails requestDetails, String serviceInstanceId, String vnfInstanceId);

    MsoResponseWrapper deleteConfiguration(
            org.onap.vid.mso.rest.RequestDetailsWrapper requestDetailsWrapper,
            String serviceInstanceId,
            String configurationId);

    MsoResponseWrapper setConfigurationActiveStatus(
            RequestDetails requestDetails,
            String serviceInstanceId,
            String configurationId,
            boolean isActivate);

    MsoResponseWrapper setPortOnConfigurationStatus(
            RequestDetails requestDetails,
            String serviceInstanceId,
            String configurationId,
            boolean isEnable);

    RequestDetailsWrapper<RequestDetails> createOperationalEnvironmentActivationRequestDetails(OperationalEnvironmentActivateInfo details);

    String getOperationalEnvironmentActivationPath(OperationalEnvironmentActivateInfo details);

    RequestDetailsWrapper<RequestDetails> createOperationalEnvironmentDeactivationRequestDetails(OperationalEnvironmentDeactivateInfo details);

    String getCloudResourcesRequestsStatusPath(String requestId);

    String getOperationalEnvironmentDeactivationPath(OperationalEnvironmentDeactivateInfo details);

    String getOperationalEnvironmentCreationPath();

    RequestDetailsWrapper<OperationEnvironmentRequestDetails> convertParametersToRequestDetails(OperationalEnvironmentController.OperationalEnvironmentCreateBody input, String userId);

    MsoResponseWrapper removeRelationshipFromServiceInstance(RequestDetails requestDetails, String serviceInstanceId);

    MsoResponseWrapper addRelationshipToServiceInstance(RequestDetails requestDetails, String serviceInstanceId);

    MsoResponseWrapper setServiceInstanceStatus(RequestDetails requestDetails , String serviceInstanceId, boolean isActivate);

    RequestDetailsWrapper generateInPlaceMsoRequest(org.onap.vid.changeManagement.RequestDetails requestDetails);

    RequestDetailsWrapper generateConfigMsoRequest(org.onap.vid.changeManagement.RequestDetails requestDetails);

    String getActivateFabricConfigurationPath(String serviceInstanceId);

    String getDeactivateAndCloudDeletePath(String serviceInstanceId, String vnfInstanceId, String vfModuleInstanceId);

    RequestDetails buildRequestDetailsForSoftDelete(SoftDeleteRequest softDeleteRequest);

    SOWorkflowList getWorkflowListByModelId(String modelVersionId);
}
