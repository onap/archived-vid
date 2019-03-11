package org.onap.simulator.presetGenerator.presets.mso;

import org.apache.commons.lang3.StringUtils;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public abstract class PresetMSOBaseCreateInstancePost extends BaseMSOPreset {

    private final String requestId;
    protected String responseInstanceId;
    public static final String DEFAULT_REQUEST_ID = "c0011670-0e1a-4b74-945d-8bf5aede1d9c";
    protected String msoTestApi;
    protected boolean withTestApi;

    public PresetMSOBaseCreateInstancePost() {
        this(null);
    }

    public PresetMSOBaseCreateInstancePost(String requestId) {
        this.requestId = requestId != null ? requestId : DEFAULT_REQUEST_ID;
        this.responseInstanceId = DEFAULT_INSTANCE_ID;
    }

    public PresetMSOBaseCreateInstancePost(String requestId, String responseInstanceId) {
        this.requestId = requestId != null ? requestId : DEFAULT_REQUEST_ID;
        this.responseInstanceId = responseInstanceId;
    }

    public PresetMSOBaseCreateInstancePost(String requestId, String responseInstanceId, String msoTestApi) {
        this(requestId, responseInstanceId, msoTestApi, true);
    }

    public PresetMSOBaseCreateInstancePost(String requestId, String responseInstanceId, String msoTestApi, boolean withTestApi) {
       this(requestId, responseInstanceId);
       this.msoTestApi = msoTestApi;
       this.withTestApi= withTestApi;
    }

    public String addTestApi() {
        if(this.withTestApi) {
            return "\"testApi\": \"" + msoTestApi + "\",";
        }
        return "";
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getResponseCode() {
        return 202;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public Object getResponseBody() {
        return "{\"requestReferences\":{\"instanceId\":\"" + responseInstanceId + "\",\"requestId\":\"" + requestId + "\"}}";
    }

    protected String formatSuffix(int suffix) {
        return (suffix==0) ? StringUtils.EMPTY : "_" + String.format("%03d", suffix);
    }
}
