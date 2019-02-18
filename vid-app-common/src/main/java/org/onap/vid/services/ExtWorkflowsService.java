package org.onap.vid.services;

import java.util.List;
import org.onap.vid.model.SOWorkflow;

public interface ExtWorkflowsService {
    List<SOWorkflow> getWorkflows(String vnfName);
}
