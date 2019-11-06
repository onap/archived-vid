package org.onap.vid.more;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.onap.vid.api.CategoryParametersApiTest.MAINTENANCE_CATEGORY_PARAMETER;
import static org.onap.vid.api.pProbeMsoApiTest.MSO_CREATE_CONFIGURATION;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.onap.simulator.presetGenerator.presets.aaf.AAFGetBasicAuthPreset;
import org.onap.simulator.presetGenerator.presets.aaf.AAFGetUrlServicePreset;
import org.onap.vid.api.BaseApiTest;
import org.onap.vid.api.OperationalEnvironmentControllerApiTest;
import org.onap.vid.api.ServiceInstanceMsoApiTest;
import org.onap.vid.more.LoggerFormatTest.LogName;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;

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

    private static final String REQUEST_ID_HEADER = "x-onap-requestid";
    private final String ECOMP_REQUEST_ID_ECHO = "x-ecomp-requestid-echo";

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
        assertThatUuidInResponseAndUuidIsInARecentLog(LogName.audit2019, responseAndUuid);

    }

    @Test
    public void frontendApi_doPOST_RequestIdReceived() {

        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.POST,
                "/" + ServiceInstanceMsoApiTest.MSO_DEACTIVATE_SERVICE_INSTANCE,
                "{}",
                ServiceInstanceMsoApiTest.DEACTIVATE_OK_JSON
        );
        assertThatUuidInResponseAndUuidIsInARecentLog(LogName.audit2019, responseAndUuid);
    }

    @Test
    public void frontendApi_doPOSTWithClientError_RequestIdReceived() {

        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.POST,
                "/" + MSO_CREATE_CONFIGURATION,
                "i'm not a json"
        );
        assertThatUuidInResponseAndUuidIsInARecentLog(LogName.error, responseAndUuid);

    }


    @Test(groups = { "worksOnlyWithLocalhostVID" })
    public void mopOwningEntityApi_doGET_RequestIdReceived() {

        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.GET,
                "/" + MAINTENANCE_CATEGORY_PARAMETER + "?familyName=PARAMETER_STANDARDIZATION",
                null
        );
        assertThatUuidInResponseAndUuidIsInARecentLog(LogName.audit2019, responseAndUuid);

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
        SimulatorApi.registerExpectationFromPreset(
                new AAFGetUrlServicePreset(),
                SimulatorApi.RegistrationStrategy.APPEND);
        SimulatorApi.registerExpectationFromPreset(
                new AAFGetBasicAuthPreset(),
                SimulatorApi.RegistrationStrategy.APPEND);
        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.POST,
                "/change-management/workflow/" + anyInstanceId,
                "{}"
        );
        assertThatUuidInResponseAndUuidIsInARecentLog(LogName.audit2019, responseAndUuid);

    }

    @Test
    public void healthcheck_doGET_RequestIdReceived() {
        String path = "/healthCheck";
        final Pair<HttpEntity, String> responseAndUuid = makeRequest(
                HttpMethod.GET, path, null
        );
        assertThatUuidInResponseAndUuidIsInARecentLog(LogName.audit2019, responseAndUuid);
        LoggerFormatTest
            .verifyExistenceOfIncomingReqsInAuditLogs(restTemplate, uri,
                responseAndUuid.getKey().getHeaders().get("X-ECOMP-RequestID-echo").get(0).toString(), path);
    }

    private void assertThatUuidInResponseAndUuidIsInARecentLog(LogName logName, Pair<HttpEntity, String> responseAndUuid) {
        assertThatResponseHasUuid(responseAndUuid.getLeft(), responseAndUuid.getRight());
        assertThatTermIsInARecentLog(logName, responseAndUuid.getRight());
    }

    private void assertThatResponseHasUuid(HttpEntity response, String uuid) {
        // THIS TEST IS NOT JUST NICE TO HAVE, it also lets us know
        // that the request/response ran through our "promise request
        // id" filter, which is great!
        Assert.assertNotNull(response);
        List<String> ecompRequestIdHeaderValues = response.getHeaders().get(ECOMP_REQUEST_ID_ECHO);
        assertThat(ecompRequestIdHeaderValues, hasItem(equalToIgnoringCase(uuid)));
    }

    private void assertThatTermIsInARecentLog(LogName logName, String uuid) {
        final String logLines = LoggerFormatTest.getLogLines(logName, 20, 0, restTemplate, uri);

        assertThat("uuid not found in any log", logLines, containsString(uuid));
    }

    private Pair<HttpEntity, String> makeRequest(HttpMethod httpMethod, String url, String body) {
        return makeRequest(httpMethod, url, body, null);
    }

    private Pair<HttpEntity, String> makeRequest(HttpMethod httpMethod, String url, String body, String expectationFilename) {
        final String uuid = UUID.randomUUID().toString();
        final HttpHeaders headers = new HttpHeaders();
        headers.add(REQUEST_ID_HEADER, uuid);
        headers.add(AUTHORIZATION, "Basic " + AAFGetBasicAuthPreset.VALID_AUTH_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (!StringUtils.isEmpty(expectationFilename)) {
            SimulatorApi.registerExpectation(expectationFilename, APPEND);
        }
        SimulatorApi.registerExpectation("create_new_instance/aai_get_full_subscribers.json", APPEND);
        SimulatorApi.registerExpectation("ecompportal_getSessionSlotCheckInterval.json", APPEND);

        HttpEntity entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response =
            restTemplateErrorAgnostic.exchange(uri + url, httpMethod, entity, String.class);

        return Pair.of(response, uuid);
    }

}
