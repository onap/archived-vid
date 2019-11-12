package org.onap.simulator.presetGenerator.presets.aaf;

import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.springframework.http.HttpMethod;
import vid.automation.test.services.SimulatorApi;

public class AAFGetUrlServicePreset extends BasePreset {
    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return "/locate/" + regexAafServerName() + ":2.0" + regexOptionalSlash();
    }

    private String regexAafServerName() {
        return "([a-z-]+\\.)*[a-z-]+";
    }

    private String regexOptionalSlash() {
        return "/?"; // question mark is NOT a url query
    }

    @Override
    protected String getRootPath() {
        return "";
    }

    @Override
    public Object getResponseBody() {
        return ""
            + "{"
            + "  \"endpoint\": [{"
            + "      \"name\": \"aaf-service\","
            + "      \"major\": 2,"
            + "      \"minor\": 0,"
            + "      \"patch\": 19,"
            + "      \"pkg\": 21,"
            + "      \"latitude\": 38.627346,"
            + "      \"longitude\": -90.19377,"
            + "      \"protocol\": \"http\","
            + "      \"subprotocol\": [],"
            + "      \"port\": " + SimulatorApi.getSimulatedResponsesPort() + ","
            + "      \"hostname\": \"" + SimulatorApi.getSimulatorHost() + "\""
            + "    }"
            + "  ]"
            + "}"; 
    }
}
