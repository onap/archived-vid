package org.onap.simulator.presetGenerator.presets.mso;

import java.util.Map;

public class PresetMSOAssignServiceInstanceGen2WithNames extends PresetMSOServiceInstanceGen2WithNames {


    public PresetMSOAssignServiceInstanceGen2WithNames(Map<Keys, String> names, int suffix) {
        super(names, suffix);
    }

    @Override
    public String getReqPath() {
        return  getRootPath() + "/serviceInstantiation/v7/serviceInstances/assign";
    }
}
