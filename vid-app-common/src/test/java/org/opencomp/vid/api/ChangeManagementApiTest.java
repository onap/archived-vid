package org.opencomp.vid.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import org.opencomp.vid.api.simulator.SimulatorApi;
import org.opencomp.vid.testUtils.TestUtils;
import org.openecomp.vid.changeManagement.*;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.openecomp.vid.controller.ChangeManagementController.CHANGE_MANAGEMENT;
import static org.openecomp.vid.controller.ChangeManagementController.GET_VNF_WORKFLOW_RELATION;
import static org.openecomp.vid.controller.ChangeManagementController.VNF_WORKFLOW_RELATION;
import static org.openecomp.vid.controller.ChangeManagementController.SCHEDULER_BY_SCHEDULE_ID;


//This is integration test that require running tomcat
public class ChangeManagementApiTest extends BaseApiTest {

    private static final String UPDATE = "Update";
    private static final String REPLACE = "Replace";
    private static final List<String> WORKFLOWS = Arrays.asList(UPDATE, REPLACE);
    public static final String APPLICATION_JSON = "application/json";
    public static final String DELETE_SCHEDULE_OK_JSON = "delete_schedule_ok.json";
    public static final String DELETE_SCHEDULE_NOT_AUTHORIZED_JSON = "delete_schedule_not_authorized.json";



    @Test
    public void testVnfWorkflowApiCRD() throws IOException {
        List<WorkflowsDetail> workflowsDetails = generateWorkflowsDetails(10);

        //create vnf to workflows relations
        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = new VnfWorkflowRelationRequest(workflowsDetails);
        vnfWorkFlowOperationOK(HttpMethod.POST, vnfWorkflowRelationRequest);

        //ensure all relations exist using get workflows for vnf
        ensureAllVnfToWorkflowsExist(workflowsDetails);

        //ensure all relations exist using get all vnf_to_workflows relations
        Response response = vnfWorkFlowOperationOK(HttpMethod.GET, null);
        VnfWorkflowRelationAllResponse vnfWorkflowRelationAllResponse = response.readEntity(VnfWorkflowRelationAllResponse.class);
        Map<VnfDetails,List<String>> vnfDetailsToWorkflowsMap = vnfWorkflowRelationAllResponse.getVnfs().stream()
                .collect(Collectors.toMap(x-> new VnfDetails(x.getUUID(),x.getInvariantUUID()), VnfDetailsWithWorkflows::getWorkflows ));

        workflowsDetails.forEach(workflowsDetail ->
                Assert.assertTrue(vnfDetailsToWorkflowsMap.get(workflowsDetail.getVnfDetails()).contains(workflowsDetail.getWorkflowName())));

        //delete vnf to workflows relations
        vnfWorkFlowOperationOK(HttpMethod.DELETE, vnfWorkflowRelationRequest);

        //make sure all relations not exist any more
        ensureAllVnfToWorkflowsRelationsNotExist(workflowsDetails);

    }

    private void ensureAllVnfToWorkflowsExist(List<WorkflowsDetail> workflowsDetails) throws IOException {
        for (WorkflowsDetail workflowsDetail : workflowsDetails) {
            GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest = new GetVnfWorkflowRelationRequest(Collections.singletonList(workflowsDetail.getVnfDetails()));
            GetWorkflowsResponse getWorkflowsResponse = getWorkflowsResponseOK(getVnfWorkflowRelationRequest);
            Assert.assertEquals(getWorkflowsResponse.getWorkflows().size(), 1);
            Assert.assertEquals(getWorkflowsResponse.getWorkflows().get(0), workflowsDetail.getWorkflowName());
        }
    }

    private void ensureAllVnfToWorkflowsRelationsNotExist(List<WorkflowsDetail> workflowsDetails) throws IOException {
        for (WorkflowsDetail workflowsDetail : workflowsDetails) {
            GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest = new GetVnfWorkflowRelationRequest(Collections.singletonList(workflowsDetail.getVnfDetails()));
            GetWorkflowsResponse getWorkflowsResponse = getWorkflowsResponseOK(getVnfWorkflowRelationRequest);
            Assert.assertEquals(getWorkflowsResponse.getWorkflows().size(), 0);
        }
    }

    @Test
    public void testVnfWorkflowIntersection() throws IOException {
        List<WorkflowsDetail> workflowsDetails = new ArrayList<>();
        List<VnfDetails> vnfDetailsList = new ArrayList<>();
        //0 - UPDATE,REPLACE
        VnfDetails vnfDetails = generateRandomVnfDetails(vnfDetailsList);
        workflowsDetails.add(new WorkflowsDetail(vnfDetails, REPLACE));
        workflowsDetails.add(new WorkflowsDetail(vnfDetails, UPDATE));
        //1 - UPDATE,REPLACE
        vnfDetails = generateRandomVnfDetails(vnfDetailsList);
        workflowsDetails.add(new WorkflowsDetail(vnfDetails, REPLACE));
        workflowsDetails.add(new WorkflowsDetail(vnfDetails, UPDATE));
        //2 - REPLACE
        vnfDetails = generateRandomVnfDetails(vnfDetailsList);
        workflowsDetails.add(new WorkflowsDetail(vnfDetails, REPLACE));
        //3 - REPLACE
        vnfDetails = generateRandomVnfDetails(vnfDetailsList);
        workflowsDetails.add(new WorkflowsDetail(vnfDetails, REPLACE));
        //4 - UPDATE
        vnfDetails = generateRandomVnfDetails(vnfDetailsList);
        workflowsDetails.add(new WorkflowsDetail(vnfDetails, UPDATE));

        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = new VnfWorkflowRelationRequest(workflowsDetails);
        vnfWorkFlowOperationOK(HttpMethod.POST, vnfWorkflowRelationRequest);

        Set<String> replaceUpdateSet = ImmutableSet.of(REPLACE,UPDATE);
        Set<String> replaceSet = ImmutableSet.of(REPLACE);
        Set<String> emptySet = ImmutableSet.of();

        assertWorkflowsListSize(vnfDetailsList,replaceUpdateSet,0, 1);
        assertWorkflowsListSize(vnfDetailsList,replaceSet,0, 2);
        assertWorkflowsListSize(vnfDetailsList,replaceSet,2, 3);
        assertWorkflowsListSize(vnfDetailsList,emptySet,3, 4);
        assertWorkflowsListSize(vnfDetailsList,replaceSet,0, 1, 2);
        assertWorkflowsListSize(vnfDetailsList,replaceSet,0, 1, 2, 3);
        assertWorkflowsListSize(vnfDetailsList,emptySet,0, 1, 2, 3, 4);
        assertWorkflowsListSize(vnfDetailsList,replaceUpdateSet,0);

        //delete vnf to workflows relations
        vnfWorkFlowOperationOK(HttpMethod.DELETE, vnfWorkflowRelationRequest);
        ensureAllVnfToWorkflowsRelationsNotExist(workflowsDetails);

        //get vnf that was inserted and relation were removed return empty set
        assertWorkflowsListSize(vnfDetailsList,emptySet,0);
    }

    @Test
    public void testGetVnfThatWasNeverInsertedReturn404() throws IOException {
        //get vnf that was never inserted return 404
        assertWorkflowsResponse(new GetVnfWorkflowRelationRequest(ImmutableList.of(generateRandomVnfDetails())),HttpStatus.NOT_FOUND);
    }

    @Test void testDeleteVnfThatWasNeverInserted() throws IOException {
        //delete vnf that was never inserted return 200 with error in body
        WorkflowsDetail randomWorkfowDetail = generateRandomWorkflowsDetail();
        Response response = vnfWorkFlowOperationOK(HttpMethod.DELETE, new VnfWorkflowRelationRequest(ImmutableList.of(randomWorkfowDetail)));
        VnfWorkflowRelationResponse vnfWorkflowRelationResponse = response.readEntity(VnfWorkflowRelationResponse.class);
        Assert.assertEquals(vnfWorkflowRelationResponse.getErrors().size(), 1);
        Assert.assertTrue(vnfWorkflowRelationResponse.getErrors().get(0).contains(randomWorkfowDetail.getVnfDetails().getUUID()));
        Assert.assertTrue(vnfWorkflowRelationResponse.getErrors().get(0).contains(randomWorkfowDetail.getVnfDetails().getInvariantUUID()));
    }

    @Test
    public void testInsertSameVnfToWorkflowsTwice() throws IOException {
        List<WorkflowsDetail> workflowsDetails = generateWorkflowsDetails(1);
        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = new VnfWorkflowRelationRequest(workflowsDetails);
        vnfWorkFlowOperationOK(HttpMethod.POST, vnfWorkflowRelationRequest);
        vnfWorkFlowOperationOK(HttpMethod.POST, vnfWorkflowRelationRequest);

        //ensure workflow exist
        ensureAllVnfToWorkflowsExist(workflowsDetails);

        //delete vnf to workflows relations
        vnfWorkFlowOperationOK(HttpMethod.DELETE, vnfWorkflowRelationRequest);

        //make sure all relations not exist any more
        ensureAllVnfToWorkflowsRelationsNotExist(workflowsDetails);
    }

    @Test
    public void testMultipleVnfsWhileOneWorkflowNotExist() throws IOException {
        List<WorkflowsDetail> workflowsDetails = generateWorkflowsDetails(3);

        //relation 0 add relation to non exist workflow
        WorkflowsDetail nonExistWorkflowsDetail = workflowsDetails.get(0);
        nonExistWorkflowsDetail.setWorkflowName("NotExist");
        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = new VnfWorkflowRelationRequest(workflowsDetails);

        Response response =  vnfWorkFlowOperationOK(HttpMethod.POST, vnfWorkflowRelationRequest);
        VnfWorkflowRelationResponse vnfWorkflowRelationResponse = response.readEntity(VnfWorkflowRelationResponse.class);
        assertErrorResponseForWorkflowDetail(nonExistWorkflowsDetail, vnfWorkflowRelationResponse);

        //ensure other vnf  to workflows exist
        ensureAllVnfToWorkflowsExist(workflowsDetails.subList(1, workflowsDetails.size()));

        //ensure there is no workflow for vnf 0
        GetWorkflowsResponse getWorkflowsResponse = getWorkflowsResponseOK(
                new GetVnfWorkflowRelationRequest(ImmutableList.of(nonExistWorkflowsDetail.getVnfDetails())));
        Assert.assertEquals(getWorkflowsResponse.getWorkflows().size(), 0);

        //delete vnf to workflows relations
        response = vnfWorkFlowOperationOK(HttpMethod.DELETE, vnfWorkflowRelationRequest);
        vnfWorkflowRelationResponse = response.readEntity(VnfWorkflowRelationResponse.class);
        assertErrorResponseForWorkflowDetail(nonExistWorkflowsDetail, vnfWorkflowRelationResponse);

        //make sure all relations not exist any more
        ensureAllVnfToWorkflowsRelationsNotExist(workflowsDetails);
    }

    @Test
    public void testInsertVnfWithEmptyUUID() throws IOException {
        assertAddVnfWithEmptyIdReturn404((vnfDetails -> vnfDetails.setUUID("")));
    }

    @Test
    public void testInsertVnfWithEmptyInvariantUUID() throws IOException {
        assertAddVnfWithEmptyIdReturn404((vnfDetails -> vnfDetails.setInvariantUUID("")));
    }

    @Test
    //This test requires a simulator which runs on VID and is mocking Scheduler
    public void testDeleteScheduledWorkflowOk() throws Exception {
        //Register required response
        SimulatorApi.registerExpectation(DELETE_SCHEDULE_OK_JSON);
        assertCancelScheduleResponse(HttpStatus.NO_CONTENT);//204
    }

    @Test
    //This test requires a simulator which runs on VID and is mocking Scheduler
    public void testDeleteScheduledWorkflowNotFound() throws Exception {
        //Register required response
        SimulatorApi.registerExpectation(DELETE_SCHEDULE_NOT_AUTHORIZED_JSON);
        assertCancelScheduleResponse(HttpStatus.UNAUTHORIZED);//401
    }


    private void assertAddVnfWithEmptyIdReturn404(Consumer<VnfDetails> emptyIdSetter) throws IOException {
        List<WorkflowsDetail> workflowsDetails = generateWorkflowsDetails(1);
        emptyIdSetter.accept(workflowsDetails.get(0).getVnfDetails());
        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = new VnfWorkflowRelationRequest(workflowsDetails);
        Response response =  vnfWorkFlowOperationOK(HttpMethod.POST, vnfWorkflowRelationRequest);
        VnfWorkflowRelationResponse vnfWorkflowRelationResponse = response.readEntity(VnfWorkflowRelationResponse.class);
        assertErrorResponseForWorkflowDetail(workflowsDetails.get(0), vnfWorkflowRelationResponse);
        assertWorkflowsResponse(new GetVnfWorkflowRelationRequest(ImmutableList.of(generateRandomVnfDetails())),HttpStatus.NOT_FOUND);
    }

    private void assertErrorResponseForWorkflowDetail(WorkflowsDetail wrongWorkflowsDetail, VnfWorkflowRelationResponse vnfWorkflowRelationResponse) {
        Assert.assertEquals(vnfWorkflowRelationResponse.getErrors().size(), 1);
        Assert.assertTrue(vnfWorkflowRelationResponse.getErrors().get(0).contains(wrongWorkflowsDetail.getWorkflowName()));
        Assert.assertTrue(vnfWorkflowRelationResponse.getErrors().get(0).contains(wrongWorkflowsDetail.getVnfDetails().getUUID()));
        Assert.assertTrue(vnfWorkflowRelationResponse.getErrors().get(0).contains(wrongWorkflowsDetail.getVnfDetails().getInvariantUUID()));
    }

    private VnfDetails generateRandomVnfDetails(List<VnfDetails> vnfDetailsList) {
        VnfDetails vnfDetails = generateRandomVnfDetails();
        vnfDetailsList.add(vnfDetails);
        return vnfDetails;
    }

    private VnfDetails generateRandomVnfDetails() {
        return new VnfDetails(UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    private void assertWorkflowsListSize(List<VnfDetails> inputList, Set<String> exceptedWorkflows, int... indices) throws IOException {
        List<VnfDetails> vnfDetailsList = new ArrayList<>();
        for (int index:indices) {
            vnfDetailsList.add(inputList.get(index));
        }
        GetWorkflowsResponse getWorkflowsResponse = getWorkflowsResponseOK(new GetVnfWorkflowRelationRequest(vnfDetailsList));
        Assert.assertEquals(getWorkflowsResponse.getWorkflows().size(), exceptedWorkflows.size());
        Assert.assertTrue(getWorkflowsResponse.getWorkflows().containsAll(exceptedWorkflows));
    }

    private void assertCancelScheduleResponse(HttpStatus expectedStatus){
        WebTarget webTarget = client.target(uri).path(CHANGE_MANAGEMENT + SCHEDULER_BY_SCHEDULE_ID.replace("{scheduleId}", "1234"));
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).delete();
        Assert.assertEquals(response.getStatus(), expectedStatus.value());
    }

    private GetWorkflowsResponse getWorkflowsResponseOK(GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest) throws IOException {
        WebTarget webTarget = client.target(uri).path(CHANGE_MANAGEMENT + "/" + GET_VNF_WORKFLOW_RELATION);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(getVnfWorkflowRelationRequest));
        TestUtils.assertStatusOK(getVnfWorkflowRelationRequest, webTarget, response);
        return response.readEntity(GetWorkflowsResponse.class);
    }

    private void assertWorkflowsResponse(GetVnfWorkflowRelationRequest getVnfWorkflowRelationRequest, HttpStatus exceptedHttpStatus) throws IOException {
        WebTarget webTarget = client.target(uri).path(CHANGE_MANAGEMENT + "/" + GET_VNF_WORKFLOW_RELATION);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(getVnfWorkflowRelationRequest));
        TestUtils.assertHttpStatus(getVnfWorkflowRelationRequest, webTarget, response, exceptedHttpStatus);
        response.readEntity(VnfWorkflowRelationResponse.class); //assert the body is of type VnfWorkflowRelationResponse
        Assert.assertTrue(((String)response.getHeaders().get("Content-Type").get(0)).contains(APPLICATION_JSON));
    }


    private Response vnfWorkFlowOperationOK(String method, VnfWorkflowRelationRequest vnfWorkflowRelationRequest) throws IOException {
        return vnfWorkFlowOperation(method, vnfWorkflowRelationRequest, HttpStatus.OK);
    }

    private Response vnfWorkFlowOperation(String method, VnfWorkflowRelationRequest vnfWorkflowRelationRequest, HttpStatus exceptedHttpStatus) throws IOException {
        WebTarget webTarget = client.target(uri).path(CHANGE_MANAGEMENT+"/"+VNF_WORKFLOW_RELATION);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).method(method, Entity.json(vnfWorkflowRelationRequest));
        TestUtils.assertHttpStatus(vnfWorkflowRelationRequest, webTarget, response, exceptedHttpStatus);
        return response;
    }

    @SuppressWarnings("SameParameterValue")
    private List<WorkflowsDetail> generateWorkflowsDetails(int size) {
        List<WorkflowsDetail> workflowsDetails = new ArrayList<>(size);
        for (int i=0; i<size; i++) {
            workflowsDetails.add(i, generateRandomWorkflowsDetail());
        }
        return workflowsDetails;
    }

    private WorkflowsDetail generateRandomWorkflowsDetail() {
        String workflow = WORKFLOWS.get(random.nextInt(WORKFLOWS.size()));
        VnfDetails vnfDetails = generateRandomVnfDetails();
        return new WorkflowsDetail(vnfDetails, workflow);
    }

}
