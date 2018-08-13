package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPresetServiceInstanceOperationsPost;

/**
 * Created by itzikliderman on 13/12/2017.
 */
public class PresetDeactivateServiceInstancePost extends BaseMSOPresetServiceInstanceOperationsPost {
    public String getReqPath() {
        return getRootPath() + "/deactivate";
    }
}
