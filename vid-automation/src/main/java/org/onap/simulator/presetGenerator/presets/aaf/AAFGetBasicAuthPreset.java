package org.onap.simulator.presetGenerator.presets.aaf;

import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.springframework.http.HttpMethod;

import java.util.Map;

public class AAFGetBasicAuthPreset extends BasePreset {
    public static final String VALID_AUTH_VALUE = "bTEyMzRAYXR0LmNvbTphYWFh";

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return "/authn/basicAuth";
    }

    @Override
    protected String getRootPath() {
        return "";
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        return ImmutableMap.of("Authorization", "Basic " + VALID_AUTH_VALUE);
    }
}
