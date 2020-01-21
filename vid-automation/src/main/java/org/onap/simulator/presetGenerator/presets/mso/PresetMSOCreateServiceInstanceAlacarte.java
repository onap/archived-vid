package org.onap.simulator.presetGenerator.presets.mso;

import static org.onap.simulator.presetGenerator.presets.mso.PresetMSOServiceInstanceGen2WithNames.Keys.SERVICE_NAME;

import java.util.Map;
import vid.automation.test.infra.ModelInfo;

public class PresetMSOCreateServiceInstanceAlacarte extends PresetMSOCreateServiceInstanceGen2WithNames {

    private final String requestorId;
    protected final ModelInfo modelInfo;

    public PresetMSOCreateServiceInstanceAlacarte(Map<Keys, String> names, String requestId, String responseInstanceId,
        String requestorId, ModelInfo modelInfo) {
        super(names, 0, requestId, responseInstanceId);
        this.requestorId = requestorId;
        this.modelInfo = modelInfo;
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                    modelInfo.createMsoModelInfo()+
                "    \"owningEntity\": {" +
                "      \"owningEntityId\": \"d61e6f2d-12fa-4cc2-91df-7c244011d6fc\"," +
                "      \"owningEntityName\": \"WayneHolland\"" +
                "    }," +
                "    \"subscriberInfo\": {" +
                "      \"globalSubscriberId\": \"e433710f-9217-458d-a79d-1c7aff376d89\"" +
                "    }," +
                "    \"project\": {" +
                "      \"projectName\": \"WATKINS\"" +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"instanceName\": \""+names.get(SERVICE_NAME)+"\"," +
                "      \"source\": \"VID\"," +
                "      \"suppressRollback\": false," +
                "      \"requestorId\": \""+this.requestorId+"\"" +
                "    }," +
                "    \"requestParameters\": {" +
                "      \"testApi\": \"GR_API\"," +
                "      \"subscriptionServiceType\": \"TYLER SILVIA\"," +
                "      \"aLaCarte\": true," +
                "      \"userParams\": []" +
                "    }" +
                "  }" +
                "}";

    }

}
