package org.onap.simulator.presetGenerator.presets.aai;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static vid.automation.test.utils.ReadFile.loadResourceAsString;

public class PresetAAIGetSubDetailsGet extends BaseAAIPreset {
    private String subscriberId;
    private String responseBody;

    private static final String baseResponseBody = loadResourceAsString("presets_templates/PresetAAIGetSubDetailsGet.json");
    private static final String GLOBAL_CUSTOMER_ID = "GLOBAL_CUSTOMER_ID";

    public PresetAAIGetSubDetailsGet(String subscriberId) {
        this.subscriberId = subscriberId == null ? "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb" : subscriberId;
        this.responseBody = baseResponseBody.replace(GLOBAL_CUSTOMER_ID, this.subscriberId);
    }

    public void mapQueryParams(Map<String, List> map) {
        map.put("depth",  Collections.singletonList("2"));
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
        return getRootPath() + "/business/customers/customer/" + getSubscriberId();
    }

    public String getSubscriberId() {
        return subscriberId;
    }
}
