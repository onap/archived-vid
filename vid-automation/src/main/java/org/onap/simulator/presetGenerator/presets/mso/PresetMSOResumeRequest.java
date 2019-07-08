package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

import java.util.Map;

public class PresetMSOResumeRequest extends BaseMSOPreset {

    private final String originalRequestId;
    private final String requestId;
    private final String instanceId;
    private final String userId;

    public PresetMSOResumeRequest(String originalRequestId, String requestId, String instanceId, String userId) {
        this.originalRequestId = originalRequestId;
        this.requestId = requestId;
        this.instanceId = instanceId;
        this.userId = userId;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getReqPath() {
        return super.getRootPath() + "/orchestrationRequests/v./" + originalRequestId + "/resume";
    }

    @Override
    public int getResponseCode() {
        return 202;
    }

    @Override
    public Object getResponseBody() {
        return "{\"requestReferences\":{\"instanceId\":\"" + instanceId + "\",\"requestId\":\"" + requestId + "\"}}";
    }

    @Override
    public Map<String, String> getRequestHeaders() {
        Map<String, String> map = super.getRequestHeaders();
        map.put("X-RequestorID", userId);
        return map;
    }
}
