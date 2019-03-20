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

package org.onap.vid.services;

import java.util.List;
import org.onap.vid.model.Workflow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class WorkflowServiceImpl implements WorkflowService {
    //TODO: Add the list of workflows hard coded or from DB.
    private List<Workflow> workflows = Arrays.asList(
            new Workflow(0, "Upgrade", new ArrayList<>(Arrays.asList("VNF1", "VNF2", "VNF3", "VNF4"))),
            new Workflow(1, "Clean", new ArrayList<>(Arrays.asList("VNF1", "VNF2", "VNF3"))),
            new Workflow(2, "Reinstall", new ArrayList<>(Arrays.asList("VNF1", "VNF2", "VNF4"))),
            new Workflow(3, "Dump", new ArrayList<>(Arrays.asList("VNF1", "VNF3", "VNF4"))),
            new Workflow(4, "Flush", new ArrayList<>(Arrays.asList("VNF2", "VNF3", "VNF4")))
    );

    @Override
    public Collection<String> getWorkflowsForVNFs(Collection<String> vnfNames) {
        return workflows.stream()
                .filter(workflow -> workflow.getVnfNames().containsAll(vnfNames))
                .map(Workflow::getWorkflowName)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Collection<String> getAllWorkflows() {
        return workflows.stream()
                .map(Workflow::getWorkflowName)
                .distinct()
                .collect(Collectors.toList());
    }
}
