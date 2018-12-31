package org.onap.vid.job.command;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onap.vid.job.Job;
import org.onap.vid.job.impl.JobSharedData;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.RestMsoImplementation;
import org.onap.vid.mso.RestObject;
import org.onap.vid.mso.rest.AsyncRequestStatus;
import org.onap.vid.services.AsyncInstantiationBaseTest;
import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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

    @InjectMocks
    private InProgressStatusService inProgressStatusService;

    @BeforeClass
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @DataProvider
    public static Object[][] jobStatuses() {
        return Stream.of(Job.JobStatus.values())
                .map(student -> new Object[] { student })
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "jobStatuses")
    public void whenGetFromMsoRequestStatus_returnItToCaller(Job.JobStatus expectedJobStatus) {

        UUID jobUuid = UUID.randomUUID();
        String userId = "mockedUserID";
        String requestId = UUID.randomUUID().toString();
        ServiceInstantiation serviceInstantiation = mock(ServiceInstantiation.class);

        when(asyncInstantiationBL.getOrchestrationRequestsPath()).thenReturn("");

        RestObject<AsyncRequestStatus> msoResponse = mock(RestObject.class);
        AsyncRequestStatus requestStatus = AsyncInstantiationBaseTest.asyncRequestStatusResponse("");

        when(msoResponse.getStatusCode()).thenReturn(200);
        when(msoResponse.get()).thenReturn(requestStatus);
        when(restMso.GetForObject(contains(requestId), eq(AsyncRequestStatus.class))).thenReturn(msoResponse);

        when(asyncInstantiationBL.calcStatus(any())).thenReturn(expectedJobStatus);

        ExpiryChecker expiryChecker = mock(ExpiryChecker.class);
        when(expiryChecker.isExpired(any())).thenReturn(false);

        JobSharedData sharedData = new JobSharedData(jobUuid, userId, serviceInstantiation);
        Job.JobStatus actualJobStatus = inProgressStatusService.call(expiryChecker, sharedData, requestId);
        assertEquals(expectedJobStatus, actualJobStatus);

        verify(asyncInstantiationBL).auditMsoStatus(eq(jobUuid), same(requestStatus.request));

        //verify we don't update service info during this case, which shall stay in_progress
        verify(asyncInstantiationBL, never()).updateServiceInfo(any(), any());


    }

}
