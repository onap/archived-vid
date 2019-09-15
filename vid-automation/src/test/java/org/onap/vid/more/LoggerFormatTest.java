package org.onap.vid.more;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.vid.api.BaseApiTest;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.infra.SkipTestUntil;
import vid.automation.test.services.SimulatorApi;

public class LoggerFormatTest extends BaseApiTest {

    private final static String logChecker = System.getProperty("EELF_LOG_CHECKER", "http://my-logchecker:8888/validate");
    private final Logger logger = LogManager.getLogger(LoggerFormatTest.class);

    @BeforeClass
    public void login() {
        super.login();
    }

    @BeforeClass
    public void setAaiSubscribers() {
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    @SkipTestUntil("2019-09-24")
    @Test
    public void validateAuditLogsFormat() {
        validateLogsFormat("audit");
    }

    @Test
    public void validateAudit2019LogsFormat() {
        validateLogsFormat("audit2019", "audit-ELS-2019.11", 0);
    }

    @Test(enabled = false) // no total-score is returned for error-log
    public void validateErrorLogsFormat() {
        validateLogsFormat("error");
    }

    @SkipTestUntil("2019-09-24")
    @Test
    public void validateMetricsLogsFormat() {
        validateLogsFormat("metrics", "metric");
    }

    private void validateLogsFormat(String logName) {
        validateLogsFormat(logName, logName);
    }

    private void validateLogsFormat(String logName, String logType) {
        validateLogsFormat(logName, logType, 0.95);
    }

    private void validateLogsFormat(String logName, String logType, double score) {

        String logLines = getLogLines(logName);
        logger.info("logLines are: "+logLines);
        JsonNode response = getCheckerResults(logType, logLines);
        logger.info("Response is:" + response.toString());
        double fieldscore = response.path("summary").path("score").path("fieldscore").asDouble();
        double overall = response.path("summary").path("score").path("overallscore").asDouble();

        assertThat(fieldscore, is(greaterThanOrEqualTo(score)));
        assertThat(overall, is(greaterThanOrEqualTo(score)));

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
