package org.opencomp.vid.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.text.StringEscapeUtils;
import org.opencomp.vid.api.simulator.SimulatorApi;
import org.opencomp.vid.api.simulator.SimulatorApi.RegistrationStrategy;
import org.openecomp.vid.utils.Streams;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

public class OperationalEnvironmentControllerApiTest extends BaseMsoApiTest {
    private static final String UUID = "927befca-e32c-4f7d-be8d-b4107e0ac31e";
    private static final String GET_STATUS_REQUEST_UUID = "3212b08c-0dcd-4d20-8c84-51e4f325c14a";
    private static final String BASIC_DEACTIVATE_REQUEST_BODY = "{}";
    private static final String BASIC_ACTIVATE_REQUEST_BODY = "" +
            "{" +
            "\"relatedInstanceId\": \"1117887f-068f-46d7-9e43-9b73bef17af8\"" +
            ", \"relatedInstanceName\": \"managing ECOMP Operational Environment\"" +
            ", \"workloadContext\": \"VNF_E2E-IST\"" +
            ", \"manifest\": {" +
            "        \"serviceModelList\": [" +
//            "          {" +
//            "            \"serviceModelVersionId\": \"uuid1\"," +
//            "            \"recoveryAction\": \"abort\"" +
//            "          }," +
            "          {" +
            "            \"serviceModelVersionId\": \"uuid2\"," +
            "            \"recoveryAction\": \"retry\"" +
            "          }" +
            "        ]" +
            "      }" +
            "}";
    private static final String BASIC_CREATE_REQUEST_BODY = "{\r\n  \"instanceName\": \"my op env\",\r\n  \"ecompInstanceId\": \"aa345d54-75b4-431b-adb2-eb6b9e546014\",\r\n  \"ecompInstanceName\":\"My Operational Environment\",\r\n  \"operationalEnvironmentType\": \"VNF\",\r\n  \"tenantContext\": \"Test\",\r\n  \"workloadContext\": \"VNF_E2E-IST\"\r\n}";
    private final String MSO_OK_RESPONSE_FOR_DEACTIVATE = "mso_ok_response_for_deactivate.json";
    private final String GET_CLOUD_RESOURCES_REQUEST_STATUS = "get_cloud_resources_request_status.json";
    private final String MSO_ERROR_RESPONSE_FOR_DEACTIVATE = "mso_error_response_for_deactivate.json";
    private final String MSO_ERROR_RESPONSE_FOR_STATUS = "mso_error_response_for_status.json";
    private final String MSO_OK_RESPONSE_FOR_POST_OPERATIONAL_ENVIRONMENT = "mso_ok_response_for_post_operational_environmnet.json";
    private final String MSO_ERROR_RESPONSE_FOR_POST_OPERATIONAL_ENVIRONMENT = "mso_error_response_for_post_operational_environmnet.json";
    private final String missingParamErrorText = "Required String parameter 'operationalEnvironment' is not present";

    /*
    # DEACTIVATION

    ## Tests
    [x]  -  Try all methods: only POST is working
    [x]  -  Send wrong requests; all are responded with 400 and nice description
    [x]      -  missing param
    [x]      -  missing body
    [x]      -  operationalEnvironment value is empty
         -  Simulate MSO responses (status and body); verify all are propagated inside a VID's 200 OK
    [x]      -  [ 200, 202, 400, 404, 500 ]

         -  Positive cases
    [x]      - Request body is just '{}'
    [x]      - Request body is with some fields
    [x]      - URI with more query params

    ### Always verify
    [x]  -  Payload to MSO is the valid schema and values
    [ ]  -  RequestorId is ok

     */

    @Override
    @BeforeClass
    public void login() {
        super.login();
    }


    @Test(dataProvider = "wrongHttpMethodsForAllUris")
    public void tryAllWrongMethods(HttpMethod httpMethod, String uri) throws IOException {
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, httpMethod, new HttpEntity<ImmutableMap>(ImmutableMap.of()), String.class);
            assertThat("Response should be method not allowed => " + responseEntity, responseEntity.getBody(), containsString("Request method '" + httpMethod + "' not supported"));
        } catch (HttpClientErrorException e) {
            assertThat("Response should be method not allowed (by error code)", e.getStatusCode(), is(METHOD_NOT_ALLOWED));
        }

    }


    @Test
    public void activateWithAdditionalQueryParam() throws IOException {
        doWithFineRequest(BASIC_ACTIVATE_REQUEST_BODY, getActivationTargetUri(ACTIVATION_URI_UUID_MODE.EXTENDED), "/activate\"");
    }

    @Test(expectedExceptions = { HttpClientErrorException.class })
    public void activateWithMissingOperationalEnvironmentParam() throws IOException {
        doWithBadRequest(BASIC_ACTIVATE_REQUEST_BODY, missingParamErrorText, getActivationTargetUri(ACTIVATION_URI_UUID_MODE.MISSING));
    }

    @Test(expectedExceptions = { HttpClientErrorException.class })
    public void activateWithNoValueForOperationalEnvironmentParam() throws IOException {
        doWithBadRequest(BASIC_ACTIVATE_REQUEST_BODY, missingParamErrorText, getActivationTargetUri(ACTIVATION_URI_UUID_MODE.NO_VALUE));
    }

    @Test
    public void deactivateWithAdditionalQueryParam() throws IOException {
        doWithFineRequest(BASIC_DEACTIVATE_REQUEST_BODY, getDeactivationTargetUri(ACTIVATION_URI_UUID_MODE.EXTENDED), "/deactivate\"");
    }

    @Test(expectedExceptions = { HttpClientErrorException.class })
    public void deactivateWithMissingOperationalEnvironmentParam() throws IOException {
        doWithBadRequest(BASIC_DEACTIVATE_REQUEST_BODY, missingParamErrorText, getDeactivationTargetUri(ACTIVATION_URI_UUID_MODE.MISSING));
    }

    @Test(expectedExceptions = { HttpClientErrorException.class })
    public void deactivateWithNoValueForOperationalEnvironmentParam() throws IOException {
        doWithBadRequest(BASIC_DEACTIVATE_REQUEST_BODY, missingParamErrorText, getDeactivationTargetUri(ACTIVATION_URI_UUID_MODE.NO_VALUE));
    }

    @Test(dataProvider = "requestPayloads")
    public void activateWithBody(String requestBody) throws IOException {
        doWithFineRequest(requestBody, getActivationTargetUri(ACTIVATION_URI_UUID_MODE.OK), "/activate\"");
    }

    @Test(dataProvider = "requestPayloads")
    public void deactivateWithBody(String requestBody) throws IOException {
        doWithFineRequest(requestBody, getDeactivationTargetUri(ACTIVATION_URI_UUID_MODE.OK), "/deactivate\"");
    }


    private void doWithFineRequest(String requestBody, String targetUri, String v1) throws IOException {
        final String expectedResult = "" +
                "{" +
                "  \"requestReferences\": {" +
                "     \"requestId\": \"dbe54591-c8ed-46d3-abc7-d3a24873dfbd\"," +
                "     \"instanceId\": \"" + UUID + "\"" +
                "  }" +
                "}";
        callMsoWithFineRequest(MSO_OK_RESPONSE_FOR_DEACTIVATE, ImmutableMap.of(
                    "/deactivate\"", v1,
                    "UUID", UUID)
                ,targetUri,requestBody,HttpStatus.ACCEPTED.value(),expectedResult, HttpMethod.POST);
       }

    @Test(dataProvider = "errorCodes")
    public void deactivateWithErrorResponse(int errorCode) throws IOException {
        doWithSimulatedErrorResponse(errorCode, getDeactivationTargetUri(ACTIVATION_URI_UUID_MODE.OK), BASIC_DEACTIVATE_REQUEST_BODY, "/deactivate\"", MSO_ERROR_RESPONSE_FOR_DEACTIVATE, HttpMethod.POST);
    }

    @Test(dataProvider = "errorCodes")
    public void activateWithErrorResponse(int errorCode) throws IOException {
        doWithSimulatedErrorResponse(errorCode, getActivationTargetUri(ACTIVATION_URI_UUID_MODE.OK), BASIC_ACTIVATE_REQUEST_BODY, "/activate\"", MSO_ERROR_RESPONSE_FOR_DEACTIVATE, HttpMethod.POST);
    }

    @Test(dataProvider = "errorCodes")
    public void testStatusWithErrorResponse(int errorCode) throws IOException {
        doWithSimulatedErrorResponse(errorCode, getStatusTargetUri(STATUS_URI_UUID_MODE.OK), "", "", MSO_ERROR_RESPONSE_FOR_STATUS, HttpMethod.GET);
    }

    private void doWithSimulatedErrorResponse(int errorCode, String targetUri, String basicRequestBody, String msoPathSuffix, String expectationTemplateFilename, HttpMethod method) throws IOException {
        final String expectedResult = "" +
                "<head>Huston, you have a problem<head>";
        callMsoWithSimulatedErrorResponse(expectationTemplateFilename, ImmutableMap.of(
                    "/deactivate\"", msoPathSuffix,
                    "UUID", UUID,
                    "500", errorCode,
                    "ERROR_PAYLOAD", StringEscapeUtils.escapeJson(expectedResult)
                    ),targetUri,basicRequestBody,errorCode, expectedResult, method);
    }

    @Test(
            dataProvider = "requestPayloads",
            expectedExceptions = { HttpClientErrorException.class }
    )
    public void activateWithBadRequest(String requestBody) throws IOException {
        doWithBadRequest(requestBody, "HttpMessageNotReadableException", getActivationTargetUri(ACTIVATION_URI_UUID_MODE.OK));
    }

    @Test(
            dataProvider = "activateBadHalfBakedPayloads",
            expectedExceptions = { HttpClientErrorException.class }
    )
    public void activateWithBadHalfBakedPayload(String requestBody) throws IOException {
        doWithBadRequest(requestBody, "HttpMessageNotReadableException", getActivationTargetUri(ACTIVATION_URI_UUID_MODE.OK));
    }

    @Test(
            dataProvider = "requestPayloads",
            expectedExceptions = { HttpClientErrorException.class }
    )
    public void deactivateWithBadRequest(String requestBody) throws IOException {
        doWithBadRequest(requestBody, "HttpMessageNotReadableException", getDeactivationTargetUri(ACTIVATION_URI_UUID_MODE.OK));
    }

    private void doWithBadRequest(String requestBody, String httpMessageNotReadableException, String targetUri) throws IOException {
        SimulatorApi.registerExpectation(MSO_OK_RESPONSE_FOR_DEACTIVATE, ImmutableMap.of("UUID", UUID), RegistrationStrategy.CLEAR_THEN_SET);

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(targetUri, requestBody, String.class);
        } catch (HttpClientErrorException e) {
            assertThat("Response should be Bad Request (by error code)", e.getStatusCode(), is(BAD_REQUEST));
            assertThat("Response should be Bad Request (by body)", e.getResponseBodyAsString(), containsString(httpMessageNotReadableException));
            throw e;
        }

    }


    @DataProvider
    public Object[][] wrongHttpMethodsForAllUris() {
        ImmutableList<String> uris = ImmutableList.of(
                getDeactivationTargetUri(ACTIVATION_URI_UUID_MODE.OK),
                getActivationTargetUri(ACTIVATION_URI_UUID_MODE.OK)
        );
        
        return Arrays.stream(HttpMethod.values())
                .filter(Streams.not(ImmutableList.of(
                        HttpMethod.POST,    // because POST *should* work
                        HttpMethod.PATCH,   // because PATCH is invalid method for Java.net
                        HttpMethod.OPTIONS, // because OPTIONS is somehow valid...  :-(  => Allow=[GET, HEAD, POST, PUT, DELETE, OPTIONS, PATCH]
                        HttpMethod.HEAD     // because HEAD is like POST/GET but without body, so error is hidden
                )::contains))
                .flatMap(httpMethod -> uris.stream()
                        .map(uri -> new Object[]{ httpMethod, uri})  // pair given method for each of the URIs
                ) 
                .collect(Collectors.toList())
                .toArray(new Object[][] {});
    }

    @DataProvider
    public static Object[][] requestPayloads(Method test) {
        switch (test.getName()) {
            case "deactivateWithBody":
                return new Object[][]{
                        {BASIC_DEACTIVATE_REQUEST_BODY}
                        , {"{ \"a\": \"b\" }"}
                        , {"{ \"a\": [ 55 ] }"}
                };
            case "activateWithBody":
                return new Object[][]{
                        {BASIC_ACTIVATE_REQUEST_BODY}
                };
            default: // bad payloads
                return new Object[][]{
                        {null}
                        , {""}
                        , {"{"}
                        , {"foo"}
                };
        }
    }

    @DataProvider
    public static Object[][] activateBadHalfBakedPayloads(Method test) {
        final ImmutableList<String> strings = ImmutableList.of(
                "\"relatedInstanceId\": \"1117887f-068f-46d7-9e43-9b73bef17af8\"",
                "\"relatedInstanceName\": \"managing ECOMP Operational Environment\"",
                "\"workloadContext\": \"VNF_E2E-IST\"",
                "\"manifest\": {" +
                        "  \"serviceModelList\": [" +
                        "    {" +
                        "      \"serviceModelVersionId\": \"uuid2\"," +
                        "      \"recoveryAction\": \"retry\"" +
                        "    }" +
                        "  ]" +
                        "}"
        );

        final LinkedList<String> tests = new LinkedList<>();
        for (int i = 0; i < strings.size(); i++) {
            final ArrayList<String> aCase = new ArrayList<>(strings);
            aCase.remove(i);
            tests.add("{" + String.join(", ", aCase) + "}");
        }

        return tests.stream().map(o -> new Object[] { o }).collect(Collectors.toList()).toArray(new Object[][]{});
    }

    @DataProvider
    public static Object[][] errorCodes(Method test) {
        return new Object[][]{
                {500}, {505}, {400}, {401}, {405}
        };
    }

    @DataProvider
    public static Object[][] statusLegitUri(Method test) {
        return new Object[][]{
                {STATUS_URI_UUID_MODE.OK}, {STATUS_URI_UUID_MODE.EXTENDED}
        };
    }

    @DataProvider
    public static Object[][] statusNotLegitUri(Method test) {
        return new Object[][]{
                {STATUS_URI_UUID_MODE.MISSING}, {STATUS_URI_UUID_MODE.NO_VALUE}
        };
    }

    private enum ACTIVATION_URI_UUID_MODE {
        MISSING(""),
        NO_VALUE("?operationalEnvironment="),
        OK(NO_VALUE.val + UUID),
        EXTENDED(OK.val + "&anotherParam=6");

        final String val;

        ACTIVATION_URI_UUID_MODE(String val) {
            this.val = val;
        }
    }

    private enum STATUS_URI_UUID_MODE {
        MISSING(""),
        NO_VALUE("?requestId="),
        OK(NO_VALUE.val + GET_STATUS_REQUEST_UUID),
        EXTENDED(OK.val + "&anotherParam=6");

        final String val;

        STATUS_URI_UUID_MODE(String val) {
            this.val = val;
        }
    }

    private String getDeactivationTargetUri(ACTIVATION_URI_UUID_MODE uriUuidMode) {
        return uri.toASCIIString() + "/operationalEnvironment/deactivate" + uriUuidMode.val;
    }

    private String getActivationTargetUri(ACTIVATION_URI_UUID_MODE uriUuidMode) {
        return uri.toASCIIString() + "/operationalEnvironment/activate" + uriUuidMode.val;
    }

    private String getStatusTargetUri(STATUS_URI_UUID_MODE uriUuidMode) {
        return uri.toASCIIString() + "/operationalEnvironment/requestStatus" + uriUuidMode.val;
    }

    private String getCreateOperationEnvironmentUri() {
        return uri.toASCIIString() + "/operationalEnvironment/create";
    }

    @Test
    public void createWithSimplestBody()throws IOException {

        final String expectedResult = "" +
                "{" +
                "  \"requestReferences\": {" +
                "     \"requestId\": \"dbe54591-c8ed-46d3-abc7-d3a24873dfbd\","+
                "     \"instanceId\": \"" + UUID + "\"" +
                "  }" +
                "}";
        callMsoWithFineRequest(MSO_OK_RESPONSE_FOR_POST_OPERATIONAL_ENVIRONMENT, ImmutableMap.of(
                "UUID", UUID),getCreateOperationEnvironmentUri(),BASIC_CREATE_REQUEST_BODY,HttpStatus.ACCEPTED.value(),expectedResult, HttpMethod.POST);
   }

    @Test(dataProvider = "errorCodes")
    public void createWithErrorResponse(int errorCode) throws IOException {
        final String expectedResult = "" +
                "<head>Huston, you have a problem<head>";
        callMsoWithSimulatedErrorResponse(MSO_ERROR_RESPONSE_FOR_POST_OPERATIONAL_ENVIRONMENT,ImmutableMap.of(
                "500", errorCode,
                "ERROR_PAYLOAD", StringEscapeUtils.escapeJson(expectedResult)
        ),  getCreateOperationEnvironmentUri(), BASIC_CREATE_REQUEST_BODY, errorCode,expectedResult, HttpMethod.POST);
    }




    @Test(dataProvider = "statusLegitUri")
    public void testStatusWithLegitUri(STATUS_URI_UUID_MODE statusUriMode) throws IOException {

        String uri = getStatusTargetUri(statusUriMode);

        final String expectedResult = "" +
                "{" +
                "  \"request\": {" +
                "    \"requestId\": \"3212b08c-0dcd-4d20-8c84-51e4f325c14a\"," +
                "    \"startTime\": \"Thu, 02 Jun 2017 02:51:59 GMT\"," +
                "    \"instanceReferences\": {" +
                "      \"operationalEnvironmentInstanceId\": \"bc305d54-75b4-431b-adb2-eb6b9e546014\"" +
                "    }," +
                "    \"requestScope\": \"operationalEnvironment\"," +
                "    \"requestType\": \"deactivate\"," +
                "    \"requestDetails\": {" +
                "      \"requestInfo\": {" +
                "        \"resourceType\": \"operationalEnvironment\"," +
                "        \"source\": \"VID\"," +
                "        \"requestorId\": \"az2017\"" +
                "      }," +
                "      \"requestParameters\": {" +
                "        \"operationalEnvironmentType\": \"VNF\"" +
                "      }" +
                "    }," +
                "    \"requestStatus\": {" +
                "      \"timestamp\": \"Thu, 02 Jun 2017 02:53:39 GMT\"," +
                "      \"requestState\": \"COMPLETE\"," +
                "      \"statusMessage\": \"Operational Environment successfully deactivated\"," +
                "      \"percentProgress\": \"100\"" +
                "    }" +
                "  }" +
                "}";

        callMsoWithFineRequest(GET_CLOUD_RESOURCES_REQUEST_STATUS, ImmutableMap.of()
                ,uri,"",HttpStatus.OK.value(),expectedResult, HttpMethod.GET);

    }

    @Test(
            expectedExceptions = {HttpClientErrorException.class},
            dataProvider = "statusNotLegitUri"
    )
    public void testStatusWithBadRequest(STATUS_URI_UUID_MODE statusUriMode) throws IOException {
        SimulatorApi.registerExpectation(GET_CLOUD_RESOURCES_REQUEST_STATUS, RegistrationStrategy.CLEAR_THEN_SET);

        String uri = getStatusTargetUri(statusUriMode);

        try {
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        } catch (HttpClientErrorException e) {
            assertThat("Response should be Bad Request (by error code)", e.getStatusCode(), is(BAD_REQUEST));
            assertThat("Response should be Bad Request (by body)", e.getResponseBodyAsString(), containsString("'requestId' is not present"));
            throw e;
        }
    }

    @Test
    public void testStatusWithWrongMethodPost() throws IOException {
        SimulatorApi.registerExpectation(GET_CLOUD_RESOURCES_REQUEST_STATUS, RegistrationStrategy.CLEAR_THEN_SET);

        String myUri = getStatusTargetUri(STATUS_URI_UUID_MODE.OK);

        String response = restTemplate.postForObject(myUri, "", String.class);
        assertThat("Response should be method not allowed => " + response, response, containsString("Request method '" + HttpMethod.POST + "' not supported"));
    }
}
