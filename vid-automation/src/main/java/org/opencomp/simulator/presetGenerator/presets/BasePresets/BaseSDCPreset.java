package org.opencomp.simulator.presetGenerator.presets.BasePresets;

/**
 * Created by itzikliderman on 27/12/2017.
 */
public abstract class BaseSDCPreset extends BasePreset {

    @Override
    protected String getRootPath() {
        return "/sdc/v1/catalog/services";
    }
}
