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

import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.onap.vid.job.Job.JobStatus.COMPLETED;
import static org.onap.vid.job.Job.JobStatus.COMPLETED_WITH_NO_ACTION;
import static org.onap.vid.job.Job.JobStatus.IN_PROGRESS;
import static org.onap.vid.job.command.ResourceCommandKt.ACTION_PHASE;
import static org.onap.vid.job.command.ResourceCommandKt.CHILD_JOBS;
import static org.onap.vid.job.command.ResourceCommandKt.INTERNAL_STATE;
import static org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator.createNetwork;
import static org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator.createService;
import static org.onap.vid.job.command.ResourceCommandTest.FakeResourceCreator.createVnf;
import static org.onap.vid.model.Action.Create;
import static org.onap.vid.testUtils.TestUtils.testWithSystemProperty;
import static org.testng.AssertJUnit.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.UUID;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobAdapter;
import org.onap.vid.job.JobType;
import org.onap.vid.job.JobsBrokerService;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.Action;
import org.onap.vid.model.serviceInstantiation.Network;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.model.serviceInstantiation.Vnf;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.properties.Features;
import org.onap.vid.properties.VidProperties;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

public class ServiceInProgressStatusCommandTest {


    @Mock
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Mock
    private JobsBrokerService jobsBrokerService;

    @Mock
    private JobAdapter jobAdapter;

    @Mock
    private FeatureManager featureManager;

    @Mock
    private JobSharedData sharedData;

    @Mock
    private ServiceInstantiation request;

    @Mock
    private InProgressStatusService inProgressStatusService;

    @Mock
    private WatchChildrenJobsBL watchChildrenJobsBL;

    @Mock
    private MsoResultHandlerService msoResultHandlerService;

    @Mock
    private MsoRequestBuilder msoRequestBuilder;

    @Mock
    private RestMsoImplementation restMsoImplementation;

    @Mock
    private AuditService auditService;

    private ALaCarteServiceCommand command;


    @DataProvider
    public static Object[][] isExpiredJobStatusData() {
        return new Object[][]{
                {ZonedDateTime.now(), "24", false},
                {getTimeNowMinus(2),  "1",  true},
                {getTimeNowMinus(24), "24",  true},
                {getTimeNowMinus(2),  "0",  false},
                {getTimeNowMinus(2),  "-1", false},
                {getTimeNowMinus(2),  "",   false},
                {getTimeNowMinus(2),  "a",  false}
        };
    }

    private static ZonedDateTime getTimeNowMinus(int hoursAgo) {
        return ZonedDateTime.ofInstant(Instant.now().minus(hoursAgo, ChronoUnit.HOURS), ZoneOffset.UTC);
    }

    @BeforeMethod
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        command = new ALaCarteServiceCommand(
                inProgressStatusService,
                watchChildrenJobsBL,
                asyncInstantiationBL,
                jobsBrokerService,
                msoRequestBuilder,
                msoResultHandlerService,
                jobAdapter,
                restMsoImplementation,
                auditService,
                featureManager
        );
    }

    @Test
    public void whenGetFromMsoCompletedAndALaCarte_generateNewJobsForVnfs() {
        UUID uuid = UUID.randomUUID();
        String userId = "mockedUserID";
        String testApi = "VNF_API";

        // Create components setPosition in order to verify on the creation order on createChildJob
        Network network = createNetwork(Create);
        network.setPosition(0);
        Vnf vnf1 = createVnf(emptyList(), Create);
        vnf1.setPosition(1);
        Vnf vnf2 = createVnf(emptyList(), Create);
        vnf2.setPosition(2);
        ServiceInstantiation serviceInstantiation = createService(
                ImmutableList.of(vnf1, vnf2),
                ImmutableList.of(network),
                emptyList());
        when(featureManager.isActive(Features.FLAG_ASYNC_ALACARTE_VNF)).thenReturn(true);

        UUID uuid1 = UUID.fromString("12345678-1234-1234-1234-123456789012");
        UUID uuid2 = UUID.fromString("12345678-1234-1234-1234-123456789013");
        UUID uuid3 = UUID.fromString("12345678-1234-1234-1234-123456789014");
        when(jobsBrokerService.add(any())).thenReturn(uuid1).thenReturn(uuid2).thenReturn(uuid3);

        JobSharedData sharedData = new JobSharedData(uuid, userId, serviceInstantiation, testApi);
        command.init(sharedData, ImmutableMap.of(
                ACTION_PHASE, Action.Create.name(),
                INTERNAL_STATE, InternalState.IN_PROGRESS.name()
                ));
        when(inProgressStatusService.call(any(), eq(sharedData), any())).thenReturn(Job.JobStatus.COMPLETED);
        when(watchChildrenJobsBL.cumulateJobStatus(Job.JobStatus.COMPLETED, COMPLETED_WITH_NO_ACTION)).thenReturn(COMPLETED);
        when(watchChildrenJobsBL.cumulateJobStatus(Job.JobStatus.COMPLETED_WITH_NO_ACTION, COMPLETED)).thenReturn(COMPLETED);
        when(msoResultHandlerService.getRequest(eq(sharedData))).thenReturn(serviceInstantiation);
        NextCommand nextCommand = command.call();
        assertEquals(IN_PROGRESS,  nextCommand.getStatus());
        nextCommand = nextCommand.getCommand().call();

        ArgumentCaptor<JobAdapter.AsyncJobRequest> argumentCaptor = ArgumentCaptor.forClass(JobAdapter.AsyncJobRequest.class);
        verify(jobAdapter, times(1)).createChildJob(eq(JobType.NetworkInstantiation), argumentCaptor.capture(), eq(sharedData), any(), eq(0));
        verify(jobAdapter, times(1)).createChildJob(eq(JobType.VnfInstantiation), argumentCaptor.capture(), eq(sharedData), any(), eq(1));
        verify(jobAdapter, times(1)).createChildJob(eq(JobType.VnfInstantiation), argumentCaptor.capture(), eq(sharedData), any(), eq(2));

        assertThat(argumentCaptor.getAllValues(), containsInAnyOrder(vnf1, vnf2, network));

        verify(jobsBrokerService, times(3)).add(any());

        //verify we don't update service info during this case, which shall stay in_progress
        verify(asyncInstantiationBL, never()).updateServiceInfo(any(), any());

        assertThat(nextCommand.getStatus(), is(Job.JobStatus.IN_PROGRESS));
        assertThat(nextCommand.getCommand().getType(), is(JobType.ALaCarteService));
        assertThat(nextCommand.getCommand().getData().get(CHILD_JOBS), is(Arrays.asList(uuid1.toString(), uuid2.toString(), uuid3.toString())));
    }

    @Test(dataProvider = "isExpiredJobStatusData")
    public void isExpiredJobStatusTest(ZonedDateTime jobStartTime, String configValue, boolean expectedResult) throws Exception {
        testWithSystemProperty(VidProperties.VID_JOB_MAX_HOURS_IN_PROGRESS, configValue, ()->
                Assert.assertEquals(command.getExpiryChecker().isExpired(jobStartTime), expectedResult)
        );
    }
}
