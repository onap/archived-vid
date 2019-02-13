package org.onap.vid.controller;

import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.vid.model.SOWorkflowParameterDefinitions;
import org.onap.vid.services.ExtWorkflowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(WorkflowsController.WORKFLOWS_MANAGEMENT)
class WorkflowParameterController extends RestrictedBaseController {


    private ExtWorkflowsService extWorkflowsService;

    @Autowired
    WorkflowParameterController(ExtWorkflowsService extWorkflowsService) {
        this.extWorkflowsService = extWorkflowsService;
    }

    @RequestMapping(value = "workflow-parameters/{id}", method = RequestMethod.GET)
    SOWorkflowParameterDefinitions getParameters(@PathVariable Long id) {
        return extWorkflowsService.getWorkflowParameterDefinitions(id);
    }

}
