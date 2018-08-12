package org.opencomp.simulator.presetGenerator.presets.mso;

import java.util.Map;

public class PresetMSOCreateServiceInstanceGen2WithNames extends PresetMSOServiceInstanceGen2WithNames {

    public PresetMSOCreateServiceInstanceGen2WithNames(Map<Keys, String> names, int suffix) {
        super(names, suffix);
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v7/serviceInstances";
    }
}
