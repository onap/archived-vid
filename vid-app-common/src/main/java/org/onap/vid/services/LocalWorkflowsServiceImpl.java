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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.onap.vid.model.LocalWorkflowParameterDefinition;
import org.onap.vid.model.LocalWorkflowParameterDefinitions;
import org.onap.vid.model.LocalWorkflowType;
import org.springframework.stereotype.Service;

@Service
public class LocalWorkflowsServiceImpl implements LocalWorkflowsService {

    Map<String, LocalWorkflowParameterDefinitions> WORKFLOWS_WITH_PARAMETERS = ImmutableMap.<String, LocalWorkflowParameterDefinitions>builder()
        .put("VNF Scale Out", new LocalWorkflowParameterDefinitions(
            ImmutableList.of(
                new LocalWorkflowParameterDefinition(1, "Configuration Parameters", true, LocalWorkflowType.STRING,".*")
            )
        ))
        .put("VNF In Place Software Update", new LocalWorkflowParameterDefinitions(
            ImmutableList.of(
                new LocalWorkflowParameterDefinition(2, "Operations timeout",true, LocalWorkflowType.STRING,"[0-9]+"),
                new LocalWorkflowParameterDefinition(3, "Existing software version", true, LocalWorkflowType.STRING, "[-a-zA-Z0-9.]+"),
                new LocalWorkflowParameterDefinition(4, "New software version", true, LocalWorkflowType.STRING, "[-a-zA-Z0-9.]+")
            )
        ))
        .put("VNF Config Update", new LocalWorkflowParameterDefinitions(
            ImmutableList.of(
                new LocalWorkflowParameterDefinition(5, "Attach configuration file", true, LocalWorkflowType.FILE, ".*", "Invalid file type. Please select a file with a CSV extension.", "Invalid file structure.", ".csv")
            )
        ))
        .build();

    @Override
    public LocalWorkflowParameterDefinitions getWorkflowParameterDefinitions(String workflowName) {
        return WORKFLOWS_WITH_PARAMETERS.get(workflowName).clone();
    }

}
