package org.onap.simulator.presetGenerator.presets.aai;

public class PresetAAIBadBodyForGetServicesGet extends PresetAAIBaseGetServicesGet {

    private String responseBody;

    public PresetAAIBadBodyForGetServicesGet(String responseBody) {
        this.responseBody = responseBody;
    }

    @Override
    public Object getResponseBody() {
        return responseBody;
    }
}
