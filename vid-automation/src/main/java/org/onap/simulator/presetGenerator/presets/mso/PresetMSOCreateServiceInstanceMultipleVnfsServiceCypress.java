package org.onap.simulator.presetGenerator.presets.mso;

import java.util.Map;

public class PresetMSOCreateServiceInstanceMultipleVnfsServiceCypress extends PresetMSOCreateServiceInstanceGen2WithNames {

    public PresetMSOCreateServiceInstanceMultipleVnfsServiceCypress(Map<Keys, String> names, int suffix, String requestId) {
        super(names, suffix, requestId);
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
                "      \"modelVersionId\": \"6e59c5de-f052-46fa-aa7e-2fca9d674c44\", " +
                "      \"modelName\": \"ComplexService\", " +
                "      \"modelVersion\": \"1.0\" " +
                "    }, " +
                "    \"owningEntity\": { " +
                "      \"owningEntityName\": \"aaa1\", " +
                "      \"owningEntityId\": \"aaa1\" " +
                "    }, " +
                "    \"project\": { " +
                "      \"projectName\": \"yyy1\" " +
                "    }, " +
                "    \"subscriberInfo\": { " +
                "      \"globalSubscriberId\": \"e433710f-9217-458d-a79d-1c7aff376d89\" " +
                "    }, " +
                "    \"requestInfo\": { " +
                "      \"source\": \"VID\", " +
                "      \"suppressRollback\": false, " +
                "      \"productFamilyId\": \"36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e\", " +
                "      \"instanceName\": \"" + names.get(Keys.SERVICE_NAME) + suffix + "\", " +
                "      \"requestorId\": \"us16807000\" " +
                "    }, " +
                "    \"requestParameters\": { " +
                "      \"testApi\": \"GR_API\", " +
                "      \"subscriptionServiceType\": \"TYLER SILVIA\", " +
                "      \"aLaCarte\": true, " +
                "      \"userParams\": [] " +
                "    } " +
                "  } " +
                "} ";

    }

}
