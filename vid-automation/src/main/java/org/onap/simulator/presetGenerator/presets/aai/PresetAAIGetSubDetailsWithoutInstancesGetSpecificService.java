package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public class PresetAAIGetSubDetailsWithoutInstancesGetSpecificService extends BaseAAIPreset {
    private String subscriberId;
    private String subscriberName = "Emanuel";

    public PresetAAIGetSubDetailsWithoutInstancesGetSpecificService(String subscriberId) {
        this.subscriberId = subscriberId == null ? "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb" : subscriberId;
    }

    public PresetAAIGetSubDetailsWithoutInstancesGetSpecificService(String subscriberId, String subscriberName) {
        this(subscriberId);
        this.subscriberName = subscriberName;
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of("depth",  Collections.singletonList("1"));
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

    @Override
    public Object getResponseBody() {
        return "{" +
                "  \"global-customer-id\":\""+this.subscriberId+"\"," +
                "  \"subscriber-name\":\""+this.subscriberName+"\"," +
                "  \"subscriber-type\":\"INFRA\"," +
                "  \"resource-version\":\"1494255056308\"," +
                "  \"service-subscriptions\":{" +
                "    \"service-subscription\":[" +
                "      {" +
                "        \"service-type\":\"vFlowLogic\"," +
                "        \"resource-version\":\"1501700976809\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"TYLER SILVIA\"," +
                "        \"resource-version\":\"1501700976809\"," +
                "        \"is-permitted\":false" +
                "      }" +
                "    ]" +
                "  }" +
                "}";
    }

}

