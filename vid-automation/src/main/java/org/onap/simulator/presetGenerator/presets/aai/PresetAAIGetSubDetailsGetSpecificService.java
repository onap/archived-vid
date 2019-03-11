package org.onap.simulator.presetGenerator.presets.aai;

import static vid.automation.test.infra.ModelInfo.serviceFabricSriovService;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;
import vid.automation.test.infra.ModelInfo;

public class PresetAAIGetSubDetailsGetSpecificService extends BaseAAIPreset {
    private String subscriberId;
    private String subscriberName = "Emanuel";
    private String orchStatus;

    private String serviceInstanceId = "c187e9fe-40c3-4862-b73e-84ff056205f61234";
    private String serviceInstanceName = "test_fabric_config";
    private ModelInfo modelInfo = serviceFabricSriovService;

    public PresetAAIGetSubDetailsGetSpecificService(String subscriberId) {
        this.subscriberId = subscriberId == null ? "a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb" : subscriberId;
    }

    public PresetAAIGetSubDetailsGetSpecificService(String subscriberId, String subscriberName, String orchStatus, ModelInfo modelInfo, String serviceInstanceId) {
        this(subscriberId);
        this.subscriberName = subscriberName;
        this.orchStatus = orchStatus;
        this.modelInfo = modelInfo;
        this.serviceInstanceId = serviceInstanceId;
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of("depth",  Collections.singletonList("2"));
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
                "        \"service-instances\":{" +
                "          \"service-instance\":[" +
                "            {" +
                "              \"service-instance-id\":\"414db2d4-18d0-415e-aac2-f255b586cfb5\"," +
                "              \"service-instance-name\":\"vFlowLogic-vflf_080117\"," +
                "              \"persona-model-id\":null," +
                "              \"persona-model-version\":null," +
                "              \"resource-version\":\"1502391806301\"," +
                "              \"orchestration-status\":\"Active\"," +
                "              \"model-invariant-id\":\"64dbe153-48c6-4d6f-95e7-12ff4cce9871\"," +
                "              \"model-version-id\":\"4e8b6372-dea3-4028-81d0-16eba59c94fb\"" +
                "            }," +
                "            {" +
                "              \"service-instance-id\":\"769be44e-981a-4da8-af3b-ca5fa76b7fb1\"," +
                "              \"service-instance-name\":\"vFlowLogic-vflp_080117\"," +
                "              \"persona-model-id\":null," +
                "              \"persona-model-version\":null," +
                "              \"resource-version\":\"1501712783617\"," +
                "              \"orchestration-status\":\"Active\"," +
                "              \"model-invariant-id\":\"14a99b56-dfb6-4300-97e1-ef10198ec936\"," +
                "              \"model-version-id\":\"9c6eee3b-8bd6-4a01-8afc-39441b63ecc9\"" +
                "            }" +
                "          ]" +
                "        }," +
                "        \"is-permitted\":false" +
                "      }," +
                "      {" +
                "        \"service-type\":\"TYLER SILVIA\"," +
                "        \"resource-version\":\"1501700976809\"," +
                "        \"service-instances\":{" +
                "          \"service-instance\":[" +
                "            {" +
                "              \"service-instance-id\": \""+this.serviceInstanceId+"\"," +
                "              \"service-instance-name\": \""+this.serviceInstanceName+"\"," +
                "              \"model-invariant-id\": \""+this.modelInfo.modelInvariantId+"\"," +
                "              \"model-version-id\": \""+this.modelInfo.modelVersionId+"\"," +
                "              \"resource-version\": \"1500789244674\"," +
                "              \"orchestration-status\": \""+this.orchStatus+"\"" +
                "            }" +
                "          ]" +
                "        }," +
                "        \"is-permitted\":false" +
                "      }" +
                "    ]" +
                "  }" +
                "}";
    }

}

