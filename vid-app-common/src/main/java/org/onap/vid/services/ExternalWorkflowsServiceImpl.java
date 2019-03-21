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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.onap.vid.controller.LoggerController;
import org.onap.vid.model.ArtifactInfo;
import org.onap.vid.model.SOWorkflow;
import org.onap.vid.model.SOWorkflowList;
import org.onap.vid.model.SOWorkflowParameterDefinitions;
import org.onap.vid.model.SOWorkflows;
import org.onap.vid.model.WorkflowSource;
import org.onap.vid.model.WorkflowSpecification;
import org.onap.vid.model.WorkflowSpecificationWrapper;
import org.onap.vid.mso.MsoBusinessLogic;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.mso.rest.MockedWorkflowsRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalWorkflowsServiceImpl implements ExternalWorkflowsService {

    private MockedWorkflowsRestClient mockedWorkflowsRestClient;
    private MsoBusinessLogic msoService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalWorkflowsServiceImpl.class);

    @Autowired
    public ExternalWorkflowsServiceImpl(MockedWorkflowsRestClient mockedWorkflowsRestClient, MsoBusinessLogic msoService) {
        this.mockedWorkflowsRestClient = mockedWorkflowsRestClient;
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

        return new SOWorkflow(artifactInfo.getArtifactUuid(), artifactInfo.getWorkflowName(), WorkflowSource.valueOf(artifactInfo.getWorkflowSource().toUpperCase()), workflow.getWorkflowInputParameters());
    }

    @Override
    public SOWorkflowParameterDefinitions getWorkflowParameterDefinitions(Long workflowId) {
        MsoResponseWrapper2<SOWorkflowParameterDefinitions> msoResponse = mockedWorkflowsRestClient.getWorkflowParameterDefinitions(workflowId);
        validateSOResponse(msoResponse, SOWorkflowParameterDefinitions.class);
        return (SOWorkflowParameterDefinitions) msoResponse.getEntity();
    }

    private List<SOWorkflow> convertMsoResponseToWorkflowList(MsoResponseWrapper2<SOWorkflows> msoResponse) {
        SOWorkflows soWorkflows = (SOWorkflows) msoResponse.getEntity();
        return soWorkflows.getWorkflows();
    }

    private void validateSOResponse(MsoResponseWrapper2 response, Class<?> expectedResponseClass) {
        if (response.getStatus() >= 400 || !expectedResponseClass.isInstance(response.getEntity())) {
            throw new BadResponseFromMso(response);
        }
    }

    public static class BadResponseFromMso extends RuntimeException {
        private final MsoResponseWrapper2<?> msoResponse;

        BadResponseFromMso(MsoResponseWrapper2<?> msoResponse) {
            this.msoResponse = msoResponse;
        }

        public MsoResponseWrapper2<?> getMsoResponse() {
            return msoResponse;
        }
    }

}
