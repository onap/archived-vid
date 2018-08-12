package org.opencomp.simulator.presetGenerator.presets.mso;

import org.opencomp.simulator.presetGenerator.presets.BasePresets.BaseMSOPresetServiceInstanceOperationsPost;

/**
 * Created by itzikliderman on 13/12/2017.
 */
public class PresetActivateServiceInstancePost extends BaseMSOPresetServiceInstanceOperationsPost {
    public String getReqPath() {
        return getRootPath() + "/activate";
    }
}
