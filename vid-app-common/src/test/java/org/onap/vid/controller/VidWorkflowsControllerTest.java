/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2019 Nokia Intellectual Property. All rights reserved.
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
package org.onap.vid.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.joshworks.restclient.http.HttpResponse;
import io.joshworks.restclient.http.JsonMapper;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.mockito.Mock;
import org.onap.vid.changeManagement.RequestParameters;
import org.onap.vid.changeManagement.UIWorkflowsRequest;
import org.onap.vid.changeManagement.WorkflowRequestDetail;
import org.onap.vid.mso.MsoUtil;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.mso.rest.MsoRestClientNew;
import org.onap.vid.mso.rest.RequestDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VidWorkflowsControllerTest {

    private static final String VID_WORKFLOWS = "/vid/workflows";

    private MockMvc mockMvc;

    VidWorkflowsController vidWorkflowsController;

    @Mock
    MsoRestClientNew msoRestClientNew;

    @BeforeClass
    public  void setUp(){
        initMocks(this);
        vidWorkflowsController = new VidWorkflowsController(msoRestClientNew);
        mockMvc = MockMvcBuilders.standaloneSetup(vidWorkflowsController).build();
    }

    @Test
    public void shouldProperlyReceivePostRequestFromUI() throws Exception {
        //  given
        HttpResponse expectedResponse = createOkResponse();

        UIWorkflowsRequest uiWorkflowsRequest = new UIWorkflowsRequest();
        ObjectMapper objectMapper = new ObjectMapper();

        WorkflowRequestDetail workflowRequestDetail = new WorkflowRequestDetail();
        RequestParameters requestParameters = new RequestParameters();
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("testKey1","testValue1");
        paramsMap.put("testKey2","testValue2");

        ArrayList<HashMap<String,String>> mapArray= new ArrayList<>();
        mapArray.add(paramsMap);
        requestParameters.setUserParameters(mapArray);

        CloudConfiguration cloudConfiguration = new CloudConfiguration();
        cloudConfiguration.setCloudOwner("testOwne");
        cloudConfiguration.setTenantId("testId");
        cloudConfiguration.setLcpCloudRegionId("testLcpCloudId");

        workflowRequestDetail.setRequestParameters(requestParameters);
        workflowRequestDetail.setCloudConfiguration(cloudConfiguration);

        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setAdditionalProperty("testKey1","testValue1");
        requestDetails.setAdditionalProperty("testKey2","testValue2");

        uiWorkflowsRequest.setRequestDetails(workflowRequestDetail);

        given(msoRestClientNew.invokeWorkflow(uiWorkflowsRequest.getRequestDetails(),"/invoke/workflows")).willReturn(MsoUtil.wrapResponse(expectedResponse));

        //  when
        ResultActions response = mockMvc.perform(post(new URI(VID_WORKFLOWS))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uiWorkflowsRequest)));

        //then
        response.andExpect(status().isAccepted());
    }


    private HttpResponse<String> createOkResponse() {
        StatusLine statusline = new BasicStatusLine(
                new ProtocolVersion("http",1,1), 202, "acceptResponse");

        org.apache.http.HttpResponse responseBase = new BasicHttpResponse(statusline);

        return new HttpResponse<>(responseBase ,String.class, new JsonMapper());
    }
}
