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

package org.onap.vid.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapper;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.mso.rest.RequestDetails;
import org.onap.vid.mso.rest.Task;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

//import java.util.UUID;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.RequestEntity;

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
     * The Constant dateFormat.
     */
    final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");

    /**
     * The Constant SVC_INSTANCE_ID.
     */
    public final static String SVC_INSTANCE_ID = "<service_instance_id>";
    public final static String REQUEST_TYPE = "<request_type>";

    /**
     * The Constant CONFIGURATION_ID
     */
    public final static String CONFIGURATION_ID = "<configuration_id>";

    /**
     * The Constant VNF_INSTANCE_ID.
     */
    public final static String VNF_INSTANCE_ID = "<vnf_instance_id>";

    private final MsoBusinessLogic msoBusinessLogic;

    @Autowired
    public MsoController(MsoBusinessLogic msoBusinessLogic) {
        this.msoBusinessLogic = msoBusinessLogic;
    }

    /**
     * Creates the svc instance.
     *
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_svc_instance", method = RequestMethod.POST)
    public ResponseEntity<String> createSvcInstance(HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {
        String methodName = "createSvcInstance";

        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        // always return OK, the MSO status code is embedded in the body

        MsoResponseWrapper w = msoBusinessLogic.createSvcInstance(mso_request);

        return (new ResponseEntity<>(w.getResponse(), HttpStatus.OK));

    }
    
    /**
     * Creates the e2e svc instance.
     *
     * @param request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_e2e_svc_instance", method = RequestMethod.POST)
    public ResponseEntity<String> createE2eSvcInstance(HttpServletRequest request, @RequestBody LinkedHashMap<String, Object> mso_request) throws Exception {
        String methodName = "createE2eSvcInstance";

        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        // always return OK, the MSO status code is embedded in the body

        MsoResponseWrapper w = msoBusinessLogic.createE2eSvcInstance(mso_request.get("requestDetails"));

        return (new ResponseEntity<>(w.getResponse(), HttpStatus.OK));

    }

    /**
     * Creates the vnf.
     *
     * @param serviceInstanceId the service instance id
     * @param request           the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_vnf_instance/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> createVnf(@PathVariable("serviceInstanceId") String serviceInstanceId, HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {

        MsoResponseWrapper w = msoBusinessLogic.createVnf(mso_request, serviceInstanceId);

        // always return OK, the MSO status code is embedded in the body

        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));

    }

    /**
     * Creates the nw instance.
     *
     * @param serviceInstanceId the service instance id
     * @param request           the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_nw_instance/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> createNwInstance(@PathVariable("serviceInstanceId") String serviceInstanceId, HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {
        String methodName = "createNwInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start, serviceInstanceId = " + serviceInstanceId);

        MsoResponseWrapper w = msoBusinessLogic.createNwInstance(mso_request, serviceInstanceId);

        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));

    }

    /**
     * Creates the volume group instance.
     *
     * @param serviceInstanceId the service instance id
     * @param vnfInstanceId     the vnf instance id
     * @param request           the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_volumegroup_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> createVolumeGroupInstance(@PathVariable("serviceInstanceId") String serviceInstanceId, @PathVariable("vnfInstanceId") String vnfInstanceId,
                                                            HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {
        String methodName = "createVolumeGroupInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.createVolumeGroupInstance(mso_request, serviceInstanceId, vnfInstanceId);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));
    }

    /**
     * Creates the vf module instance.
     *
     * @param serviceInstanceId the service instance id
     * @param vnfInstanceId     the vnf instance id
     * @param request           the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_vfmodule_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> createVfModuleInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
                                                         @PathVariable("vnfInstanceId") String vnfInstanceId, HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {
        String methodName = "createVfModuleInstance";

        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.createVfModuleInstance(mso_request, serviceInstanceId, vnfInstanceId);

        // always return OK, the MSO status code is embedded in the body

        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));
    }

    /**
     * Creates a configuration instance.
     *
     * @param serviceInstanceId the service instance id
     * @param request           the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_create_configuration_instance/{serviceInstanceId}/configurations/", method = RequestMethod.POST)
    public ResponseEntity<String> createConfigurationInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
                                                         HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {
        String methodName = "createConfigurationInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.createConfigurationInstance(mso_request, serviceInstanceId);

        // always return OK, the MSO status code is embedded in the body

        return (new ResponseEntity<>(w.getResponse(), HttpStatus.OK));
    }

	/**
     * Delete svc instance.
     *
     * @param serviceInstanceId the service instance id
     * @param request           the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_delete_svc_instance/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteSvcInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
                                                    HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {

        String methodName = "deleteSvcInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.deleteSvcInstance(mso_request, serviceInstanceId);

        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());
        // always return OK, the MSO status code is embedded in the body

        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));

    }

    /**
     * Delete vnf.
     *
     * @param serviceInstanceId the service instance id
     * @param vnfInstanceId     the vnf instance id
     * @param request           the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_delete_vnf_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)

    public ResponseEntity<String> deleteVnf(@PathVariable("serviceInstanceId") String serviceInstanceId, @PathVariable("vnfInstanceId") String vnfInstanceId,
                                            HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {
        String methodName = "deleteVnf";

        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.deleteVnf(mso_request, serviceInstanceId, vnfInstanceId);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));

    }

    /**
     * Delete configuration instance
     * @param serviceInstanceId the service instance id
     * @param configurationId the configuration id
     * @param mso_request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "mso_delete_configuration/{serviceInstanceId}/configurations/{configurationId}",
            method = RequestMethod.POST)
    public ResponseEntity<String> deleteConfiguration(
            @PathVariable("serviceInstanceId") String serviceInstanceId,
            @PathVariable ("configurationId") String configurationId,
            @RequestBody RequestDetails mso_request) throws Exception {

        String methodName = "deleteConfiguration";
        LOGGER.debug(EELFLoggerDelegate.debugLogger,
                dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.deleteConfiguration(mso_request, serviceInstanceId, configurationId);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));
    }

    /**
     * Activate configuration instance
     * @param serviceInstanceId the service instace id
     * @param configurationId the configuration id
     * @param mso_request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "mso_activate_configuration/{serviceInstanceId}/configurations/{configurationId}",
            method = RequestMethod.POST)
    public ResponseEntity<String> activateConfiguration(
            @PathVariable("serviceInstanceId") String serviceInstanceId,
            @PathVariable("configurationId") String configurationId,
            @RequestBody RequestDetails mso_request) throws Exception {

        MsoResponseWrapper w = msoBusinessLogic.setConfigurationActiveStatus(mso_request, serviceInstanceId, configurationId, true);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));
    }

    /**
     * Deactivate configuration instance
     * @param serviceInstanceId the service instace id
     * @param configurationId the configuration id
     * @param mso_request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "mso_deactivate_configuration/{serviceInstanceId}/configurations/{configurationId}",
            method = RequestMethod.POST)
    public ResponseEntity<String> deactivateConfiguration(
            @PathVariable("serviceInstanceId") String serviceInstanceId,
            @PathVariable("configurationId") String configurationId,
            @RequestBody RequestDetails mso_request) throws Exception {

        MsoResponseWrapper w = msoBusinessLogic.setConfigurationActiveStatus(mso_request, serviceInstanceId, configurationId, false);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));
    }

    /**
     * Disable port on configuration instance
     * @param serviceInstanceId the service instance id
     * @param configurationId the configuration instance id
     * @param mso_request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "mso_disable_port_configuration/{serviceInstanceId}/configurations/{configurationId}",
            method = RequestMethod.POST)
    public ResponseEntity<String> disablePortOnConfiguration(
            @PathVariable("serviceInstanceId") String serviceInstanceId,
            @PathVariable("configurationId") String configurationId,
            @RequestBody RequestDetails mso_request) throws Exception {

        MsoResponseWrapper w = msoBusinessLogic.setPortOnConfigurationStatus(mso_request, serviceInstanceId, configurationId, false);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));
    }

    /**
     * Enable port on configuration instance
     * @param serviceInstanceId the service instance id
     * @param configurationId the configuration instance id
     * @param mso_request the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "mso_enable_port_configuration/{serviceInstanceId}/configurations/{configurationId}",
            method = RequestMethod.POST)
    public ResponseEntity<String> enablePortOnConfiguration(
            @PathVariable("serviceInstanceId") String serviceInstanceId,
            @PathVariable("configurationId") String configurationId,
            @RequestBody RequestDetails mso_request) throws Exception {

        MsoResponseWrapper w = msoBusinessLogic.setPortOnConfigurationStatus(mso_request, serviceInstanceId, configurationId, true);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));
    }

    /**
     * Delete vf module.
     *
     * @param serviceInstanceId the service instance id
     * @param vnfInstanceId     the vnf instance id
     * @param vfModuleId        the vf module id
     * @param request           the request
     * @return the response entity
     * @throws Exception the exception
     */
    //mso_delete_vf_module/bc305d54-75b4-431b-adb2-eb6b9e546014/vnfs/fe9000-0009-9999/vfmodules/abeeee-abeeee-abeeee
    @RequestMapping(value = "/mso_delete_vfmodule_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}/vfModules/{vfModuleId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteVfModule(
            @PathVariable("serviceInstanceId") String serviceInstanceId, @PathVariable("vnfInstanceId") String vnfInstanceId,
            @PathVariable("vfModuleId") String vfModuleId, HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {

        String methodName = "deleteVfModule";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.deleteVfModule(mso_request, serviceInstanceId, vnfInstanceId, vfModuleId);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));

    }

    /**
     * Delete volume group instance.
     *
     * @param serviceInstanceId the service instance id
     * @param vnfInstanceId     the vnf instance id
     * @param volumeGroupId     the volume group id
     * @param request           the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_delete_volumegroup_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}/volumeGroups/{volumeGroupId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteVolumeGroupInstance(
            @PathVariable("serviceInstanceId") String serviceInstanceId, @PathVariable("vnfInstanceId") String vnfInstanceId, @PathVariable("volumeGroupId") String volumeGroupId,
            HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {
        String methodName = "deleteVolumeGroupInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.deleteVolumeGroupInstance(mso_request, serviceInstanceId, vnfInstanceId, volumeGroupId);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));
    }

    /**
     * Delete nw instance.
     *
     * @param serviceInstanceId the service instance id
     * @param networkInstanceId the network instance id
     * @param request           the request
     * @return the response entity
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_delete_nw_instance/{serviceInstanceId}/networks/{networkInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> deleteNwInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
                                                   @PathVariable("networkInstanceId") String networkInstanceId, HttpServletRequest request, @RequestBody RequestDetails mso_request) throws Exception {
        String methodName = "deleteNwInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.deleteNwInstance(mso_request, serviceInstanceId, networkInstanceId);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));

    }

    /**
     * Gets the orchestration request.
     *
     * @param requestId the request id
     * @param request   the request
     * @return the orchestration request
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_get_orch_req/{requestId}", method = RequestMethod.GET)
    public ResponseEntity<String> getOrchestrationRequest(@PathVariable("requestId") String requestId,
                                                          HttpServletRequest request) throws Exception {

        String methodName = "getOrchestrationRequest";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");


        MsoResponseWrapper w = msoBusinessLogic.getOrchestrationRequest(requestId);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));
    }


    /**
     * Gets the orchestration requests.
     *
     * @param filterString the filter string
     * @param request      the request
     * @return the orchestration requests
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_get_orch_reqs/{filterString}", method = RequestMethod.GET)
    public ResponseEntity<String> getOrchestrationRequests(@PathVariable("filterString") String filterString,
                                                           HttpServletRequest request) throws Exception {

        String methodName = "getOrchestrationRequests";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");


        MsoResponseWrapper w = msoBusinessLogic.getOrchestrationRequests(filterString);

        // always return OK, the MSO status code is embedded in the body
        return (new ResponseEntity<String>(w.getResponse(), HttpStatus.OK));
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
    public ResponseEntity<String> activateServiceInstance(@PathVariable("serviceInstanceId") String serviceInstanceId, @RequestBody RequestDetails requestDetails) throws Exception {
        String methodName = "activateServiceInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

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
    public ResponseEntity<String> deactivateServiceInstance(@PathVariable("serviceInstanceId") String serviceInstanceId, @RequestBody RequestDetails requestDetails) throws Exception {
        String methodName = "deactivateServiceInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.setServiceInstanceStatus(requestDetails, serviceInstanceId, false);
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }


    /**
     * Gets the orchestration requests for the dashboard.
     *  currently its all the orchestration requests with RequestType updateInstance or replaceInstance.
     * @return the orchestration requests
     * @throws Exception the exception
     */
    @RequestMapping(value = "/mso_get_orch_reqs/dashboard", method = RequestMethod.GET)
    public List<Request> getOrchestrationRequestsForDashboard() throws Exception {

        String methodName = "getOrchestrationRequestsForDashboard";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");


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
    public List<Task> getManualTasksByRequestId(@PathVariable("originalRequestId") String originalRequestId) throws Exception {

        String methodName = "getManualTasksByRequestId";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        return  msoBusinessLogic.getManualTasksByRequestId(originalRequestId);
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
    public ResponseEntity<String> manualTaskComplete(@PathVariable("taskId") String taskId , @RequestBody RequestDetails requestDetails) throws Exception {

        String methodName = "manualTaskComplete";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w = msoBusinessLogic.completeManualTask(requestDetails, taskId);
        return new ResponseEntity<String>(w.getResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mso_remove_relationship/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> removeRelationshipFromServiceInstance(@PathVariable("serviceInstanceId") String serviceInstanceId ,
                                                                        @RequestBody RequestDetails requestDetails) throws Exception {

        String methodName = "removeRelationshipFromServiceInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w;
        try {
            w = msoBusinessLogic.removeRelationshipFromServiceInstance(requestDetails, serviceInstanceId);
        } catch (Exception e){
            LOGGER.error("Internal error when calling MSO controller logic for {}", methodName, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }

    @RequestMapping(value = "/mso_add_relationship/{serviceInstanceId}", method = RequestMethod.POST)
    public ResponseEntity<String> addRelationshipToServiceInstance(@PathVariable("serviceInstanceId") String serviceInstanceId ,
                                                                        @RequestBody RequestDetails requestDetails) throws Exception {

        String methodName = "addRelationshipToServiceInstance";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");

        MsoResponseWrapper w;
        try {
            w = msoBusinessLogic.addRelationshipToServiceInstance(requestDetails, serviceInstanceId);
        } catch (Exception e){
            LOGGER.error("Internal error when calling MSO controller logic for {}", methodName, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(w.getResponse(), HttpStatus.OK);
    }


    /**
     * Exception handler.
     *
     * @param e        the e
     * @param response the response
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @ExceptionHandler(Exception.class)
    private void exceptionHandler(Exception e, HttpServletResponse response) throws IOException {

		/*
         * The following "logger.error" lines "should" be sufficient for logging the exception.
		 * However, the console output in my Eclipse environment is NOT showing ANY of the
		 * logger statements in this class. Thus the temporary "e.printStackTrace" statement
		 * is also included.
		 */

        String methodName = "exceptionHandler";
        LOGGER.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + "." + methodName + e.toString());
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        LOGGER.error(EELFLoggerDelegate.errorLogger, sw.toString());

		/*
         *  Temporary - IF the above  mentioned "logger.error" glitch is resolved ...
		 *  this statement could be removed since it would then likely result in duplicate
		 *  trace output. 
		 */
        e.printStackTrace(System.err);

        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setException(e.getClass().toString().replaceFirst("^.*\\.", ""));
        exceptionResponse.setMessage(e.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));

        response.flushBuffer();

    }

}
