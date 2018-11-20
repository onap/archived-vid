/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright © 2018 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright 2018 Nokia
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

package org.onap.vid.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.onap.vid.controllers.ChangeManagementController.CHANGE_MANAGEMENT;
import static org.onap.vid.controllers.ChangeManagementController.FAILED_TO_ADD_MSG;
import static org.onap.vid.controllers.ChangeManagementController.FAILED_TO_DELETE_MSG;
import static org.onap.vid.controllers.ChangeManagementController.FAILED_TO_GET_ALL_MSG;
import static org.onap.vid.controllers.ChangeManagementController.FAILED_TO_GET_MSG;
import static org.onap.vid.controllers.ChangeManagementController.GET_VNF_WORKFLOW_RELATION;
import static org.onap.vid.controllers.ChangeManagementController.SCHEDULER_BY_SCHEDULE_ID;
import static org.onap.vid.controllers.ChangeManagementController.VNF_WORKFLOW_RELATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.BasicConfigurator;
import org.json.simple.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.onap.vid.changeManagement.ChangeManagementRequest;
import org.onap.vid.changeManagement.GetVnfWorkflowRelationRequest;
import org.onap.vid.changeManagement.GetWorkflowsResponse;
import org.onap.vid.changeManagement.RequestDetails;
import org.onap.vid.changeManagement.VnfDetails;
import org.onap.vid.changeManagement.VnfDetailsWithWorkflows;
import org.onap.vid.changeManagement.VnfWorkflowRelationAllResponse;
import org.onap.vid.changeManagement.VnfWorkflowRelationRequest;
import org.onap.vid.changeManagement.VnfWorkflowRelationResponse;
import org.onap.vid.domain.mso.InstanceIds;
import org.onap.vid.domain.mso.RequestStatus;
import org.onap.vid.exceptions.NotFoundException;
import org.onap.vid.model.ExceptionResponse;
import org.onap.vid.mso.rest.Request;
import org.onap.vid.services.ChangeManagementService;
import org.onap.vid.services.WorkflowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

@RunWith(MockitoJUnitRunner.class)
public class ChangeManagementControllerTest {

    public static final String FILE_NAME = "file";
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
    private ObjectMapper objectMapper;

    private ArgumentMatcher<Collection<String>> collectionMatcher(Collection<String> coll) {
        return new ArgumentMatcher<Collection<String>>() {
            @Override
            public boolean matches(Object o) {
                return CollectionUtils.isEqualCollection(coll, (Collection<String>) o);
            }
        };
    }

    private ArgumentMatcher<MultipartFile> multipartFileMatcher(MultipartFile otherFile) {
        return new ArgumentMatcher<MultipartFile>() {
            @Override
            public boolean matches(Object o) {
                if (o.getClass() != otherFile.getClass()) {
                    return false;
                }
                MultipartFile file = (MultipartFile) o;
                try {
                    return file.getName().equals(otherFile.getName())
                        && file.getSize() == otherFile.getSize()
                        && Arrays.equals(file.getBytes(), otherFile.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
        };
    }

    private <T> ArgumentMatcher<T> requestMatcher(T req) {
        return new ArgumentMatcher<T>() {
            @Override
            public boolean matches(Object o) {
                return o.equals(req);
            }
        };
    }

    @Before
    public void setUp() {
        controller = new ChangeManagementController(workflowService, changeManagementService, objectMapper);
        BasicConfigurator.configure();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        objectMapper = new ObjectMapper();
    }

    @Test
    public void getWorkflow_shouldReturnListOfVnfs_whenServiceReturnsCorrectValue() throws Exception {

        Collection<String> givenVnfs = Arrays.asList("vnf1", "vnf2", "vnf3");
        Collection<String> resultVnfs = Arrays.asList("vnf1", "vnf3");

        given(workflowService.getWorkflowsForVNFs(argThat(collectionMatcher(givenVnfs)))).willReturn(resultVnfs);

        mockMvc.perform(get("/change-management/workflow")
            .param("vnfs", StringUtils.join(givenVnfs, ",")))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(resultVnfs)));
    }


    @Test
    public void getMSOChangeManagements_shouldReturnCollectionOfRequests_whenServiceReturnsCorrectValue()
        throws Exception {

        Collection<Request> requests = Arrays.asList(
            createRequest("network-instance-id-1", "status-message-1"),
            createRequest("network-instance-id-2", "status-message-2"));

        given(changeManagementService.getMSOChangeManagements()).willReturn(requests);

        mockMvc.perform(get("/change-management/mso"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(requests)));
    }

    @Test
    public void changeManagement_shouldReturnOkResponse_whenServiceReturnsCorrectValue() throws Exception {

        String vnfName = "vnfName1";
        ChangeManagementRequest request = createChangeManagementRequest("request-type-1");
        String jsonBody = "{'param1': 'paramparam'}";
        ResponseEntity<String> response = ResponseEntity.ok().body(jsonBody);

        given(changeManagementService.doChangeManagement(
            argThat(requestMatcher(request)), eq(vnfName))).willReturn(response);

        mockMvc.perform(post("/change-management/workflow/{name}", vnfName)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
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
            .fileUpload("/change-management/uploadConfigUpdateFile")
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

        WebApplicationException ex = new WebApplicationException(mockResponse);

        willThrow(ex).given(changeManagementService)
            .uploadConfigUpdateFile(argThat(multipartFileMatcher(file)));

        mockMvc.perform(MockMvcRequestBuilders
            .fileUpload("/change-management/uploadConfigUpdateFile")
            .file(file))
            .andExpect(status().isNotFound())
            .andExpect(content().json(objectMapper.writeValueAsString(new ExceptionResponse(ex))));
    }

    @Test
    public void uploadConfigUpdateFile_shouldReturnInternalServerError_whenServiceThrowsRuntimeException()
        throws Exception {
        byte[] fileContent = "some file content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(FILE_NAME, fileContent);

        RuntimeException ex = new RuntimeException("runtime exception message");

        willThrow(ex).given(changeManagementService)
            .uploadConfigUpdateFile(argThat(multipartFileMatcher(file)));

        mockMvc.perform(MockMvcRequestBuilders
            .fileUpload("/change-management/uploadConfigUpdateFile")
            .file(file))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(objectMapper.writeValueAsString(new ExceptionResponse(ex))));
    }

    @Test
    public void getSchedulerChangeManagements_shouldReturnJsonArray_whenServiceReturnsCorrectValue() throws Exception {

        JSONArray arr = new JSONArray();
        arr.add("element1");
        arr.add("element2");

        given(changeManagementService.getSchedulerChangeManagements()).willReturn(arr);

        mockMvc.perform(get("/change-management/scheduler"))
            .andExpect(status().isOk())
            .andExpect(content().json(arr.toJSONString()));
    }

    @Test
    public void deleteSchedule_shouldReturnOkResponse_whenServiceReturnsOkStatus() throws Exception {

        String id = "schedule-id-1";
        Pair<String, Integer> pair = new ImmutablePair<>("myString", HttpStatus.OK.value());

        given(changeManagementService.deleteSchedule(id)).willReturn(pair);

        mockMvc.perform(delete("/change-management" + SCHEDULER_BY_SCHEDULE_ID, id))
            .andExpect(status().isOk());
    }

    @Test
    public void deleteSchedule_shouldReturnNotFoundResponse_whenServiceReturnsNotFoundStatus() throws Exception {

        String id = "schedule-id-1";
        Pair<String, Integer> pair = new ImmutablePair<>("myString", HttpStatus.NOT_FOUND.value());

        given(changeManagementService.deleteSchedule(id)).willReturn(pair);

        mockMvc.perform(delete("/change-management" + SCHEDULER_BY_SCHEDULE_ID, id))
            .andExpect(status().isNotFound());
    }

    @Test
    public void getWorkflows_shouldReturnOkResponse_whenServiceReturnsOkStatus() throws Exception {

        GetVnfWorkflowRelationRequest request = createGetVnfWorkflowRelationRequest("abc1", "abc2");
        ImmutableList<String> elements = ImmutableList.of("workflow1", "workflow2");
        GetWorkflowsResponse response = new GetWorkflowsResponse();
        response.setWorkflows(elements);

        given(changeManagementService.getWorkflowsForVnf(
            argThat(requestMatcher(request)))).willReturn(elements);

        mockMvc.perform(post("/change-management/" + GET_VNF_WORKFLOW_RELATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void getWorkflows_shouldReturnNotFound_whenServiceThrowsNotFoundException() throws Exception {

        GetVnfWorkflowRelationRequest request = createGetVnfWorkflowRelationRequest("abc1", "abc2");

        String errorMsg = "not found";
        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of(errorMsg));

        willThrow(new NotFoundException(errorMsg))
            .given(changeManagementService)
            .getWorkflowsForVnf(argThat(requestMatcher(request)));

        mockMvc.perform(post("/change-management/" + GET_VNF_WORKFLOW_RELATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void getWorkflows_shouldReturnInternalServerError_whenServiceThrowsRuntimeException() throws Exception {

        GetVnfWorkflowRelationRequest request = createGetVnfWorkflowRelationRequest("abc1", "abc2");
        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of(FAILED_TO_GET_MSG));

        willThrow(new RuntimeException("runtime exception message"))
            .given(changeManagementService).getWorkflowsForVnf(isA(GetVnfWorkflowRelationRequest.class));

        mockMvc.perform(post("/change-management/" + GET_VNF_WORKFLOW_RELATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void createWorkflowRelation_shouldReturnOkResponse_whenServiceReturnsOkStatus() throws Exception {

        VnfWorkflowRelationRequest request = new VnfWorkflowRelationRequest();
        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse();

        given(changeManagementService.addVnfWorkflowRelation(argThat(requestMatcher(request))))
            .willReturn(response);

        mockMvc.perform(post("/" + CHANGE_MANAGEMENT + "/" + VNF_WORKFLOW_RELATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void createWorkflowRelation_shouldReturnInternalServerError_whenServiceThrowsException() throws Exception {

        VnfWorkflowRelationRequest request = new VnfWorkflowRelationRequest();
        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of(FAILED_TO_ADD_MSG));

        willThrow(new RuntimeException("runtime exception message"))
            .given(changeManagementService).addVnfWorkflowRelation(argThat(requestMatcher(request)));

        mockMvc.perform(post("/" + CHANGE_MANAGEMENT + "/" + VNF_WORKFLOW_RELATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void getAllWorkflowRelation_shouldReturnOkResponse_whenServiceReturnsOkStatus() throws Exception {

        VnfDetailsWithWorkflows workflows = new VnfDetailsWithWorkflows();
        workflows.setWorkflows(ImmutableList.of("workflow1", "workflow2"));
        VnfWorkflowRelationAllResponse response = new VnfWorkflowRelationAllResponse(ImmutableList.of(workflows));

        given(changeManagementService.getAllVnfWorkflowRelations()).willReturn(response);

        mockMvc.perform(get("/" + CHANGE_MANAGEMENT + "/" + VNF_WORKFLOW_RELATION))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void getAllWorkflowRelation_shouldReturnInternalServerError_whenServiceThrowsRuntimeException()
        throws Exception {

        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of(FAILED_TO_GET_ALL_MSG));

        willThrow(new RuntimeException("runtime exception message"))
            .given(changeManagementService).getAllVnfWorkflowRelations();

        mockMvc.perform(get("/" + CHANGE_MANAGEMENT + "/" + VNF_WORKFLOW_RELATION))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void deleteWorkflowRelation_shouldReturnOkResponse_whenServiceReturnsOkStatus() throws Exception {
        VnfWorkflowRelationRequest request = new VnfWorkflowRelationRequest();
        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of("abc"));

        given(changeManagementService.deleteVnfWorkflowRelation(argThat(requestMatcher(request))))
            .willReturn(response);

        mockMvc.perform(delete("/" + CHANGE_MANAGEMENT + "/" + VNF_WORKFLOW_RELATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    public void deleteWorkflowRelation_shouldReturnInternalServerError_whenServiceThrowsRuntimeException()
        throws Exception {
        VnfWorkflowRelationRequest request = new VnfWorkflowRelationRequest();
        VnfWorkflowRelationResponse response = new VnfWorkflowRelationResponse(ImmutableList.of(FAILED_TO_DELETE_MSG));

        willThrow(new RuntimeException("runtime exception message"))
            .given(changeManagementService).deleteVnfWorkflowRelation(argThat(requestMatcher(request)));

        mockMvc.perform(delete("/" + CHANGE_MANAGEMENT + "/" + VNF_WORKFLOW_RELATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isInternalServerError())
            .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    private GetVnfWorkflowRelationRequest createGetVnfWorkflowRelationRequest(String detailsUUID,
        String invariantUUID) {
        GetVnfWorkflowRelationRequest request = new GetVnfWorkflowRelationRequest();
        request.setVnfDetails(ImmutableList.of(new VnfDetails(detailsUUID, invariantUUID)));
        return request;
    }

    private ChangeManagementRequest createChangeManagementRequest(String requestType) {
        ChangeManagementRequest req = new ChangeManagementRequest();

        RequestDetails rd = new RequestDetails();
        rd.setVnfInstanceId("instance1");

        req.setRequestType(requestType);
        req.setRequestDetails(ImmutableList.of(rd));

        return req;
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
}