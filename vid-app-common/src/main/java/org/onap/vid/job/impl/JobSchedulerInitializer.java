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

import com.google.common.collect.ImmutableMap;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.JobCommandFactory;
import org.onap.vid.properties.Features;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.togglz.core.manager.FeatureManager;

import javax.annotation.PostConstruct;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Component
public class JobSchedulerInitializer {

    private JobsBrokerService jobsBrokerService;
    private SchedulerFactoryBean schedulerFactoryBean;
    private FeatureManager featureManager;
    private JobCommandFactory jobCommandFactory;
    private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(JobSchedulerInitializer.class);

    @Autowired
    public JobSchedulerInitializer(
            JobsBrokerService jobsBrokerService,
            SchedulerFactoryBean schedulerFactoryBean,
            FeatureManager featureManager,
            JobCommandFactory JobCommandFactory
    ) {
        this.jobsBrokerService = jobsBrokerService;
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.featureManager = featureManager;
        this.jobCommandFactory = JobCommandFactory;

    }

    @PostConstruct
    public void init() {
        if (!featureManager.isActive(Features.FLAG_ASYNC_JOBS)) {
            return;
        }
        scheduleJobWorker(Job.JobStatus.PENDING, 1);
        scheduleJobWorker(Job.JobStatus.CREATING, 1);
        scheduleJobWorker(Job.JobStatus.IN_PROGRESS, 1);
        scheduleJobWorker(Job.JobStatus.RESOURCE_IN_PROGRESS, 1);
    }

    private void scheduleJobWorker(Job.JobStatus topic, int intervalInSeconds) {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob().ofType(JobWorker.class)
                .withIdentity("AsyncWorkersJob" + topic)
                .withDescription("Job that run async worker for " + topic)
                .setJobData(new JobDataMap(ImmutableMap.of(
                        "jobsBrokerService", jobsBrokerService,
                        "jobCommandFactory", jobCommandFactory,
                        "featureManager", featureManager,
                        "topic", topic
                )))
                .build();
        Trigger asyncWorkerTrigger = TriggerBuilder.newTrigger().forJob(jobDetail)
                .withIdentity("AsyncWorkersTrigger" + topic)
                .withDescription("Trigger to run async worker for " + topic)
                .withSchedule(simpleSchedule().repeatForever().withIntervalInSeconds(intervalInSeconds))
                .build();
        try {
            scheduler.scheduleJob(jobDetail, asyncWorkerTrigger);
        } catch (SchedulerException e) {
            logger.error(EELFLoggerDelegate.errorLogger, "Failed to schedule trigger for async worker jobs: {}", e.getMessage());
            throw new GenericUncheckedException(e);
        }
    }
}
