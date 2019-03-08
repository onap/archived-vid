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

import java.util.List;
import org.onap.vid.model.LocalWorkflowParameterDefinitions;
import org.onap.vid.model.SOWorkflow;
import org.onap.vid.model.SOWorkflowParameterDefinitions;
import org.onap.vid.services.ExternalWorkflowsService;
import org.onap.vid.services.LocalWorkflowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(WorkflowsController.WORKFLOWS_MANAGEMENT)
public class WorkflowsController extends VidRestrictedBaseController {
    static final String WORKFLOWS_MANAGEMENT = "workflows-management";

    private ExternalWorkflowsService externalWorkflowsService;
    private LocalWorkflowsService localWorkflowsService;

    @Autowired
    public WorkflowsController(ExternalWorkflowsService externalWorkflowsService, LocalWorkflowsService localWorkflowsService) {
        this.externalWorkflowsService = externalWorkflowsService;
        this.localWorkflowsService = localWorkflowsService;
    }

    @RequestMapping(value = "workflows", method = RequestMethod.GET)
    public List<SOWorkflow> getWorkflows(@RequestParam(value = "vnfName") String vnfName){
        return externalWorkflowsService.getWorkflows(vnfName);
    }

    @RequestMapping(value = "remote-workflow-parameters/{id}", method = RequestMethod.GET)
    SOWorkflowParameterDefinitions getParameters(@PathVariable Long id) {
        return externalWorkflowsService.getWorkflowParameterDefinitions(id);
    }

    @RequestMapping(value = "local-workflow-parameters/{name}", method = RequestMethod.GET)
    LocalWorkflowParameterDefinitions getParameters(@PathVariable String name) {
        return localWorkflowsService.getWorkflowParameterDefinitions(name);
    }

}

