package org.onap.vid.more;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.onap.vid.api.TestUtils.assertAndRetryIfNeeded;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.APPEND;
import static vid.automation.test.services.SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET;
import static vid.automation.test.services.SimulatorApi.registerExpectationFromPreset;

import java.util.List;
import java.util.function.Supplier;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetVpnsByType;
import org.onap.vid.api.BaseApiTest;
import org.onap.vid.more.LoggerFormatTest.LogName;
import org.springframework.http.ResponseEntity;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;

public class AuditLoggerTest extends BaseApiTest {

    private final String ECOMP_REQUEST_ID_ECHO = "x-ecomp-requestid-echo";

    @BeforeClass
    public void login() {
        super.login();
    }

    @BeforeMethod
    public void resetPreset() {
        SimulatorApi.registerExpectation("create_new_instance/aai_get_full_subscribers.json", CLEAR_THEN_SET);
    }

    @Test
    public void aaiController2$GetVpnList_requestIdIsAuditedInEntryAndInExit() {
        registerExpectationFromPreset(new PresetAAIGetVpnsByType(), APPEND);
        String requestId = getRequestId(() -> restTemplate.getForEntity(buildUri("aai_get_vpn_list"), String.class));

        assertAndRetryIfNeeded(5, () -> assertThat("request id must be found in exactly two rows", getRequestLogLines(requestId),
            contains(
                allOf(containsString(requestId), containsString("Entering")),
                allOf(containsString(requestId), containsString("Exiting"))
            )));
    }

    private List<String> getRequestLogLines(String requestId) {
        return LoggerFormatTest.getRequestLogLines(requestId, LogName.audit2019, restTemplate, uri);
    }

    private String getRequestId(Supplier<ResponseEntity<?>> request) {
        ResponseEntity<?> response = request.get();

        assertThat(response.getHeaders(), hasKey(equalToIgnoringCase(ECOMP_REQUEST_ID_ECHO)));
        List<String> requestIds = response.getHeaders().get(ECOMP_REQUEST_ID_ECHO);

        assertThat(requestIds, hasSize(1));
        return requestIds.get(0);
    }

}
