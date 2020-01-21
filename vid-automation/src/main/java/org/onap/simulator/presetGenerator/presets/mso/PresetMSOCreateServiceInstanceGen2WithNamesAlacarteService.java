package org.onap.simulator.presetGenerator.presets.mso;

import java.util.Map;

public class PresetMSOCreateServiceInstanceGen2WithNamesAlacarteService extends PresetMSOCreateServiceInstanceGen2WithNames {

    public PresetMSOCreateServiceInstanceGen2WithNamesAlacarteService(Map<Keys, String> names, int suffix, String requestId) {
        super(names, suffix, requestId);
    }

    public PresetMSOCreateServiceInstanceGen2WithNamesAlacarteService(Map<Keys, String> names, int suffix, String requestId, String responseInstanceId) {
        super(names, suffix, requestId, responseInstanceId);
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
                "      \"modelInvariantId\": \"0367689e-d41e-483f-b200-eab17e4a7f8d\", " +
                "      \"modelVersionId\": \"e3c34d88-a216-4f1d-a782-9af9f9588705\", " +
                "      \"modelName\": \"gayawabawe\", " +
                "      \"modelVersion\": \"5.1\" " +
                "    }, " +
                "    \"owningEntity\": { " +
                "      \"owningEntityId\": \"038d99af-0427-42c2-9d15-971b99b9b489\", " +
                "      \"owningEntityName\": \"Lucine Sarika\" " +
                "    }, " +
                "    \"project\": { " +
                "      \"projectName\": \"zasaki\" " +
                "    }, " +
                "    \"subscriberInfo\": { " +
                "      \"globalSubscriberId\": \"e433710f-9217-458d-a79d-1c7aff376d89\" " +
                "    }, " +
                "    \"requestInfo\": { " +
                "      \"source\": \"VID\", " +
                "      \"suppressRollback\": true, " +
                "      \"instanceName\": \"" + names.get(Keys.SERVICE_NAME) + suffix + "\", " +
                "      \"productFamilyId\": \"ddf9cc0f-6331-4d35-bed0-a37f2d5e9cb3\", " +
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
