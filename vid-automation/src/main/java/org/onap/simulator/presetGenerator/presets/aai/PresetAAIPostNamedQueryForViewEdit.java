package org.onap.simulator.presetGenerator.presets.aai;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public class PresetAAIPostNamedQueryForViewEdit extends BaseAAIPreset {

    private final String serviceInstanceId;
    private final boolean hasProxyConfigurationInstance;
    private final boolean hasFabricConfigurationInstance;
    private String serviceInstanceName;
    public static final String DEFAULT_SERVICE_INSTANCE_NAME = "test_sssdad";

    public PresetAAIPostNamedQueryForViewEdit(String serviceInstanceId, boolean hasProxyConfigurationInstance, boolean hasFabricConfigurationInstance) {
        this(serviceInstanceId, DEFAULT_SERVICE_INSTANCE_NAME, hasProxyConfigurationInstance, hasFabricConfigurationInstance);
    }

    public PresetAAIPostNamedQueryForViewEdit(String serviceInstanceId, String serviceInstanceName, boolean hasProxyConfigurationInstance, boolean hasFabricConfigurationInstance) {
        this.serviceInstanceId = serviceInstanceId;
        this.serviceInstanceName = serviceInstanceName;
        this.hasProxyConfigurationInstance = hasProxyConfigurationInstance;
        this.hasFabricConfigurationInstance = hasFabricConfigurationInstance;
    }

    // inspired by
    // registration_to_simulator/search_for_service_instance/aai_named_query_for_view_edit_test_sssdad.json

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getReqPath() {
        return "/aai/search/named-query";
    }

    @Override
    public Object getRequestBody() {
        return "" +
                "{" +
                "  \"instance-filters\": {" +
                "    \"instance-filter\": [{" +
                "        \"customer\": {" +
                "          \"global-customer-id\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "        }," +
                "        \"service-instance\": {" +
                //"          \"service-instance-id\": \"c187e9fe-40c3-4862-b73e-84ff056205f6\"" +
                "          \"service-instance-id\": \"" + serviceInstanceId + "\"" +
                "        }," +
                "        \"service-subscription\": {" +
                "          \"service-type\": \"TYLER SILVIA\"" +
                "        }" +
                "      }" +
                "    ]" +
                "  }," +
                "  \"query-parameters\": {" +
                "    \"named-query\": {" +
                "      \"named-query-uuid\": \"0367193e-c785-4d5f-9cb8-7bc89dc9ddb7\"" +
                "    }" +
                "  }" +
                "}";
    }

    @Override
    public Object getResponseBody() {
        String response =
                "{" +
                " \"inventory-response-item\": [{" +
                "   \"model-name\": \"0eOHz2Yh8WJcBYzKy079\"," +
                "   \"service-instance\": {" +
                "    \"service-instance-id\": \"" + serviceInstanceId + "\"," +
                "    \"service-instance-name\": \"" + serviceInstanceName + "\"" +
                "   }," +
                "   \"extra-properties\": {}";
        if(hasProxyConfigurationInstance) {
            response += "," +
                    "   \"inventory-response-items\": {" +
                    "    \"inventory-response-item\": [{" +
                    "      \"model-name\": \"Proxy-Config-Instance\"," +
                    "      \"configuration\": {" +
                    "       \"configuration-id\": \"9533-config-LB1113\"," +
                    "       \"configuration-name\": \"dummy_instance\"," +
                    "       \"configuration-type\": \"configuration-type-9533\"," +
                    "       \"configuration-sub-type\": \"configuration-sub-type-9533\"," +
                    "       \"model-invariant-id\": \"model-invariant-id-9533\"," +
                    "       \"model-version-id\": \"model-version-id-9533\"," +
                    //"       \"orchestration-status\": \"<ORCH_STATUS>\"," +
                    "       \"orchestration-status\": \"Active\"," +
                    "       \"operational-status\": \"\"," +
                    "       \"configuration-selflink\": \"\"," +
                    "       \"model-customization-id\": \"08a181aa-72eb-435f-9593-e88a3ad0a86b\"," +
                    "       \"resource-version\": \"1504038855716\"" +
                    "      }," +
                    "      \"extra-properties\": {}" +
                    "     }" +
                    "    ]" +
                    "   }";
        }
        else if(hasFabricConfigurationInstance) {
            response += "," +
                    "   \"inventory-response-items\": {" +
                    "    \"inventory-response-item\": [{" +
                    "      \"model-name\": \"Fabric-Configuration-Instance\"," +
                    "      \"configuration\": {" +
                    "       \"configuration-id\": \"1234-fabric-config-LB1113\"," +
                    "       \"configuration-name\": \"dummy_instance\"," +
                    "       \"configuration-type\": \"fabric-configuration-type-1234\"," +
                    "       \"configuration-sub-type\": \"fabric-configuration-sub-type-1234\"," +
                    "       \"model-invariant-id\": \"model-invariant-id-1234\"," +
                    "       \"model-version-id\": \"model-version-id-1234\"," +
                    "       \"orchestration-status\": \"Assigned\"," +
                    "       \"operational-status\": \"\"," +
                    "       \"configuration-selflink\": \"\"," +
                    "       \"model-customization-id\": \"model-customization-id-1234\"," +
                    "       \"resource-version\": \"1504038855716\"" +
                    "      }," +
                    "      \"extra-properties\": {}" +
                    "     }" +
                    "    ]" +
                    "   }";
        }
        response +=  "  }" +
                " ]" +
                "}";
        return response;
    }
}
