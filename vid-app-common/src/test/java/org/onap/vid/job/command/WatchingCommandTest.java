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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.portalsdk.core.service.DataAccessService;
import org.onap.vid.job.Job;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class WatchingCommandTest {

    @Mock
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Mock
    private DataAccessService dataAccessService;

    @Mock
    private WatchChildrenJobsBL watchChildrenJobsBL;

    @InjectMocks
    private WatchingCommand watchingCommand = new WatchingCommand();



    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @DataProvider
    public static Object[][] testWatchingDataProvider() {
        return new Object[][]{
                {"all children final, no failed child, is service", Job.JobStatus.COMPLETED, true, Job.JobStatus.COMPLETED},
                {"all children final, there is failed child, is service", Job.JobStatus.COMPLETED_WITH_ERRORS, true, Job.JobStatus.COMPLETED_WITH_ERRORS},
                {"not all children final, is service", Job.JobStatus.IN_PROGRESS, true, Job.JobStatus.IN_PROGRESS},
                {"all children final, no failed child, not service", Job.JobStatus.COMPLETED, false, Job.JobStatus.COMPLETED},
                {"all children final, there is failed child, not service", Job.JobStatus.COMPLETED_WITH_ERRORS, false, Job.JobStatus.COMPLETED_WITH_ERRORS},
                {"not all children final, not service", Job.JobStatus.IN_PROGRESS, false, Job.JobStatus.RESOURCE_IN_PROGRESS},
        };
    }



    @Test(dataProvider = "testWatchingDataProvider")
    public void whenGetChildrenStatus_thenJobStatusAsExpected(String desc, Job.JobStatus childrenComulativeStatus, boolean isService, Job.JobStatus expectedCommandStatus) {
        UUID jobUUID = UUID.randomUUID();
        JobSharedData sharedData = new JobSharedData(jobUUID, "mockedUserID", mock(ServiceInstantiation.class));
        List<String> uuids = mock(List.class);
        watchingCommand.init(sharedData, uuids, isService);
        when(watchChildrenJobsBL.retrieveChildrenJobsStatus(eq(uuids))).thenReturn(childrenComulativeStatus);
        when(watchChildrenJobsBL.cumulateJobStatus(eq(childrenComulativeStatus),eq(Job.JobStatus.COMPLETED))).thenReturn(childrenComulativeStatus);

        //execute command and verify
        NextCommand nextCommand = watchingCommand.call();
        assertThat(nextCommand.getStatus(), is(expectedCommandStatus));
        if (!expectedCommandStatus.equals(Job.JobStatus.IN_PROGRESS) && isService) {
            verify(asyncInstantiationBL).updateServiceInfoAndAuditStatus(jobUUID, expectedCommandStatus);
        } else {
            verify(asyncInstantiationBL, never()).updateServiceInfoAndAuditStatus(jobUUID, expectedCommandStatus);
        }
    }
}
