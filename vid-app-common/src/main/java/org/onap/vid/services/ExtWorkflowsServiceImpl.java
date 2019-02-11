package org.onap.vid.services;

import java.util.List;
import org.onap.vid.model.SOWorkflow;
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
        if (msoResponse.getStatus() >= 400 || msoResponse.getEntity() == null) {
            throw new BadResponseFromMso(msoResponse);
        }
        return convertMsoResponseToWorkflowList(msoResponse);
    }

    private List<SOWorkflow> convertMsoResponseToWorkflowList(MsoResponseWrapper2<SOWorkflows> msoResponse) {
        SOWorkflows soWorkflows = (SOWorkflows) msoResponse.getEntity();
        return soWorkflows.getWorkflows();
    }

    public static class BadResponseFromMso extends RuntimeException {
        private final MsoResponseWrapper2<SOWorkflows> msoResponse;

        public BadResponseFromMso(MsoResponseWrapper2<SOWorkflows> msoResponse) {
            this.msoResponse = msoResponse;
        }

        public MsoResponseWrapper2<SOWorkflows> getMsoResponse() {
            return msoResponse;
        }
    }

}
