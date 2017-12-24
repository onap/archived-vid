package org.opencomp.vid.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.opencomp.vid.api.simulator.SimulatorApi;
import org.opencomp.vid.api.simulator.SimulatorApi.RegistrationStrategy;
import org.opencomp.vid.testUtils.TestUtils;
import org.openecomp.vid.changeManagement.*;
import org.openecomp.vid.model.MsoExceptionResponse;
import org.openecomp.vid.mso.MsoResponseWrapper;
import org.openecomp.vid.mso.MsoResponseWrapper2;
import org.springframework.http.HttpStatus;
import org.springframework.util.StopWatch;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.opencomp.vid.testUtils.TestUtils.getNestedPropertyInMap;
import static org.openecomp.vid.controller.ChangeManagementController.*;


//This is integration test that require running tomcat
public class ChangeManagementApiTest extends BaseApiTest {

    public static final String WORKFLOW = "/workflow/{vnfname}";
    public static final String APPLICATION_JSON = "application/json";
    public static final String DELETE_SCHEDULE_OK_JSON = "delete_schedule_ok.json";
    public static final String DELETE_SCHEDULE_NOT_AUTHORIZED_JSON = "delete_schedule_not_authorized.json";
    private static final String UPDATE = "Update";
    private static final String REPLACE = "Replace";
    private static final List<String> WORKFLOWS = Arrays.asList(UPDATE, REPLACE);

    @DataProvider
    public static Object[][] requestWithoutServiceInstanceId(Method test) {
        return new Object[][]{
                {(Consumer<ChangeManagementRequest>) changeManagementRequest -> changeManagementRequest.getRequestDetails().get(0).setRelatedInstList(null)},
                {(Consumer<ChangeManagementRequest>) changeManagementRequest -> changeManagementRequest.getRequestDetails().get(0).setRelatedInstList(new ArrayList<>())},
                {(Consumer<ChangeManagementRequest>) changeManagementRequest -> changeManagementRequest.getRequestDetails().get(0).getRelatedInstList().get(0).setRelatedInstance(null)},
                {(Consumer<ChangeManagementRequest>) changeManagementRequest -> changeManagementRequest.getRequestDetails().get(0).getRelatedInstList().get(0).getRelatedInstance().setInstanceId(null)}

        };
    }

    @DataProvider
    public static Object[][] requestWithoutPayload(Method test) {
        return new Object[][]{
                {(Consumer<ChangeManagementRequest>) changeManagementRequest -> changeManagementRequest.getRequestDetails().get(0).getRequestParameters().getAdditionalProperties().clear()},
                {(Consumer<ChangeManagementRequest>) changeManagementRequest -> changeManagementRequest.getRequestDetails().get(0).setRequestParameters(null)},
        };
    }

    @DataProvider
    public static Object[][] wrongPayloads(Method test) {
        return new Object[][]{
                {"{\"existing-software-version\": \"3.1%\",\"new-software-version\": \"3.2\", \"operations-timeout\": \"3600\"}", "existing-software-version"},
                {"{\"new-software-version\": \"3.2\", \"operations-timeout\": \"3600\"}", "existing-software-version"},
                {"{\"existing-software-version\": 3.1,\"new-software-version\": \"3.2\", \"operations-timeout\": \"3600\"}", "existing-software-version"},
                {"{\"existing-software-version\": \"\",\"new-software-version\": \"3.2\", \"operations-timeout\": \"3600\"}", "existing-software-version"},
                {"{\"existing-software-version\": null,\"new-software-version\": \"3.2\", \"operations-timeout\": \"3600\"}", "existing-software-version"},
                {"{\"existing-software-version\": \"3.1\",\"new-software-version\": \"3^.2\", \"operations-timeout\": \"3600\"}", "new-software-version"},
                {"{\"existing-software-version\": \"3.1\",\"new-software-version\": 3.2, \"operations-timeout\": \"3600\"}", "new-software-version"},
                {"{\"existing-software-version\": \"3.1\", \"operations-timeout\": \"3600\"}", "new-software-version"},
                {"{\"existing-software-version\": \"3.1\",\"new-software-version\": \"\", \"operations-timeout\": \"3600\"}", "new-software-version"},
                {"{\"existing-software-version\": \"3.1\",\"new-software-version\": null, \"operations-timeout\": \"3600\"}", "new-software-version"},
                {"{\"existing-software-version\": \"3.1\",\"new-software-version\": \"3.2\", \"operations-timeout\": \"a3600\"}", "operations-timeout"},
                {"{\"existing-software-version\": \"3.1\",\"new-software-version\": \"3.2\"}", "operations-timeout"},
                {"{\"existing-software-version\": \"3.1\",\"new-software-version\": \"3.2\", \"operations-timeout\": \"\"}", "operations-timeout"},
                {"{\"existing-software-version\": \"3.1\",\"new-software-version\": \"3.2\", \"operations-timeout\": null}", "operations-timeout"},
                {"", ""},
        };
    }

    @DataProvider
    public static Object[][] goodPayloads(Method test) {
        return new Object[][]{
                {"{\"existing-software-version\": \"a3a.1\",\"new-software-version\": \"b3.2c\", \"operations-timeout\": \"3600\"}"},
                {"{\"existing-software-version\": \"a3a.1\",\"new-software-version\": \"b3.2c\", \"operations-timeout\": 3600}"},
                {"{\"existing-software-version\": \"a3a.1\",\"new-software-version\": \"b3.2c\", \"operations-timeout\": 3600, \"extra\": \"me\"}"},
                {"{\"existing-software-version\": \"3.1\",\"new-software-version\": \"a.c\", \"operations-timeout\": \"0180\"}"},

        };
    }

    @Test
    public void testInPlaceSoftwareUpdateHappyPath() throws IOException {

        VnfIds vnfIds = new VnfIds();
        MsoResponseWrapper2 body = callInPlaceSoftwareUpdate(vnfIds, "mso_in_place_software_update_ok.json", MsoResponseWrapper2.class);
        assertForHappyPath(vnfIds, body);
    }

    private void assertForHappyPath(VnfIds vnfIds, MsoResponseWrapper2 body) {
        Assert.assertEquals(body.getStatus(), 202, "IN_PLACE_SOFTWARE_UPDATE failed with wrong http status");
        Assert.assertEquals(
                getNestedPropertyInMap(body.getEntity(), "requestReferences/instanceId"),
                vnfIds.serviceInstanceId,
                String.format("Failed to find instanceId: %s in IN_PLACE_SOFTWARE_UPDATE response.  Actual body:%s",
                        vnfIds.serviceInstanceId, body.getEntity()));
    }

    private <T> T callInPlaceSoftwareUpdate(VnfIds vnfIds, String expectationPath, Class<T> responseClass) {
        SimulatorApi.registerExpectation(
                expectationPath,
                ImmutableMap.of("SERVICE_INSTANCE_ID", vnfIds.serviceInstanceId, "VNF_INSTANCE_ID", vnfIds.vnfInstanceId));

        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds);
        Response response = callInPlaceSoftwareUpdate(vnfIds, changeManagementRequest);
        return response.readEntity(responseClass);
    }

    private Response callInPlaceSoftwareUpdate(VnfIds vnfIds, ChangeManagementRequest changeManagementRequest) {
        WebTarget webTarget = client.target(uri).
                path(CHANGE_MANAGEMENT + WORKFLOW).resolveTemplate("vnfname", vnfIds.vnfName);
        Entity entity = Entity.json(changeManagementRequest);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(entity);
        return response;
    }

    @Test
    public void testInPlaceSoftwareUpdate409Error() throws IOException {
        VnfIds vnfIds = new VnfIds();
        MsoResponseWrapper2 body = callInPlaceSoftwareUpdate(vnfIds, "mso_in_place_software_update_error_409.json", MsoResponseWrapper2.class);
        Assert.assertEquals(body.getStatus(), 409, "IN_PLACE_SOFTWARE_UPDATE failed with wrong http status");
        Assert.assertEquals(
                getNestedPropertyInMap(body.getEntity(), "serviceException/messageId"),
                "SVC2000",
                String.format("Failed to find messageId: %s in IN_PLACE_SOFTWARE_UPDATE response.  Actual body:%s",
                        "SVC2000", body.getEntity()));


        assertThat(getNestedPropertyInMap(body.getEntity(), "serviceException/text"), containsString(vnfIds.vnfInstanceId));
    }

    @Test
    public void testInPlaceSoftwareUpdate404Error() throws IOException {
        VnfIds vnfIds = new VnfIds();
        MsoResponseWrapper body = callInPlaceSoftwareUpdate(vnfIds, "mso_in_place_software_update_error_404.json", MsoResponseWrapper.class);
        Assert.assertEquals(body.getStatus(), 404, "IN_PLACE_SOFTWARE_UPDATE failed with wrong http status");
        assertThat(body.getEntity().toString(), equalTo("<html><head><title>Error</title></head><body>404 - Not Found</body></html>"));
    }

    @Test
    public void testInPlaceSoftwareUpdateWithoutVnfInstanceId() throws IOException {
        VnfIds vnfIds = new VnfIds();
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds);
        changeManagementRequest.getRequestDetails().get(0).setVnfInstanceId(null);
        MsoExceptionResponse exceptionResponse = callInPlaceSoftwareUpdateAndExpectForException(vnfIds, changeManagementRequest);
        assertThat(exceptionResponse.serviceException.text, containsString("No vnfInstanceId in request"));
    }

    @Test(dataProvider = "requestWithoutServiceInstanceId")
    public void testInPlaceSoftwareUpdateWithoutServiceInstanceId(Consumer<ChangeManagementRequest> dropInstanceIdMethod) throws IOException {
        VnfIds vnfIds = new VnfIds();
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds);
        dropInstanceIdMethod.accept(changeManagementRequest);
        MsoExceptionResponse exceptionResponse = callInPlaceSoftwareUpdateAndExpectForException(vnfIds, changeManagementRequest);
        assertThat(exceptionResponse.serviceException.text, containsString("No instanceId in request"));
    }

    @Test(dataProvider = "wrongPayloads")
    public void testInPlaceSoftwareUpdateInvalidPayload(String payload, String propertyName) throws IOException {

        VnfIds vnfIds = new VnfIds();
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds);
        changeManagementRequest.getRequestDetails().get(0).getRequestParameters().getAdditionalProperties().put("payload", payload);
        MsoExceptionResponse exceptionResponse = callInPlaceSoftwareUpdateAndExpectForException(vnfIds, changeManagementRequest);
        assertThat(exceptionResponse.serviceException.text, containsString(propertyName));
        assertThat(exceptionResponse.serviceException.text, containsString("No valid payload"));
    }

    @Test(dataProvider = "requestWithoutPayload")
    public void testInPlaceSoftwareUpdateWithoutPayload(Consumer<ChangeManagementRequest> dropPayloadMethod) throws IOException {

        VnfIds vnfIds = new VnfIds();
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds);
        dropPayloadMethod.accept(changeManagementRequest);
        MsoExceptionResponse exceptionResponse = callInPlaceSoftwareUpdateAndExpectForException(vnfIds, changeManagementRequest);
        assertThat(exceptionResponse.serviceException.text, containsString("No valid payload"));
    }

    private MsoExceptionResponse callInPlaceSoftwareUpdateAndExpectForException(VnfIds vnfIds, ChangeManagementRequest changeManagementRequest) {
        Response response = callInPlaceSoftwareUpdate(vnfIds, changeManagementRequest);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK.value(), "IN_PLACE_SOFTWARE_UPDATE wrong http status");
        MsoResponseWrapper2<MsoExceptionResponse> msoResponseWrapper2 = response.readEntity(new GenericType<MsoResponseWrapper2<MsoExceptionResponse>>() {
        });
        assertThat(msoResponseWrapper2.getStatus(), equalTo(400));
        assertThat(msoResponseWrapper2.getEntity(), instanceOf(MsoExceptionResponse.class));
        return (MsoExceptionResponse) msoResponseWrapper2.getEntity();
    }

    @Test(dataProvider = "goodPayloads")
    public void testInPlaceSoftwareUpdateGoodPayload(String payload) throws IOException {
        VnfIds vnfIds = new VnfIds();
        SimulatorApi.registerExpectation(
                "mso_in_place_software_update_ok.json",
                ImmutableMap.of("SERVICE_INSTANCE_ID", vnfIds.serviceInstanceId, "VNF_INSTANCE_ID", vnfIds.vnfInstanceId));
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds);
        changeManagementRequest.getRequestDetails().get(0).getRequestParameters().getAdditionalProperties().put("payload", payload);
        Response response = callInPlaceSoftwareUpdate(vnfIds, changeManagementRequest);
        MsoResponseWrapper2 body = response.readEntity(MsoResponseWrapper2.class);
        assertForHappyPath(vnfIds, body);
    }

    @Test
    public void testInPlaceSoftwareUpdateSimultaneousCalls() throws IOException, InterruptedException {
        SimulatorApi.clearExpectations();
        final int SIZE = 20;
        StopWatch stopWatch = new StopWatch("InPlaceSoftwareUpdateSimultaneousCalls");

        stopWatch.start("init");
        ExecutorService executor = Executors.newFixedThreadPool(SIZE);
        List<VnfIds> vnfList = Stream.generate(VnfIds::new).limit(SIZE).collect(Collectors.toList());
        stopWatch.stop();

        stopWatch.start("invoke registration to simulator");
        List<Callable<String>> siumlatorRegistrations = vnfList.stream().map(
                vnfIds -> ((Callable<String>) () ->
                {
                    SimulatorApi.registerExpectation(
                            "mso_in_place_software_update_ok.json",
                            ImmutableMap.of("SERVICE_INSTANCE_ID", vnfIds.serviceInstanceId, "VNF_INSTANCE_ID", vnfIds.vnfInstanceId),
                            RegistrationStrategy.APPEND);
                    return null;
                }))
                .collect(Collectors.toList());

        executor.invokeAll(siumlatorRegistrations)
                .forEach(future -> {
                    try {
                        future.get();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        stopWatch.stop();

        stopWatch.start("init requests");
        List<ChangeManagementRequest> requestsList = vnfList.stream().map(this::createChangeManagementRequest).collect(Collectors.toList());
        WebTarget webTarget = client.target(uri).
                path(CHANGE_MANAGEMENT + WORKFLOW).resolveTemplate("vnfname", "VidVnf");
        List<Callable<Response>> callables = requestsList.stream().map(request -> ((Callable<Response>) () -> webTarget.request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(request)))).collect(Collectors.toList());
        stopWatch.stop();

        stopWatch.start("invoke calling to vid");
        List<MsoResponseWrapper2> responseList = executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get().readEntity(MsoResponseWrapper2.class);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        stopWatch.stop();

        stopWatch.start("assertion");
        Assert.assertEquals(responseList.size(), SIZE, "Failed to get all responses from server");
        responseList.forEach(response -> Assert.assertEquals(response.getStatus(), 202, "wrong http status for " + response.getEntity()));
        vnfList.forEach(vnfIds ->
                Assert.assertTrue(isTextContainsInList(responseList, vnfIds.serviceInstanceId),
                        "Failed to find response for isntanceId: " + vnfIds.serviceInstanceId));
        stopWatch.stop();
        System.out.print(stopWatch.prettyPrint());
    }

    private boolean isTextContainsInList(List<MsoResponseWrapper2> responseList, String str) {
        for (MsoResponseWrapper2 response : responseList) {
            if (response.getEntity().toString().contains(str))
                return true;
        }
        return false;
    }

    private ChangeManagementRequest createChangeManagementRequest(VnfIds vnfDetails) {
        String payload = "{\"existing-software-version\": \"3.1\",\"new-software-version\": \"3.2\", \"operations-timeout\": \"3600\"}";
        RequestDetails requestDetails = new RequestDetails();

        org.openecomp.vid.domain.mso.CloudConfiguration cloudConfiguration = new org.openecomp.vid.domain.mso.CloudConfiguration();
        cloudConfiguration.setLcpCloudRegionId("mdt1");
        cloudConfiguration.setTenantId("88a6ca3ee0394ade9403f075db23167e");
        requestDetails.setCloudConfiguration(cloudConfiguration);

        org.openecomp.vid.domain.mso.RequestInfo requestInfo = new org.openecomp.vid.domain.mso.RequestInfo();
        requestInfo.setSource("VID");
        requestInfo.setRequestorId("az2016");
        requestDetails.setRequestInfo(requestInfo);

        org.openecomp.vid.domain.mso.RequestParameters requestParameters = new org.openecomp.vid.domain.mso.RequestParameters();
        requestParameters.setAdditionalProperty("payload", payload);
        requestDetails.setRequestParameters(requestParameters);


        RelatedInstance relatedInstance = new RelatedInstance();
        relatedInstance.instanceId = vnfDetails.serviceInstanceId;
        RelatedInstanceList relatedInstanceList = new RelatedInstanceList();
        relatedInstanceList.setRelatedInstance(relatedInstance);
        requestDetails.setRelatedInstList(Collections.singletonList(relatedInstanceList));

        requestDetails.setVnfName(vnfDetails.vnfName);
        requestDetails.setVnfInstanceId(vnfDetails.vnfInstanceId);

        ChangeManagementRequest changeManagementRequest = new ChangeManagementRequest();
        changeManagementRequest.setRequestDetails(Collections.singletonList(requestDetails));
        changeManagementRequest.setRequestType(ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE);
        return changeManagementRequest;
    }

//    @Test
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
        Map<VnfDetails, List<String>> vnfDetailsToWorkflowsMap = vnfWorkflowRelationAllResponse.getVnfs().stream()
                .collect(Collectors.toMap(x -> new VnfDetails(x.getUUID(), x.getInvariantUUID()), VnfDetailsWithWorkflows::getWorkflows));

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

//    @Test
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

        Set<String> replaceUpdateSet = ImmutableSet.of(REPLACE, UPDATE);
        Set<String> replaceSet = ImmutableSet.of(REPLACE);
        Set<String> emptySet = ImmutableSet.of();

        assertWorkflowsListSize(vnfDetailsList, replaceUpdateSet, 0, 1);
        assertWorkflowsListSize(vnfDetailsList, replaceSet, 0, 2);
        assertWorkflowsListSize(vnfDetailsList, replaceSet, 2, 3);
        assertWorkflowsListSize(vnfDetailsList, emptySet, 3, 4);
        assertWorkflowsListSize(vnfDetailsList, replaceSet, 0, 1, 2);
        assertWorkflowsListSize(vnfDetailsList, replaceSet, 0, 1, 2, 3);
        assertWorkflowsListSize(vnfDetailsList, emptySet, 0, 1, 2, 3, 4);
        assertWorkflowsListSize(vnfDetailsList, replaceUpdateSet, 0);

        //delete vnf to workflows relations
        vnfWorkFlowOperationOK(HttpMethod.DELETE, vnfWorkflowRelationRequest);
        ensureAllVnfToWorkflowsRelationsNotExist(workflowsDetails);

        //get vnf that was inserted and relation were removed return empty set
        assertWorkflowsListSize(vnfDetailsList, emptySet, 0);
    }

    @Test
    public void testGetVnfThatWasNeverInsertedReturn404() throws IOException {
        //get vnf that was never inserted return 404
        assertWorkflowsResponse(new GetVnfWorkflowRelationRequest(ImmutableList.of(generateRandomVnfDetails())), HttpStatus.NOT_FOUND);
    }

//    @Test
    void testDeleteVnfThatWasNeverInserted() throws IOException {
        //delete vnf that was never inserted return 200 with error in body
        WorkflowsDetail randomWorkfowDetail = generateRandomWorkflowsDetail();
        Response response = vnfWorkFlowOperationOK(HttpMethod.DELETE, new VnfWorkflowRelationRequest(ImmutableList.of(randomWorkfowDetail)));
        VnfWorkflowRelationResponse vnfWorkflowRelationResponse = response.readEntity(VnfWorkflowRelationResponse.class);
        Assert.assertEquals(vnfWorkflowRelationResponse.getErrors().size(), 1);
        Assert.assertTrue(vnfWorkflowRelationResponse.getErrors().get(0).contains(randomWorkfowDetail.getVnfDetails().getUUID()));
        Assert.assertTrue(vnfWorkflowRelationResponse.getErrors().get(0).contains(randomWorkfowDetail.getVnfDetails().getInvariantUUID()));
    }

//    @Test
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

//    @Test
    public void testMultipleVnfsWhileOneWorkflowNotExist() throws IOException {
        List<WorkflowsDetail> workflowsDetails = generateWorkflowsDetails(3);

        //relation 0 add relation to non exist workflow
        WorkflowsDetail nonExistWorkflowsDetail = workflowsDetails.get(0);
        nonExistWorkflowsDetail.setWorkflowName("NotExist");
        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = new VnfWorkflowRelationRequest(workflowsDetails);

        Response response = vnfWorkFlowOperationOK(HttpMethod.POST, vnfWorkflowRelationRequest);
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

//    @Test
    public void testInsertVnfWithEmptyUUID() throws IOException {
        assertAddVnfWithEmptyIdReturn404((vnfDetails -> vnfDetails.setUUID("")));
    }

//    @Test
    public void testInsertVnfWithEmptyInvariantUUID() throws IOException {
        assertAddVnfWithEmptyIdReturn404((vnfDetails -> vnfDetails.setInvariantUUID("")));
    }

    @Test
    //This test requires a simulator which runs on VID and is mocking Scheduler
    public void testDeleteScheduledWorkflowOk() throws Exception {
        //Register required response
        SimulatorApi.registerExpectation(DELETE_SCHEDULE_OK_JSON, RegistrationStrategy.APPEND);
        assertCancelScheduleResponse(HttpStatus.NO_CONTENT);//204
    }

    @Test
    //This test requires a simulator which runs on VID and is mocking Scheduler
    public void testDeleteScheduledWorkflowNotFound() throws Exception {
        //Register required response
        SimulatorApi.registerExpectation(DELETE_SCHEDULE_NOT_AUTHORIZED_JSON, RegistrationStrategy.APPEND);
        assertCancelScheduleResponse(HttpStatus.UNAUTHORIZED);//401
    }

    private void assertAddVnfWithEmptyIdReturn404(Consumer<VnfDetails> emptyIdSetter) throws IOException {
        List<WorkflowsDetail> workflowsDetails = generateWorkflowsDetails(1);
        emptyIdSetter.accept(workflowsDetails.get(0).getVnfDetails());
        VnfWorkflowRelationRequest vnfWorkflowRelationRequest = new VnfWorkflowRelationRequest(workflowsDetails);
        Response response = vnfWorkFlowOperationOK(HttpMethod.POST, vnfWorkflowRelationRequest);
        VnfWorkflowRelationResponse vnfWorkflowRelationResponse = response.readEntity(VnfWorkflowRelationResponse.class);
        assertErrorResponseForWorkflowDetail(workflowsDetails.get(0), vnfWorkflowRelationResponse);
        assertWorkflowsResponse(new GetVnfWorkflowRelationRequest(ImmutableList.of(generateRandomVnfDetails())), HttpStatus.NOT_FOUND);
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
        for (int index : indices) {
            vnfDetailsList.add(inputList.get(index));
        }
        GetWorkflowsResponse getWorkflowsResponse = getWorkflowsResponseOK(new GetVnfWorkflowRelationRequest(vnfDetailsList));
        Assert.assertEquals(getWorkflowsResponse.getWorkflows().size(), exceptedWorkflows.size());
        Assert.assertTrue(getWorkflowsResponse.getWorkflows().containsAll(exceptedWorkflows));
    }

    private void assertCancelScheduleResponse(HttpStatus expectedStatus) {
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
        Assert.assertTrue(((String) response.getHeaders().get("Content-Type").get(0)).contains(APPLICATION_JSON));
    }

    private Response vnfWorkFlowOperationOK(String method, VnfWorkflowRelationRequest vnfWorkflowRelationRequest) throws IOException {
        return vnfWorkFlowOperation(method, vnfWorkflowRelationRequest, HttpStatus.OK);
    }

    private Response vnfWorkFlowOperation(String method, VnfWorkflowRelationRequest vnfWorkflowRelationRequest, HttpStatus exceptedHttpStatus) throws IOException {
        WebTarget webTarget = client.target(uri).path(CHANGE_MANAGEMENT + "/" + VNF_WORKFLOW_RELATION);
        Response response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).method(method, Entity.json(vnfWorkflowRelationRequest));
        TestUtils.assertHttpStatus(vnfWorkflowRelationRequest, webTarget, response, exceptedHttpStatus);
        return response;
    }

    @SuppressWarnings("SameParameterValue")
    private List<WorkflowsDetail> generateWorkflowsDetails(int size) {
        List<WorkflowsDetail> workflowsDetails = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            workflowsDetails.add(i, generateRandomWorkflowsDetail());
        }
        return workflowsDetails;
    }

    private WorkflowsDetail generateRandomWorkflowsDetail() {
        String workflow = WORKFLOWS.get(random.nextInt(WORKFLOWS.size()));
        VnfDetails vnfDetails = generateRandomVnfDetails();
        return new WorkflowsDetail(vnfDetails, workflow);
    }

    static public class VnfIds {
        public String serviceInstanceId;
        public String vnfInstanceId;
        public String vnfName;

        public VnfIds() {
            this.serviceInstanceId = UUID.randomUUID().toString();
            this.vnfInstanceId = UUID.randomUUID().toString();
            this.vnfName = "VidVnf";
        }


    }

}
