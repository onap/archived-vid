package org.onap.simulator.presetGenerator.presets.ecompportal_att;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseEcompPortalPreset;
import org.springframework.http.HttpMethod;

public class PresetExtendSessionTimeOutsPost extends BaseEcompPortalPreset {
    public Object getResponseBody() {
        return "300000";
    }

    public HttpMethod getReqMethod() {
        return HttpMethod.POST;
    }

    public String getReqPath() {
        return getRootPath() + "//extendSessionTimeOuts";
    }
}
