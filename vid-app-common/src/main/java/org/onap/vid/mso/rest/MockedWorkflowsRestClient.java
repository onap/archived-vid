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

package org.onap.vid.mso.rest;

import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.model.SOWorkflowParameterDefinitions;
import org.onap.vid.model.SOWorkflows;
import org.onap.vid.mso.MsoResponseWrapper2;

public class MockedWorkflowsRestClient {

    private SyncRestClient syncRestClient;
    private String baseUrl;

    public MockedWorkflowsRestClient(SyncRestClient syncRestClient, String baseUrl) {
        this.syncRestClient = syncRestClient;
        this.baseUrl = baseUrl;
    }

    public MsoResponseWrapper2<SOWorkflows> getWorkflows(String vnfName) {
        // Temporary skip vnfName and call mocked service
        return new MsoResponseWrapper2<>(syncRestClient
            .get(getWorkflowsUrl(),
                Collections.emptyMap(),
                Collections.emptyMap(),
                SOWorkflows.class));
    }

    public MsoResponseWrapper2<SOWorkflowParameterDefinitions> getWorkflowParameterDefinitions(Long workflowId) {
        return new MsoResponseWrapper2<>(syncRestClient
                .get((workflowId <= 3 && workflowId > 0) ? getParametersUrl(workflowId) : getParametersUrl(),
                        Collections.emptyMap(),
                        Collections.emptyMap(),
                        SOWorkflowParameterDefinitions.class));
    }

    @NotNull
    private String getWorkflowsUrl() {
        return baseUrl + "so/workflows";
    }


    @NotNull
    private String getParametersUrl() {
        return baseUrl + "so/workflow-parameters";
    }

    @NotNull
    private String getParametersUrl(Long workflowId) {
        return baseUrl + "so/workflow-parameters/" + workflowId;
    }
}
