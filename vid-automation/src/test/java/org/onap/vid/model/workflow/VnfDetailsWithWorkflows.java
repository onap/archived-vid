package org.onap.vid.model.workflow;


import java.util.List;

public class VnfDetailsWithWorkflows extends VnfDetails {

    private List<String> workflows;

    public VnfDetailsWithWorkflows() {
    }


    @SuppressWarnings("WeakerAccess")
    public VnfDetailsWithWorkflows(String UUID, String invariantUUID, List<String> workflows) {
        super(UUID, invariantUUID);
        this.workflows = workflows;
    }

    public List<String> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(List<String> workflows) {
        this.workflows = workflows;
    }
}
