package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public class PresetMSODeleteInstanceOrchestrationRequestGet extends BaseMSOPreset {

    private final static String DEFAULT_REQUEST_ID = "c0011670-0e1a-4b74-945d-8bf5aede1d9c";
    private final String requestId;
    private final String requestScope;
    String requestState;

    public PresetMSODeleteInstanceOrchestrationRequestGet() {
        this("Service");
    }

    public PresetMSODeleteInstanceOrchestrationRequestGet(String requestScope) {
        this(requestScope, "COMPLETE");
    }

    public PresetMSODeleteInstanceOrchestrationRequestGet(String requestScope, String requestState) {
        this.requestScope = requestScope;
        this.requestState = requestState;
        this.requestId = DEFAULT_REQUEST_ID;
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
                "    \"requestScope\": \"" + this.requestScope.toLowerCase() + "\"," +
                "    \"requestType\": \"deleteInstance\"," +
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
        return "COMPLETE".equals(requestState) ?
                this.requestScope + " has been deleted successfully." :
                (this.requestScope + " has been " + requestState.toLowerCase() + " successfully.");
    }
}
