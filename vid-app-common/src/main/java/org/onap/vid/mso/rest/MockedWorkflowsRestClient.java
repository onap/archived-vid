package org.onap.vid.mso.rest;

import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.onap.vid.client.SyncRestClient;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.onap.vid.client.SyncRestClient;
import org.onap.vid.model.SOWorkflowParameterDefinition;
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
                .get((workflowId <= 3) ? getParametersUrl(workflowId) : getParametersUrl(),
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
