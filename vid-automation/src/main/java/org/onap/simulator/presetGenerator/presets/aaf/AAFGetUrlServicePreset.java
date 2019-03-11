package org.onap.simulator.presetGenerator.presets.aaf;

import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.springframework.http.HttpMethod;

public class AAFGetUrlServicePreset extends BasePreset {
    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return "/locate/com.att.aaf.service:2.0";
    }

    @Override
    protected String getRootPath() {
        return "";
    }

    @Override
    public Object getResponseBody() {
        return "{\"endpoint\":[{\"name\":\"com.att.aaf.service\",\"major\": 2,\"minor\": 0,\"patch\": 19,\"pkg\": 21,\"latitude\": 38.627346,\"longitude\": -90.19377,\"protocol\": \"http\",\"subprotocol\": [],\"hostname\": \"127.0.0.1\",\"port\": 1080}]}";
    }
}
