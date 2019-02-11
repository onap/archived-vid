package org.onap.vid.model;

import java.util.List;
import java.util.stream.Collectors;

public class SOWorkflows {

    private List<SOWorkflow> workflows;

    public SOWorkflows(List<SOWorkflow> workflows) {
        this.workflows = workflows;
    }

    public SOWorkflows() {
    }

    public void setWorkflows(List<SOWorkflow> workflows) {
        this.workflows = workflows;
    }

    public List<SOWorkflow> getWorkflows() {
        return workflows;
    }

    public SOWorkflows clone(){
        return new SOWorkflows(workflows.stream().map(SOWorkflow::clone).collect(Collectors.toList()));
    }

}
