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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.matchesPattern;
import static org.mockito.Mockito.mock;
import static org.onap.vid.testUtils.TestUtils.testWithSystemProperty;
import static org.testng.Assert.assertEquals;

import org.onap.vid.job.JobsBrokerService;
import org.quartz.JobDetail;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.testng.annotations.Test;

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