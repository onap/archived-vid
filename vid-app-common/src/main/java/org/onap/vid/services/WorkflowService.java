package org.onap.vid.services;

import java.util.Collection;

public interface WorkflowService {
    Collection<String> getWorkflowsForVNFs(Collection<String> vnfNames);
    Collection<String> getAllWorkflows();
}
