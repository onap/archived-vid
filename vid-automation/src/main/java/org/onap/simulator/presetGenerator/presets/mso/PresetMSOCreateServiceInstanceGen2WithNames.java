package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;

import java.util.Map;

public class PresetMSOCreateServiceInstanceGen2WithNames extends PresetMSOServiceInstanceGen2WithNames {

    public PresetMSOCreateServiceInstanceGen2WithNames(Map<Keys, String> names, int suffix) {
        super(names, suffix);
        this.cloudOwner = PresetAAIGetCloudOwnersByCloudRegionId.ATT_SABABA;
    }

    public PresetMSOCreateServiceInstanceGen2WithNames(Map<Keys, String> names, int suffix, String requestId) {
        super(names, suffix, requestId);
    }

    public PresetMSOCreateServiceInstanceGen2WithNames(Map<Keys, String> names, int suffix, String requestId, String responseInstanceId) {
        super(names, suffix, requestId, responseInstanceId);
    }


    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances";
    }
}
