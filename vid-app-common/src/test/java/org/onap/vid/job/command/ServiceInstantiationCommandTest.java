package org.onap.vid.job.command;


import io.joshworks.restclient.http.HttpResponse;
import org.mockito.Mock;
import org.onap.vid.aai.AaiResponse;
import org.onap.vid.aai.exceptions.InvalidAAIResponseException;
import org.onap.vid.changeManagement.RequestDetailsWrapper;
import org.onap.vid.domain.mso.RequestReferences;
import org.onap.vid.exceptions.MaxRetriesException;
import org.onap.vid.job.Job;
import org.onap.vid.job.NextCommand;
import org.onap.vid.model.RequestReferencesContainer;
import org.onap.vid.model.serviceInstantiation.ServiceInstantiation;
import org.onap.vid.mso.MsoInterface;
import org.onap.vid.mso.model.ServiceInstantiationRequestDetails;
import org.onap.vid.mso.rest.AsyncRequestStatus;

import org.onap.vid.services.AsyncInstantiationBusinessLogic;
import org.onap.vid.services.AuditService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ServiceInstantiationCommandTest {
    @Mock
    private AsyncInstantiationBusinessLogic asyncInstantiationBusinessLogic;

    @Mock
    private MsoInterface msoInterface;

    @Mock
    private AuditService auditService;

    @Mock
    private ServiceInstantiation serviceInstantiation;

    @Mock
    private HttpResponse<RequestReferencesContainer> msoResponse;

    @Mock
    private AsyncRequestStatus asyncRequestStatus;

    @Mock
    private AsyncRequestStatus.Request request;

    @Mock
    private RequestDetailsWrapper<ServiceInstantiationRequestDetails> requestDetailsWrapper;

    @Mock
    private AaiResponse aaiResponse;

    @Mock
    private RequestReferencesContainer requestReferencesContainer;


    private UUID uuid = UUID.randomUUID();


    private ServiceInstantiationCommand serviceInstantiationCommand;

    @BeforeMethod
    public void setUp() {
        initMocks(this);
        serviceInstantiationCommand = new ServiceInstantiationCommand(asyncInstantiationBusinessLogic,auditService,msoInterface,uuid,serviceInstantiation,"sampleUserId");
    }


    @Test
    public void shouldProperlyHandleMaxRetriesException(){
        when(asyncInstantiationBusinessLogic.generateServiceInstantiationRequest(uuid,serviceInstantiation,"sampleUserId")).thenThrow(new MaxRetriesException("",2));

        NextCommand call = serviceInstantiationCommand.call();

        assertThat(call.getCommand(), is(nullValue()));
        assertThat(call.getStatus(),is(equalTo(Job.JobStatus.FAILED)));

        verify(asyncInstantiationBusinessLogic).handleFailedInstantiation(uuid);
    }

    @Test
    public void shouldProperlyHandleInvalidAAIResponseException(){
        doThrow(new InvalidAAIResponseException(aaiResponse)).when(asyncInstantiationBusinessLogic).generateServiceInstantiationRequest(uuid,serviceInstantiation,"sampleUserId");

        NextCommand call = serviceInstantiationCommand.call();

        assertThat(call.getCommand(), is(serviceInstantiationCommand));
        assertThat(call.getStatus(),is(equalTo(Job.JobStatus.IN_PROGRESS)));
    }


    @Test
    public void shouldProperlyHandleInvalidSOResponse(){
        when(asyncInstantiationBusinessLogic.generateServiceInstantiationRequest(uuid,serviceInstantiation,"sampleUserId")).thenReturn(requestDetailsWrapper);
        when(asyncInstantiationBusinessLogic.getServiceInstantiationPath(serviceInstantiation)).thenReturn("samplePath");
        when(msoInterface.post("samplePath",requestDetailsWrapper,RequestReferencesContainer.class)).thenReturn(msoResponse);
        when(msoResponse.getStatus()).thenReturn(500);
        when(msoResponse.getBody()).thenReturn(requestReferencesContainer);

        NextCommand call = serviceInstantiationCommand.call();

        assertThat(call.getCommand(), is(nullValue()));
        assertThat(call.getStatus(),is(equalTo(Job.JobStatus.FAILED)));

        verify(auditService).setFailedAuditStatusFromMso(uuid,null,500,requestReferencesContainer.toString());
    }


    @Test
    public void shouldProperlyUpdateServiceStatusAndReturnInProgressCommand(){
        RequestReferences requestReferences = createRequestReferences();

        when(asyncInstantiationBusinessLogic.generateServiceInstantiationRequest(uuid,serviceInstantiation,"sampleUserId")).thenReturn(requestDetailsWrapper);
        when(asyncInstantiationBusinessLogic.getServiceInstantiationPath(serviceInstantiation)).thenReturn("samplePath");
        when(msoInterface.post("samplePath",requestDetailsWrapper,RequestReferencesContainer.class)).thenReturn(msoResponse);
        when(msoResponse.getStatus()).thenReturn(200);
        when(msoResponse.getBody()).thenReturn(requestReferencesContainer);
        when(requestReferencesContainer.getRequestReferences()).thenReturn(requestReferences);


        NextCommand call = serviceInstantiationCommand.call();

        assertThat(call.getCommand(),instanceOf(InProgressStatusCommand.class));
        assertThat(call.getStatus(),is(equalTo(Job.JobStatus.IN_PROGRESS)));

    }

    private RequestReferences createRequestReferences() {
        RequestReferences requestReferences=new RequestReferences();
        requestReferences.setInstanceId("sampleInstanceId");
        requestReferences.setRequestId("sampleRequestId");
        return requestReferences;
    }
}