package org.onap.vid.job.impl;


import org.mockito.Mock;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.command.JobCommandFactory;
import org.onap.vid.properties.Features;
import org.quartz.Scheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

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


    private JobSchedulerInitializer jobSchedulerInitializer;

    @BeforeMethod
    public void setUp(){
        initMocks(this);

        jobSchedulerInitializer=new JobSchedulerInitializer(brokerService,schedulerFactoryBean,featureManager,commandFactory);
    }


    @Test
    public void shouldNotInitializeSchedulerWhenAsyncJobsAreDisabled(){
        when(featureManager.isActive(Features.FLAG_ASYNC_JOBS)).thenReturn(false);

        jobSchedulerInitializer.init();

        verifyZeroInteractions(schedulerFactoryBean);
    }

    //TODO Add more assertions
    @Test
    public void shouldInitializeSchedulerWhenAsyncJobsAreEnabled(){
        when(featureManager.isActive(Features.FLAG_ASYNC_JOBS)).thenReturn(true);

        when(schedulerFactoryBean.getScheduler()).thenReturn(scheduler);

        jobSchedulerInitializer.init();
    }
}