package org.onap.vid.services;

import java.util.List;
import org.onap.vid.model.SOWorkflow;
import org.onap.vid.model.SOWorkflowParameterDefinitions;

public interface ExtWorkflowsService {
    List<SOWorkflow> getWorkflows(String vnfName);

    SOWorkflowParameterDefinitions getWorkflowParameterDefinitions(Long workflowId);
}
