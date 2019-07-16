package org.onap.vid.job.impl;

import org.onap.vid.job.JobsBrokerService;
import org.quartz.JobDetail;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.mock;
import static org.onap.vid.testUtils.TestUtils.testWithSystemProperty;
import static org.testng.Assert.assertEquals;

public class DeleteOldJobsSchedulerInitializerTest {

    @Test
    public void testCreateJobDetail() throws Exception {
        testWithSystemProperty("vid.asyncJob.howLongToKeepOldJobsInDays", "7", ()-> {
            JobsBrokerService mockBroker = mock(JobsBrokerService.class);
            DeleteOldJobsSchedulerInitializer underTest = new DeleteOldJobsSchedulerInitializer(mockBroker, null);
            JobDetail jobDetail = underTest.createJobDetail();
            assertEquals(DeleteOldJobsWorker.class, jobDetail.getJobClass());
            assertEquals(mockBroker, jobDetail.getJobDataMap().get("jobsBrokerService"));
            assertEquals(604800L, jobDetail.getJobDataMap().get("secondsAgo"));
        });
    }

    @Test
    public void testCreateTrigger() {
        DeleteOldJobsSchedulerInitializer underTest = new DeleteOldJobsSchedulerInitializer(null, null);
        CronTriggerImpl trigger = (CronTriggerImpl) underTest.createTrigger();
        assertThat(trigger.getCronExpression(), matchesPattern("0 [1-5]?[0-9] 6 \\? \\* \\*"));
    }

}