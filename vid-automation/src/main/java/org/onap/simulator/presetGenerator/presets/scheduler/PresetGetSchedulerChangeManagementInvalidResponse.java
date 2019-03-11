package org.onap.simulator.presetGenerator.presets.scheduler;

public class PresetGetSchedulerChangeManagementInvalidResponse extends PresetGetSchedulerChangeManagements {
    private int httpCode;

    public PresetGetSchedulerChangeManagementInvalidResponse(int httpCode) {
        this.httpCode = httpCode;
    }

    @Override
    public int getResponseCode() { return httpCode; }

    @Override
    public Object getResponseBody() {
        return "this payload is an invalid json";
    }
}
