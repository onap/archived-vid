package org.onap.vid.services;

import org.onap.vid.model.SOWorkflow;
import org.onap.vid.model.SOWorkflowParameterDefinitions;
import org.onap.vid.mso.MsoBusinessLogic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExternalWorkflowsSpecificationService implements ExternalWorkflowsService {

    private MsoBusinessLogic msoService;

    @Autowired
    public ExternalWorkflowsSpecificationService(MsoBusinessLogic msoService) {
        this.msoService = msoService;
    }

    @Override
    public List<SOWorkflow> getWorkflows(String vnfModelId) {
        msoService.getWorkflowListByModelId(vnfModelId);
        return null;
    }

    @Override
    public SOWorkflowParameterDefinitions getWorkflowParameterDefinitions(Long workflowId) {
        return null;
    }
}
