/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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

package org.onap.vid.services;

import org.onap.vid.model.ArtifactInfo;
import org.onap.vid.model.SOWorkflow;
import org.onap.vid.model.SOWorkflowList;
import org.onap.vid.model.WorkflowSource;
import org.onap.vid.model.WorkflowSpecification;
import org.onap.vid.mso.MsoBusinessLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class ExternalWorkflowsServiceImpl implements ExternalWorkflowsService {

    private MsoBusinessLogic msoService;

    @Autowired
    public ExternalWorkflowsServiceImpl(MsoBusinessLogic msoService) {
        this.msoService = msoService;
    }

    @Override
    public List<SOWorkflow> getWorkflows(String vnfModelId) {
        SOWorkflowList workflowListByModelId = msoService.getWorkflowListByModelId(vnfModelId);
        List<SOWorkflow> soWorkflows = new ArrayList<>();
        Objects.requireNonNull(workflowListByModelId
                .getWorkflowSpecificationList())
                .forEach(
                        workflow -> soWorkflows.add(convertWorkflow(workflow.getWorkflowSpecification()))
                );

        return soWorkflows;
    }

    private SOWorkflow convertWorkflow(WorkflowSpecification workflow) {
        ArtifactInfo artifactInfo = workflow.getArtifactInfo();

        return new SOWorkflow(artifactInfo.getArtifactUuid(),
                artifactInfo.getWorkflowName(),
                WorkflowSource.valueOf(artifactInfo.getWorkflowSource().toUpperCase()),
                workflow.getWorkflowInputParameters());
    }
}
