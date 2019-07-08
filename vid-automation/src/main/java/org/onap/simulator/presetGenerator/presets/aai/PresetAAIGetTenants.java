package org.onap.simulator.presetGenerator.presets.aai;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import static vid.automation.test.utils.ReadFile.loadResourceAsString;

public class PresetAAIGetTenants extends BaseAAIPreset {
    private final String subscriberId;
    private final String serviceType;
    private String responseBody;

    public PresetAAIGetTenants(String subscriberId, String serviceType, String responseBodyResource) {
        this.subscriberId = subscriberId;
        this.serviceType = serviceType;
        this.responseBody = loadResourceAsString(responseBodyResource);
    }

    public PresetAAIGetTenants() {
        this(
            "e433710f-9217-458d-a79d-1c7aff376d89",
            "TYLER SILVIA",
            "presets_templates/PresetAAIGetTenants.json"
        );
    }

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
        return getRootPath() + "/business/customers/customer/" + this.subscriberId + "/service-subscriptions/service-subscription/" + this.serviceType;
    }


}
