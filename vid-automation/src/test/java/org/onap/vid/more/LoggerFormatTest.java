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
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.onap.vid.api.TestUtils.assertAndRetryIfNeeded;
import static vid.automation.test.services.SimulatorApi.retrieveRecordedRequests;

import com.fasterxml.jackson.databind.JsonNode;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetSubscribersGet;
import org.onap.vid.api.BaseApiTest;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import vid.automation.test.services.SimulatorApi;
import vid.automation.test.services.SimulatorApi.RecordedRequests;

public class LoggerFormatTest extends BaseApiTest {

    private final static String logChecker = System.getProperty("EELF_LOG_CHECKER", "http://my-logchecker:8888/validate");
    private final Logger logger = LogManager.getLogger(LoggerFormatTest.class);
    private final int PRIORITY_LAST = 999;

    public enum LogName {
        audit2019, error, metrics2019, debug
    }

    @BeforeClass
    public void login() {
        super.login();
    }

    @BeforeClass
    public void setAaiSubscribers() {
        SimulatorApi.registerExpectationFromPreset(new PresetAAIGetSubscribersGet(), SimulatorApi.RegistrationStrategy.CLEAR_THEN_SET);
    }

    @DataProvider
    public static Object[][] logsAndFormats(Method test) {
        return new Object[][]{
                {LogName.debug, "debug", 0.95 },
                {LogName.metrics2019, "metric-ELS-2019.11", 0.95},
                {LogName.audit2019, "audit-ELS-2019.11", 0.95},
                {LogName.error, "error", 0.75 }
        };
    }


    @Test(dataProvider = "logsAndFormats", priority = PRIORITY_LAST)
    public void validateLogsAndFormat(LogName logName, String logCheckerFormat, Double expectedRank){
        String logLines = validateLogsFormat(logName, logCheckerFormat, expectedRank);

        if (logName == LogName.audit2019)
        {
            moreValidationsForAuditFormat(logLines);
        }
    }

    //more validations for log format that logcheck doesn't verify
    private void moreValidationsForAuditFormat (String logLines){
        splitLogLines(logLines).forEach(line -> {
            String[] records = line.split("\\|");
            assertThat("server name shall be empty", records[5], emptyOrNullString());

            //authenticated request shall logs with userId.
            final String serviceName = records[6];
            if (StringUtils.containsAny(serviceName, "aai", "mso")) {
                assertThat("Partner name shall be userId", records[7], matchesPattern("^[A-Za-z0-9]{4,15}$"));
            }

            assertThat("Severity shall be empty", records[13], emptyOrNullString());
            assertThat("marker", records[21], either(is("ENTRY")).or(is("EXIT")));
        });
    }

    private String validateLogsFormat (LogName logName, String logType){
        return validateLogsFormat(logName, logType, 0.95);
    }

    private String validateLogsFormat (LogName logName, String logType,double score){

        String logLines = getLogLines(logName);
        logger.info("logLines are: " + logLines);
        JsonNode response = getCheckerResults(logType, logLines);
        logger.info("Response is:" + response.toString());

        int total_records = response.path("summary").path("total_records").asInt();
        int valid_records = response.path("summary").path("valid_records").asInt();

        assertThat(total_records, greaterThan(30)); //make sure we have at least 30 total records
        assertThat((double) valid_records / total_records, is(greaterThanOrEqualTo(score)));

        return logLines;
    }

    private String getLogLines (LogName logname){
        return getLogLines(logname, 5000, 30, restTemplate, uri);
    }

    public static String getLogLines (LogName logname,int maxRows, int minRows, RestTemplate restTemplate, URI uri){
        String logLines = restTemplate.getForObject(uri + "/logger/" + logname.name() + "?limit={maxRows}", String.class, maxRows);
        assertThat("expecting at least " + minRows + " rows in " + logname.name(),
                StringUtils.countMatches(logLines, '\n') + 1,
                is(greaterThanOrEqualTo(minRows)));
        return logLines;
    }

    /**
     * @return Chronological-ordered list of recent log-lines
     */
    public static List<String> getLogLinesAsList (LogName logname,int maxRows, int minRows, RestTemplate restTemplate, URI uri){
        String logLines = LoggerFormatTest.getLogLines(logname, maxRows, minRows, restTemplate, uri);
        List<String> lines = splitLogLines(logLines);

        // Reverse
        reverse(lines);

        return lines;
    }

    @NotNull
    private static List<String> splitLogLines (String logLines){
        return new ArrayList<>(Arrays.asList(logLines.split("(\\r?\\n)")));
    }


    /**
     * @return Chronological-ordered list of recent log-lines of a given requestId
     */
    public static List<String> getRequestLogLines (String requestId, LogName logname, RestTemplate restTemplate, URI uri){

        List<String> lines = getLogLinesAsList(logname, 30, 1, restTemplate, uri);

        //Filter
        lines.removeIf(line -> !StringUtils.containsIgnoreCase(line, requestId));

        return lines;
    }

    public static void verifyExistenceOfIncomingReqsInAuditLogs (RestTemplate restTemplate, URI uri, String requestId, String path){
        assertAndRetryIfNeeded(5, () -> {
            List<String> logLines = getRequestLogLines(requestId, LogName.audit2019, restTemplate, uri);
            String requestIdPrefix = "RequestID=";
            assertThat("\nENTRY & EXIT logs are expected to include RequestId: " + requestId
                    + " \nAnd request path: "
                    + path +
                    "\nin exactly two rows - inside the audit log matching lines:\n"
                    + String.join("\n", logLines) + "\n",
                logLines,
                contains(
                    allOf(
                        containsString(requestIdPrefix + requestId),
                        containsString("ENTRY"),
                        containsString(path)),
                    allOf(
                        containsString(requestIdPrefix + requestId),
                        containsString("EXIT"),
                        containsString(path))
                ));
        });
    }

    public static void assertHeadersAndMetricLogs (RestTemplate restTemplate, URI uri, String requestId, String path, int requestsSize){
        List<RecordedRequests> requests = retrieveRecordedRequests();
        List<RecordedRequests> underTestRequests =
                requests.stream().filter(x -> x.path.contains(path)).collect(toList());

        assertThat(underTestRequests, hasSize(requestsSize));

        underTestRequests.forEach(request -> {
            assertThat("X-ONAP-RequestID", request.headers.get("X-ONAP-RequestID"), contains(requestId));
            assertThat("X-ECOMP-RequestID", request.headers.get("X-ECOMP-RequestID"), contains(requestId));
            assertThat("X-ONAP-PartnerName", request.headers.get("X-ONAP-PartnerName"), contains("VID.VID"));
        });

        List<String> allInvocationIds = new LinkedList<>();

        underTestRequests.forEach(request -> {

            List<String> invocationIds = request.headers.get("X-InvocationID");
            assertThat(invocationIds, hasSize(1));

            String invocationId = invocationIds.get(0);
            allInvocationIds.add(invocationId);

            assertAndRetryIfNeeded(5, () -> assertIdsInMetricsLog(
                getRequestLogLines(requestId, LogName.metrics2019, restTemplate, uri), requestId, invocationId)
            );
        });

        //make sure no InvocationId is repeated twice
        assertThat("expect all InvocationIds to be unique",
                allInvocationIds, containsInAnyOrder(new HashSet<>(allInvocationIds).toArray()));
    }

    public static void assertIdsInMetricsLog (List < String > logLines, String requestId, String invocationId){
        assertThat("request id and invocation id must be found in exactly two rows in: \n" + String.join("\n", logLines),
                logLines,
                containsInRelativeOrder(
                        allOf(
                                containsString("RequestID=" + requestId),
                                containsString("InvocationID=" + invocationId),
                                containsString("Invoke")),
                        allOf(
                                containsString("RequestID=" + requestId),
                                containsString("InvocationID=" + invocationId),
                                containsString("InvokeReturn"))
                ));
    }

    private JsonNode getCheckerResults (String logtype, String logLines){

        final int MAX_RETRIES = 3;

        Map<String, String> params = new HashMap<>();
        params.put("format", "raw");
        params.put("type", logtype);
        params.put("component", "vid");
        params.put("data", logLines);

        for (int i=0; i< MAX_RETRIES; i++) {
            try {
                return restTemplate.postForObject(logChecker, params, JsonNode.class);
            } catch (RestClientException exception) { //retry for cases that logchecker is not available immediately
                logger.error("Failed to call to logChecker try: " + i, exception);
                if (i<(MAX_RETRIES-1)) { //no need to sleep on last retry
                    try {
                        Thread.sleep((new Random().nextInt(2000) + 1000)); //random sleep between 1-3 seconds
                    } catch (InterruptedException e) {
                        ExceptionUtils.rethrow(e);
                    }
                }
            }
        }
        throw new AssertionError("failed to call to logChecker after max retries: "+MAX_RETRIES);
    }
}
