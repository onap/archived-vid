package org.opencomp.vid.api;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.text.StringEscapeUtils;
import org.opencomp.vid.testUtils.TestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class ServiceInstanceMsoApiTest extends BaseMsoApiTest{

    //Urls
    private static final String MSO_ACTIVATE_SERVICE_INSTANCE = "mso/mso_activate_service_instance/f36f5734-e9df-4fbf-9f35-61be13f028a1";
    private static final String MSO_DEACTIVATE_SERVICE_INSTANCE = "mso/mso_deactivate_service_instance/f36f5734-e9df-4fbf-9f35-61be13f028a1";

    //Request Details
    private static final String ACTIVATE_SERVICE_REQUEST_DETAILS = "registration_to_simulator/body_jsons/mso_request_activate_service_instance.json";
    private static final String DEACTIVATE_SERVICE_REQUEST_DETAILS = "registration_to_simulator/body_jsons/mso_request_deactivate_service_instance.json";

    //Jsons
    private static final String ACTIVATE_OK_JSON = "activate_service_instance.json";
    private static final String ACTIVATE_FAILED_JSON = "activate_service_instance_error.json";
    private static final String DEACTIVATE_OK_JSON = "deactivate_service_instance.json";
    private static final String DEACTIVATE_FAILED_JSON = "deactivate_service_instance_error.json";

    //Expected Responses
    private static final String EXPECTED_SUCCESS_MSO_RESPONSE = "{\"requestReferences\": {\"instanceId\": \"f36f5734-e9df-4fbf-9f35-61be13f028a1\", \"requestId\": \"b6dc9806-b094-42f7-9386-a48de8218ce8\"}}";
    private static final String EXPECTED_ERROR_MSO_RESPONSE = "{\"error\":\"222\",\"message\":\"error message\"}";

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
}
