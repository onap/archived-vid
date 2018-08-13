package org.onap.simulator.presetGenerator.presets.sdc;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseSDCPreset;

public abstract class SdcPresetWithModelVersionId extends BaseSDCPreset {

    public SdcPresetWithModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    protected String modelVersionId;

    public String getModelVersionId() {
        return modelVersionId;
    }

    public String getReqPath() {
        return getRootPath() + "/"+getModelVersionId();
    }


}
