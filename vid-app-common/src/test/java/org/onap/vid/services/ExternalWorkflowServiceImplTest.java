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


import com.google.common.collect.Lists;
import io.joshworks.restclient.http.HttpResponse;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.model.ArtifactInfo;
import org.onap.vid.model.SOWorkflow;
import org.onap.vid.model.SOWorkflowList;
import org.onap.vid.model.SOWorkflowParameterDefinition;
import org.onap.vid.model.SOWorkflowParameterDefinitions;
import org.onap.vid.model.SOWorkflowType;
import org.onap.vid.model.SOWorkflows;
import org.onap.vid.model.WorkflowInputParameter;
import org.onap.vid.model.WorkflowSource;
import org.onap.vid.model.WorkflowSpecification;
import org.onap.vid.model.WorkflowSpecificationWrapper;
import org.onap.vid.mso.MsoBusinessLogic;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class ExternalWorkflowServiceImplTest {

    @Mock
    private MsoBusinessLogic msoBusinessLogic;


    private static final UUID SAMPLE_UUID = UUID.randomUUID();

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnWorkflowsOnValidResponse() {
        // given
        ExternalWorkflowsService extWorkflowsService = new ExternalWorkflowsServiceImpl(msoBusinessLogic);
        WorkflowInputParameter parameter = new WorkflowInputParameter("sampleLabel", "text",
                true, Collections.EMPTY_LIST, "sampleName", "userParams", "description");
        SOWorkflowList workflowList = createWorkflowList(parameter);
        SOWorkflow workflow = new SOWorkflow(SAMPLE_UUID.toString(), "sampleName", WorkflowSource.SDC, Collections.singletonList(parameter));
        when(msoBusinessLogic.getWorkflowListByModelId("test")).thenReturn(workflowList);
        // when
        List<SOWorkflow> workflows = extWorkflowsService.getWorkflows("test");
        // then
        assertThat(workflows).hasSize(1).contains(workflow);
    }

    private SOWorkflowList createWorkflowList(WorkflowInputParameter parameter) {
        ArtifactInfo artifactInfo = new ArtifactInfo("workflow", SAMPLE_UUID.toString(), "sampleArtifactName",
                "sampleVersion", "sampleDescription", "sampleName", "sampleOperation", "sdc", "vnf");
        WorkflowSpecification specification = new WorkflowSpecification(artifactInfo, Collections.EMPTY_LIST, Collections.singletonList(parameter));
        WorkflowSpecificationWrapper wrapper = new WorkflowSpecificationWrapper(specification);
        return new SOWorkflowList(Collections.singletonList(wrapper));
    }

}
