/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.job.impl;


import static org.quartz.CronScheduleBuilder.dailyAtHourAndMinute;

import com.google.common.collect.ImmutableMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.portalsdk.core.util.SystemProperties;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.job.JobsBrokerService;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

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
            logger.error("Failed to schedule trigger for delete old jobs: {}", e.getMessage());
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
        logger.info(EELFLoggerDelegate.debugLogger, "trigger for DeleteOldJobs is {}:{} ", hours, minutes);

        return TriggerBuilder.newTrigger()
                .withIdentity("DeleteOldJobsTrigger")
                .withDescription("Trigger to run delete old vid jobs worker")
                .withSchedule(dailyAtHourAndMinute(hours, minutes))
                .build();
    }
}
