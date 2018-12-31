package org.onap.vid.job.command;

import org.onap.vid.job.Job;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WatchingCommand extends BaseWatchingCommand {

    public WatchingCommand() {}

    public WatchingCommand(JobSharedData sharedData, List<String> childrenJobsIds, boolean isService) {
        super(sharedData, childrenJobsIds, isService);
    }

    protected NextCommand getNextCommand(Job.JobStatus cumulativeJobsStatus) {
        if (cumulativeJobsStatus==Job.JobStatus.IN_PROGRESS) {
            return (isService) ? new NextCommand(Job.JobStatus.IN_PROGRESS, this)
                               : new NextCommand(Job.JobStatus.RESOURCE_IN_PROGRESS, this);
        }
        if (isService) {
            asyncInstantiationBL.updateServiceInfoAndAuditStatus(getSharedData().getJobUuid(), cumulativeJobsStatus);
        }
        return new NextCommand(cumulativeJobsStatus);
    }

}
