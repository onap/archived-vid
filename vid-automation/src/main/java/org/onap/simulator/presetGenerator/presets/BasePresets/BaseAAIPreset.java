package org.onap.simulator.presetGenerator.presets.BasePresets;

import java.util.Map;

public abstract class BaseAAIPreset extends BasePreset {

    @Override
    protected String getRootPath() {
        return "/aai/v..";
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> map = super.getRequestHeaders();
        map.put("X-ONAP-PartnerName", "VID.VID");
        map.put("X-InvocationID", "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}");
        map.put("X-ONAP-RequestID", "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}");
        return map;
    }
}
