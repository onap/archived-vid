/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright (C) 2018 - 2019 Nokia. All rights reserved.
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.ImmutableList;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.changeManagement.*;
import org.onap.vid.exceptions.NotFoundException;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.mso.rest.InstanceIds;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.mso.rest.RequestStatus;
import org.onap.vid.services.ChangeManagementService;
import org.onap.vid.services.WorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.isA;
import static org.onap.vid.controller.ChangeManagementController.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class ChangeManagementControllerTest {

    private static final String FILE_NAME = "file";
    private static final String GET_VNF_WORKFLOW_RELATION_URL =
        "/" + CHANGE_MANAGEMENT + "/" + GET_VNF_WORKFLOW_RELATION;
    private static final String WORKFLOW_URL = "/" + CHANGE_MANAGEMENT + "/workflow";
    private static final String WORKFLOW_NAME_URL = WORKFLOW_URL + "/{name}";
    private static final String MSO_URL = "/" + CHANGE_MANAGEMENT + "/mso";
    private static final String UPLOAD_CONFIG_UPDATE_FILE_URL = "/" + CHANGE_MANAGEMENT + "/uploadConfigUpdateFile";
    private static final String SCHEDULER_URL = "/" + CHANGE_MANAGEMENT + "/scheduler";
    private static final String SCHEDULER_BY_SCHEDULE_ID_URL = "/" + CHANGE_MANAGEMENT + SCHEDULER_BY_SCHEDULE_ID;
    private static final String VNF_WORKFLOW_RELATION_URL = "/" + CHANGE_MANAGEMENT + "/" + VNF_WORKFLOW_RELATION;
    private static final String VNFS = "vnfs";

    private static final String FAILED_TO_GET_MSG = "Failed to get workflows for vnf";
    private static final String FAILED_TO_ADD_MSG = "Failed to add vnf to workflow relation";
    private static final String FAILED_TO_GET_ALL_MSG = "Failed to get all vnf to workflow relations";
    private static final String FAILED_TO_DELETE_MSG = "Failed to delete vnf from workflow relation";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private ChangeManagementController controller;
    private MockMvc mockMvc;
    @Mock
    private WorkflowService workflowService;
    @Mock
    private ChangeManagementService changeManagementService;
    @Mock
    private Response mockResponse;
    @Mock
    private Response.StatusType statusType;
    private ClassLoader classLoader = getClass().getClassLoader();
    private final String CHANGE_MANAGEMENT_REQUEST_JSON = getRequestContent("change-management-request.json");
    private final String GET_VNF_WORKFLOW_RELATION_REQUEST_JSON = getRequestContent(
        "get-vnf-workflow-relation-request.json");
    private final String VNF_WORKFLOW_RELATION_REQUEST_JSON = getRequestContent("vnf-workflow-relation-request.json");

    @Before
    public void setUp() {
        controller = new ChangeManagementController(workflowService, changeManagementService, objectMapper);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void getWorkflow_shouldReturnListOfVnfs_whenServiceReturnsCorrectValue() throws Exception {

        Collection<String> givenVnfs = ImmutableList.of("vnf1", "vnf2", "vnf3");
        Collection<String> resultWorkflows = ImmutableList.of("workflow1", "workflow2");

        given(
            workflowService.getWorkflowsForVNFs(argThat(other -> CollectionUtils.isEqualCollection(givenVnfs, other))))
            .willReturn(resultWorkflows);

        mockMvc.perform(get(WORKFLOW_URL)
            .param(VNFS, StringUtils.join(givenVnfs, ",")))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(resultWorkflows)));
    }

    @Test
    public void getMSOChangeManagements_shouldReturnCollectionOfRequests_whenServiceReturnsCorrectValue()
        throws Exception {

        Collection<Request> requests = ImmutableList.of(
            createRequest("network-instance-id-1", "status-message-1"),
            createRequest("network-instance-id-2", "status-message-2"));

        given(changeManagementService.getMSOChangeManagements()).willReturn(requests);

        mockMvc.perform(get(MSO_URL))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(requests)));
    }

    @Test
    public void changeManagement_shouldReturnOkResponse_whenServiceReturnsCorrectValue() throws Exception {

        String vnfName = "vnfName1";
        String jsonBody = "{'param1': 'paramparam'}";

        given(changeManagementService.doChangeManagement(
            argThat(request -> matches(request, CHANGE_MANAGEMENT_REQUEST_JSON)), eq(vnfName)))
            .willReturn(ResponseEntity.ok().body(jsonBody));

        mockMvc.perform(post(WORKFLOW_NAME_URL, vnfName)
            .contentType(MediaType.APPLICATION_JSON)
            .content(CHANGE_MANAGEMENT_REQUEST_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(jsonBody));
    }

    @Test
    public void uploadConfigUpdateFile_shouldReturnOkResponse_whenServiceReturnsCorrectJson() throws Exception {

        String jsonString = "{'param1': 'paramparam'}";
        byte[] fileContent = "some file content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(FILE_NAME, fileContent);

        given(changeManagementService
            .uploadConfigUpdateFile(argThat(multipartFileMatcher(file))))
            .willReturn(jsonString);

        mockMvc.perform(MockMvcRequestBuilders
            .fileUpload(UPLOAD_CONFIG_UPDATE_FILE_URL)
            .file(file))
            .andExpect(status().isOk())
            .andExpect(content().json(jsonString));
    }

    @Test
    public void uploadConfigUpdateFile_shouldReturnResponseStatus_whenServiceThrowsWebApplicationException()
        throws Exception {

        byte[] fileContent = "some file content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(FILE_NAME, fileContent);

        given(statusType.getStatusCode()).willReturn(HttpStatus.NOT_FOUND.value());
        given(mockResponse.getStatus()).willReturn(HttpStatus.NOT_FOUND.value());
        given(mockResponse.getStatusInfo()).willReturn(statusType);

        WebApplicationException exception = new WebApplicationException(mockResponse);

        willThrow(exception).given(changeManagementService)
            .uploadConfigUpdateFile(argThat(multipartFileMatcher(file)));

        mockMvc.perform(MockMvcRequestBuilders
            .fileUpload(UPLOAD_CONFIG_UPDATE_FILE_URL)
            .file(file))
            .andExpect(status().isNotFound())
            .andExpect(content().json(objectMapper.writeValueAsString(new ExceptionResponse(exception))));
    }

    @Test
    public void uploadConfigUpdateFile_shouldReturnInternalServerError_whenServiceThrowsRuntimeException()
        throws Exception {

        byte[] fileContent = "some file content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(FILE_NAME, fileContent);

        RuntimeException exception = new RuntimeException("runtime exception message");

        willThrow(exception).given(changeManagementService)
            .uploadConfigUpdateFile(argThat(multipartFileMatcher(file)));

        mockMvc.perform(MockMvcRequestBuilders
            .fileUpload(UPLOAD_CONFIG_UPDATE_FILE_URL)
            .file(file))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(objectMapper.writeValueAsString(new ExceptionResponse(exception))));
    }

    @Test
    public void getSchedulerChangeManagements_shouldReturnJsonArray_whenServiceReturnsCorrectValue() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        array.add("element1");
        array.add("element2");

        given(changeManagementService.getSchedulerChangeManagements()).willReturn(array);

        mockMvc.perform(get(SCHEDULER_URL))
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(array)));
    }

    @Test
    public void deleteSchedule_shouldReturnOkResponse_whenServiceReturnsOkStatus() throws Exception {

        String id = "schedule-id-1";
        Pair<String, Integer> pair = new ImmutablePair<>("myString", HttpStatus.OK.value());

        given(changeManagementService.deleteSchedule(id)).willReturn(pair);

        mockMvc.perform(delete(SCHEDULER_BY_SCHEDULE_ID_URL, id))
            .andExpect(status().isOk());
    }

    @Test
    public void deleteSchedule_shouldReturnNotFoundResponse_whenServiceReturnsNotFoundStatus() throws Exception {

        String id = "schedule-id-1";
        Pair<String, Integer> pair = new ImmutablePair<>("myString", HttpStatus.NOT_FOUND.value());

        given(changeManagementService.deleteSchedule(id)).willReturn(pair);

        mockMvc.perform(delete(SCHEDULER_BY_SCHEDULE_ID_URL, id))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getWorkflows_shouldReturnOkResponse_whenServiceReturnsOkStatus() throws Exception {

        ImmutableList<String> elements = ImmutableList.of("workflow1", "workflow2");
        GetWorkflowsResponse response = new GetWorkflowsResponse();
        response.setWorkflows(elements);

        given(changeManagementService
            .getWorkflowsForVnf(argThat(request -> matches(request, GET_VNF_WORKFLOW_RELATION_REQUEST_JSON))))
            .willReturn(elements);

        mockMvc.perform(post(GET_VNF_WORKFLOW_RELATION_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GET_VNF_WORKFLOW_RELATION_REQUEST_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void getWorkflows_shouldReturnNotFound_whenServiceThrowsNotFoundException() throws Exception {

        String errorMsg = "not found";
        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of(errorMsg));

        willThrow(new NotFoundException(errorMsg))
            .given(changeManagementService)
            .getWorkflowsForVnf(argThat(request -> matches(request, GET_VNF_WORKFLOW_RELATION_REQUEST_JSON)));

        mockMvc.perform(post(GET_VNF_WORKFLOW_RELATION_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(GET_VNF_WORKFLOW_RELATION_REQUEST_JSON))
            .andExpect(status().isNotFound())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void getWorkflows_shouldReturnInternalServerError_whenServiceThrowsRuntimeException() throws Exception {

        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of(FAILED_TO_GET_MSG));

        willThrow(new RuntimeException("runtime exception message"))
            .given(changeManagementService).getWorkflowsForVnf(isA(GetVnfWorkflowRelationRequest.class));

        mockMvc.perform(post(GET_VNF_WORKFLOW_RELATION_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(VNF_WORKFLOW_RELATION_REQUEST_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void createWorkflowRelation_shouldReturnOkResponse_whenServiceReturnsOkStatus() throws Exception {

        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse();

        given(changeManagementService
            .addVnfWorkflowRelation(argThat(request -> matches(request, VNF_WORKFLOW_RELATION_REQUEST_JSON))))
            .willReturn(response);

        mockMvc.perform(post(VNF_WORKFLOW_RELATION_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(VNF_WORKFLOW_RELATION_REQUEST_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void createWorkflowRelation_shouldReturnInternalServerError_whenServiceThrowsException() throws Exception {

        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of(FAILED_TO_ADD_MSG));

        willThrow(new RuntimeException("runtime exception message"))
            .given(changeManagementService).addVnfWorkflowRelation(argThat(request -> matches(request,
            VNF_WORKFLOW_RELATION_REQUEST_JSON)));

        mockMvc.perform(post(VNF_WORKFLOW_RELATION_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(VNF_WORKFLOW_RELATION_REQUEST_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void getAllWorkflowRelation_shouldReturnOkResponse_whenServiceReturnsOkStatus() throws Exception {

        VnfDetailsWithWorkflows workflows = new VnfDetailsWithWorkflows();
        workflows.setWorkflows(ImmutableList.of("workflow1", "workflow2"));
        VnfWorkflowRelationAllResponse response = new VnfWorkflowRelationAllResponse(ImmutableList.of(workflows));

        given(changeManagementService.getAllVnfWorkflowRelations()).willReturn(response);

        mockMvc.perform(get(VNF_WORKFLOW_RELATION_URL))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void getAllWorkflowRelation_shouldReturnInternalServerError_whenServiceThrowsRuntimeException()
        throws Exception {

        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of(FAILED_TO_GET_ALL_MSG));

        willThrow(new RuntimeException("runtime exception message"))
            .given(changeManagementService).getAllVnfWorkflowRelations();

        mockMvc.perform(get(VNF_WORKFLOW_RELATION_URL))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void deleteWorkflowRelation_shouldReturnOkResponse_whenServiceReturnsOkStatus() throws Exception {
        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of("abc"));

        given(changeManagementService.deleteVnfWorkflowRelation(argThat(request -> matches(request,
            VNF_WORKFLOW_RELATION_REQUEST_JSON))))
            .willReturn(response);

        mockMvc.perform(delete(VNF_WORKFLOW_RELATION_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(VNF_WORKFLOW_RELATION_REQUEST_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void deleteWorkflowRelation_shouldReturnInternalServerError_whenServiceThrowsRuntimeException()
        throws Exception {
        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of(FAILED_TO_DELETE_MSG));

        willThrow(new RuntimeException("runtime exception message"))
            .given(changeManagementService).deleteVnfWorkflowRelation(argThat(request -> matches(request,
            VNF_WORKFLOW_RELATION_REQUEST_JSON)));

        mockMvc.perform(delete(VNF_WORKFLOW_RELATION_URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(VNF_WORKFLOW_RELATION_REQUEST_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    private <T> boolean matches(T request, String expectedJson) {
        try {
            return objectMapper.writeValueAsString(request).equals(expectedJson);
        } catch (JsonProcessingException e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
        return false;
    }

    private ArgumentMatcher<MultipartFile> multipartFileMatcher(MultipartFile otherFile) {
        return other -> {
            try {
                return other.getName().equals(otherFile.getName())
                    && other.getSize() == otherFile.getSize()
                    && Arrays.equals(other.getBytes(), otherFile.getBytes());
            } catch (IOException e) {
                System.out.println("IOException occurred: " + e.getMessage());
            }
            return false;
        };
    }

    private Request createRequest(String networkInstanceId, String statusMessage) {
        Request req = new Request();
        InstanceIds instanceIds = new InstanceIds();
        instanceIds.setNetworkInstanceId(networkInstanceId);

        RequestStatus requestStatus = new RequestStatus();
        requestStatus.setStatusMessage(statusMessage);

        req.setInstanceIds(instanceIds);
        req.setRequestStatus(requestStatus);

        return req;
    }

    private String getRequestContent(String filename) {
        InputStream inputStream = classLoader.getResourceAsStream(filename);
        return new Scanner(inputStream).useDelimiter("\\A").next().replaceAll("\\s+", "");
    }
}
