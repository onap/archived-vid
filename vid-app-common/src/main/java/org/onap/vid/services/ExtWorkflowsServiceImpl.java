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
        validateSOResponse(msoResponse);
        return convertMsoResponseToWorkflowList(msoResponse);
    }

    @Override
    public SOWorkflowParameterDefinitions getWorkflowParameterDefinitions(Long workflowId) {
        MsoResponseWrapper2<SOWorkflowParameterDefinitions> msoResponse = mockedWorkflowsRestClient.getWorkflowParameterDefinitions(workflowId);
        validateSOResponse(msoResponse);
        return (SOWorkflowParameterDefinitions) msoResponse.getEntity();
    }

    private List<SOWorkflow> convertMsoResponseToWorkflowList(MsoResponseWrapper2<SOWorkflows> msoResponse) {
        SOWorkflows soWorkflows = (SOWorkflows) msoResponse.getEntity();
        return soWorkflows.getWorkflows();
    }

    private void validateSOResponse(MsoResponseWrapper2 response){
        if (response.getStatus() >= 400 || response.getEntity() == null) {
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
