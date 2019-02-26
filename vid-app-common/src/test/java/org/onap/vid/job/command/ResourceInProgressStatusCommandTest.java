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

package org.onap.vid.job.command;

import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.Job;
import org.onap.vid.job.NextCommand;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class ResourceInProgressStatusCommandTest {

    @InjectMocks
    private ResourceInProgressStatusCommand commandUnderTest = new ResourceInProgressStatusCommand();

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @DataProvider
    public static Object[][] givenStatusToExpectedStatus() {
        return new Object[][]{
                {Job.JobStatus.IN_PROGRESS, Job.JobStatus.IN_PROGRESS},
                {Job.JobStatus.FAILED, Job.JobStatus.FAILED},
                {Job.JobStatus.COMPLETED, Job.JobStatus.COMPLETED}
        };
    }

    @Test(dataProvider = "givenStatusToExpectedStatus")
    public void whenGetStatusFromMso_returnExpectedNextCommand(Job.JobStatus jobStatus, Job.JobStatus expectedNextStatus) {
        NextCommand nextCommand = commandUnderTest.processJobStatus(jobStatus);
        assertThat(nextCommand.getStatus(), is(expectedNextStatus));
        assertThat(nextCommand.getCommand(), is(commandUnderTest));
    }
}
