package org.opencomp.simulator.presetGenerator.presets.BasePresets;

/**
 * Created by itzikliderman on 27/12/2017.
 */
public abstract class BaseAAIPreset extends BasePreset {

    @Override
    protected String getRootPath() {
        return "/aai/v..";
    }
}
