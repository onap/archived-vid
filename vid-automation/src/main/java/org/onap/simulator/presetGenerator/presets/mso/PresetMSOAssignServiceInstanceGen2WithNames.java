package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;

import java.util.Map;

public class PresetMSOAssignServiceInstanceGen2WithNames extends PresetMSOServiceInstanceGen2WithNames {


    public PresetMSOAssignServiceInstanceGen2WithNames(Map<Keys, String> names, int suffix) {
        super(names, suffix);
        this.cloudOwner = PresetAAIGetCloudOwnersByCloudRegionId.ATT_SABABA;
    }

    @Override
    public String getReqPath() {
        return  getRootPath() + "/serviceInstantiation/v./serviceInstances/assign";
    }
}
