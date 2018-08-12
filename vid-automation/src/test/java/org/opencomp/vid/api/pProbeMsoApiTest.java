package org.opencomp.vid.api;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class pProbeMsoApiTest extends BaseMsoApiTest{

    //Urls
    private static final String MSO_REMOVE_RELATIONSHIP = "/mso/mso_remove_relationship/f36f5734-e9df-4fbf-9f35-61be13f028a1";
    private static final String MSO_ADD_RELATIONSHIP = "/mso/mso_add_relationship/f36f5734-e9df-4fbf-9f35-61be13f028a1";
    public static final String MSO_CREATE_CONFIGURATION = "mso/mso_create_configuration_instance/f36f5734-e9df-4fbf-9f35-61be13f028a1/configurations/";
    public static final String MSO_ACTIVATE_CONFIGURATION = "mso/mso_activate_configuration/f36f5734-e9df-4fbf-9f35-61be13f028a1/configurations/a53c9ca8-8986-44da-9e5e-9e4179e6c78a";



    //Request Details
    private static final String ADD_REMOVE_RELATIONSHIP_REQUEST_DETAILS = "registration_to_simulator/body_jsons/mso_request_dissociate_pnf_from_service.json";

    //Request Details
    private static final String CREATE_CONFIGURATION_REQUEST_DETAILS = "registration_to_simulator/body_jsons/mso_request_create_configuration.json";

    //Jsons
    private static final String DISSOCIATE_OK_JSON = "dissociate_pnf_from_service_instance.json";
    private static final String DISSOCIATE_FAILED_JSON = "dissociate_pnf_from_service_instance_error.json";
    private static final String ASSOCIATE_OK_JSON = "mso_add_relationships.json";
    private static final String ASSOCIATE_FAILED_JSON = "mso_add_relationships_error.json";
    private static final String CREATE_CONFIGURATION_OK_JSON = "mso_create_configurations.json";
    private static final String CREATE_CONFIGURATION_FAILED_JSON = "mso_create_configurations_error.json";
    private static final String ACTIVATE_CONFIGURATION_OK_JSON = "mso_activate_configurations.json";

    //Expected Responses
    private static final String EXPECTED_SUCCESS_MSO_RESPONSE = "{\"requestReferences\": {\"instanceId\": \"f36f5734-e9df-4fbf-9f35-61be13f028a1\", \"requestId\": \"b6dc9806-b094-42f7-9386-a48de8218ce8\"}}";
    private static final String EXPECTED_ERROR_MSO_RESPONSE = "{\"error\":\"222\",\"message\":\"error message\"}";


    @Test
    public void testRemovePnfFromServiceInstanceSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, ADD_REMOVE_RELATIONSHIP_REQUEST_DETAILS);
        callMsoWithFineRequest(DISSOCIATE_OK_JSON, ImmutableMap.of(), buildUri(MSO_REMOVE_RELATIONSHIP), requestBody,
                HttpStatus.ACCEPTED.value(), EXPECTED_SUCCESS_MSO_RESPONSE, HttpMethod.POST);
    }

    @Test
    public void testRemovePnfFromServiceInstanceFailed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, ADD_REMOVE_RELATIONSHIP_REQUEST_DETAILS);
        callMsoWithSimulatedErrorResponse(DISSOCIATE_FAILED_JSON, ImmutableMap.of(), buildUri(MSO_REMOVE_RELATIONSHIP), requestBody,
                HttpStatus.NOT_FOUND.value(), "", HttpMethod.POST);
    }


    @Test
    public void testAddPnf2ServiceInstanceSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, ADD_REMOVE_RELATIONSHIP_REQUEST_DETAILS);
        callMsoWithFineRequest(ASSOCIATE_OK_JSON, ImmutableMap.of(), buildUri(MSO_ADD_RELATIONSHIP), requestBody,
                HttpStatus.ACCEPTED.value(),EXPECTED_SUCCESS_MSO_RESPONSE , HttpMethod.POST);
    }

    @Test(dataProvider = "errorCodes")
    public void testAddPnf2ServiceInstanceError(int errorCode) throws IOException, URISyntaxException {
        String requestBody = TestUtils.convertRequest(objectMapper, ADD_REMOVE_RELATIONSHIP_REQUEST_DETAILS);
        callMsoWithSimulatedErrorResponse(ASSOCIATE_FAILED_JSON,
                ImmutableMap.of("500", Integer.toString(errorCode),"\"ERROR_PAYLOAD\"", StringEscapeUtils.escapeJson(EXPECTED_ERROR_MSO_RESPONSE)),
                buildUri(MSO_ADD_RELATIONSHIP), requestBody,errorCode,EXPECTED_ERROR_MSO_RESPONSE,HttpMethod.POST);

    }

    @Test
    public void testCreateConfigurationSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, CREATE_CONFIGURATION_REQUEST_DETAILS);
        callMsoWithFineRequest(CREATE_CONFIGURATION_OK_JSON, ImmutableMap.of(), buildUri(MSO_CREATE_CONFIGURATION),
                requestBody, HttpStatus.ACCEPTED.value(),EXPECTED_SUCCESS_MSO_RESPONSE , HttpMethod.POST);
    }

    @Test
    public void testActivateConfigurationSucceed() throws Exception {
        String requestBody = "" +
                "{" +
                "   \"val\": \"dummy payload\"" +
                "}";
        callMsoWithFineRequest(ACTIVATE_CONFIGURATION_OK_JSON, ImmutableMap.of(), buildUri(MSO_ACTIVATE_CONFIGURATION),
                requestBody, HttpStatus.ACCEPTED.value(),EXPECTED_SUCCESS_MSO_RESPONSE , HttpMethod.POST);
    }

    @Test(dataProvider = "errorCodes")
    public void testCreateConfigurationError(int errorCode) throws IOException, URISyntaxException {
        String requestBody = TestUtils.convertRequest(objectMapper, CREATE_CONFIGURATION_REQUEST_DETAILS);
        callMsoWithSimulatedErrorResponse(CREATE_CONFIGURATION_FAILED_JSON,
                ImmutableMap.of("\"<ERROR_CODE>\"", Integer.toString(errorCode),"\"<ERROR_PAYLOAD>\"", StringEscapeUtils.escapeJson(EXPECTED_ERROR_MSO_RESPONSE)),
                buildUri(MSO_CREATE_CONFIGURATION), requestBody,errorCode,EXPECTED_ERROR_MSO_RESPONSE,HttpMethod.POST);

    }

    @Test
    public void testCreateConfigurationFail() throws Exception {
        String requestBody = "498/*ht5ru7 mjhnb";
        callMsoWithSimulatedErrorResponse(CREATE_CONFIGURATION_FAILED_JSON,
                ImmutableMap.of("\"<ERROR_CODE>\"", 500),
                buildUri(MSO_CREATE_CONFIGURATION), requestBody,500,EXPECTED_ERROR_MSO_RESPONSE,HttpMethod.POST);

    }
}
