package org.onap.simulator.presetGenerator.presets.mso;

import java.util.Map;

public class PresetMSOCreateServiceInstanceGen2AlacarteService extends PresetMSOCreateServiceInstanceGen2WithNames {

    public PresetMSOCreateServiceInstanceGen2AlacarteService(Map<Keys, String> names, int suffix, String requestId) {
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
                "      \"modelInvariantId\": \"16e56d12-40b3-4db1-a40e-d48c36679e2e\", " +
                "      \"modelVersionId\": \"4659e8bd-0920-4eed-8ec5-550b4c8dceeb\", " +
                "      \"modelName\": \"SR-IOV Provider-1\", " +
                "      \"modelVersion\": \"1.0\" " +
                "    }, " +
                "    \"owningEntity\": { " +
                "      \"owningEntityName\": \"WayneHolland\", " +
                "      \"owningEntityId\": \"d61e6f2d-12fa-4cc2-91df-7c244011d6fc\" " +
                "    }, " +
                "    \"project\": { " +
                "      \"projectName\": \"WATKINS\" " +
                "    }, " +
                "    \"subscriberInfo\": { " +
                "      \"globalSubscriberId\": \"e433710f-9217-458d-a79d-1c7aff376d89\" " +
                "    }, " +
                "    \"requestInfo\": { " +
                "      \"source\": \"VID\", " +
                "      \"suppressRollback\": false, " +
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
