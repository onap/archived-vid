package org.onap.vid.job.impl;


import com.google.common.collect.ImmutableMap;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.job.JobsBrokerService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;

@Component
public class DeleteOldJobsSchedulerInitializer {

    private JobsBrokerService jobsBrokerService;
    private SchedulerFactoryBean schedulerFactoryBean;
    private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(DeleteOldJobsSchedulerInitializer.class);

    @Autowired
    public DeleteOldJobsSchedulerInitializer(
            JobsBrokerService jobsBrokerService,
            SchedulerFactoryBean schedulerFactoryBean
    ) {
        this.jobsBrokerService = jobsBrokerService;
        this.schedulerFactoryBean = schedulerFactoryBean;
    }

    @PostConstruct
    public void init() {
        try {
            JobDetail jobDetail = createJobDetail();
            Trigger deleteOldJobsTrigger = createTrigger();
            schedulerFactoryBean.getScheduler().scheduleJob(jobDetail, deleteOldJobsTrigger);
        } catch (SchedulerException e) {
            logger.error(EELFLoggerDelegate.errorLogger, "Failed to schedule trigger for delete old jobs: {}", e.getMessage());
            throw new GenericUncheckedException(e);
        }
    }

    JobDetail createJobDetail() {
        int days = Integer.parseInt(SystemProperties.getProperty("vid.asyncJob.howLongToKeepOldJobsInDays"));
        long secondsAgo = TimeUnit.DAYS.toSeconds(days);
        return JobBuilder.newJob().ofType(DeleteOldJobsWorker.class)
                .withIdentity("DeleteOldJobsWorker")
                .withDescription("worker that delete old vid jobs from DB")
                .setJobData(new JobDataMap(ImmutableMap.of(
                        "jobsBrokerService", jobsBrokerService,
                        "secondsAgo", secondsAgo
                )))
                .build();
    }

    Trigger createTrigger() {
        int minutes = new Random(System.nanoTime()).nextInt(59);
        int hours = 6;
        logger.info("trigger for DeleteOldJobs is {}:{} ", hours, minutes);

        return TriggerBuilder.newTrigger()
                .withIdentity("DeleteOldJobsTrigger")
                .withDescription("Trigger to run delete old vid jobs worker")
                .withSchedule(dailyAtHourAndMinute(hours, minutes))
                .build();
    }
}
