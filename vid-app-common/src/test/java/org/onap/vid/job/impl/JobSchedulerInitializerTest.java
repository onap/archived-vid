/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2018 Nokia Intellectual Property. All rights reserved.
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


import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.JobCommandFactory;
import org.onap.vid.properties.Features;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class JobSchedulerInitializerTest {

    @Mock
    private JobsBrokerService brokerService;

    @Mock
    private SchedulerFactoryBean schedulerFactoryBean;

    @Mock
    private FeatureManager featureManager;

    @Mock
    private JobCommandFactory commandFactory;

    @Mock
    private Scheduler scheduler;

    @InjectMocks
    private JobSchedulerInitializer jobSchedulerInitializer;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
    }


    @Test
    public void shouldNotInitializeSchedulerWhenAsyncJobsAreDisabled() {
        when(featureManager.isActive(Features.FLAG_ASYNC_JOBS)).thenReturn(false);

        jobSchedulerInitializer.init();

        verifyZeroInteractions(schedulerFactoryBean);
    }


    @Test
    public void shouldInitializeSchedulerWhenAsyncJobsAreEnabled() throws SchedulerException {
        ArgumentCaptor<JobDetail> jobDetailArgumentCaptor = ArgumentCaptor.forClass(JobDetail.class);
        ArgumentCaptor<Trigger> triggerArgumentCaptor = ArgumentCaptor.forClass(Trigger.class);
        when(featureManager.isActive(Features.FLAG_ASYNC_JOBS)).thenReturn(true);
        when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);

        jobSchedulerInitializer.init();

        verify(scheduler, times(2)).scheduleJob(jobDetailArgumentCaptor.capture(), triggerArgumentCaptor.capture());

        List<Object> topics = extractTopics(jobDetailArgumentCaptor);

        List<String> descriptions = extractDescription(triggerArgumentCaptor);

        assertThat(topics, containsInAnyOrder(org.onap.vid.job.Job.JobStatus.IN_PROGRESS, org.onap.vid.job.Job.JobStatus.PENDING));
        assertThat(descriptions, containsInAnyOrder("Trigger to run async worker for PENDING", "Trigger to run async worker for IN_PROGRESS"));
    }

    private List<Object> extractTopics(ArgumentCaptor<JobDetail> jobDetailArgumentCaptor) {
        return jobDetailArgumentCaptor
                .getAllValues()
                .stream()
                .map(JobDetail::getJobDataMap)
                .map(x -> x.get("topic"))
                .collect(Collectors.toList());
    }

    private List<String> extractDescription(ArgumentCaptor<Trigger> triggerArgumentCaptor) {
        return triggerArgumentCaptor
                .getAllValues()
                .stream()
                .map(Trigger::getDescription)
                .collect(Collectors.toList());
    }
}