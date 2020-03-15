package org.onap.vid.api;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.onap.vid.api.ChangeManagementUserApiLoggingTest.MSO_GET_CHANGE_MANAGEMENTS_SCALEOUT;
import static org.onap.vid.api.TestUtils.getNestedPropertyInMap;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.onap.simulator.presetGenerator.presets.aaf.AAFGetBasicAuthPreset;
import org.onap.simulator.presetGenerator.presets.aaf.AAFGetUrlServicePreset;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOOrchestrationRequestsGetNoTaskInfoBody;
import org.onap.simulator.presetGenerator.presets.mso.changeManagement.PresetMsoChangeManagementBase;
import org.onap.simulator.presetGenerator.presets.mso.changeManagement.PresetMsoVnfInPlaceSoftwareUpdate;
import org.onap.simulator.presetGenerator.presets.mso.changeManagement.PresetMsoVnfReplace;
import org.onap.simulator.presetGenerator.presets.mso.changeManagement.PresetMsoVnfUpdate;
import org.onap.vid.model.mso.ChangeManagementRequest;
import org.onap.vid.model.mso.ChangeManagementRequestDetails;
import org.onap.vid.model.mso.CloudConfiguration;
import org.onap.vid.model.mso.MsoExceptionResponse;
import org.onap.vid.model.mso.MsoResponseWrapper2;
import org.onap.vid.model.mso.RelatedInstance;
import org.onap.vid.model.mso.RelatedInstanceList;
import org.onap.vid.model.mso.RequestInfo;
import org.onap.vid.model.mso.RequestParameters;
import org.onap.vid.model.workflow.GetVnfWorkflowRelationRequest;
import org.onap.vid.model.workflow.GetWorkflowsResponse;
import org.onap.vid.model.workflow.VnfDetails;
import org.onap.vid.model.workflow.VnfDetailsWithWorkflows;
import org.onap.vid.model.workflow.VnfWorkflowRelationAllResponse;
import org.onap.vid.model.workflow.VnfWorkflowRelationRequest;
import org.onap.vid.model.workflow.VnfWorkflowRelationResponse;
import org.onap.vid.model.workflow.WorkflowsDetail;
import org.onap.vid.more.LoggerFormatTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.infra.FeatureTogglingTest;
import vid.automation.test.infra.Features;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.SimulatorApi.RegistrationStrategy;
import vid.automation.test.utils.InsecureHttpsClient;
import vid.automation.test.utils.ReadFile;


//This is integration test that require running tomcat
public class ChangeManagementApiTest extends BaseApiTest {

    public static final String WORKFLOW = "/workflow/{vnfname}";
    public static final String APPLICATION_JSON = "application/json";
    public static final String DELETE_SCHEDULE_OK_JSON = "delete_schedule_ok.json";
    public static final String DELETE_SCHEDULE_NOT_AUTHORIZED_JSON = "delete_schedule_not_authorized.json";
    private static final String UPDATE = "Update";
    private static final String REPLACE = "Replace";
    private static final List<String> WORKFLOWS = Arrays.asList(UPDATE, REPLACE);
    public static final String CHANGE_MANAGEMENT = "change-management";
    public static final String GET_VNF_WORKFLOW_RELATION = "get_vnf_workflow_relation";
    public static final String VNF_WORKFLOW_RELATION = "vnf_workflow_relation";
    public static final String SCHEDULER_BY_SCHEDULE_ID = "/scheduler/schedules/{scheduleId}";
    public static final String MSO = "/mso";

    @DataProvider
    public static Object[][] requestWithoutServiceInstanceId(Method test) {
        return new Object[][]{
                {"RelatedInstList null", (Consumer<ChangeManagementRequest>) changeManagementRequest -> changeManagementRequest.getRequestDetails().get(0).setRelatedInstList(null)},
                {"Empty list", (Consumer<ChangeManagementRequest>) changeManagementRequest -> changeManagementRequest.getRequestDetails().get(0).setRelatedInstList(new ArrayList<>())},
                {"Related instance null", (Consumer<ChangeManagementRequest>) changeManagementRequest -> changeManagementRequest.getRequestDetails().get(0).getRelatedInstList().get(0).setRelatedInstance(null)},
                {"instanceId null", (Consumer<ChangeManagementRequest>) changeManagementRequest -> changeManagementRequest.getRequestDetails().get(0).getRelatedInstList().get(0).getRelatedInstance().setInstanceId(null)}
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
                {"{\"existing_software_version\": \"3.1%\",\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}", "existing_software_version"},
                {"{\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}", "existing_software_version"},
                {"{\"existing_software_version\": 3.1,\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}", "existing_software_version"},
                {"{\"existing_software_version\": \"\",\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}", "existing_software_version"},
                {"{\"existing_software_version\": null,\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}", "existing_software_version"},
                {"{\"existing_software_version\": \"3.1\",\"new_software_version\": \"3^.2\", \"operations_timeout\": \"3600\"}", "new_software_version"},
                {"{\"existing_software_version\": \"3.1\",\"new_software_version\": 3.2, \"operations_timeout\": \"3600\"}", "new_software_version"},
                {"{\"existing_software_version\": \"3.1\", \"operations_timeout\": \"3600\"}", "new_software_version"},
                {"{\"existing_software_version\": \"3.1\",\"new_software_version\": \"\", \"operations_timeout\": \"3600\"}", "new_software_version"},
                {"{\"existing_software_version\": \"3.1\",\"new_software_version\": null, \"operations_timeout\": \"3600\"}", "new_software_version"},
                {"{\"existing_software_version\": \"3.1\",\"new_software_version\": \"3.2\", \"operations_timeout\": \"a3600\"}", "operations_timeout"},
                {"{\"existing_software_version\": \"3.1\",\"new_software_version\": \"3.2\"}", "operations_timeout"},
                {"{\"existing_software_version\": \"3.1\",\"new_software_version\": \"3.2\", \"operations_timeout\": \"\"}", "operations_timeout"},
                {"{\"existing_software_version\": \"3.1\",\"new_software_version\": \"3.2\", \"operations_timeout\": null}", "operations_timeout"},
                {"", ""},
        };
    }

    @DataProvider
    public static Object[][] goodPayloads(Method test) {
        return new Object[][]{
                {"{\"existing_software_version\": \"a3a.1\",\"new_software_version\": \"b3.2c\", \"operations_timeout\": \"3600\"}"},
                {"{\"existing_software_version\": \"a3a.1\",\"new_software_version\": \"b3.2c\", \"operations_timeout\": 3600}"},
                {"{\"existing_software_version\": \"a3a.1\",\"new_software_version\": \"b3.2c\", \"operations_timeout\": 3600, \"extra\": \"me\"}"},
                {"{\"existing_software_version\": \"3.1\",\"new_software_version\": \"a.c\", \"operations_timeout\": \"0180\"}"},

        };
    }

    @DataProvider
    public static Object[][] wrongConfigPayloads(Method test) {
        return new Object[][]{
                {"{\"request-parameters\": \"3.1%\",\"new_software_version\": \"3.2\"}", "configuration-parameters"},
                {"{\"configuration-parameters\": 3.1,\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}", "request-parameters"},
                {"{\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}", "request-parameters"},
                {"{\"payload\": {\"configuration-parameters\": 3.1,\"request-parameters\": \"3.2\" }}", "request-parameters"},
                {"",""},
        };
    }

    @DataProvider
    public static Object[][] goodConfigPayloads(Method test){
        return new Object[][]{
                {"{\"configuration-parameters\": 3.1,\"request-parameters\": \"3.2\" }"},
                {"{\"configuration-parameters\": 3.1,\"request-parameters\": \"3.2\", \"operations_timeout\": \"3600\"}"},
                {"{\"configuration-parameters\": 3.1,\"new_software_version\": \"3.2\",\"request-parameters\": \"3.2\" }"}
        };
    }

    @DataProvider
    public static Object[][] credentialsFilterTestData(Method test) {
        return new Object[][]{
                {AAFGetBasicAuthPreset.VALID_AUTH_VALUE, 200},
//                {null, 401}, // No auth header   TODO: Uncomment when the bug in epsdk will be fixed. Currently If there is no auth header then no credentials validation.
                {"xyz", 401}, //undecodable value
                {"YUBiLmM6ZGVm", 401} //decodable value but not that expected by cadi filter (simulator)

        };
    }

    @BeforeClass
    public static void commonSimulatorRegistration() {
        SimulatorApi.registerExpectationFromPreset(
                PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MDT1_TO_ATT_NC,
                RegistrationStrategy.CLEAR_THEN_SET);
        SimulatorApi.registerExpectationFromPreset(
                new AAFGetUrlServicePreset(),
                RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectationFromPreset(
                new AAFGetBasicAuthPreset(),
                RegistrationStrategy.APPEND);
    }

//  IN_PLACE_SOFTWARE_UPDATE
    @Test
    public void testInPlaceSoftwareUpdateHappyPath() throws IOException {
        testHappyPath("mso_in_place_software_update_ok.json", ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE);
    }

    @Test
    public void testInPlaceSoftwareUpdate409Error() throws IOException {
        testChangeManagement409Error("mso_in_place_software_update_error_409.json", ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE);
    }

    @Test
    public void testInPlaceSoftwareUpdate404Error() throws IOException {
        testChangeManagement404Error("mso_in_place_software_update_error_404.json", ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE);
    }

    @Test
    public void testInPlaceSoftwareUpdateWithoutVnfInstanceId() throws IOException {
        testChangeManagementWithoutVnfInstanceId(ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE);
    }

    @Test(dataProvider = "requestWithoutServiceInstanceId")
    public void testInPlaceSoftwareUpdateWithoutServiceInstanceId(String desc, Consumer<ChangeManagementRequest> dropInstanceIdMethod) throws IOException {
        testChangeManagementServiceInstanceId(dropInstanceIdMethod, ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE);
    }

    @Test(dataProvider = "wrongPayloads")
    public void testInPlaceSoftwareUpdateInvalidPayload(String payload, String propertyName) throws IOException {
        testChangeManagementInvalidPayload(payload, propertyName, ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE);
    }

    @Test(dataProvider = "requestWithoutPayload")
    public void testInPlaceSoftwareUpdateWithoutPayload(Consumer<ChangeManagementRequest> dropPayloadMethod) throws IOException {
        testChangeManagementWithoutPayload(dropPayloadMethod, ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE);
    }

    @Test(dataProvider = "goodPayloads")
    public void testInPlaceSoftwareUpdateGoodPayload(String payload) throws IOException {
        testChangeManagementGoodPayload(payload, "mso_in_place_software_update_ok.json", ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE);
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
                vnfIds->((Callable<String>)() ->
                {
                    SimulatorApi.registerExpectation(
                            "mso_in_place_software_update_ok.json",
                            ImmutableMap.of("SERVICE_INSTANCE_ID", vnfIds.serviceInstanceId, "VNF_INSTANCE_ID", vnfIds.vnfInstanceId),
                            RegistrationStrategy.APPEND);
                    SimulatorApi.registerExpectationFromPreset(
                            new AAFGetUrlServicePreset(),
                            RegistrationStrategy.APPEND);
                    SimulatorApi.registerExpectationFromPreset(
                            new AAFGetBasicAuthPreset(),
                            RegistrationStrategy.APPEND);
                    return null;
                }))
                .collect(Collectors.toList());

        executor.invokeAll(siumlatorRegistrations)
                .forEach(future -> {
                    try {
                        future.get();
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        stopWatch.stop();

        stopWatch.start("init requests");
        List<ChangeManagementRequest> requestsList = vnfList.stream().map(vnfIds -> this.createChangeManagementRequest(vnfIds, ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE)).collect(Collectors.toList());
        WebTarget webTarget = buildWebTarget("VidVnf");
        List<Callable<Response>> callables = requestsList.stream().map(request->((Callable<Response>) () -> webTarget.request(MediaType.APPLICATION_JSON_TYPE).header("Authorization", "Basic " + AAFGetBasicAuthPreset.VALID_AUTH_VALUE).post(Entity.json(request)))).collect(Collectors.toList());
        stopWatch.stop();

        stopWatch.start("invoke calling to vid");
        List<MsoResponseWrapper2> responseList = executor.invokeAll(callables)
                .stream()
                .map(future -> {
                    try {
                        return future.get().readEntity(MsoResponseWrapper2.class);
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
        stopWatch.stop();

        stopWatch.start("assertion");
        Assert.assertEquals(responseList.size(),SIZE,"Failed to get all responses from server");
        responseList.forEach(response->Assert.assertEquals(response.getStatus(), 202, "wrong http status for "+response.getEntity() ));
        vnfList.forEach(vnfIds->
                Assert.assertTrue(isTextContainsInList(responseList, vnfIds.serviceInstanceId),
                        "Failed to find response for isntanceId: "+vnfIds.serviceInstanceId));
        stopWatch.stop();
        System.out.print(stopWatch.prettyPrint());
    }


    @Test
    @FeatureTogglingTest(Features.FLAG_EXP_USE_FORMAT_PARAMETER_FOR_CM_DASHBOARD)
    public void getOrchestrationForDashboardShouldResponseWithFullBody() {
        SimulatorApi.registerExpectation(MSO_GET_CHANGE_MANAGEMENTS_SCALEOUT, RegistrationStrategy.CLEAR_THEN_SET);
        SimulatorApi.registerExpectationFromPreset(new PresetMSOOrchestrationRequestsGetNoTaskInfoBody(), APPEND);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(buildUri(CHANGE_MANAGEMENT + MSO ), String.class);

        assertFalse(responseEntity.getBody().contains("requestProcessingData"));

    }

//  CONFIG_UPDATE
    @Test
    public void testConfigUpdateHappyPath() throws IOException {
        testHappyPath("mso_config_update_ok.json", ChangeManagementRequest.CONFIG_UPDATE);
    }

    @Test
    public void testConfigUpdate409Error() throws IOException {
        testChangeManagement409Error("mso_config_update_error_409.json", ChangeManagementRequest.CONFIG_UPDATE);
    }

    @Test
    public void testConfigUpdate404Error() throws IOException {
        testChangeManagement404Error("mso_config_update_error_404.json", ChangeManagementRequest.CONFIG_UPDATE);
    }

    @Test
    public void testConfigUpdateWithoutVnfInstanceId() throws IOException {
        testChangeManagementWithoutVnfInstanceId(ChangeManagementRequest.CONFIG_UPDATE);
    }

    @Test(dataProvider = "requestWithoutServiceInstanceId")
    public void testConfigUpdateWithoutServiceInstanceId(String desc, Consumer<ChangeManagementRequest> dropInstanceIdMethod) throws IOException {
        testChangeManagementServiceInstanceId(dropInstanceIdMethod, ChangeManagementRequest.CONFIG_UPDATE);
    }

    @Test(dataProvider = "wrongConfigPayloads")
    public void testConfigUpdateInvalidPayload(String payload, String propertyName) throws IOException {
        testChangeManagementInvalidPayload(payload, propertyName, ChangeManagementRequest.CONFIG_UPDATE);
    }

    @Test(dataProvider = "requestWithoutPayload")
    public void testConfigUpdateWithoutPayload(Consumer<ChangeManagementRequest> dropPayloadMethod) throws IOException {
        testChangeManagementWithoutPayload(dropPayloadMethod, ChangeManagementRequest.CONFIG_UPDATE);
    }

    @Test(dataProvider = "goodConfigPayloads")
    public void testConfigUpdateGoodPayload(String payload) throws IOException {
        testChangeManagementGoodPayload(payload, "mso_config_update_ok.json", ChangeManagementRequest.CONFIG_UPDATE);
    }

    @Test(dataProvider = "credentialsFilterTestData")
    public void testCadiCredentialsFilter(String authValue, int expectedStatusCode)
    {
        VnfIds vnfIds = new VnfIds();
        ChangeManagementRequest changeManagementRequest = createBasicChangeManagementRequest(vnfIds);
        changeManagementRequest.setRequestType(ChangeManagementRequest.REPLACE);
        WebTarget webTarget = buildWebTarget(vnfIds.vnfName);
        Entity entity = Entity.json(changeManagementRequest);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        if (authValue != null) {
            invocationBuilder.header("Authorization", "Basic " + authValue);
        }
        Response response = invocationBuilder.post(entity);
        String body = response.readEntity(String.class);
        Assert.assertEquals(response.getStatus(), expectedStatusCode, "Full response: " + body);
    }

    @DataProvider
    public static Object[][] requestFromSchedulerDataProvider(Method test) {
        return new Object[][]{
                {
                    "e9181708-f105-4dfd-9a36-0e089faa72ba",
                    new PresetMsoVnfReplace("254583ad-b38c-498b-bdbd-b8de5e07541b", "e9181708-f105-4dfd-9a36-0e089faa72ba"),
                    "changeManagement/vnfReplaceRequest.json"
                },
                {
                    "c929b7ec-cc9d-11e8-a8d5-f2801f1b9fd1",
                    new PresetMsoVnfUpdate("d0c59d4a-cc9d-11e8-a8d5-f2801f1b9fd1","c929b7ec-cc9d-11e8-a8d5-f2801f1b9fd1"),
                    "changeManagement/vnfUpdateRequest.json"
                },
                {
                    "e5403738-da34-4090-8864-ba9cf1bcdd88",
                    new PresetMsoVnfInPlaceSoftwareUpdate("12709275-787c-4be7-8c9c-fce64ab7ca8c","e5403738-da34-4090-8864-ba9cf1bcdd88"),
                    "changeManagement/vnfInPlaceSoftwareUpdate.json"
                }
        };
    }

    @Test(dataProvider = "requestFromSchedulerDataProvider")
    public void whenGetFromSchedulerRequest_sendAsExpectedToMso(String vnfInstanceId, PresetMsoChangeManagementBase preset, String requestPath) {
        SimulatorApi.registerExpectationFromPreset(
                preset,
                RegistrationStrategy.APPEND);
        String schedulerRequest = ReadFile.loadResourceAsString(requestPath);
        Response response = callChangeManagement("VidVnf", Entity.json(schedulerRequest));
        MsoResponseWrapper2 body = response.readEntity(MsoResponseWrapper2.class);
        assertEquals(202, body.getStatus());
        assertEquals(vnfInstanceId, getNestedPropertyInMap(body.getEntity(), "requestReferences/instanceId"));
    }


    private void testHappyPath(String expectationPath, String requestType) {
        VnfIds vnfIds = new VnfIds();
        MsoResponseWrapper2 body = callChangeManagementUpdate(vnfIds, expectationPath, MsoResponseWrapper2.class, requestType);
        assertForHappyPath(vnfIds, body, requestType);
    }

    private void assertForHappyPath(VnfIds vnfIds, MsoResponseWrapper2 body, String requestType) {
        Assert.assertEquals(body.getStatus(), 202, requestType + " failed with wrong http status");
        Assert.assertEquals(
                getNestedPropertyInMap(body.getEntity(), "requestReferences/instanceId"),
                vnfIds.serviceInstanceId,
                String.format("Failed to find instanceId: %s in " + requestType + " response.  Actual body:%s",
                        vnfIds.serviceInstanceId, body.getEntity()));
    }

    private <T> T callChangeManagementUpdate(VnfIds vnfIds, String expectationPath, Class<T> responseClass, String requestType) {
        SimulatorApi.registerExpectation(
                expectationPath,
                ImmutableMap.of("SERVICE_INSTANCE_ID", vnfIds.serviceInstanceId, "VNF_INSTANCE_ID", vnfIds.vnfInstanceId), RegistrationStrategy.APPEND);
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds, requestType);
        Response response =  callChangeManagementUpdate(vnfIds, changeManagementRequest);
        return response.readEntity(responseClass);
    }

    private Response callChangeManagementUpdate(VnfIds vnfIds, ChangeManagementRequest changeManagementRequest) {
        Entity entity = Entity.json(changeManagementRequest);
        return callChangeManagement(vnfIds.vnfName, entity);
    }

    private Response callChangeManagement(String vnfName, Entity entity) {
        WebTarget webTarget = buildWebTarget(vnfName);
        return webTarget.request(MediaType.APPLICATION_JSON_TYPE).header("Authorization", "Basic " + AAFGetBasicAuthPreset.VALID_AUTH_VALUE).post(entity);
    }

    private WebTarget buildWebTarget(String vnfName) {
        return client.target(uri).
                path(CHANGE_MANAGEMENT + WORKFLOW).resolveTemplate("vnfname", vnfName);
    }

    private void testChangeManagement409Error(String expectationPath, String requestType) throws IOException {
        VnfIds vnfIds = new VnfIds();
        MsoResponseWrapper2 body = callChangeManagementUpdate(vnfIds, expectationPath, MsoResponseWrapper2.class, requestType);
        Assert.assertEquals(body.getStatus(), 409, requestType + " failed with wrong http status");
        Assert.assertEquals(
                getNestedPropertyInMap(body.getEntity(), "serviceException/messageId"),
                "SVC2000",
                String.format("Failed to find messageId: %s in " + requestType + " response.  Actual body:%s",
                        "SVC2000", body.getEntity()));


        assertThat(getNestedPropertyInMap(body.getEntity(), "serviceException/text"), containsString(vnfIds.vnfInstanceId));
    }

    private void testChangeManagement404Error(String expectationPath, String requestType) throws IOException {
        VnfIds vnfIds = new VnfIds();
        MsoResponseWrapper2 body = callChangeManagementUpdate(vnfIds, expectationPath, MsoResponseWrapper2.class, requestType);
        Assert.assertEquals(body.getStatus(), 404, requestType + " failed with wrong http status");
        assertThat(body.getEntity(), equalTo("<html><head><title>Error</title></head><body>404 - Not Found</body></html>"));
    }

    private void testChangeManagementWithoutVnfInstanceId(String requestType) throws IOException {
        VnfIds vnfIds = new VnfIds();
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds, requestType);
        changeManagementRequest.getRequestDetails().get(0).setVnfInstanceId(null);
        MsoExceptionResponse exceptionResponse = callChangeManagementAndExpectForException(vnfIds, changeManagementRequest, requestType);
        assertThat(exceptionResponse.serviceException.text, containsString("No vnfInstanceId in request"));
    }

    private void testChangeManagementServiceInstanceId(Consumer<ChangeManagementRequest> dropInstanceIdMethod, String requestType) throws IOException {
        VnfIds vnfIds = new VnfIds();
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds, requestType);
        dropInstanceIdMethod.accept(changeManagementRequest);
        MsoExceptionResponse exceptionResponse = callChangeManagementAndExpectForException(vnfIds, changeManagementRequest, requestType);
        assertThat(exceptionResponse.serviceException.text, containsString("No instanceId in request"));
    }

    private void testChangeManagementInvalidPayload(String payload, String propertyName, String requestType) throws IOException {
        VnfIds vnfIds = new VnfIds();
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds, requestType);
        changeManagementRequest.getRequestDetails().get(0).getRequestParameters().getAdditionalProperties().put("payload",payload);
        MsoExceptionResponse exceptionResponse = callChangeManagementAndExpectForException(vnfIds, changeManagementRequest, requestType);
        assertThat(exceptionResponse.serviceException.text, containsString(propertyName));
        assertThat(exceptionResponse.serviceException.text, containsString("No valid payload"));
    }

    private void testChangeManagementWithoutPayload(Consumer<ChangeManagementRequest> dropPayloadMethod, String requestType) throws IOException {
        VnfIds vnfIds = new VnfIds();
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds, requestType);
        dropPayloadMethod.accept(changeManagementRequest);
        MsoExceptionResponse exceptionResponse = callChangeManagementAndExpectForException(vnfIds, changeManagementRequest, requestType);
        assertThat(exceptionResponse.serviceException.text, containsString("No valid payload"));
    }

    private MsoExceptionResponse callChangeManagementAndExpectForException(VnfIds vnfIds, ChangeManagementRequest changeManagementRequest, String requestType) {
        Response response = callChangeManagementUpdate(vnfIds, changeManagementRequest);
        Assert.assertEquals(response.getStatus(), HttpStatus.OK.value() , requestType + " wrong http status");
        MsoResponseWrapper2<MsoExceptionResponse> msoResponseWrapper2 = response.readEntity(new GenericType<MsoResponseWrapper2<MsoExceptionResponse>>(){});
        assertThat(msoResponseWrapper2.getStatus(), equalTo(400));
        assertThat(msoResponseWrapper2.getEntity(), instanceOf(MsoExceptionResponse.class));
        return (MsoExceptionResponse) msoResponseWrapper2.getEntity();
    }

    private void testChangeManagementGoodPayload(String payload, String expectationFileName, String requestType) throws IOException {
        VnfIds vnfIds = new VnfIds();
        SimulatorApi.registerExpectation(
                expectationFileName,
                ImmutableMap.of("SERVICE_INSTANCE_ID", vnfIds.serviceInstanceId, "VNF_INSTANCE_ID", vnfIds.vnfInstanceId), RegistrationStrategy.APPEND);
        ChangeManagementRequest changeManagementRequest = createChangeManagementRequest(vnfIds, requestType);
        changeManagementRequest.getRequestDetails().get(0).getRequestParameters().getAdditionalProperties().put("payload",payload);
        Response response = callChangeManagementUpdate(vnfIds, changeManagementRequest);
        MsoResponseWrapper2 body = response.readEntity(MsoResponseWrapper2.class);
        assertForHappyPath(vnfIds, body, requestType);
        RestTemplate manualRestTemplate = InsecureHttpsClient.newRestTemplate();
        super.loginWithChosenRESTClient(getUserCredentials(), manualRestTemplate);
        SimulatorApi.registerExpectationFromPreset( new PresetAAIGetSubscribersGet(), RegistrationStrategy.APPEND);
        LoggerFormatTest
            .verifyExistenceOfIncomingReqsInAuditLogs(manualRestTemplate, uri,
                response.getHeaders().get("X-ECOMP-RequestID-echo").get(0).toString(),
                "/vid/change-management/workflow/VidVnf");

    }

    private ChangeManagementRequest createChangeManagementRequest(VnfIds vnfDetails, String requestType) {
        ChangeManagementRequest changeManagementRequest = createBasicChangeManagementRequest(vnfDetails);
        changeManagementRequest.setRequestType(requestType);
        if(requestType.equals(ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE)) {
            CloudConfiguration cloudConfiguration = new CloudConfiguration();
            cloudConfiguration.lcpCloudRegionId = "mdt1";
            cloudConfiguration.tenantId = "88a6ca3ee0394ade9403f075db23167e";
            changeManagementRequest.getRequestDetails().get(0).setCloudConfiguration(cloudConfiguration);
        }
        changeManagementRequest.getRequestDetails().get(0).getRequestParameters().setAdditionalProperty("payload", getPayload(requestType));
        return changeManagementRequest;
    }

    private ChangeManagementRequest createBasicChangeManagementRequest(VnfIds vnfDetails)
    {
        ChangeManagementRequestDetails requestDetails = new ChangeManagementRequestDetails();

//        org.onap.vid.domain.mso.CloudConfiguration cloudConfiguration = new org.onap.vid.domain.mso.CloudConfiguration();
//        cloudConfiguration.setLcpCloudRegionId("mdt1");
//        cloudConfiguration.setTenantId("88a6ca3ee0394ade9403f075db23167e");
//        requestDetails.setCloudConfiguration(cloudConfiguration);

        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setSource("VID");
        requestInfo.setRequestorId("az2016");
        requestDetails.setRequestInfo(requestInfo);

        RequestParameters requestParameters = new RequestParameters();
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
        return changeManagementRequest;
    }

    private String getPayload(String requestType) {
        if(requestType.equals(ChangeManagementRequest.VNF_IN_PLACE_SOFTWARE_UPDATE)) {
            return "{\"existing_software_version\": \"3.1\",\"new_software_version\": \"3.2\", \"operations_timeout\": \"3600\"}";
        }
        else if(requestType.equals(ChangeManagementRequest.CONFIG_UPDATE)) {
            return "{\"request-parameters\": \"3.1\",\"configuration-parameters\": \"3.2\", \"operations_timeout\": \"3600\"}";
        }
        return "";
    }

    private boolean isTextContainsInList(List<MsoResponseWrapper2> responseList, String str) {
        for (MsoResponseWrapper2 response : responseList) {
            if (response.getEntity().toString().contains(str))
                return true;
        }
        return false;
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
