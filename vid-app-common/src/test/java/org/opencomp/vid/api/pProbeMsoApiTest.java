package org.opencomp.vid.api;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.text.StringEscapeUtils;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.opencomp.vid.testUtils.TestUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class pProbeMsoApiTest extends BaseMsoApiTest{

    //Urls
    private static final String MSO_REMOVE_RELATIONSHIP = "/mso/mso_remove_relationship/f36f5734-e9df-4fbf-9f35-61be13f028a1";
    private static final String MSO_ADD_RELATIONSHIP = "/mso/mso_add_relationship/f36f5734-e9df-4fbf-9f35-61be13f028a1";


    //Request Details
    private static final String REMOVE_RELATIONSHIP_REQUEST_DETAILS = "registration_to_simulator/body_jsons/mso_request_dissociate_pnf_from_service.json";
    //Same body works for associate
    private static final String ADD_RELATIONSHIP_REQUEST_DETAILS = "registration_to_simulator/body_jsons/mso_request_dissociate_pnf_from_service.json";


    //Jsons
    private static final String DISSOCIATE_OK_JSON = "dissociate_pnf_from_service_instance.json";
    private static final String DISSOCIATE_FAILED_JSON = "dissociate_pnf_from_service_instance_error.json";
    private static final String ASSOCIATE_OK_JSON = "mso_add_relationships.json";
    private static final String ASSOCIATE_FAILED_JSON = "mso_add_relationships_error.json";

    //Expected Responses
    private static final String EXPECTED_SUCCESS_MSO_RESPONSE = "{\"requestReferences\": {\"instanceId\": \"f36f5734-e9df-4fbf-9f35-61be13f028a1\", \"requestId\": \"b6dc9806-b094-42f7-9386-a48de8218ce8\"}}";
    private static final String EXPECTED_ERROR_MSO_RESPONSE = "{\"error\":\"222\",\"message\":\"error message\"}";


    @Test
    public void testRemovePnfFromServiceInstanceSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, REMOVE_RELATIONSHIP_REQUEST_DETAILS);
        callMsoWithFineRequest(DISSOCIATE_OK_JSON, ImmutableMap.of(), buildUri(MSO_REMOVE_RELATIONSHIP), requestBody,
                HttpStatus.ACCEPTED.value(), EXPECTED_SUCCESS_MSO_RESPONSE, HttpMethod.POST);
    }

    @Test
    public void testRemovePnfFromServiceInstanceFailed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, REMOVE_RELATIONSHIP_REQUEST_DETAILS);
        callMsoWithSimulatedErrorResponse(DISSOCIATE_FAILED_JSON, ImmutableMap.of(), buildUri(MSO_REMOVE_RELATIONSHIP), requestBody,
                HttpStatus.NOT_FOUND.value(), "", HttpMethod.POST);
    }


    @Test
    public void testAddPnf2ServiceInstanceSucceed() throws Exception {
        String requestBody = TestUtils.convertRequest(objectMapper, REMOVE_RELATIONSHIP_REQUEST_DETAILS);
        callMsoWithFineRequest(ASSOCIATE_OK_JSON, ImmutableMap.of(), buildUri(MSO_ADD_RELATIONSHIP), requestBody,
                HttpStatus.ACCEPTED.value(),EXPECTED_SUCCESS_MSO_RESPONSE , HttpMethod.POST);
    }

    @Test(dataProvider = "errorCodes")
    public void testAddPnf2ServiceInstanceError(int errorCode) throws IOException, URISyntaxException {
        String requestBody = TestUtils.convertRequest(objectMapper, REMOVE_RELATIONSHIP_REQUEST_DETAILS);
        callMsoWithSimulatedErrorResponse(ASSOCIATE_FAILED_JSON,
                ImmutableMap.of("500", Integer.toString(errorCode),"\"ERROR_PAYLOAD\"", StringEscapeUtils.escapeJson(EXPECTED_ERROR_MSO_RESPONSE)),
                buildUri(MSO_ADD_RELATIONSHIP), requestBody,errorCode,EXPECTED_ERROR_MSO_RESPONSE,HttpMethod.POST);

    }
}
