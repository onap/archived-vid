package org.onap.simulator.presetGenerator.presets.aai;

public class PresetAAIGetSubscribersGetInvalidResponse extends PresetAAIGetSubscribersGet {
    private int httpCode;

    public PresetAAIGetSubscribersGetInvalidResponse(int httpCode) {
        this.httpCode = httpCode;
    }

    @Override
    public int getResponseCode() { return httpCode; }

    @Override
    public Object getResponseBody() {
        return "this payload is an invalid json";
    }
}
