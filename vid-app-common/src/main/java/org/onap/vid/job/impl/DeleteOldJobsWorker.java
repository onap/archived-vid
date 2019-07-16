package org.onap.vid.job.impl;

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.job.JobsBrokerService;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class DeleteOldJobsWorker extends QuartzJobBean {

    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(DeleteOldJobsWorker.class);

    private JobsBrokerService jobsBrokerService;
    private long secondsAgo;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        LOGGER.info("delete old final jobs that has finished before {} seconds", secondsAgo);
        jobsBrokerService.deleteOldFinalJobs(secondsAgo);
    }

    //the following methods are used by quartz to inject members
    public void setJobsBrokerService(JobsBrokerService jobsBrokerService) {
        this.jobsBrokerService = jobsBrokerService;
    }

    public void setSecondsAgo(long secondsAgo) {
        this.secondsAgo = secondsAgo;
    }
}
