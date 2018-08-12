package org.opencomp.simulator.presetGenerator.presets.mso;

import org.apache.commons.lang3.StringUtils;
import org.opencomp.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

/**
 * Created by itzikliderman on 13/12/2017.
 */
public class PresetMSOOrchestrationRequestGet extends BaseMSOPreset {

    private final String DEFAULT_REQUEST_ID = "c0011670-0e1a-4b74-945d-8bf5aede1d9c";
    private final String requestId;
    private String statusMessage;
    String requestState;

    public PresetMSOOrchestrationRequestGet() {
        requestState = "COMPLETE";
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

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    public String getReqPath() {
        return getRootPath() + "/orchestrationRequests/v5/" + requestId;
    }

    @Override
    public Object getResponseBody() {
        return "{" +
                "  \"request\": {" +
                "    \"requestId\": \"" + requestId + "\"," +
                "    \"startTime\": \"Mon, 11 Dec 2017 07:27:49 GMT\"," +
                "    \"requestScope\": \"service\"," +
                "    \"requestType\": \"createInstance\"," +
                "    \"instanceReferences\": {" +
                "      \"serviceInstanceId\": \"f8791436-8d55-4fde-b4d5-72dd2cf13cfb\"," +
                "      \"serviceInstanceName\": \"asdfasdf234234asdf\"," +
                "      \"requestorId\": \"il883e\"" +
                "    }," +
                "    \"requestStatus\": {" +
                "      \"requestState\": \"" + requestState + "\"," +
                "      \"statusMessage\": \"" + getStatusMessage() + "\"," +
                "      \"percentProgress\": 100," +
                "      \"finishTime\": \"Mon, 11 Dec 2017 07:27:53 GMT\"" +
                "    }" +
                "  }" +
                "}";
    }

    private String getStatusMessage() {
        if (!StringUtils.isEmpty(statusMessage))
            return statusMessage;
        return "COMPLETE".equals(requestState) ?
                "Service Instance was created successfully." :
                ("Service Instance was " + requestState.toLowerCase() + " successfully.");
    }
}
