package org.onap.simulator.presetGenerator.presets.mso;

import java.util.Map;

public class PresetMSOCreateServiceInstanceGen2WithNamesAlacarteServiceCypress extends PresetMSOCreateServiceInstanceGen2WithNames {

    public PresetMSOCreateServiceInstanceGen2WithNamesAlacarteServiceCypress(Map<Keys, String> names, int suffix, String requestId, String testApi, boolean withTestApi) {
        super(names, suffix, requestId);
        this.msoTestApi = testApi;
        this.withTestApi = withTestApi;
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public Object getRequestBody() {
        return "" +
                "{ " +
                "  \"requestDetails\": { " +
                "    \"modelInfo\": { " +
                "      \"modelType\": \"service\", " +
                "      \"modelInvariantId\": \"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0\", " +
                "      \"modelVersionId\": \"2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd\", " +
                "      \"modelName\": \"action-data\", " +
                "      \"modelVersion\": \"1.0\" " +
                "    }, " +
                "    \"owningEntity\": { " +
                "      \"owningEntityName\": \"MetroPacketCore\", " +
                "      \"owningEntityId\": \"d61e6f2d-12fa-4cc2-91df-7c244011d6fc\" " +
                "    }, " +
                "    \"project\": { " +
                "      \"projectName\": \"DFW\" " +
                "    }, " +
                "    \"subscriberInfo\": { " +
                "      \"globalSubscriberId\": \"e433710f-9217-458d-a79d-1c7aff376d89\" " +
                "    }, " +
                "    \"requestInfo\": { " +
                "      \"source\": \"VID\", " +
                "      \"suppressRollback\": false, " +
                "      \"productFamilyId\": \"ebc3bc3d-62fd-4a3f-a037-f619df4ff034\", " +
                "      \"instanceName\": \"" + names.get(Keys.SERVICE_NAME) + suffix + "\", " +
                "      \"requestorId\": \"us16807000\" " +
                "    }, " +
                "    \"requestParameters\": { " +
                addTestApi()+
                "      \"subscriptionServiceType\": \"TYLER SILVIA\", " +
                "      \"aLaCarte\": true, " +
                "      \"userParams\": [] " +
                "    } " +
                "  } " +
                "} ";

    }

}
