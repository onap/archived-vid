package org.onap.simulator.presetGenerator.presets.BasePresets;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

import java.util.Map;
import vid.automation.test.infra.Features;

public abstract class BaseMSOPreset extends BasePreset {

    public static final String DEFAULT_CLOUD_OWNER = "irma-aic";
    public static final String DEFAULT_INSTANCE_ID = "f8791436-8d55-4fde-b4d5-72dd2cf13cfb";
    protected String cloudOwner = DEFAULT_CLOUD_OWNER;

    protected String addCloudOwnerIfNeeded() {
        return Features.FLAG_1810_CR_ADD_CLOUD_OWNER_TO_MSO_REQUEST.isActive() ?
            "\"cloudOwner\": \"" + cloudOwner + "\"," : "";
    }

    protected String addPlatformIfNeeded(String platform) {
        return isNotEmpty(platform) ?
            " \"platform\": {" +
                " \"platformName\": \"" + platform + "\"," +
            "}," : "";
    }

    @Override
    protected String getRootPath() {
        return "/mso";
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> map = super.getRequestHeaders();
        map.put("X-ONAP-PartnerName", "VID.VID");
        map.put("X-ECOMP-RequestID", UUID_REGEX);
        map.put("X-InvocationID", UUID_REGEX);
        map.put("X-ONAP-RequestID", UUID_REGEX);
        return map;
    }
}
