package org.onap.vid.job.command;

import org.onap.vid.job.Job;
import org.onap.vid.job.JobType;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.command.CommandParentData.CommandDataKey;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.serviceInstantiation.VfModule;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class VolumeGroupInProgressStatusCommand extends ResourceWithChildrenInProgressCommand {

    public VolumeGroupInProgressStatusCommand(
            JobSharedData sharedData,
            String requestId,
            String instanceId,
            CommandParentData parentData) {
        super(sharedData, requestId, instanceId, parentData);
    }

    public VolumeGroupInProgressStatusCommand() {
    }

    @Override
    protected NextCommand processJobStatus(Job.JobStatus jobStatus) {
        if (jobStatus == Job.JobStatus.FAILED) {
            return new NextCommand(Job.JobStatus.FAILED);
        }
        VfModule request = (VfModule) getSharedData().getRequest();

        if (jobStatus == Job.JobStatus.COMPLETED) {
            //vf module creation
            Map<String, Object> dataForChild = buildDataForChild();
            List<String> vfModuleJob = Arrays.asList(jobsBrokerService.add(
                            jobAdapter.createChildJob(JobType.VfmoduleInstantiation, Job.JobStatus.CREATING , request, getSharedData(), dataForChild)).toString());

            return new NextCommand(Job.JobStatus.RESOURCE_IN_PROGRESS, new WatchingCommand(getSharedData(), vfModuleJob, false));
        }

        //in case of JobStatus.PAUSE we leave the job itself as IN_PROGRESS, for keep tracking job progress
        if (jobStatus == Job.JobStatus.PAUSE) {
            return new NextCommand(Job.JobStatus.RESOURCE_IN_PROGRESS, this);
        }
        return new NextCommand(jobStatus, this);
    }

    @Override
    protected Map<String, Object> buildDataForChild() {
        commandParentData.addInstanceId(CommandDataKey.VG_INSTANCE_ID, this.instanceId);
        return super.buildDataForChild();
    }

    @Override
    protected ExpiryChecker getExpiryChecker() {
        return x->false;
    }
}
