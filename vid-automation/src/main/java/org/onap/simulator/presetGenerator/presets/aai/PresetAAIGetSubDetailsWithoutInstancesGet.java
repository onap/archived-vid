package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAIGetSubDetailsWithoutInstancesGet extends BaseAAIPreset {
    private String subscriberId;
    private String subscriberName = "Emanuel";


    public PresetAAIGetSubDetailsWithoutInstancesGet(String subscriberId) {
        this.subscriberId = subscriberId == null ? "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb" : subscriberId;
    }

    public PresetAAIGetSubDetailsWithoutInstancesGet(String subscriberId, boolean isSubscriberUspVoice) {
        this(subscriberId);
        if (isSubscriberUspVoice) {
            this.subscriberName = "SILVIA ROBBINS";
        }
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
                "  \"global-customer-id\":\"" + subscriberId + "\"," +
                "  \"subscriber-name\":\" " + subscriberName + "\"," +
                "  \"subscriber-type\":\"INFRA\"," +
                "  \"resource-version\":\"1494255056308\"," +
                "  \"service-subscriptions\":{" +
                "    \"service-subscription\":[" +
                "      {" +
                "        \"service-type\":\"vRichardson\"," +
                "        \"resource-version\":\"1501700976809\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"TYLER SILVIA\"," +
                "        \"resource-version\":\"1501700976809\"," +
                "        \"is-permitted\":true" +
                "      }," +
                "      {" +
                "        \"service-type\":\"Emanuel\"," +
                "        \"resource-version\":\"1505402148533\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vJamie\"," +
                "        \"resource-version\":\"1498068165053\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vVoiceMail\"," +
                "        \"resource-version\":\"1494254889686\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"Kennedy\"," +
                "        \"resource-version\":\"1509369877126\"," +
                "        \"is-permitted\":true" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vPorfirio\"," +
                "        \"resource-version\":\"1494254851983\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vVM\"," +
                "        \"resource-version\":\"1494255049530\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vOTA\"," +
                "        \"resource-version\":\"1501692006937\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vFLORENCE\"," +
                "        \"resource-version\":\"1497729820723\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vMNS\"," +
                "        \"resource-version\":\"1509391084129\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vEsmeralda\"," +
                "        \"resource-version\":\"1501532104695\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"VPMS\"," +
                "        \"resource-version\":\"1509738154248\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vWINIFRED\"," +
                "        \"resource-version\":\"1498752217386\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"SSD\"," +
                "        \"resource-version\":\"1494254866295\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vMOG\"," +
                "        \"resource-version\":\"1500675745252\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"LINDSEY\"," +
                "        \"resource-version\":\"1509387060781\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"JOHANNA_SANTOS\"," +
                "        \"resource-version\":\"1494255101364\"," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"vCarroll\"," +
                "        \"resource-version\":\"1494254877333\"," +
                "        \"is-permitted\":false" +
                "      }" +
                "    ]" +
                "  }" +
                "}";
    }
}
