package org.onap.simulator.presetGenerator.presets.BasePresets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.model.RegistrationRequest;
import org.springframework.http.HttpMethod;

/**
 * Created by itzikliderman on 13/12/2017.
 */
public abstract class BasePreset {

    public static final String UUID_REGEX = "[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}";

    public RegistrationRequest generateScenario() {
        Map<String, String> responseHeaders = new HashMap<>();
        responseHeaders.put("Content-Type", getContentType());

        return new RegistrationRequest(
                new RegistrationRequest.SimulatorRequest(getReqMethod(), getReqPath(), getQueryParams(), getRequestBody(), isStrictMatch(), getRequestHeaders()),
                new RegistrationRequest.SimulatorResponse(getResponseCode(), responseHeaders, getResponseBody(), getFile()),
                new RegistrationRequest.Misc(getNumberOfTimes(), getReplace()));
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

    public boolean isStrictMatch() {
        return false;
    }

    public Map<String, List> getQueryParams() { return null; }

    protected abstract String getRootPath();

    protected Integer getNumberOfTimes() {return null;}

    protected boolean getReplace()  {return true;}

    public Map<String,String> getRequestHeaders() {
        return new HashMap<>();
    }
}
