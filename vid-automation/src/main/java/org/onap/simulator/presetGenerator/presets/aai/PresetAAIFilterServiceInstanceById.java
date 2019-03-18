package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAIFilterServiceInstanceById extends PresetAAIBaseSearchNodeQuery {

    private String subscriberId;
    private String serviceType;
    private String serviceInstanceId;

    public PresetAAIFilterServiceInstanceById(String subscriberId, String serviceType, String serviceInstanceId) {
        this.subscriberId = subscriberId;
        this.serviceType = serviceType;
        this.serviceInstanceId = serviceInstanceId;
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of("search-node-type", Collections.singletonList("service-instance"),
                "filter", Collections.singletonList("service-instance-id:EQUALS:" + this.serviceInstanceId));
    }

    @Override
    public Object getResponseBody() {
        return "" +
                "{" +
                 "\"result-data\": [\n" +
                "        {\n" +
                "          \"resource-type\": \"service-instance\",\n" +
                "          \"resource-link\": \"/aai/v11/business/customers/customer/" + this.subscriberId + "/service-subscriptions/service-subscription/" + this.serviceType + "/service-instances/service-instance/" + this.serviceInstanceId + "\"\n" +
                "        }\n" +
                "    ]" +
                "}";
    }
}
