package org.openecomp.vid.services;

import org.openecomp.vid.model.Workflow;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class WorkflowServiceImpl implements WorkflowService {
    //TODO: Add the list of workflows hard coded or from DB.
    private ArrayList<Workflow> workflows = new ArrayList<>(Arrays.asList(
            new Workflow(0, "Upgrade", new ArrayList<>(Arrays.asList("VNF1", "VNF2", "VNF3", "VNF4"))),
            new Workflow(1, "Clean", new ArrayList<>(Arrays.asList("VNF1", "VNF2", "VNF3"))),
            new Workflow(2, "Reinstall", new ArrayList<>(Arrays.asList("VNF1", "VNF2", "VNF4"))),
            new Workflow(3, "Dump", new ArrayList<>(Arrays.asList("VNF1", "VNF3", "VNF4"))),
            new Workflow(4, "Flush", new ArrayList<>(Arrays.asList("VNF2", "VNF3", "VNF4")))
    ));

    @Override
    public Collection<String> getWorkflowsForVNFs(Collection<String> vnfNames) {
        Collection<String> result = workflows.stream()
                .filter(workflow -> workflow.getVnfNames().containsAll(vnfNames))
                .map(workflow -> workflow.getWorkflowName())
                .distinct()
                .collect(Collectors.toList());

        return result;
    }

    @Override
    public Collection<String> getAllWorkflows() {
        return workflows.stream()
                .map(workflow -> workflow.getWorkflowName())
                .distinct()
                .collect(Collectors.toList());
    }
}
