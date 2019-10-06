package org.onap.simulator.presetGenerator.presets.BasePresets;

import java.util.Map;

public abstract class BaseSDCPreset extends BasePreset {

    @Override
    protected String getRootPath() {
        return "/sdc/v1/catalog/services";
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> map = super.getRequestHeaders();
        map.put("X-ONAP-PartnerName", "VID.VID");
        return map;
    }

}
