package org.onap.vid.model.serviceInstantiation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.mso.model.ModelInfo;

import java.util.Collection;
import java.util.Collections;

public class InstanceGroupMember extends BaseResource implements JobAdapter.AsyncJobRequest{

    public InstanceGroupMember(@JsonProperty("instanceId") String instanceId,
                               @JsonProperty("action") String action,
                               @JsonProperty("trackById") String trackById,
                               @JsonProperty("isFailed") Boolean isFailed,
                               @JsonProperty("statusMessage") String statusMessage) {
        super(new ModelInfo(), null, action, null, null, null, null, false, instanceId, trackById, isFailed, statusMessage);
    }

    @Override
    protected String getModelType() {
        return "vnf";
    }

    @Override
    public Collection<BaseResource> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public JobType getJobType() {
        return JobType.InstanceGroupMember;
    }
}
