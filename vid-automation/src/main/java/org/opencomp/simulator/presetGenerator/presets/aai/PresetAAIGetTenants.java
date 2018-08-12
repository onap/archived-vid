package org.opencomp.simulator.presetGenerator.presets.aai;

import org.opencomp.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import static vid.automation.test.utils.ReadFile.loadResourceAsString;

public class PresetAAIGetTenants extends BaseAAIPreset {

    @Override
    public Object getResponseBody() {
        return responseBody;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/business/customers/customer/e433710f-9217-458d-a79d-1c7aff376d89/service-subscriptions/service-subscription/VIRTUAL USP";
    }

    private String responseBody = loadResourceAsString("presets_templates/PresetAAIGetTenants.json");

}
