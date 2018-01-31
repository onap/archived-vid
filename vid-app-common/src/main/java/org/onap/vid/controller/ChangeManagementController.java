package org.onap.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.json.simple.JSONArray;
import org.openecomp.portalsdk.core.controller.UnRestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.changeManagement.*;
import org.onap.vid.exceptions.NotFoundException;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.model.MsoExceptionResponse;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.mso.MsoResponseWrapperInterface;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.services.ChangeManagementService;
import org.onap.vid.services.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.onap.vid.utils.Logging.getMethodName;
import static org.springframework.http.HttpStatus.*;

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
    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(ChangeManagementController.class);
    private String fromAppId;
    private final WorkflowService workflowService;
    private final ChangeManagementService changeManagementService;
    private final ObjectMapper objectMapper;


    @Autowired
    public ChangeManagementController(WorkflowService workflowService, ChangeManagementService changeManagementService, ObjectMapper objectMapper) {
        this.fromAppId = "VidChangeManagementController";
        this.workflowService = workflowService;
        this.changeManagementService = changeManagementService;
        this.objectMapper = objectMapper;
    }

    @RequestMapping(value = {"/workflow"}, method = RequestMethod.GET)
    public ResponseEntity<Collection<String>> getWorkflow(@RequestParam("vnfs") Collection<String> vnfs) throws IOException, InterruptedException {
        Collection<String> result = this.workflowService.getWorkflowsForVNFs(vnfs);
        return new ResponseEntity<>(result, OK);
    }

    @RequestMapping(value = {"/mso"}, method = RequestMethod.GET)
    public ResponseEntity<Collection<Request>> getMSOChangeManagements() throws Exception, IOException, InterruptedException {

        Collection<Request> result = this.changeManagementService.getMSOChangeManagements();
        return new ResponseEntity<>(result, OK);
    }

    @RequestMapping(value = "/workflow/{vnfName}", method = RequestMethod.POST)
    public ResponseEntity<String> changeManagement(@PathVariable("vnfName") String vnfName,
                                                   HttpServletRequest request,
                                                   @RequestBody ChangeManagementRequest changeManagmentRequest)
            throws Exception {
        return this.changeManagementService.doChangeManagement(changeManagmentRequest, vnfName);
    }

    @RequestMapping(value = "/uploadConfigUpdateFile", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity uploadConfigUpdateFile(@RequestPart("file") MultipartFile file)
        throws Exception {
        try {
            String jsonString = this.changeManagementService.uploadConfigUpdateFile(file);
            return new ResponseEntity<>(jsonString, HttpStatus.OK);
        }
        catch(WebApplicationException e){
            return new ResponseEntity<>(handleException(e),  HttpStatus.valueOf(e.getResponse().getStatus()));
        }
        catch (Exception e) {
            return new ResponseEntity<>(handleException(e), INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(value = {"/scheduler"}, method = RequestMethod.GET)
    public ResponseEntity<JSONArray> getSchedulerChangeManagements() throws IOException, InterruptedException {
        JSONArray result = this.changeManagementService.getSchedulerChangeManagements();
        return new ResponseEntity<>(result, OK);
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
            return ResponseEntity.status(OK).body(response);
        }
        catch (NotFoundException exception) {
            LOGGER.error(exception.getMessage(), exception);
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
        
        return new ResponseEntity<>(vnfWorkflowRelationResponse, OK);
    }

    @RequestMapping(value = {VNF_WORKFLOW_RELATION}, method = RequestMethod.GET)
    public ResponseEntity getAllWorkflowRelation() throws IOException, InterruptedException {

        try {
            VnfWorkflowRelationAllResponse vnfWorkflowRelationAllResponse = changeManagementService.getAllVnfWorkflowRelations();
            return new ResponseEntity<>(vnfWorkflowRelationAllResponse, OK);
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

        return new ResponseEntity<>(vnfWorkflowRelationResponse, OK);
    }

    private ResponseEntity handleException(Exception exception, String msg) {
        LOGGER.error(msg, exception);
        return new ResponseEntity<>(new VnfWorkflowRelationResponse(Collections.singletonList(msg)), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private ExceptionResponse handleException(Exception e) {
        return ControllersUtils.handleException(e, LOGGER);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value=OK) //return 200 for Backwards compatibility with the previous responses to scheduler
    private MsoResponseWrapperInterface exceptionHandler(Exception e) {
        return exceptionHandler(e, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({
            javax.ws.rs.BadRequestException.class,
    })
    @ResponseStatus(value = OK) //return 200 for Backwards compatibility with the previous responses to scheduler
    public MsoResponseWrapperInterface clientDerivedExceptionAsBadRequest(Exception e) {
        // same handler, different HTTP Code
        return exceptionHandler(e, BAD_REQUEST);
    }

    private MsoResponseWrapperInterface exceptionHandler(Exception e, HttpStatus httpStatus) {
        LOGGER.error(EELFLoggerDelegate.errorLogger, "{}: {}", getMethodName(), ExceptionUtils.getMessage(e), e);
        MsoResponseWrapper2<MsoExceptionResponse> responseWrapper2 = new MsoResponseWrapper2<>(httpStatus.value(), new MsoExceptionResponse(e));
        return responseWrapper2;
    }

}
