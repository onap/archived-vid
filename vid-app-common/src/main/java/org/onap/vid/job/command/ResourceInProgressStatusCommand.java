package org.onap.vid.job.command;

import org.onap.vid.job.Job;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ResourceInProgressStatusCommand extends BaseInProgressStatusCommand {

    public ResourceInProgressStatusCommand() {
    }

    ResourceInProgressStatusCommand(JobSharedData sharedData, String requestId, String instanceId) {
        init(sharedData, requestId, instanceId);
    }

    @Override
    protected ExpiryChecker getExpiryChecker() {
        return x->false;
    }

    @Override
    protected NextCommand processJobStatus(Job.JobStatus jobStatus) {
        return new NextCommand(jobStatus, this);
    }


}
