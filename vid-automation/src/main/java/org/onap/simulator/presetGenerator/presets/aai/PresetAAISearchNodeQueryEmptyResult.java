package org.onap.simulator.presetGenerator.presets.aai;

public class PresetAAISearchNodeQueryEmptyResult extends PresetAAIBaseSearchNodeQuery {

    @Override
    public Object getResponseBody() {
        return "{}";
    }

    @Override
    public int getResponseCode() {
        return 200;
    }
}
