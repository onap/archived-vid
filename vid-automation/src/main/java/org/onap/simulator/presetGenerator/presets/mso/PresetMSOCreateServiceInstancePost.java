package org.onap.simulator.presetGenerator.presets.mso;

/**
 * Created by itzikliderman on 13/12/2017.
 */
public class PresetMSOCreateServiceInstancePost extends PresetMSOBaseCreateInstancePost {

    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances";
    }

}
