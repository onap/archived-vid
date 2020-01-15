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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.onap.vid.job.impl.AsyncInstantiationIntegrationTest.RAW_DATA_FROM_MSO;
import static org.onap.vid.job.impl.AsyncInstantiationIntegrationTest.createResponse;
import static org.testng.AssertJUnit.assertEquals;

import java.util.UUID;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.Job;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.mso.RestObject;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.onap.vid.utils.Logging;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MsoResultHandlerServiceTest {

    @Mock
    private AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic;

    @Mock
    private AuditService auditService;

    @Mock
    private Logging logging;

    @InjectMocks
    private MsoResultHandlerService underTest;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @DataProvider
    public static Object[][] okStatusCodes() {
        return new Object[][]{
                {200}, {202} , {300}, {399}
        };
    }

    @Test(dataProvider = "okStatusCodes")
    public void whenOkResponseFromMso_getResultsWithIdsAndCompleteWithNoAction(int statusCode) {
        String instanceId = UUID.randomUUID().toString();
        String requestId = UUID.randomUUID().toString();
        JobSharedData sharedData = new JobSharedData();
        RestObject<RequestReferencesContainer> msoResponse = createResponse(statusCode, instanceId, requestId);
        MsoResult expectedResult = new MsoResult(Job.JobStatus.COMPLETED_WITH_NO_ACTION, new MsoResourceIds(requestId, instanceId));
        MsoResult actualMsoResult = underTest.handleResponse(sharedData, msoResponse, "test desc");
        assertEquals(expectedResult, actualMsoResult);
        verify(asyncInstantiationBusinessLogic).addResourceInfo(eq(sharedData), eq(Job.JobStatus.IN_PROGRESS), eq(instanceId));
        verifyZeroInteractions(auditService);
    }

    @DataProvider
    public static Object[][] notOkStatusCodes() {
        return new Object[][]{
                {199}, {400} , {404}, {500}
        };
    }

    @Test(dataProvider = "notOkStatusCodes")
    public void whenNotOkFromMso_getResultsWithFailedStatus(int statusCode) {
        UUID jobUuid = UUID.fromString("fc312bd5-75aa-44b4-839b-667adac64be9");
        String requestId = "3b59afeb-a45d-432c-a36e-558088ec69a0";
        Mockito.reset(asyncInstantiationBusinessLogic);

        JobSharedData sharedData = mock(JobSharedData.class);
        when(sharedData.getRootJobId()).thenReturn(jobUuid);
        when(logging.currentRequestId()).thenReturn(requestId);

        RestObject<RequestReferencesContainer> msoResponse = createResponse(statusCode);
        MsoResult actualMsoResult = underTest.handleResponse(sharedData, msoResponse, "test desc");

        MsoResult expectedResult = new MsoResult(Job.JobStatus.FAILED);
        assertEquals(expectedResult, actualMsoResult);
        verify(asyncInstantiationBusinessLogic).addFailedResourceInfo(eq(sharedData), eq(msoResponse));
        verify(auditService).setFailedAuditStatusFromMso(jobUuid, requestId, statusCode, "Failed to test desc. Details: " + RAW_DATA_FROM_MSO);
    }
}
