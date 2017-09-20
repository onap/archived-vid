package org.openecomp.vid.model;


import java.util.Collection;

public class Workflow {
    //Private members:
    private int id;
    private String workflowName;
    private Collection<String> vnfNames;


    //Constructors:
    public Workflow() {}

    public Workflow(int id, String workflowName, Collection<String> vnfNames) {
        this.id = id;
        this.workflowName = workflowName;
        this.vnfNames = vnfNames;
    }


    //Setters and getters:
    public int getId() {
        return id;
    }

    public String getWorkflowName() {
        return workflowName;
    }

    public Collection<String> getVnfNames() {
        return this.vnfNames;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWorkflowName(String workflowName) {
        this.workflowName = workflowName;
    }

    public void setVnfName(Collection<String> vnfNames) {
        this.vnfNames = vnfNames;
    }
}
