package org.onap.vid.model.serviceInstantiation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.mso.model.ModelInfo;

public class InstanceGroup extends BaseResource implements JobAdapter.AsyncJobRequest {

    public InstanceGroup(@JsonProperty("modelInfo") ModelInfo modelInfo,
                         @JsonProperty("instanceName") String instanceName,
                         @JsonProperty("action") String action,
                         @JsonProperty("rollbackOnFailure") boolean rollbackOnFailure,
                         @JsonProperty("instanceId") String instanceId) {

        super(modelInfo, instanceName, action, null, null, null, null, rollbackOnFailure, instanceId);
    }

    @Override
    protected String getModelType() {
        return "instanceGroup";
    }
}
