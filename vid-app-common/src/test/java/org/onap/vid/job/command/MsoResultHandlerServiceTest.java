package org.onap.vid.job.command;

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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.onap.vid.job.impl.AsyncInstantiationIntegrationTest.createResponse;
import static org.testng.AssertJUnit.assertEquals;

public class MsoResultHandlerServiceTest {

    @Mock
    private AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic;

    @Mock
    private AuditService auditService;

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
    }

    @DataProvider
    public static Object[][] notOkStatusCodes() {
        return new Object[][]{
                {199}, {400} , {404}, {500}
        };
    }

    @Test(dataProvider = "notOkStatusCodes")
    public void whenNotOkFromMso_getResultsWithFailedStatus(int statusCode) {
        Mockito.reset(asyncInstantiationBusinessLogic);
        JobSharedData sharedData = new JobSharedData();
        RestObject<RequestReferencesContainer> msoResponse = createResponse(statusCode);
        MsoResult expectedResult = new MsoResult(Job.JobStatus.FAILED);
        MsoResult actualMsoResult = underTest.handleResponse(new JobSharedData(), msoResponse, "test desc");
        assertEquals(expectedResult, actualMsoResult);
        verify(asyncInstantiationBusinessLogic).addFailedResourceInfo(eq(sharedData), eq(msoResponse));
    }
}
