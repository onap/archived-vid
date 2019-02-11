package org.onap.vid.services;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import io.joshworks.restclient.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.model.SOWorkflow;
import org.onap.vid.model.SOWorkflows;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.mso.rest.MockedWorkflowsRestClient;
import org.onap.vid.services.ExtWorkflowsServiceImpl.BadResponseFromMso;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExtWorkflowServiceImplTest {

    @Mock
    private MockedWorkflowsRestClient client;
    @Mock
    private HttpResponse<SOWorkflows> response;

    @BeforeMethod
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnWorkflowsOnValidResponse(){
        // given
        ExtWorkflowsService extWorkflowsService = new ExtWorkflowsServiceImpl(client);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.getBody()).thenReturn(new SOWorkflows(Collections.singletonList(new SOWorkflow(1L, "xyz"))));
        MsoResponseWrapper2<SOWorkflows> msoResponseStub = new MsoResponseWrapper2<>(response);
        Mockito.when(client.getWorkflows("test")).thenReturn(msoResponseStub);
        // when
        List<SOWorkflow> workflows = extWorkflowsService.getWorkflows("test");
        // then
        Mockito.verify(client).getWorkflows("test");
        assertThat(workflows.get(0).getName(), is("xyz"));
    }

    @Test(expectedExceptions = BadResponseFromMso.class)
    public void shouldThrowBadResponseOnInvalidResponse(){
        // given
        ExtWorkflowsService extWorkflowsService = new ExtWorkflowsServiceImpl(client);
        Mockito.when(response.getStatus()).thenReturn(500);
        Mockito.when(response.getBody()).thenReturn(new SOWorkflows(Collections.singletonList(new SOWorkflow(1L, "xyz"))));
        MsoResponseWrapper2<SOWorkflows> msoResponseStub = new MsoResponseWrapper2<>(response);
        Mockito.when(client.getWorkflows("test")).thenReturn(msoResponseStub);
        // when
        extWorkflowsService.getWorkflows("test");
        // then throw exception
    }

}
