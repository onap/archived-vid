package org.onap.simulator.presetGenerator.presets.aai;

public class PresetAAIGetL3NetworksByCloudRegionWithError extends AAIBaseGetL3NetworksByCloudRegionPreset {
    private int errorCode;
    private String errorText;


    public PresetAAIGetL3NetworksByCloudRegionWithError(int errorCode, String errorText) {
        super();
        this.errorCode = errorCode;
        this.errorText = errorText;
    }

    @Override
    public int getResponseCode() {
        return errorCode;
    }

    @Override
    public Object getResponseBody() {
        return "{\"status\":\"Error\", \"text\":\"" + errorText + "\"}";
    }
}
