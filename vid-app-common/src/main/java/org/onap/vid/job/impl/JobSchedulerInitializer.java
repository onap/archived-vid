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

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.List;
import javax.annotation.PostConstruct;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.JobCommandFactory;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import org.togglz.core.manager.FeatureManager;

@Component
public class JobSchedulerInitializer {

    private JobsBrokerService jobsBrokerService;
    private SchedulerFactoryBean schedulerFactoryBean;
    private FeatureManager featureManager;
    private JobCommandFactory jobCommandFactory;
    private EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(JobSchedulerInitializer.class);
    public static final List<Job.JobStatus> WORKERS_TOPICS = ImmutableList.of(
            Job.JobStatus.PENDING,
            Job.JobStatus.CREATING,
            Job.JobStatus.IN_PROGRESS,
            Job.JobStatus.RESOURCE_IN_PROGRESS,
            Job.JobStatus.PENDING_RESOURCE
    );


    @Autowired
    public JobSchedulerInitializer(
            JobsBrokerService jobsBrokerService,
            SchedulerFactoryBean schedulerFactoryBean,
            FeatureManager featureManager,
            JobCommandFactory jobCommandFactory
    ) {
        this.jobsBrokerService = jobsBrokerService;
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.featureManager = featureManager;
        this.jobCommandFactory = jobCommandFactory;

    }

    @PostConstruct
    public void init() {
        WORKERS_TOPICS.forEach(topic->scheduleJobWorker(topic, 1));
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
            logger.error("Failed to schedule trigger for async worker jobs: {}", e.getMessage());
            throw new GenericUncheckedException(e);
        }
    }
}
