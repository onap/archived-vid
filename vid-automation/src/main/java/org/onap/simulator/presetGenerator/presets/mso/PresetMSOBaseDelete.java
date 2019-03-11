package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public abstract class PresetMSOBaseDelete extends BaseMSOPreset {
    protected final String requestId;
    protected final String instanceId;

    public static final String DEFAULT_REQUEST_ID = "c0011670-0e1a-4b74-945d-8bf5aede1d9c";

    public PresetMSOBaseDelete(String requestId) {
        this.requestId = requestId != null ? requestId : DEFAULT_REQUEST_ID;
        this.instanceId = DEFAULT_INSTANCE_ID;
    }

    public PresetMSOBaseDelete(String requestId, String instanceId) {
        this.requestId = requestId;
        this.instanceId = instanceId;
    }


    @Override
    protected String getRootPath() {
        return super.getRootPath() + "/serviceInstantiation/v./serviceInstances/";
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
        return "{\"requestReferences\":{\"instanceId\":\""+instanceId+"\",\"requestId\":\"" + requestId + "\"}}";
    }
}
