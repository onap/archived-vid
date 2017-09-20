package org.openecomp.vid.services;

import org.openecomp.vid.model.Workflow;
import java.util.Collection;

public interface WorkflowService {
    Collection<String> getWorkflowsForVNFs(Collection<String> vnfNames);
    Collection<String> getAllWorkflows();
}
