package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOOrchestrationRequestGetErrorResponse extends PresetMSOOrchestrationRequestGet {
    private final int responseCode;

    public PresetMSOOrchestrationRequestGetErrorResponse(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public Object getResponseBody() {
        return "{" +
                "    \"serviceException\": {" +
                "        \"messageId\": \"SVC0002\"," +
                "        \"text\": \"JSON Object Mapping Request\"" +
                "    }" +
                "}";
    }
}
