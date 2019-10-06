package org.onap.simulator.presetGenerator.presets.aai;

import org.apache.commons.lang3.StringUtils;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import static vid.automation.test.utils.ReadFile.loadResourceAsString;

public class PresetAAIGetTenants extends BaseAAIPreset {
    private final String subscriberId;
    private final String serviceType;
    private String responseBody;
    private static String responseBodyResourceDefault = "presets_templates/PresetAAIGetTenants.json";

    public PresetAAIGetTenants(String subscriberId, String serviceType, String responseBodyResource) {
        this.subscriberId = subscriberId;
        this.serviceType = serviceType;
        this.responseBody = loadResponseBody(responseBodyResource);
    }

    public PresetAAIGetTenants() {
        this(
                "e433710f-9217-458d-a79d-1c7aff376d89",
                "TYLER SILVIA",
                responseBodyResourceDefault
        );
    }

    public PresetAAIGetTenants(String subscriberId, String serviceType) {
        this(
                subscriberId,
                serviceType,
                responseBodyResourceDefault
        );
    }

    private String loadResponseBody(String responseBodyResource) {

        String responseBody = loadResourceAsString(responseBodyResource);

        if (StringUtils.equals(responseBodyResource, responseBodyResourceDefault)) {
            responseBody = setServiceTypeInTheResponse(responseBody);
        }

        return responseBody;
    }

    private String setServiceTypeInTheResponse(String resourceBodyAsString) {
        final String serviceTypePlaceHolder = "<service-type>";
        return resourceBodyAsString.replace(serviceTypePlaceHolder, this.serviceType);
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
