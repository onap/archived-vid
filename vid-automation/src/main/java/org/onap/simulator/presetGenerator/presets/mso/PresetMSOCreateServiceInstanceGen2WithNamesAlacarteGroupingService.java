package org.onap.simulator.presetGenerator.presets.mso;

import java.util.Map;

public class PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService extends PresetMSOCreateServiceInstanceGen2WithNames {

    private String userId = "us16807000";

    public PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService(Map<Keys, String> names, int suffix, String requestId) {
        super(names, suffix, requestId);
    }

    public PresetMSOCreateServiceInstanceGen2WithNamesAlacarteGroupingService(Map<Keys, String> names, int suffix, String requestId, String responseInstanceId, String userId) {
        super(names, suffix, requestId);
        this.responseInstanceId = responseInstanceId;
        this.userId = userId;
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
                "      \"modelInvariantId\": \"7ee41ce4-4827-44b0-a48e-2707a59905d2\", " +
                "      \"modelVersionId\": \"4117a0b6-e234-467d-b5b9-fe2f68c8b0fc\", " +
                "      \"modelName\": \"Grouping Service for Test\", " +
                "      \"modelVersion\": \"1.0\" " +
                "    }, " +
                "    \"owningEntity\": { " +
                "      \"owningEntityId\": \"d61e6f2d-12fa-4cc2-91df-7c244011d6fc\", " +
                "      \"owningEntityName\": \"WayneHolland\" " +
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
                "      \"requestorId\": \"" + userId + "\" " +
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
