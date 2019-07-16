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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.onap.vid.job.JobsBrokerService;
import org.quartz.JobExecutionException;
import org.testng.annotations.Test;

public class DeleteOldJobsWorkerTest {

    @Test
    public void whenExecuteInternal_thenCallToDeleteOldFinalJobs() throws JobExecutionException {
        JobsBrokerService mockBroker = mock(JobsBrokerService.class);
        long secondsAgo = 42L;
        DeleteOldJobsWorker underTest = new DeleteOldJobsWorker();
        underTest.setJobsBrokerService(mockBroker);
        underTest.setSecondsAgo(secondsAgo);
        underTest.executeInternal(null);
        verify(mockBroker).deleteOldFinalJobs(secondsAgo);
    }

}
