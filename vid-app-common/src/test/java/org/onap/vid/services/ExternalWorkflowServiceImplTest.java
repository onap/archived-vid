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

package org.onap.vid.services;



import com.google.common.collect.Lists;
import io.joshworks.restclient.http.HttpResponse;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.onap.vid.model.SOWorkflow;
import org.onap.vid.model.SOWorkflowParameterDefinition;
import org.onap.vid.model.SOWorkflowParameterDefinitions;
import org.onap.vid.model.SOWorkflowType;
import org.onap.vid.model.SOWorkflows;
import org.onap.vid.mso.MsoResponseWrapper2;
import org.onap.vid.mso.rest.MockedWorkflowsRestClient;
import org.onap.vid.services.ExternalWorkflowsServiceImpl.BadResponseFromMso;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;



public class ExternalWorkflowServiceImplTest {

    @Mock
    private MockedWorkflowsRestClient client;
    @Mock
    private HttpResponse<SOWorkflows> response;

    @Mock
    private HttpResponse<SOWorkflowParameterDefinitions> parameterDefinitionsHttpResponse;


    @BeforeMethod
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldReturnWorkflowsOnValidResponse(){
        // given
        ExternalWorkflowsService extWorkflowsService = new ExternalWorkflowsServiceImpl(client);
        Mockito.when(response.getStatus()).thenReturn(200);
        Mockito.when(response.getBody()).thenReturn(new SOWorkflows(Collections.singletonList(new SOWorkflow(1L, "xyz"))));
        MsoResponseWrapper2<SOWorkflows> msoResponseStub = new MsoResponseWrapper2<>(response);
        Mockito.when(client.getWorkflows("test")).thenReturn(msoResponseStub);
        // when
        List<SOWorkflow> workflows = extWorkflowsService.getWorkflows("test");
        // then
        Mockito.verify(client).getWorkflows("test");
        Assertions.assertThat(workflows.get(0).getName()).isEqualTo("xyz");
    }

    @Test(expectedExceptions = BadResponseFromMso.class)
    public void shouldThrowBadResponseOnInvalidResponse(){
        // given
        ExternalWorkflowsService extWorkflowsService = new ExternalWorkflowsServiceImpl(client);
        Mockito.when(response.getStatus()).thenReturn(500);
        Mockito.when(response.getBody()).thenReturn(new SOWorkflows(Collections.singletonList(new SOWorkflow(1L, "xyz"))));
        MsoResponseWrapper2<SOWorkflows> msoResponseStub = new MsoResponseWrapper2<>(response);
        Mockito.when(client.getWorkflows("test")).thenReturn(msoResponseStub);
        // when
        extWorkflowsService.getWorkflows("test");
        // then throw exception
    }
    @Test
    public void shouldReturnWorkflowParametersOnValidResponse() {
        SOWorkflowParameterDefinitions parameters = new SOWorkflowParameterDefinitions(Collections.singletonList(new SOWorkflowParameterDefinition(1L, "sample", "[0-9]", SOWorkflowType.STRING, true)));
        ExternalWorkflowsService extWorkflowsService = new ExternalWorkflowsServiceImpl(client);
        Mockito.when(parameterDefinitionsHttpResponse.getStatus()).thenReturn(200);
        Mockito.when(parameterDefinitionsHttpResponse.getBody()).thenReturn(parameters);
        MsoResponseWrapper2<SOWorkflowParameterDefinitions> msoResponseWrapper = new MsoResponseWrapper2<>(parameterDefinitionsHttpResponse);
        Mockito.when(client.getWorkflowParameterDefinitions(1L)).thenReturn(msoResponseWrapper);


        SOWorkflowParameterDefinitions workflowParameterDefinitions = extWorkflowsService.getWorkflowParameterDefinitions(1L);

        Assertions.assertThat(workflowParameterDefinitions).isEqualTo(parameters);
    }

    @Test
    public void shouldProperlyHandleEmptyParametersList(){
        ExternalWorkflowsService extWorkflowsService = new ExternalWorkflowsServiceImpl(client);
        Mockito.when(parameterDefinitionsHttpResponse.getStatus()).thenReturn(200);
        Mockito.when(parameterDefinitionsHttpResponse.getBody()).thenReturn(new SOWorkflowParameterDefinitions(Lists.newArrayList()));

        MsoResponseWrapper2<SOWorkflowParameterDefinitions> msoResponseWrapper = new MsoResponseWrapper2<>(parameterDefinitionsHttpResponse);
        Mockito.when(client.getWorkflowParameterDefinitions(1L)).thenReturn(msoResponseWrapper);


        SOWorkflowParameterDefinitions workflowParameterDefinitions = extWorkflowsService.getWorkflowParameterDefinitions(1L);
        Assertions.assertThat(workflowParameterDefinitions.getParameterDefinitions()).isEmpty();
    }
}
