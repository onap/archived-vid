package org.opencomp.vid.more;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.opencomp.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.opencomp.vid.api.BaseApiTest;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class LoggerFormatTest extends BaseApiTest {


    // See: https://wiki.web.att.com/display/KSAT/REST-based+Log+Checker
    private final static String logChecker = "http://eelflogcheck.it.att.com:31820/validate";

    @BeforeClass
    public void login() {
        super.login();
    }

    @BeforeClass
    public void setAaiSubscribers() {
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    @Test
    public void validateAuditLogsFormat() {
        validateLogsFormat("audit");
    }

    @Test(enabled = false) // no total-score is returned for error-log
    public void validateErrorLogsFormat() {
        validateLogsFormat("error");
    }

    @Test
    public void validateMetricsLogsFormat() {
        validateLogsFormat("metrics", "metric");
    }

    private void validateLogsFormat(String logName) {
        validateLogsFormat(logName, logName);
    }

    private void validateLogsFormat(String logName, String logType) {

        String logLines = getLogLines(logName);
        JsonNode response = getCheckerResults(logType, logLines);

        System.out.println(response);
        double fieldscore = response.path("summary").path("score").path("fieldscore").asDouble();
        double overall = response.path("summary").path("score").path("overallscore").asDouble();

        assertThat(fieldscore, is(greaterThan(0.95)));
        assertThat(overall, is(greaterThan(0.95)));

    }

    private String getLogLines(String logname) {
        return getLogLines(logname, 5000, 30, restTemplate, uri);
    }

    public static String getLogLines(String logname, int maxRows, int minRows, RestTemplate restTemplate, URI uri) {
        String logLines = restTemplate.getForObject(uri + "/logger/" + logname + "?limit={maxRows}", String.class, maxRows);
        assertThat("expecting at least " + minRows + " rows in " + logname,
                StringUtils.countMatches(logLines, '\n') + 1,
                is(greaterThanOrEqualTo(minRows)));
        return logLines;
    }

    private JsonNode getCheckerResults(String logtype, String logLines) {
        Map<String, String> params = new HashMap<>();
        params.put("format", "raw");
        params.put("type", logtype);
        params.put("component", "vid");
        params.put("data", logLines);

        return restTemplate.postForObject(logChecker, params, JsonNode.class);
    }
}
