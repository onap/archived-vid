package org.onap.vid.job.command;

import org.onap.vid.job.Job;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;

import java.util.HashMap;
import java.util.Map;


public class ResourceWithChildrenInProgressCommand extends BaseInProgressStatusCommand {

    public ResourceWithChildrenInProgressCommand() {
    }

    public ResourceWithChildrenInProgressCommand(JobSharedData sharedData,
                                                 String requestId,
                                                 String instanceId,
                                                 CommandParentData commandParentData) {
        init(sharedData, requestId, instanceId, commandParentData);
    }

    protected BaseInProgressStatusCommand init(JobSharedData sharedData,
                                               String requestId,
                                               String instanceId,
                                               CommandParentData commandParentData) {
        init(sharedData, requestId, instanceId);
        this.commandParentData= commandParentData;
        return this;
    }


    @Override
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>(super.getData());
        data.putAll(buildDataForChild());
        return data;
    }

    @Override
    public BaseInProgressStatusCommand init(JobSharedData sharedData, Map<String, Object> commandData) {
        return init(
                sharedData,
                (String) commandData.get("requestId"),
                (String) commandData.get("instanceId"),
                commandParentData.initParentData(commandData));
    }

    protected Map<String, Object> buildDataForChild() {
       return commandParentData.getParentData();
    }



    @Override
    protected NextCommand processJobStatus(Job.JobStatus jobStatus) {
        return new NextCommand(jobStatus, this);
    }

    @Override
    protected ExpiryChecker getExpiryChecker() {
        return x->false;
    }

}
