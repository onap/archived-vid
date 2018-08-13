package org.onap.simulator.presetGenerator.presets.ecompportal_att;

import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;

import java.util.Arrays;
import java.util.List;

public class EcompPortalPresetsUtils {

    private final static List<BasePreset> ecompPortalPresets = Arrays.asList(
            new PresetGetUserGet(),
            new PresetGetSessionSlotCheckIntervalGet(),
            new PresetExtendSessionTimeOutsPost());

    public static List<BasePreset> getEcompPortalPresets() {
        return ecompPortalPresets;
    }
}
