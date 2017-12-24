package org.openecomp.vid.changeManagement;

import org.openecomp.vid.model.VNFDao;
import org.openecomp.vid.model.VidWorkflow;

import java.util.List;
import java.util.stream.Collectors;

public class VnfDetailsWithWorkflows extends VnfDetails {

    private List<String> workflows;

    public VnfDetailsWithWorkflows() {
    }


    @SuppressWarnings("WeakerAccess")
    public VnfDetailsWithWorkflows(String UUID, String invariantUUID, List<String> workflows) {
        super(UUID, invariantUUID);
        this.workflows = workflows;
    }

    public VnfDetailsWithWorkflows(VNFDao vnfDao) {
        this(vnfDao.getVnfUUID(),
             vnfDao.getVnfInvariantUUID(),
             vnfDao.getWorkflows().stream().map(VidWorkflow::getWokflowName).collect(Collectors.toList()));
    }

    public List<String> getWorkflows() {
        return workflows;
    }

    public void setWorkflows(List<String> workflows) {
        this.workflows = workflows;
    }
}
