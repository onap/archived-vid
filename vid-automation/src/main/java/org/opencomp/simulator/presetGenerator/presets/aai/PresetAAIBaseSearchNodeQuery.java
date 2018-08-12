package org.opencomp.simulator.presetGenerator.presets.aai;

import org.opencomp.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public abstract class PresetAAIBaseSearchNodeQuery extends BaseAAIPreset {
    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/search/nodes-query";
    }
}
