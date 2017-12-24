package org.openecomp.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fj.data.Either;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONArray;
import org.openecomp.portalsdk.core.controller.UnRestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.changeManagement.*;
import org.openecomp.vid.exceptions.NotFoundException;
import org.openecomp.vid.services.ChangeManagementService;
import org.openecomp.vid.services.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.openecomp.vid.mso.rest.Request;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller to handle ChangeManagement feature requests.
 */
@RestController
@RequestMapping(ChangeManagementController.CHANGE_MANAGEMENT)
public class ChangeManagementController extends UnRestrictedBaseController {
    private static final String GetWorkflowsResponse = null;
    public static final String VNF_WORKFLOW_RELATION = "vnf_workflow_relation";
    public static final String CHANGE_MANAGEMENT = "change-management";
    public static final String GET_VNF_WORKFLOW_RELATION = "get_vnf_workflow_relation";
    public static final String SCHEDULER_BY_SCHEDULE_ID = "/scheduler/schedules/{scheduleId}";
    private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(ChangeManagementController.class);
    private String fromAppId;
    private final WorkflowService workflowService;
    private final ChangeManagementService changeManagementService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChangeManagementController(WorkflowService workflowService, ChangeManagementService changeManagementService, ObjectMapper objectMapper) {
        this.logger = EELFLoggerDelegate.getLogger(ChangeManagementController.class);
        this.fromAppId = "VidChangeManagementController";
        this.workflowService = workflowService;
        this.changeManagementService = changeManagementService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = {"/workflow"}, method = RequestMethod.GET)
    public ResponseEntity<Collection<String>> getWorkflow(@RequestParam("vnfs") Collection<String> vnfs) throws IOException, InterruptedException {
        Collection<String> result = this.workflowService.getWorkflowsForVNFs(vnfs);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = {"/mso"}, method = RequestMethod.GET)
    public ResponseEntity<Collection<Request>> getMSOChangeManagements() throws IOException, InterruptedException {
        Collection<Request> result = this.changeManagementService.getMSOChangeManagements();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/workflow/{vnfName}", method = RequestMethod.POST)
    public ResponseEntity<String> changeManagement(@PathVariable("vnfName") String vnfName,
                                                   HttpServletRequest request,
                                                   @RequestBody ChangeManagementRequest changeManagmentRequest)
            throws Exception {
        return this.changeManagementService.doChangeManagement(changeManagmentRequest, vnfName);
    }


    @RequestMapping(value = {"/scheduler"}, method = RequestMethod.GET)
    public ResponseEntity<JSONArray> getSchedulerChangeManagements() throws IOException, InterruptedException {
        JSONArray result = this.changeManagementService.getSchedulerChangeManagements();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = {SCHEDULER_BY_SCHEDULE_ID}, method = RequestMethod.DELETE)
    public ResponseEntity deleteSchedule(@PathVariable("scheduleId") String scheduleId) throws IOException, InterruptedException {
        Pair<String, Integer> result = this.changeManagementService.deleteSchedule(scheduleId);
        return ResponseEntity.status(result.getRight()).build();
    }

    
    @RequestMapping(value = {GET_VNF_WORKFLOW_RELATION}, method = RequestMethod.POST)
    public ResponseEntity getWorkflows(@RequestBody GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest) throws IOException, InterruptedException {
        try {
            GetWorkflowsResponse response = new GetWorkflowsResponse(changeManagementService.getWorkflowsForVnf(getVnfWorkflowRelationRequest));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (NotFoundException exception) {
            logger.error(exception.getMessage(), exception);
            return new ResponseEntity<>(new VnfWorkflowRelationResponse(Collections.singletonList(exception.getMessage())),HttpStatus.NOT_FOUND);
        }
        catch (Exception exception) {
            return handleException(exception, "Failed to get workflows for vnf");
        }
    }
    
    @RequestMapping(value = {VNF_WORKFLOW_RELATION}, method = RequestMethod.POST)
    public ResponseEntity createWorkflowRelation(@RequestBody VnfWorkflowRelationRequest vnfWorkflowRelationRequest) throws IOException, InterruptedException {
    	VnfWorkflowRelationResponse vnfWorkflowRelationResponse;
        try {
            vnfWorkflowRelationResponse = changeManagementService.addVnfWorkflowRelation(vnfWorkflowRelationRequest);
        }
        catch (Exception exception) {
            return handleException(exception, "Failed to add vnf to workflow relation");
        }
        
        return new ResponseEntity<>(vnfWorkflowRelationResponse, HttpStatus.OK);
    }

    @RequestMapping(value = {VNF_WORKFLOW_RELATION}, method = RequestMethod.GET)
    public ResponseEntity getAllWorkflowRelation() throws IOException, InterruptedException {

        try {
            VnfWorkflowRelationAllResponse vnfWorkflowRelationAllResponse = changeManagementService.getAllVnfWorkflowRelations();
            return new ResponseEntity<>(vnfWorkflowRelationAllResponse, HttpStatus.OK);
        }
        catch (Exception exception) {
            return handleException(exception, "Failed to get all vnf to workflow relations");
        }
    }
    
    @RequestMapping(value = {VNF_WORKFLOW_RELATION}, method = RequestMethod.DELETE)
    public ResponseEntity deleteWorkflowRelation(@RequestBody VnfWorkflowRelationRequest vnfWorkflowRelationRequest) throws IOException, InterruptedException {
    	VnfWorkflowRelationResponse vnfWorkflowRelationResponse;
    	try {
    		vnfWorkflowRelationResponse = changeManagementService.deleteVnfWorkflowRelation(vnfWorkflowRelationRequest);
        }
        catch (Exception exception) {
            return handleException(exception, "Failed to delete vnf from workflow relation");
        }

        return new ResponseEntity<>(vnfWorkflowRelationResponse, HttpStatus.OK);
    }

    private ResponseEntity handleException(Exception exception, String msg) {
        logger.error(msg, exception);
        return new ResponseEntity<>(new VnfWorkflowRelationResponse(Collections.singletonList(msg)), HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
