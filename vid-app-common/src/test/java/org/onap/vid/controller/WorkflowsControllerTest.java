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
import org.onap.vid.changeManagement.UIWorkflowsRequest;
import org.onap.vid.changeManagement.WorkflowRequestDetail;
import org.onap.vid.mso.MsoUtil;
import org.onap.vid.mso.model.CloudConfiguration;
import org.onap.vid.services.ChangeManagementService;
import org.onap.vid.services.ExternalWorkflowsService;
import org.onap.vid.services.LocalWorkflowsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WorkflowsControllerTest {

    private static final String VID_WORKFLOWS = "/workflows-management/{serviceInstanceId}/{vnfInstanceId}/{workflow_UUID}";

    private MockMvc mockMvc;

    private WorkflowsController workflowsController;

    @Mock
    private ExternalWorkflowsService externalWorkflowsService;

    @Mock
    private LocalWorkflowsService localWorkflowsService;

    @Mock
    private ChangeManagementService changeManagementService;

    @BeforeClass
    public  void setUp(){
        initMocks(this);
        workflowsController = new WorkflowsController(externalWorkflowsService,localWorkflowsService,changeManagementService);
        mockMvc = MockMvcBuilders.standaloneSetup(workflowsController).build();
    }

    @Test
    public void shouldProperlyReceivePostRequestFromUI() throws Exception {
        //  given
        HttpResponse<String> expectedResponse = createOkResponse();
        ObjectMapper objectMapper = new ObjectMapper();

        UIWorkflowsRequest uiWorkflowsRequest = new UIWorkflowsRequest();

        WorkflowRequestDetail workflowRequestDetail = createWorkflowRequestDetail();

        uiWorkflowsRequest.setRequestDetails(workflowRequestDetail);

        UUID serviceInstanceId = new UUID(1,10);
        UUID vnfInstanceId = new UUID(2,20);
        UUID workflow_UUID = new UUID(3,30);

        given(changeManagementService.invokeVnfWorkflow(
                any(HttpServletRequest.class), eq(uiWorkflowsRequest.getRequestDetails()),eq(serviceInstanceId),eq(vnfInstanceId), eq(workflow_UUID)
                )).willReturn(MsoUtil.wrapResponse(expectedResponse));

        //  when
        ResultActions response = mockMvc.perform( post(VID_WORKFLOWS,serviceInstanceId,vnfInstanceId,workflow_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uiWorkflowsRequest)));

        //then
        response.andExpect(status().isOk());
    }

    private WorkflowRequestDetail createWorkflowRequestDetail() {
        WorkflowRequestDetail workflowRequestDetail = new WorkflowRequestDetail();
        org.onap.vid.changeManagement.RequestParameters requestParameters = new org.onap.vid.changeManagement.RequestParameters();
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("testKey1","testValue1");
        paramsMap.put("testKey2","testValue2");

        List<Map<String,String>> mapArray= new ArrayList<>();
        mapArray.add(paramsMap);
        requestParameters.setUserParams(mapArray);

        CloudConfiguration cloudConfiguration = new CloudConfiguration();
        cloudConfiguration.setCloudOwner("testOwne");
        cloudConfiguration.setTenantId("testId");
        cloudConfiguration.setLcpCloudRegionId("testLcpCloudId");

        workflowRequestDetail.setRequestParameters(requestParameters);
        workflowRequestDetail.setCloudConfiguration(cloudConfiguration);
        return workflowRequestDetail;
    }

    private HttpResponse<String> createOkResponse() {
        StatusLine statusline = new BasicStatusLine(
                new ProtocolVersion("http",1,1), 200, "acceptResponse");

        org.apache.http.HttpResponse responseBase = new BasicHttpResponse(statusline);

        return new HttpResponse<>(responseBase ,String.class, new JsonMapper());
    }
}
