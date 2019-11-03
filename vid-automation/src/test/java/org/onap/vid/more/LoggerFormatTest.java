package org.onap.vid.more;

import static java.util.Collections.reverse;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static vid.automation.test.services.SimulatorApi.retrieveRecordedRequests;

import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.vid.api.BaseApiTest;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.SimulatorApi.RecordedRequests;

public class LoggerFormatTest extends BaseApiTest {

    private final static String logChecker = System.getProperty("EELF_LOG_CHECKER", "http://my-logchecker:8888/validate");
    private final Logger logger = LogManager.getLogger(LoggerFormatTest.class);

    public enum LogName {
        audit2019, error, metrics2019
    }

    @BeforeClass
    public void login() {
        super.login();
    }

    @BeforeClass
    public void setAaiSubscribers() {
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    @Test
    public void validateAudit2019LogsFormat() {
        validateLogsFormat(LogName.audit2019, "audit-ELS-2019.11", 0.8);
    }

    @Test(enabled = false) // no total-score is returned for error-log
    public void validateErrorLogsFormat() {
        validateLogsFormat(LogName.error);
    }

    @Test
    public void validateMetrics2019LogsFormat() {
        validateLogsFormat(LogName.metrics2019, "metric-ELS-2019.11");
    }

    private void validateLogsFormat(LogName logName) {
        validateLogsFormat(logName, logName.name());
    }

    private void validateLogsFormat(LogName logName, String logType) {
        validateLogsFormat(logName, logType, 0.95);
    }

    private void validateLogsFormat(LogName logName, String logType, double score) {

        String logLines = getLogLines(logName);
        logger.info("logLines are: "+logLines);
        JsonNode response = getCheckerResults(logType, logLines);
        logger.info("Response is:" + response.toString());

        int total_records = response.path("summary").path("total_records").asInt();
        int valid_records = response.path("summary").path("valid_records").asInt();

        assertThat(total_records, greaterThan(30)); //make sure we have at least 30 total records
        assertThat((double)valid_records/total_records, is(greaterThanOrEqualTo(score)));
    }

    private String getLogLines(LogName logname) {
        return getLogLines(logname, 5000, 30, restTemplate, uri);
    }

    public static String getLogLines(LogName logname, int maxRows, int minRows, RestTemplate restTemplate, URI uri) {
        String logLines = restTemplate.getForObject(uri + "/logger/" + logname.name() + "?limit={maxRows}", String.class, maxRows);
        assertThat("expecting at least " + minRows + " rows in " + logname.name(),
            StringUtils.countMatches(logLines, '\n') + 1,
            is(greaterThanOrEqualTo(minRows)));
        return logLines;
    }

    /**
     * @return Chronological-ordered list of recent log-lines
     */
    public static List<String> getLogLinesAsList(LogName logname, int maxRows, int minRows, RestTemplate restTemplate, URI uri) {
        String logLines = LoggerFormatTest.getLogLines(logname, maxRows, minRows, restTemplate, uri);

        // Split
        List<String> lines = new ArrayList<>(Arrays.asList(logLines.split("(\\r?\\n)")));

        // Reverse
        reverse(lines);

        return lines;
    }


    /**
     * @return Chronological-ordered list of recent log-lines of a given requestId
     */
    public static List<String> getRequestLogLines(String requestId, LogName logname, RestTemplate restTemplate, URI uri) {

        List<String> lines = getLogLinesAsList(logname, 30, 1, restTemplate, uri);

        //Filter
        lines.removeIf(line -> !StringUtils.containsIgnoreCase(line, requestId));

        return lines;
    }


    public static void assertHeadersAndMetricLogs(RestTemplate restTemplate, URI uri, String requestId, String path, int requestsSize) {
        List<String> logLines =
            getRequestLogLines(requestId, LogName.metrics2019, restTemplate, uri);

        List<RecordedRequests> requests = retrieveRecordedRequests();
        List<RecordedRequests> underTestRequests =
            requests.stream().filter(x->x.path.contains(path)).collect(toList());

        assertThat(underTestRequests, hasSize(requestsSize));

        underTestRequests.forEach(request-> {
            assertThat("X-ONAP-RequestID", request.headers.get("X-ONAP-RequestID"), contains(requestId));
            assertThat("X-ECOMP-RequestID", request.headers.get("X-ECOMP-RequestID"), contains(requestId));
            assertThat("X-ONAP-PartnerName", request.headers.get("X-ONAP-PartnerName"), contains("VID.VID"));
        });

        List<String> allInvocationIds = new LinkedList<>();

        underTestRequests.forEach(request->{

            List<String> invocationIds = request.headers.get("X-InvocationID");
            assertThat(invocationIds, hasSize(1));

            String invocationId = invocationIds.get(0);
            allInvocationIds.add(invocationId);

            assertIdsInMetricsLog(logLines, requestId, invocationId);
        });

        //make sure no InvocationId is repeated twice
        assertThat("expect all InvocationIds to be unique",
            allInvocationIds, containsInAnyOrder(new HashSet<>(allInvocationIds).toArray()));
    }

    public static void assertIdsInMetricsLog(List<String> logLines, String requestId, String invocationId) {
        assertThat("request id and invocation id must be found in exactly two rows",
            logLines,
            containsInRelativeOrder(
                allOf(
                    containsString("RequestID="+requestId),
                    containsString("InvocationID="+ invocationId),
                    containsString("Invoke")),
                allOf(
                    containsString("RequestID="+requestId),
                    containsString("InvocationID="+ invocationId),
                    containsString("InvokeReturn"))
            ));
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
