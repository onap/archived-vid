package org.onap.simulator.presetGenerator.presets.BasePresets;

import java.util.Map;

public abstract class BaseSDCPreset extends BasePreset {

    public static final String SDC_ROOT_PATH = "/sdc/v1/catalog/services";

    @Override
    protected String getRootPath() {
        return SDC_ROOT_PATH;
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> map = super.getRequestHeaders();
        map.put("X-ONAP-PartnerName", "VID.VID");
        map.put("X-ONAP-InvocationID", "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}");
        map.put("X-ONAP-RequestID", "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}");
        return map;
    }

}
