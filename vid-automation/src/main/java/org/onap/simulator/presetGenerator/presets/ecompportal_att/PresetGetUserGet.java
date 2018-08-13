package org.onap.simulator.presetGenerator.presets.ecompportal_att;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseEcompPortalPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;

public class PresetGetUserGet extends BaseEcompPortalPreset {
    public Object getResponseBody() {
        return Collections.EMPTY_LIST;
    }

    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    public String getReqPath() {
        return getRootPath() + "/context/get_user";
    }
}
