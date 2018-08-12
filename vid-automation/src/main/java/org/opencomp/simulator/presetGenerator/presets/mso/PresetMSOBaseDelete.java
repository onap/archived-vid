package org.opencomp.simulator.presetGenerator.presets.mso;

import org.opencomp.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public abstract class PresetMSOBaseDelete extends BaseMSOPreset {
    private final String requestId;
    public static final String DEFAULT_REQUEST_ID = "c0011670-0e1a-4b74-945d-8bf5aede1d9c";

    public PresetMSOBaseDelete() {
        this(null );
    }

    public PresetMSOBaseDelete(String requestId) {
        this.requestId = requestId != null ? requestId : DEFAULT_REQUEST_ID;
    }

    @Override
    protected String getRootPath() {
        return super.getRootPath() + "/serviceInstances/v./";
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.DELETE;
    }

    @Override
    public int getResponseCode() {
        return 202;
    }

    @Override
    public Object getResponseBody() {
        return "{\"requestReferences\":{\"instanceId\":\"f8791436-8d55-4fde-b4d5-72dd2cf13cfb\",\"requestId\":\"" + requestId + "\"}}";
    }
}
