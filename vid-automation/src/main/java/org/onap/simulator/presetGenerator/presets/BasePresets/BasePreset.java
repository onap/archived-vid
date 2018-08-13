package org.onap.simulator.presetGenerator.presets.BasePresets;

import org.onap.simulator.presetGenerator.presets.model.RegistrationRequest;
import org.springframework.http.HttpMethod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by itzikliderman on 13/12/2017.
 */
public abstract class BasePreset {

    public RegistrationRequest generateScenario() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", getContentType());

        return new RegistrationRequest(
                new RegistrationRequest.SimulatorRequest(getReqMethod(), getReqPath(), getQueryParams(), getRequestBody()),
                new RegistrationRequest.SimulatorResponse(getResponseCode(), headers, getResponseBody(), getFile()));
    }

    public Object getResponseBody() { return  null; };

    public String getContentType() {
        return "application/json";
    }

    public String getFile() {
        return null;
    }

    public int getResponseCode() { return 200; }

    public abstract HttpMethod getReqMethod();

    public abstract String getReqPath();

    public Object getRequestBody() {
        return null;
    }

    public Map<String, List> getQueryParams() { return null; }

    protected abstract String getRootPath();
}
