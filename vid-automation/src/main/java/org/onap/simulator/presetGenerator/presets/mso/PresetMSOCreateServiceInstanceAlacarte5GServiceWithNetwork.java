package org.onap.simulator.presetGenerator.presets.mso;

import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;

import java.util.Map;

public class PresetMSOCreateServiceInstanceAlacarte5GServiceWithNetwork extends PresetMSOCreateServiceInstanceGen2WithNames {

    private final String requestorId;

    public PresetMSOCreateServiceInstanceAlacarte5GServiceWithNetwork(Map<Keys, String> names, String requestId, String requestorId) {
        super(names, 0, requestId);
        this.requestorId = requestorId;
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                "    \"modelInfo\": {" +
                "      \"modelInvariantId\": \"16e56d12-40b3-4db1-a40e-d48c36679e2e\"," +
                "      \"modelVersionId\": \"4659e8bd-0920-4eed-8ec5-550b4c8dceeb\"," +
                "      \"modelName\": \"SR-IOV Provider-1\"," +
                "      \"modelType\": \"service\"," +
                "      \"modelVersion\": \"1.0\"" +
                "    }," +
                "    \"owningEntity\": {" +
                "      \"owningEntityId\": \"d61e6f2d-12fa-4cc2-91df-7c244011d6fc\"," +
                "      \"owningEntityName\": \"MetroPacketCore\"" +
                "    }," +
                "    \"subscriberInfo\": {" +
                "      \"globalSubscriberId\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "    }," +
                "    \"project\": {" +
                "      \"projectName\": \"DFW\"" +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"instanceName\": \""+names.get(SERVICE_NAME)+"\"," +
                "      \"source\": \"VID\"," +
                "      \"suppressRollback\": false," +
                "      \"requestorId\": \""+this.requestorId+"\"" +
                "    }," +
                "    \"requestParameters\": {" +
                "      \"testApi\": \"VNF_API\"," +
                "      \"subscriptionServiceType\": \"TYLER SILVIA\"," +
                "      \"aLaCarte\": true," +
                "      \"userParams\": []" +
                "    }" +
                "  }" +
                "}";

    }

}
