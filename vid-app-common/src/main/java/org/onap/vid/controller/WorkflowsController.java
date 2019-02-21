package org.onap.vid.controller;

import java.util.List;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.vid.model.SOWorkflow;
import org.onap.vid.model.SOWorkflowParameterDefinitions;
import org.onap.vid.services.ExtWorkflowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WorkflowsController.WORKFLOWS_MANAGEMENT)
public class WorkflowsController extends RestrictedBaseController {
    static final String WORKFLOWS_MANAGEMENT = "workflows-management";

    private ExtWorkflowsService extWorkflowsService;

    @Autowired
    public WorkflowsController(ExtWorkflowsService extWorkflowsService) {
        this.extWorkflowsService = extWorkflowsService;
    }

    @RequestMapping(value = "workflows", method = RequestMethod.GET)
    public List<SOWorkflow> getWorkflows(@RequestParam(value = "vnfName") String vnfName){
        return extWorkflowsService.getWorkflows(vnfName);
    }

    @RequestMapping(value = "workflow-parameters/{id}", method = RequestMethod.GET)
    SOWorkflowParameterDefinitions getParameters(@PathVariable Long id) {
        return extWorkflowsService.getWorkflowParameterDefinitions(id);
    }

}

