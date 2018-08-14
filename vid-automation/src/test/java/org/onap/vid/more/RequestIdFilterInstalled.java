package org.onap.vid.more;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.onap.vid.api.OperationalEnvironmentControllerApiTest;
import org.onap.vid.api.BaseApiTest;
import org.onap.vid.api.ServiceInstanceMsoApiTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.onap.vid.api.CategoryParametersApiTest.GET_CATEGORY_PARAMETER_PROPERTIES;
import static org.onap.vid.api.pProbeMsoApiTest.MSO_CREATE_CONFIGURATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

public class RequestIdFilterInstalled extends BaseApiTest {

    /*
    Tests whether every incoming request to VID goes through
    the requestId filter. This happens by checking the log
    AND by checking the echoed header.

    The correctness of the Filter itself is done by unit-
    tests.

    The outgoing (outgress) headers are checked by the REST
    Clients unit-tests.
     */

    private static final String ECOMP_REQUEST_ID = "x-ecomp-requestid";
    private final String ECOMP_REQUEST_ID_ECHO = ECOMP_REQUEST_ID + "-echo";

    @BeforeClass
    public void login() {
        super.login();
    }

    @Test
    public void frontendApi_doGET_RequestIdReceived() {

        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.GET,
                "/operationalEnvironment/requestStatus?requestId=" + OperationalEnvironmentControllerApiTest.GET_STATUS_REQUEST_UUID,
                null,
                OperationalEnvironmentControllerApiTest.GET_CLOUD_RESOURCES_REQUEST_STATUS
        );
        assertThatUuidInResponseAndUuidIsInARecentLog(responseAndUuid);

    }

    @Test
    public void frontendApi_doPOST_RequestIdReceived() {

        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.POST,
                "/" + ServiceInstanceMsoApiTest.MSO_DEACTIVATE_SERVICE_INSTANCE,
                "{}",
                ServiceInstanceMsoApiTest.DEACTIVATE_OK_JSON
        );
        assertThatUuidInResponseAndUuidIsInARecentLog(responseAndUuid);
    }

    @Test
    public void frontendApi_doPOSTWithClientError_RequestIdReceived() {

        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.POST,
                "/" + MSO_CREATE_CONFIGURATION,
                "i'm not a json"
        );
        assertThatUuidInResponseAndUuidIsInARecentLog(responseAndUuid);

    }


    @Test(groups = { "worksOnlyWithLocalhostVID" })
    public void mopOwningEntityApi_doGET_RequestIdReceived() {

        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.GET,
                "/" + GET_CATEGORY_PARAMETER_PROPERTIES + "?familyName=PARAMETER_STANDARDIZATION",
                null
        );

        assertThatUuidInResponseAndUuidIsInARecentLog(responseAndUuid);

        /*
        test should be for:
         x few FE requests;
         x few FE errors requests;
         - few UI elements requests;
         x scheduler callback;
         - MOP of workflows;
         x MOP of OE;
         - health-check
         */
    }

    @Test
    public void schedulerApi_doPOST_RequestIdReceived() {

        final String anyInstanceId = "any instance id";
        SimulatorApi.registerExpectation(
                "mso_in_place_software_update_ok.json",
                ImmutableMap.of("SERVICE_INSTANCE_ID", anyInstanceId, "VNF_INSTANCE_ID", anyInstanceId), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.POST,
                "/change-management/workflow/" + anyInstanceId,
                "{}"
        );
        assertThatUuidInResponseAndUuidIsInARecentLog(responseAndUuid);

    }

    @Test
    public void healthcheck_doGET_RequestIdReceived(){
        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.GET, "/healthCheck", null
        );
        assertThatUuidInResponseAndUuidIsInARecentLog(responseAndUuid);
    }

    private void assertThatUuidInResponseAndUuidIsInARecentLog(Pair<HttpEntity, String> responseAndUuid) {
        assertThatResponseHasUuid(responseAndUuid.getLeft(), responseAndUuid.getRight());
        assertThatTermIsInARecentLog(responseAndUuid.getRight());
    }

    private void assertThatResponseHasUuid(HttpEntity response, String uuid) {
        // THIS TEST IS NOT JUST NICE TO HAVE, it also lets us know
        // that the request/response ran through our "promise request
        // id" filter, which is great!
        assertThat(response, not(nullValue()));
        assertThat(response.getHeaders().get(ECOMP_REQUEST_ID_ECHO), containsInAnyOrder(uuid));
    }

    private void assertThatTermIsInARecentLog(String uuid) {
        final ImmutableList<String> logLines = ImmutableList.of(
                LoggerFormatTest.getLogLines("audit", 5, 0, restTemplate, uri),
                LoggerFormatTest.getLogLines("error", 5, 0, restTemplate, uri)
        );

        // Assert that audit *OR* error has the uuid
        assertThat("uuid not found in any log", logLines, hasItem(containsString(uuid)));
    }

    private Pair<HttpEntity, String> makeRequest(HttpMethod httpMethod, String url, String body) {
        return makeRequest(httpMethod, url, body, null);
    }

    private Pair<HttpEntity, String> makeRequest(HttpMethod httpMethod, String url, String body, String expectationFilename) {
        final String uuid = UUID.randomUUID().toString();
        final HttpHeaders headers = new HttpHeaders();
        headers.add(ECOMP_REQUEST_ID, uuid);
        headers.add(AUTHORIZATION, "Basic 123==");

        SimulatorApi.clearExpectations();
        if (!StringUtils.isEmpty(expectationFilename)) {
            SimulatorApi.registerExpectation(expectationFilename, APPEND);
        }
        SimulatorApi.registerExpectation("aai_get_full_subscribers.json", APPEND);
        SimulatorApi.registerExpectation("ecompportal_getSessionSlotCheckInterval.json", APPEND);

        HttpEntity entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = null;
        response = restTemplateErrorAgnostic.exchange(uri + url,
                httpMethod, entity, String.class);

        return Pair.of(response, uuid);
    }

}
