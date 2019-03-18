package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOOrchestrationRequestsManyStatusesInvalidResponseGet extends PresetMSOOrchestrationRequestsManyStatusesGet {
    private final int responseCode;

    public PresetMSOOrchestrationRequestsManyStatusesInvalidResponseGet(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public Object getResponseBody() {
        return "this payload is an invalid json";
    }
}
