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

package org.onap.vid.controller;

import static org.onap.vid.utils.KotlinUtilsKt.JACKSON_OBJECT_MAPPER;
import static org.onap.vid.utils.Logging.getMethodName;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.SoftDeleteRequest;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.mso.rest.RequestDetailsWrapper;
import org.onap.vid.mso.rest.Task;
import org.onap.vid.services.CloudOwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class MsoController.
 */
@RestController
@RequestMapping("mso")
public class MsoController extends RestrictedBaseController {

    /**
     * The logger.
     */
    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(MsoController.class);

    /**
     * The Constant SVC_INSTANCE_ID.
     */
    public static final String SVC_INSTANCE_ID = "<service_instance_id>";
    public static final String REQUEST_TYPE = "<request_type>";

    /**
     * The Constant CONFIGURATION_ID
     */
    public static final String CONFIGURATION_ID = "<configuration_id>";

    /**
     * The Constant VNF_INSTANCE_ID.
     */
    public static final String VNF_INSTANCE_ID = "<vnf_instance_id>";
    public static final String WORKFLOW_ID = "<workflow_UUID>";
    public static final String START_LOG = " start";

    private final MsoBusinessLogic msoBusinessLogic;
    private final RestMsoImplementation restMso;
    private final CloudOwnerService cloudOwnerService;

    @Autowired
    public MsoController(MsoBusinessLogic msoBusinessLogic, RestMsoImplementation msoClientInterface, CloudOwnerService cloudOwnerService) {
        this.msoBusinessLogic = msoBusinessLogic;
        this.restMso = msoClientInterface;
        this.cloudOwnerService = cloudOwnerService;
    }

    /**
     * Creates the svc instance.
     *
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_svc_instance", method = RequestMethod.POST)
    public ResponseEntity<String> createSvcInstance(HttpServletRequest request,
        @RequestBody RequestDetails msoRequest) {
        String methodName = "createSvcInstance";

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        // always return OK, the MSO status code is embedded in the body

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic.createSvcInstance(msoRequest);

        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Creates the e2e svc instance.
     *
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_e2e_svc_instance", method = RequestMethod.POST)
    public ResponseEntity<String> createE2eSvcInstance(HttpServletRequest request,
        @RequestBody LinkedHashMap<String, Object> msoRequest) {
        String methodName = "createE2eSvcInstance";

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        // always return OK, the MSO status code is embedded in the body

        //cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic.createE2eSvcInstance(msoRequest.get("requestDetails"));

        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Creates the vnf.
     *
     * @param serviceInstanceId the service instance id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_vnf_instance/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> createVnf(@PathVariable("serviceInstanceId") String serviceInstanceId,
        HttpServletRequest request, @RequestBody RequestDetails msoRequest) {

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic.createVnf(msoRequest, serviceInstanceId);

        // always return OK, the MSO status code is embedded in the body

        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Creates the nw instance.
     *
     * @param serviceInstanceId the service instance id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_nw_instance/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> createNwInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
        HttpServletRequest request, @RequestBody RequestDetails msoRequest) {
        String methodName = "createNwInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger,
            "<== " + methodName + " start, serviceInstanceId = " + serviceInstanceId);

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic.createNwInstance(msoRequest, serviceInstanceId);

        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Creates the volume group instance.
     *
     * @param serviceInstanceId the service instance id
     * @param vnfInstanceId the vnf instance id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_volumegroup_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> createVolumeGroupInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("vnfInstanceId") String vnfInstanceId,
        HttpServletRequest request, @RequestBody RequestDetails msoRequest) {
        String methodName = "createVolumeGroupInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic.createVolumeGroupInstance(msoRequest, serviceInstanceId, vnfInstanceId);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Creates the vf module instance.
     *
     * @param serviceInstanceId the service instance id
     * @param vnfInstanceId the vnf instance id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_vfmodule_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> createVfModuleInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("vnfInstanceId") String vnfInstanceId, HttpServletRequest request,
        @RequestBody RequestDetails msoRequest) {
        String methodName = "createVfModuleInstance";

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic.createVfModuleInstance(msoRequest, serviceInstanceId, vnfInstanceId);

        // always return OK, the MSO status code is embedded in the body

        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Creates a configuration instance.
     *
     * @param serviceInstanceId the service instance id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_configuration_instance/{serviceInstanceId}/configurations/", method = RequestMethod.POST)
    public ResponseEntity<String> createConfigurationInstance(
        @PathVariable("serviceInstanceId") String serviceInstanceId,
        HttpServletRequest request, @RequestBody RequestDetailsWrapper msoRequest) {
        String methodName = "createConfigurationInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest.getRequestDetails());
        MsoResponseWrapper w = msoBusinessLogic.createConfigurationInstance(msoRequest, serviceInstanceId);

        // always return OK, the MSO status code is embedded in the body

        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Delete E2e svc instance.
     *
     * @param serviceInstanceId the service instance id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_delete_e2e_svc_instance/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteE2eSvcInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
        HttpServletRequest request, @RequestBody LinkedHashMap<String, Object> msoRequest) {

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodName(), msoRequest);
        MsoResponseWrapper w = msoBusinessLogic
            .deleteE2eSvcInstance(msoRequest.get("requestDetails"), serviceInstanceId);
        // always return OK, the MSO status code is embedded in the body

        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Delete svc instance.
     *
     * @param serviceInstanceId the service instance id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_delete_svc_instance/{serviceInstanceId}", method = RequestMethod.POST)
    public String deleteSvcInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
        HttpServletRequest request, @RequestBody RequestDetails msoRequest,
        @RequestParam(value = "serviceStatus") String serviceStatus) {

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodName(), msoRequest);
        MsoResponseWrapper w = msoBusinessLogic.deleteSvcInstance(msoRequest, serviceInstanceId, serviceStatus);
        // always return OK, the MSO status code is embedded in the body

        return w.getResponse();
    }

    /**
     * Delete vnf.
     *
     * @param serviceInstanceId the service instance id
     * @param vnfInstanceId the vnf instance id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_delete_vnf_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteVnf(@PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("vnfInstanceId") String vnfInstanceId,
        HttpServletRequest request, @RequestBody RequestDetails msoRequest) {
        String methodName = "deleteVnf";

        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);
        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic.deleteVnf(msoRequest, serviceInstanceId, vnfInstanceId);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Delete configuration instance
     *
     * @param serviceInstanceId the service instance id
     * @param configurationId the configuration id
     * @param msoRequest the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "mso_delete_configuration/{serviceInstanceId}/configurations/{configurationId}",
        method = RequestMethod.POST)
    public ResponseEntity<String> deleteConfiguration(
        @PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("configurationId") String configurationId,
        @RequestBody RequestDetailsWrapper msoRequest) {

        String methodName = "deleteConfiguration";
        LOGGER.debug(EELFLoggerDelegate.debugLogger,
            "<== " + methodName + START_LOG);

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest.getRequestDetails());
        MsoResponseWrapper w = msoBusinessLogic.deleteConfiguration(msoRequest, serviceInstanceId, configurationId);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Activate configuration instance
     *
     * @param serviceInstanceId the service instace id
     * @param configurationId the configuration id
     * @param msoRequest the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "mso_activate_configuration/{serviceInstanceId}/configurations/{configurationId}",
        method = RequestMethod.POST)
    public ResponseEntity<String> activateConfiguration(
        @PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("configurationId") String configurationId,
        @RequestBody RequestDetails msoRequest) {

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic
            .setConfigurationActiveStatus(msoRequest, serviceInstanceId, configurationId, true);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Deactivate configuration instance
     *
     * @param serviceInstanceId the service instace id
     * @param configurationId the configuration id
     * @param msoRequest the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "mso_deactivate_configuration/{serviceInstanceId}/configurations/{configurationId}",
        method = RequestMethod.POST)
    public ResponseEntity<String> deactivateConfiguration(
        @PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("configurationId") String configurationId,
        @RequestBody RequestDetails msoRequest) {

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic
            .setConfigurationActiveStatus(msoRequest, serviceInstanceId, configurationId, false);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Disable port on configuration instance
     *
     * @param serviceInstanceId the service instance id
     * @param configurationId the configuration instance id
     * @param msoRequest the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "mso_disable_port_configuration/{serviceInstanceId}/configurations/{configurationId}",
        method = RequestMethod.POST)
    public ResponseEntity<String> disablePortOnConfiguration(
        @PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("configurationId") String configurationId,
        @RequestBody RequestDetails msoRequest) {

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic
            .setPortOnConfigurationStatus(msoRequest, serviceInstanceId, configurationId, false);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Enable port on configuration instance
     *
     * @param serviceInstanceId the service instance id
     * @param configurationId the configuration instance id
     * @param msoRequest the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "mso_enable_port_configuration/{serviceInstanceId}/configurations/{configurationId}",
        method = RequestMethod.POST)
    public ResponseEntity<String> enablePortOnConfiguration(
        @PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("configurationId") String configurationId,
        @RequestBody RequestDetails msoRequest) {

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic
            .setPortOnConfigurationStatus(msoRequest, serviceInstanceId, configurationId, true);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Delete vf module.
     *
     * @param serviceInstanceId the service instance id
     * @param vnfInstanceId the vnf instance id
     * @param vfModuleId the vf module id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_delete_vfmodule_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}/vfModules/{vfModuleId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteVfModule(
        @PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("vnfInstanceId") String vnfInstanceId,
        @PathVariable("vfModuleId") String vfModuleId, HttpServletRequest request,
        @RequestBody RequestDetails msoRequest) {

        String methodName = "deleteVfModule";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic
            .deleteVfModule(msoRequest, serviceInstanceId, vnfInstanceId, vfModuleId);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Delete volume group instance.
     *
     * @param serviceInstanceId the service instance id
     * @param vnfInstanceId the vnf instance id
     * @param volumeGroupId the volume group id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_delete_volumegroup_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}/volumeGroups/{volumeGroupId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteVolumeGroupInstance(
        @PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("vnfInstanceId") String vnfInstanceId, @PathVariable("volumeGroupId") String volumeGroupId,
        HttpServletRequest request, @RequestBody RequestDetails msoRequest) {
        String methodName = "deleteVolumeGroupInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic
            .deleteVolumeGroupInstance(msoRequest, serviceInstanceId, vnfInstanceId, volumeGroupId);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Delete nw instance.
     *
     * @param serviceInstanceId the service instance id
     * @param networkInstanceId the network instance id
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_delete_nw_instance/{serviceInstanceId}/networks/{networkInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteNwInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
        @PathVariable("networkInstanceId") String networkInstanceId, HttpServletRequest request,
        @RequestBody RequestDetails msoRequest) {
        String methodName = "deleteNwInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        cloudOwnerService.enrichRequestWithCloudOwner(msoRequest);
        MsoResponseWrapper w = msoBusinessLogic.deleteNwInstance(msoRequest, serviceInstanceId, networkInstanceId);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Gets the orchestration request.
     *
     * @param requestId the request id
     * @param request the request
     * @return the orchestration request
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_get_orch_req/{requestId}", method = RequestMethod.GET)
    public ResponseEntity<String> getOrchestrationRequest(@PathVariable("requestId") String requestId,
        HttpServletRequest request) {

        String methodName = "getOrchestrationRequest";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        MsoResponseWrapper w = msoBusinessLogic.getOrchestrationRequest(requestId);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Gets the orchestration requests.
     *
     * @param filterString the filter string
     * @param request the request
     * @return the orchestration requests
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_get_orch_reqs/{filterString}", method = RequestMethod.GET)
    public ResponseEntity<String> getOrchestrationRequests(@PathVariable("filterString") String filterString,
        HttpServletRequest request) {

        String methodName = "getOrchestrationRequests";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        MsoResponseWrapper w = msoBusinessLogic.getOrchestrationRequests(filterString);

        // always return OK, the MSO status code is embedded in the body
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * activate to a pnf instance.
     *
     * @param serviceInstanceId the id of the service.
     * @param requestDetails the body of the request.
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_activate_service_instance/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> activateServiceInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
        @RequestBody RequestDetails requestDetails) {
        String methodName = "activateServiceInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        MsoResponseWrapper w = msoBusinessLogic.setServiceInstanceStatus(requestDetails, serviceInstanceId, true);
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * deactivate a service instance.
     *
     * @param serviceInstanceId the id of the service.
     * @param requestDetails the body of the request.
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_deactivate_service_instance/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> deactivateServiceInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
        @RequestBody RequestDetails requestDetails) {
        String methodName = "deactivateServiceInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        MsoResponseWrapper w = msoBusinessLogic.setServiceInstanceStatus(requestDetails, serviceInstanceId, false);
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    /**
     * Gets the orchestration requests for the dashboard.
     * currently its all the orchestration requests with RequestType updateInstance or replaceInstance.
     *
     * @return the orchestration requests
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_get_orch_reqs/dashboard", method = RequestMethod.GET)
    public List<Request> getOrchestrationRequestsForDashboard() {

        String methodName = "getOrchestrationRequestsForDashboard";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        return msoBusinessLogic.getOrchestrationRequestsForDashboard();
    }

    /**
     * Gets the Manual Tasks for the given request id.
     *
     * @param originalRequestId the id of the original request.
     * @return the tasks
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_get_man_task/{originalRequestId}", method = RequestMethod.GET)
    public List<Task> getManualTasksByRequestId(@PathVariable("originalRequestId") String originalRequestId) {

        String methodName = "getManualTasksByRequestId";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        return msoBusinessLogic.getManualTasksByRequestId(originalRequestId);
    }

    /**
     * Complete the manual task.
     *
     * @param taskId the id of the task to complete.
     * @param requestDetails the body of the request.
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_post_man_task/{taskId}", method = RequestMethod.POST)
    public ResponseEntity<String> manualTaskComplete(@PathVariable("taskId") String taskId,
        @RequestBody RequestDetails requestDetails) {

        String methodName = "manualTaskComplete";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        MsoResponseWrapper w = msoBusinessLogic.completeManualTask(requestDetails, taskId);
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mso_remove_relationship/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> removeRelationshipFromServiceInstance(
        @PathVariable("serviceInstanceId") String serviceInstanceId,
        @RequestBody RequestDetails requestDetails) {

        String methodName = "removeRelationshipFromServiceInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        MsoResponseWrapper w;
        try {
            w = msoBusinessLogic.removeRelationshipFromServiceInstance(requestDetails, serviceInstanceId);
        } catch (Exception e) {
            LOGGER.error("Internal error when calling MSO controller logic for {}", methodName, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mso_add_relationship/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> addRelationshipToServiceInstance(
        @PathVariable("serviceInstanceId") String serviceInstanceId,
        @RequestBody RequestDetails requestDetails) {

        String methodName = "addRelationshipToServiceInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        MsoResponseWrapper w;
        try {
            w = msoBusinessLogic.addRelationshipToServiceInstance(requestDetails, serviceInstanceId);
        } catch (Exception e) {
            LOGGER.error("Internal error when calling MSO controller logic for {}", methodName, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mso_activate_fabric_configuration/{serviceInstanceId}", method = RequestMethod.POST)
    public MsoResponseWrapper2 activateFabricConfiguration(
            @PathVariable("serviceInstanceId") String serviceInstanceId ,
            @RequestBody RequestDetails requestDetails) {

        String methodName = "activateFabricConfiguration";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        String path = msoBusinessLogic.getActivateFabricConfigurationPath(serviceInstanceId);
        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(requestDetails, path, RequestReferencesContainer.class);

        return new MsoResponseWrapper2<>(msoResponse);
    }

    @RequestMapping(value = "/mso_vfmodule_soft_delete/{serviceInstanceId}/{vnfInstanceId}/{vfModuleInstanceId}", method = RequestMethod.POST)
    public MsoResponseWrapper2 deactivateAndCloudDelete(
            @PathVariable("serviceInstanceId") String serviceInstanceId,
            @PathVariable("vnfInstanceId") String vnfInstanceId,
            @PathVariable("vfModuleInstanceId") String vfModuleInstanceId,
            @RequestBody SoftDeleteRequest softDeleteRequest) {

        String methodName = "deactivateAndCloudDelete";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + START_LOG);

        String path = msoBusinessLogic.getDeactivateAndCloudDeletePath(serviceInstanceId, vnfInstanceId, vfModuleInstanceId);
        RequestDetails requestDetails = msoBusinessLogic.buildRequestDetailsForSoftDelete(softDeleteRequest);

        cloudOwnerService.enrichRequestWithCloudOwner(requestDetails);
        RestObject<RequestReferencesContainer> msoResponse = restMso.PostForObject(new org.onap.vid.changeManagement.RequestDetailsWrapper<>(requestDetails), path, RequestReferencesContainer.class);

        return new MsoResponseWrapper2<>(msoResponse);
    }

    /**
     * Exception handler.
     *
     * @param e the e
     * @param response the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @ExceptionHandler(Exception.class)
    private void exceptionHandler(Exception e, HttpServletResponse response) throws IOException {

        ControllersUtils.handleException(e, LOGGER);

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setException(e.getClass().toString().replaceFirst("^.*\\.", ""));
        exceptionResponse.setMessage(e.getMessage());

        response.getWriter().write(JACKSON_OBJECT_MAPPER.writeValueAsString(exceptionResponse));

        response.flushBuffer();
    }
}
