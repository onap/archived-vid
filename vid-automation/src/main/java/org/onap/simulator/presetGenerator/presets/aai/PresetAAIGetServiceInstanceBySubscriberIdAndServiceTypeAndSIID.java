package org.onap.simulator.presetGenerator.presets.aai;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public class PresetAAIGetServiceInstanceBySubscriberIdAndServiceTypeAndSIID extends BaseAAIPreset {
    private String subscriberId;

    public String getSubscriberId() {
        return subscriberId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    private String serviceType;
    private String serviceInstanceId;

    public PresetAAIGetServiceInstanceBySubscriberIdAndServiceTypeAndSIID(String subscriberId, String serviceType, String serviceInstanceId) {
        this.subscriberId = subscriberId;
        this.serviceType = serviceType;
        this.serviceInstanceId = serviceInstanceId;
    }

//    @Override
//    public Map<String, List> getQueryParams() {
//        return ImmutableMap.of("depth",  Collections.singletonList("1"));
//    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return getRootPath() +
                "/business/customers/customer/"+getSubscriberId()+
                "/service-subscriptions/service-subscription/"+getServiceType()+
                "/service-instances/service-instance/"+getServiceInstanceId();
    }

    @Override
    public Object getResponseBody() {
        return "{\n"
            + " \"service-instance-id\": \"5d942bc7-3acf-4e35-836a-393619ebde66\","
            + " \"service-instance-name\": \"dpa2actsf5001v_Port_Mirroring_dpa2a_SVC\","
            + " \"model-invariant-id\": \"0757d856-a9c6-450d-b494-e1c0a4aab76f\","
            + " \"model-version-id\": \"a9088517-efe8-4bed-9c54-534462cb08c2\","
            + " \"resource-version\": \"1500789244673\","
            + " \"orchestration-status\": \"Active\","
            + " \"relationship-list\": {"
            + "  \"relationship\": ["
            + "    {"
            + "      \"related-to\": \"generic-vnf\","
            + "      \"related-link\": \"/aai/v11/network/generic-vnfs/generic-vnf/c015cc0f-0f37-4488-aabf-53795fd93cd3\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"generic-vnf.vnf-id\","
            + "          \"relationship-value\": \"c015cc0f-0f37-4488-aabf-53795fd93cd3\""
            + "        }"
            + "      ],"
            + "      \"related-to-property\": ["
            + "        {"
            + "          \"property-key\": \"generic-vnf.vnf-name\","
            + "          \"property-value\": \"fsd\""
            + "        }"
            + "      ]"
            + "    },"
            + "    {"
            + "      \"related-to\": \"generic-vnf\","
            + "      \"related-link\": \"/aai/v11/network/generic-vnfs/generic-vnf/0846287b-65bf-45a6-88f6-6a1af4149fac\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"generic-vnf.vnf-id\","
            + "          \"relationship-value\": \"0846287b-65bf-45a6-88f6-6a1af4149fac\""
            + "        }"
            + "      ],"
            + "      \"related-to-property\": ["
            + "        {"
            + "          \"property-key\": \"generic-vnf.vnf-name\","
            + "          \"property-value\": \"kjkjk\""
            + "        }"
            + "      ]"
            + "    },"
            + "    {"
            + "      \"related-to\": \"generic-vnf\","
            + "      \"related-link\": \"/aai/v11/network/generic-vnfs/generic-vnf/9908b762-136f-4b1f-8eb4-ef670ef58bb4\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"generic-vnf.vnf-id\","
            + "          \"relationship-value\": \"9908b762-136f-4b1f-8eb4-ef670ef58bb4\""
            + "        }"
            + "      ],"
            + "      \"related-to-property\": ["
            + "        {"
            + "          \"property-key\": \"generic-vnf.vnf-name\","
            + "          \"property-value\": \"uiui\""
            + "        }"
            + "      ]"
            + "    },"
            + "    {"
            + "      \"related-to\": \"generic-vnf\","
            + "      \"related-link\": \"/aai/v11/network/generic-vnfs/generic-vnf/543931f5-e50e-45a2-a69f-ab727e4c7f2f\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"generic-vnf.vnf-id\","
            + "          \"relationship-value\": \"543931f5-e50e-45a2-a69f-ab727e4c7f2f\""
            + "        }"
            + "      ],"
            + "      \"related-to-property\": ["
            + "        {"
            + "          \"property-key\": \"generic-vnf.vnf-name\","
            + "          \"property-value\": \"sdfsdfdsf\""
            + "        }"
            + "      ]"
            + "    },"
            + "    {"
            + "      \"related-to\": \"generic-vnf\","
            + "      \"related-link\": \"/aai/v11/network/generic-vnfs/generic-vnf/25e84884-22d5-44c9-8212-cb459f63e0ba\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"generic-vnf.vnf-id\","
            + "          \"relationship-value\": \"25e84884-22d5-44c9-8212-cb459f63e0ba\""
            + "        }"
            + "      ],"
            + "      \"related-to-property\": ["
            + "        {"
            + "          \"property-key\": \"generic-vnf.vnf-name\","
            + "          \"property-value\": \"sdada\""
            + "        }"
            + "      ]"
            + "    },"
            + "    {"
            + "      \"related-to\": \"generic-vnf\","
            + "      \"related-link\": \"/aai/v11/network/generic-vnfs/generic-vnf/013fb0ba-977b-496c-9faa-7f8e5f083eec\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"generic-vnf.vnf-id\","
            + "          \"relationship-value\": \"013fb0ba-977b-496c-9faa-7f8e5f083eec\""
            + "        }"
            + "      ],"
            + "      \"related-to-property\": ["
            + "        {"
            + "          \"property-key\": \"generic-vnf.vnf-name\","
            + "          \"property-value\": \"gvb\""
            + "        }"
            + "      ]"
            + "    },"
            + "    {"
            + "      \"related-to\": \"generic-vnf\","
            + "      \"related-link\": \"/aai/v11/network/generic-vnfs/generic-vnf/06914296-cb46-4b62-9453-329a706a6cbb\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"generic-vnf.vnf-id\","
            + "          \"relationship-value\": \"06914296-cb46-4b62-9453-329a706a6cbb\""
            + "        }"
            + "      ],"
            + "      \"related-to-property\": ["
            + "        {"
            + "          \"property-key\": \"generic-vnf.vnf-name\","
            + "          \"property-value\": \"lkllll\""
            + "        }"
            + "      ]"
            + "    },"
            + "    {"
            + "      \"related-to\": \"generic-vnf\","
            + "      \"related-link\": \"/aai/v11/network/generic-vnfs/generic-vnf/c55da606-cf38-42c7-bc3c-be8e23b19299\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"generic-vnf.vnf-id\","
            + "          \"relationship-value\": \"c55da606-cf38-42c7-bc3c-be8e23b19299\""
            + "        }"
            + "      ],"
            + "      \"related-to-property\": ["
            + "        {"
            + "          \"property-key\": \"generic-vnf.vnf-name\","
            + "          \"property-value\": \"ss\""
            + "        }"
            + "      ]"
            + "    },"
            + "    {"
            + "      \"related-to\": \"generic-vnf\","
            + "      \"related-link\": \"/aai/v11/network/generic-vnfs/generic-vnf/27cc0914-70be-453e-b552-3df6b1d6cda9\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"generic-vnf.vnf-id\","
            + "          \"relationship-value\": \"27cc0914-70be-453e-b552-3df6b1d6cda9\""
            + "        }"
            + "      ],"
            + "      \"related-to-property\": ["
            + "        {"
            + "          \"property-key\": \"generic-vnf.vnf-name\","
            + "          \"property-value\": \"yh\""
            + "        }"
            + "      ]"
            + "    },"
            + "    {"
            + "      \"related-to\": \"logical-link\","
            + "      \"related-link\": \"/aai/v11/network/logical-links/logical-link/tesai372ve2%3Aae10%7Ctesaaisdgrbclz1a1%3Apo100\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"logical-link.link-name\","
            + "          \"relationship-value\": \"tesai372ve2:ae10|tesaaisdgrbclz1a1:po100\""
            + "        }"
            + "      ]"
            + "    },"
            + "    {"
            + "      \"related-to\": \"logical-link\","
            + "      \"related-link\": \"/aai/v11/network/logical-links/logical-link/SANITY6758cce9%3ALAG1992%7CSANITY6785cce9%3ALAG1961\","
            + "      \"relationship-data\": ["
            + "        {"
            + "          \"relationship-key\": \"logical-link.link-name\","
            + "          \"relationship-value\": \"SANITY6758cce9:LAG1992|SANITY6785cce9:LAG1961\""
            + "        }"
            + "           ]"
            + "         }"
            + "       ]"
            + "     }"
            + "   }"
            + " }";
    }

}

