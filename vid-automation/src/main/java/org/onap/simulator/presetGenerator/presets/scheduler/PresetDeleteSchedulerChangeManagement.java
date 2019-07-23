package org.onap.simulator.presetGenerator.presets.scheduler;

import org.onap.simulator.presetGenerator.presets.BasePresets.BasePreset;
import org.springframework.http.HttpMethod;

public class PresetDeleteSchedulerChangeManagement extends BasePreset {

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.DELETE;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/83aec7bf-602f-49eb-9788-bbc33ac550d9";
    }

    @Override
    protected String getRootPath() {
        return "/scheduler/v1/ChangeManagement/schedules";
    }

    @Override
    public Object getResponseBody() {
        return "{}";
    }

    @Override
    public int getResponseCode() { return 204; }

}
