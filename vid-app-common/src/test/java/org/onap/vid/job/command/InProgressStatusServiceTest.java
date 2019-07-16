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

import org.jetbrains.annotations.NotNull;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.Job;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.properties.Features;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.onap.vid.services.AsyncInstantiationBaseTest;
import org.onap.vid.testUtils.TestUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.togglz.core.manager.FeatureManager;

import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.assertEquals;

public class InProgressStatusServiceTest {

    @Mock
    private RestMsoImplementation restMso;

    @Mock
    private AsyncInstantiationBusinessLogic asyncInstantiationBL;

    @Mock
    private AuditService auditService;

    @Mock
    private FeatureManager featureManager;

    @InjectMocks
    private InProgressStatusService inProgressStatusService;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeMethod
    public void resetMocks() {
        Mockito.reset(restMso);
        Mockito.reset(asyncInstantiationBL);
    }

    @DataProvider
    public static Object[][] jobStatuses() {
        return Stream.of(Job.JobStatus.values())
                .map(status -> new Object[] { status })
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "jobStatuses")
    public void whenGetFromMsoRequestStatus_returnItToCaller(Job.JobStatus expectedJobStatus) {

        UUID jobUuid = UUID.randomUUID();
        String userId = "mockedUserID";
        String testApi = "mockedTestApi";
        String requestId = UUID.randomUUID().toString();
        ServiceInstantiation serviceInstantiation = mock(ServiceInstantiation.class);

        when(asyncInstantiationBL.getOrchestrationRequestsPath()).thenReturn("");

        AsyncRequestStatus requestStatus = AsyncInstantiationBaseTest.asyncRequestStatusResponse("");
        RestObject<AsyncRequestStatus> msoResponse = createMockedAsyncRequestStatus(requestStatus, 200);
        when(restMso.GetForObject(contains(requestId), eq(AsyncRequestStatus.class))).thenReturn(msoResponse);

        when(asyncInstantiationBL.calcStatus(any())).thenReturn(expectedJobStatus);

        ExpiryChecker expiryChecker = mock(ExpiryChecker.class);
        when(expiryChecker.isExpired(any())).thenReturn(false);

        JobSharedData sharedData = new JobSharedData(jobUuid, userId, serviceInstantiation, testApi);
        Job.JobStatus actualJobStatus = inProgressStatusService.call(expiryChecker, sharedData, requestId);
        assertEquals(expectedJobStatus, actualJobStatus);

        verify(auditService).auditMsoStatus(eq(jobUuid), same(requestStatus.request));
        verify(asyncInstantiationBL).updateResourceInfo(eq(sharedData), eq(expectedJobStatus), eq(requestStatus));
        //verify we don't update service info during this case, which shall stay in_progress
        verify(asyncInstantiationBL, never()).updateServiceInfo(any(), any());
    }

    @NotNull
    protected RestObject<AsyncRequestStatus> createMockedAsyncRequestStatus(AsyncRequestStatus requestStatus, int statusCode) {
        RestObject<AsyncRequestStatus> msoResponse = mock(RestObject.class);
        when(msoResponse.getStatusCode()).thenReturn(statusCode);
        when(msoResponse.get()).thenReturn(requestStatus);
        return msoResponse;
    }

    @Test(dataProvider = "trueAndFalse", dataProviderClass = TestUtils.class)
    public void whenGetAsyncRequestStatus_thenRightResponseReturned(boolean isResumeFlagActive) {
        String requestId = "abcRequest";
        String baseMso = "/fakeBase/v15";

        when(asyncInstantiationBL.getOrchestrationRequestsPath()).thenReturn(baseMso);
        when(featureManager.isActive(Features.FLAG_1908_RESUME_MACRO_SERVICE)).thenReturn(isResumeFlagActive);

        AsyncRequestStatus requestStatus = AsyncInstantiationBaseTest.asyncRequestStatusResponse("");
        RestObject<AsyncRequestStatus> mockedResponse = createMockedAsyncRequestStatus(requestStatus, 399);
        String path = baseMso + "/" + requestId + (isResumeFlagActive ? "?format=detail" : "");
        when(restMso.GetForObject(eq(path), eq(AsyncRequestStatus.class))).thenReturn(mockedResponse);

        assertEquals(mockedResponse, inProgressStatusService.getAsyncRequestStatus(requestId));
    }

    @DataProvider
    public static Object[][] getAsyncReturnErrorDataProvider() {
        return new Object[][]{
                {AsyncInstantiationBaseTest.asyncRequestStatusResponse("xyz"), 400},
                {AsyncInstantiationBaseTest.asyncRequestStatusResponse("xyz"), 401},
                {AsyncInstantiationBaseTest.asyncRequestStatusResponse("xyz"), 500},
                {null, 200},
        };
    }

    @Test(dataProvider = "getAsyncReturnErrorDataProvider", expectedExceptions = InProgressStatusService.BadResponseFromMso.class)
    public void whenGetAsyncReturnError_thenExceptionIsThrown(AsyncRequestStatus requestStatus, int statusCode) {
        String requestId = "abcRequest";
        String baseMso = "/fakeBase/v15";
        when(asyncInstantiationBL.getOrchestrationRequestsPath()).thenReturn(baseMso);

        RestObject<AsyncRequestStatus> mockedResponse = createMockedAsyncRequestStatus(requestStatus, statusCode);
        when(restMso.GetForObject(eq(baseMso + "/" + requestId), eq(AsyncRequestStatus.class))).thenReturn(mockedResponse);

        inProgressStatusService.getAsyncRequestStatus(requestId);
    }

}
