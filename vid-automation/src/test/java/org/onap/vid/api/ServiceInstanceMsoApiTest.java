package org.onap.vid.api;

import static vid.automation.test.services.SimulatorApi.registerExpectationFromPresets;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.net.URISyntaxException;
import org.apache.commons.text.StringEscapeUtils;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOActivateFabricConfiguration;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOActivateFabricConfigurationErrorResponse;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeactivateAndCloudDelete;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSODeactivateAndCloudDeleteErrorResponse;
import org.onap.vid.more.LoggerFormatTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi.RegistrationStrategy;


public class ServiceInstanceMsoApiTest extends BaseMsoApiTest{

    //Urls
    private static final String MSO_ACTIVATE_SERVICE_INSTANCE = "mso/mso_activate_service_instance/f36f5734-e9df-4fbf-9f35-61be13f028a1";
    public static final String MSO_DEACTIVATE_SERVICE_INSTANCE = "mso/mso_deactivate_service_instance/f36f5734-e9df-4fbf-9f35-61be13f028a1";
    public static final String MSO_DELETE_SERVICE_INSTANCE = "mso/mso_delete_svc_instance/f36f5734-e9df-4fbf-9f35-61be13f028a1?serviceStatus=active";
    public static final String MSO_UNASSIGN_SERVICE_INSTANCE = "mso/mso_delete_svc_instance/f36f5734-e9df-4fbf-9f35-61be13f028a1?serviceStatus=created";
    public static final String MSO_ACTIVATE_FABRIC_CONFIGURATION = "mso/mso_activate_fabric_configuration/f36f5734-e9df-4fbf-9f35-61be13f028a1";
    public static final String MSO_DEACTIVATE_AND_CLOUD_DELETE = "mso/mso_vfmodule_soft_delete/f36f5734-e9df-4fbf-9f35-61be13f028a1/vnfId/vfModuleId";

    //Request Details
    private static final String ACTIVATE_SERVICE_REQUEST_DETAILS = "registration_to_simulator/body_jsons/mso_request_activate_service_instance.json";
    private static final String DEACTIVATE_SERVICE_REQUEST_DETAILS = "registration_to_simulator/body_jsons/mso_request_deactivate_service_instance.json";
    private static final String DELETE_AND_UNASSIGN_SERVICE_REQUEST_DETAILS = "registration_to_simulator/body_jsons/mso_request_delete_or_unassign_service_instance.json";
    private static final String ACTIVATE_FABRIC_CONFIGURATION_REQUEST_DETAILS = "registration_to_simulator/body_jsons/mso_request_activate_fabric_configuration.json";
    private static final String DEACTIVATE_AND_CLOUD_DELETE_DATA = "registration_to_simulator/body_jsons/mso_request_deactivate_and_cloud_delete.json";

    //Jsons
    private static final String ACTIVATE_OK_JSON = "activate_service_instance.json";
    private static final String ACTIVATE_FAILED_JSON = "activate_service_instance_error.json";
    public static final String DEACTIVATE_OK_JSON = "deactivate_service_instance.json";
    private static final String DEACTIVATE_FAILED_JSON = "deactivate_service_instance_error.json";
    private static final String UNASSIGN_OK_JSON = "unassign_service_instance.json";
    private static final String DELETE_SERVICE_REQUEST_DETAILS = "delete_service_instance1802.json";
    private static final String DELETE_OR_UNASSIGN_FAILED_JSON = "delete_or_unassign_service_instance_error.json";

    //Expected Responses
    private static final String EXPECTED_SUCCESS_MSO_RESPONSE = "{\"requestReferences\": {\"instanceId\": \"f36f5734-e9df-4fbf-9f35-61be13f028a1\", \"requestId\": \"b6dc9806-b094-42f7-9386-a48de8218ce8\"}}";
    private static final String EXPECTED_ERROR_MSO_RESPONSE = "{\"message\":\"error message\",\"error\":\"222\"}";


    @Test
    public void testActivateServiceInstanceSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, ACTIVATE_SERVICE_REQUEST_DETAILS);
        callMsoWithFineRequest(ACTIVATE_OK_JSON, ImmutableMap.of(), buildUri(MSO_ACTIVATE_SERVICE_INSTANCE), requestBody,
                HttpStatus.ACCEPTED.value(), EXPECTED_SUCCESS_MSO_RESPONSE, HttpMethod.POST);
    }

    @Test(dataProvider = "errorCodes")
    public void testActivateServiceInstanceFailed(int errorCode) throws IOException, URISyntaxException {
        String requestBody = TestUtils.convertRequest(objectMapper, ACTIVATE_SERVICE_REQUEST_DETAILS);
        callMsoWithSimulatedErrorResponse(ACTIVATE_FAILED_JSON,
                ImmutableMap.of("500", Integer.toString(errorCode),"\"ERROR_PAYLOAD\"", StringEscapeUtils.escapeJson(EXPECTED_ERROR_MSO_RESPONSE)),
                buildUri(MSO_ACTIVATE_SERVICE_INSTANCE), requestBody, errorCode, EXPECTED_ERROR_MSO_RESPONSE, HttpMethod.POST);

    }

    @Test
    public void testUnassignServiceInstanceSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, DELETE_AND_UNASSIGN_SERVICE_REQUEST_DETAILS);
        callMsoWithFineRequest(UNASSIGN_OK_JSON, ImmutableMap.of(), buildUri(MSO_UNASSIGN_SERVICE_INSTANCE), requestBody,
                HttpStatus.ACCEPTED.value(), EXPECTED_SUCCESS_MSO_RESPONSE, HttpMethod.POST);
    }


    @Test
    public void testDeleteServiceInstanceSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, DELETE_AND_UNASSIGN_SERVICE_REQUEST_DETAILS);
        callMsoWithFineRequest(UNASSIGN_OK_JSON, ImmutableMap.of(
                "/unassign", "",
                "POST", "DELETE"), buildUri(MSO_DELETE_SERVICE_INSTANCE), requestBody,
                HttpStatus.ACCEPTED.value(), EXPECTED_SUCCESS_MSO_RESPONSE, HttpMethod.POST);
    }

    @Test(dataProvider = "errorCodes")
    public void testUnassignServiceInstanceFailed(int errorCode) throws IOException {
        String requestBody = TestUtils.convertRequest(objectMapper, DELETE_AND_UNASSIGN_SERVICE_REQUEST_DETAILS);
        callMsoWithSimulatedErrorResponse(DELETE_OR_UNASSIGN_FAILED_JSON,
                ImmutableMap.of("500", Integer.toString(errorCode),"\"ERROR_PAYLOAD\"", StringEscapeUtils.escapeJson(EXPECTED_ERROR_MSO_RESPONSE)),
                buildUri(MSO_UNASSIGN_SERVICE_INSTANCE), requestBody, errorCode, EXPECTED_ERROR_MSO_RESPONSE, HttpMethod.POST);

    }

    @Test(dataProvider = "errorCodes")
    public void testDeleteServiceInstanceFailed(int errorCode) throws IOException {
        String requestBody = TestUtils.convertRequest(objectMapper, DELETE_AND_UNASSIGN_SERVICE_REQUEST_DETAILS);
        callMsoWithSimulatedErrorResponse(DELETE_OR_UNASSIGN_FAILED_JSON,
                ImmutableMap.of("/unassign", "",
                        "POST", "DELETE",
                        "500", Integer.toString(errorCode),
                        "\"ERROR_PAYLOAD\"", StringEscapeUtils.escapeJson(EXPECTED_ERROR_MSO_RESPONSE)),
                buildUri(MSO_DELETE_SERVICE_INSTANCE), requestBody, errorCode, EXPECTED_ERROR_MSO_RESPONSE, HttpMethod.POST);

    }

    @Test
    public void testDeactivateServiceInstanceSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, DEACTIVATE_SERVICE_REQUEST_DETAILS);
        callMsoWithFineRequest(DEACTIVATE_OK_JSON, ImmutableMap.of(), buildUri(MSO_DEACTIVATE_SERVICE_INSTANCE), requestBody,
                HttpStatus.ACCEPTED.value(), EXPECTED_SUCCESS_MSO_RESPONSE, HttpMethod.POST);
    }

    @Test(dataProvider = "errorCodes")
    public void testDeactivateServiceInstanceFailed(int errorCode) throws IOException, URISyntaxException {
        String requestBody = TestUtils.convertRequest(objectMapper, DEACTIVATE_SERVICE_REQUEST_DETAILS);
        callMsoWithSimulatedErrorResponse(DEACTIVATE_FAILED_JSON,
                ImmutableMap.of("500", Integer.toString(errorCode),"\"ERROR_PAYLOAD\"", StringEscapeUtils.escapeJson(EXPECTED_ERROR_MSO_RESPONSE)),
                buildUri(MSO_DEACTIVATE_SERVICE_INSTANCE), requestBody, errorCode, EXPECTED_ERROR_MSO_RESPONSE, HttpMethod.POST);

    }

    @Test
    public void testActivateFabricConfigurationSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, ACTIVATE_FABRIC_CONFIGURATION_REQUEST_DETAILS);
        callMsoWithFineRequest(new PresetMSOActivateFabricConfiguration("f36f5734-e9df-4fbf-9f35-61be13f028a1", "b6dc9806-b094-42f7-9386-a48de8218ce8"), buildUri(MSO_ACTIVATE_FABRIC_CONFIGURATION), requestBody,
                HttpStatus.ACCEPTED.value(), EXPECTED_SUCCESS_MSO_RESPONSE, HttpMethod.POST);
    }

    @Test
    public void testWhenCallMsoRequestLoggedInMetrics() {
        String msoRootPath = "/mso/serviceInstantiation/v7";
        String requestBody = TestUtils.convertRequest(objectMapper, ACTIVATE_FABRIC_CONFIGURATION_REQUEST_DETAILS);
        registerExpectationFromPresets(ImmutableList.of(
            new PresetMSOActivateFabricConfiguration("f36f5734-e9df-4fbf-9f35-61be13f028a1", "b6dc9806-b094-42f7-9386-a48de8218ce8"),
            new PresetAAIGetSubscribersGet()), RegistrationStrategy.CLEAR_THEN_SET);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(buildUri(MSO_ACTIVATE_FABRIC_CONFIGURATION), requestBody, String.class);
        String requestId = responseEntity.getHeaders().getFirst("X-ECOMP-RequestID-echo");
        LoggerFormatTest.assertHeadersAndMetricLogs(restTemplate, uri, requestId, msoRootPath, 1);
        LoggerFormatTest.verifyExistenceOfIncomingReqsInAuditLogs(restTemplate, uri, requestId, MSO_ACTIVATE_FABRIC_CONFIGURATION);
    }

    @Test(dataProvider = "errorCodes")
    public void testActivateFabricConfigurationFailed(int errorCode) throws IOException, URISyntaxException {
        String requestBody = TestUtils.convertRequest(objectMapper, ACTIVATE_FABRIC_CONFIGURATION_REQUEST_DETAILS);
        callMsoWithSimulatedErrorResponse(new PresetMSOActivateFabricConfigurationErrorResponse("f36f5734-e9df-4fbf-9f35-61be13f028a1", errorCode),
                buildUri(MSO_ACTIVATE_FABRIC_CONFIGURATION), requestBody, errorCode, EXPECTED_ERROR_MSO_RESPONSE, HttpMethod.POST);
    }

    @Test
    public void testDeactivateAndCloudDeleteSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, DEACTIVATE_AND_CLOUD_DELETE_DATA);
        callMsoWithFineRequest(ImmutableList.of(
                    new PresetMSODeactivateAndCloudDelete("f36f5734-e9df-4fbf-9f35-61be13f028a1", "vnfId", "vfModuleId", "b6dc9806-b094-42f7-9386-a48de8218ce8", "irma-aic"),
                    PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN6_TO_ATT_AIC),
                buildUri(MSO_DEACTIVATE_AND_CLOUD_DELETE), requestBody,
                HttpStatus.ACCEPTED.value(), EXPECTED_SUCCESS_MSO_RESPONSE, HttpMethod.POST);
    }

    @Test(dataProvider = "errorCodes")
    public void testDeactivateAndCloudDeleteFailed(int errorCode) throws IOException, URISyntaxException {
        String requestBody = TestUtils.convertRequest(objectMapper, DEACTIVATE_AND_CLOUD_DELETE_DATA);
        callMsoWithSimulatedErrorResponse(ImmutableList.of(
                    new PresetMSODeactivateAndCloudDeleteErrorResponse("f36f5734-e9df-4fbf-9f35-61be13f028a1", "vnfId", "vfModuleId", errorCode, "irma-aic"),
                    PresetAAIGetCloudOwnersByCloudRegionId.PRESET_MTN6_TO_ATT_AIC),
                buildUri(MSO_DEACTIVATE_AND_CLOUD_DELETE), requestBody, errorCode, EXPECTED_ERROR_MSO_RESPONSE, HttpMethod.POST);
    }
}
