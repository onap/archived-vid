package org.onap.simulator.presetGenerator.presets.mso;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Created by itzikliderman on 13/12/2017.
 */
public class PresetMSOOrchestrationRequestGet extends BaseMSOPreset {
    private static final Logger logger = LogManager.getLogger(PresetMSOOrchestrationRequestGet.class);

    public static final String COMPLETE = "COMPLETE";
    public static final String DEFAULT_REQUEST_ID = "c0011670-0e1a-4b74-945d-8bf5aede1d9c";
    public static final String DEFAULT_SERVICE_INSTANCE_ID = BaseMSOPreset.DEFAULT_INSTANCE_ID;

    private final String requestId;
    private String statusMessage;
    private String requestState;
    private int startedHoursAgo = 1;


    public PresetMSOOrchestrationRequestGet() {
        requestState = COMPLETE;
        this.requestId = DEFAULT_REQUEST_ID;
    }

    public PresetMSOOrchestrationRequestGet(String requestState) {
        this.requestState = requestState;
        this.requestId = DEFAULT_REQUEST_ID;
    }

    public PresetMSOOrchestrationRequestGet(String requestState, String overrideRequestId) {
        this.requestState = requestState;
        this.requestId = overrideRequestId;
    }

    public PresetMSOOrchestrationRequestGet(String requestState, String overrideRequestId, String statusMessage) {
        this.requestState = requestState;
        this.requestId = overrideRequestId;
        this.statusMessage = statusMessage;
    }

    public PresetMSOOrchestrationRequestGet(String requestState, String overrideRequestId, String statusMessage, int startedHoursAgo) {
        this.requestState = requestState;
        this.requestId = overrideRequestId;
        this.statusMessage = statusMessage;
        this.startedHoursAgo = startedHoursAgo;
    }

    public PresetMSOOrchestrationRequestGet(String requestState, int startedHoursAgo) {
        this.requestState = requestState;
        this.requestId = DEFAULT_REQUEST_ID;
        this.startedHoursAgo = startedHoursAgo;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    public String getReqPath() {
        return getRootPath() + "/orchestrationRequests/v./" + requestId;
    }

    @Override
    public Object getResponseBody() {
         String body = "{" +
                "  \"request\": {" +
                "    \"requestId\": \"" + requestId + "\"," +
                "    \"startTime\": \"" + getTimeHoursAgo(startedHoursAgo) + "\"," +
                "    \"requestScope\": \"service\"," +
                "    \"requestType\": \"createInstance\"," +
                "    \"instanceReferences\": {" +
                "      \"serviceInstanceId\": \"" + DEFAULT_SERVICE_INSTANCE_ID + "\"," +
                "      \"serviceInstanceName\": \"asdfasdf234234asdf\"," +
                "      \"requestorId\": \"il883e\"" +
                "    }," +
                "    \"requestStatus\": {" +
                "      \"requestState\": \"" + requestState + "\"," +
                "      \"statusMessage\": \"" + getStatusMessage() + "\"," +
                "      \"percentProgress\": 100," +
                "      \"timestamp\": \"" + getTimeNow() + "\"" +
                "    }" +
                "  }" +
                "}";
        logger.info(body);
        return body;
    }

    private String getStatusMessage() {
        return StringUtils.defaultIfEmpty(statusMessage,
                "COMPLETE".equals(requestState) ?
                        "Service Instance was created successfully." :
                        ("Service Instance was " + requestState.toLowerCase() + " successfully."));
    }

    private String getTimeNow() {
        return getTimeHoursAgo(0);
    }

    private String getTimeHoursAgo(int delta) {
        Instant instant = Instant.now();
        Instant instantMinus = instant.minus(delta, ChronoUnit.HOURS);
        ZonedDateTime dateDayAgo = ZonedDateTime.ofInstant(instantMinus, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
        return formatter.format(dateDayAgo);
    }
}
