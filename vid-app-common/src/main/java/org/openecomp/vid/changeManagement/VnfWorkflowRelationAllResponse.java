package org.openecomp.vid.changeManagement;

import java.util.List;

public class VnfWorkflowRelationAllResponse {

    private List<VnfDetailsWithWorkflows> vnfs;

    public VnfWorkflowRelationAllResponse() {
    }

    public VnfWorkflowRelationAllResponse(List<VnfDetailsWithWorkflows> vnfs) {
        this.vnfs = vnfs;
    }

    public List<VnfDetailsWithWorkflows> getVnfs() {
        return vnfs;
    }

    public void setVnfs(List<VnfDetailsWithWorkflows> vnfs) {
        this.vnfs = vnfs;
    }
}
