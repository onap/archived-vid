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
import org.onap.vid.model.SOWorkflow;
import org.onap.vid.model.SOWorkflowParameterDefinitions;
import org.onap.vid.model.SOWorkflows;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.mso.rest.MockedWorkflowsRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExtWorkflowsServiceImpl implements ExtWorkflowsService {

    private MockedWorkflowsRestClient mockedWorkflowsRestClient;

    @Autowired
    public ExtWorkflowsServiceImpl(MockedWorkflowsRestClient mockedWorkflowsRestClient) {
        this.mockedWorkflowsRestClient = mockedWorkflowsRestClient;
    }

    @Override
    public List<SOWorkflow> getWorkflows(String vnfName) {
        MsoResponseWrapper2<SOWorkflows> msoResponse = mockedWorkflowsRestClient.getWorkflows(vnfName);
        validateSOResponse(msoResponse, SOWorkflows.class);
        return convertMsoResponseToWorkflowList(msoResponse);
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

    private void validateSOResponse(MsoResponseWrapper2 response, Class<?> expectedResponseClass){
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
