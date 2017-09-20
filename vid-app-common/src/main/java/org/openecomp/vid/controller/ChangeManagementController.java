package org.openecomp.vid.controller;

import org.json.simple.JSONArray;
import org.openecomp.portalsdk.core.controller.UnRestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
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

import org.openecomp.vid.changeManagement.ChangeManagementRequest;
import org.openecomp.vid.mso.rest.Request;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller to handle ChangeManagement feature requests.
 */
@RestController
@RequestMapping("change-management")
public class ChangeManagementController extends UnRestrictedBaseController {
    private EELFLoggerDelegate logger;
    private String fromAppId;
    private final WorkflowService workflowService;
    private final ChangeManagementService changeManagementService;

    @Autowired
    public ChangeManagementController(WorkflowService workflowService, ChangeManagementService changeManagementService) {
        this.logger = EELFLoggerDelegate.getLogger(ChangeManagementController.class);
        this.fromAppId = "VidChangeManagementController";
        this.workflowService = workflowService;
        this.changeManagementService = changeManagementService;
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
}
