package org.onap.simulator.presetGenerator.presets.mso.configuration;

import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;
import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;
import org.springframework.http.HttpMethod;

public class PresetMsoEnableDisablePort extends PresetMSOBaseCreateInstancePost {

    private final String serviceInstanceId;
    private final String configurationId;
    private final String action;


    public PresetMsoEnableDisablePort(String serviceInstanceId, String configurationId, String action) {
        super("314cc766-b673-4a50-b9c5-471f68914585", serviceInstanceId);
        this.serviceInstanceId = serviceInstanceId;
        this.configurationId = configurationId;
        this.action = action;
        this.cloudOwner = PresetAAIGetCloudOwnersByCloudRegionId.ATT_NC;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/"+serviceInstanceId+"/configurations/"+configurationId+"/" + action;
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "      \"requestDetails\": {" +
                "        \"cloudConfiguration\": {" +
                            addCloudOwnerIfNeeded() +
                "          \"lcpCloudRegionId\": \"mdt1\"" +
                "        }," +
                "        \"modelInfo\": {" +
                "          \"modelCustomizationId\": \"08a181aa-72eb-435f-9593-e88a3ad0a86b\"," +
                "          \"modelInvariantId\": \"model-invariant-id-9533\"," +
                "          \"modelVersionId\": \"model-version-id-9533\"," +
                "          \"modelType\": \"configuration\"" +
                "        }," +
                "        \"requestInfo\": {" +
                "          \"source\": \"VID\"," +
                "          \"requestorId\": \"us16807000\"" +
                "        }," +
                "        \"relatedInstanceList\": [" +
                "          {" +
                "            \"relatedInstance\": {" +
                "              \"instanceId\": \""+serviceInstanceId+"\"," +
                "              \"modelInfo\": {" +
                "                \"modelType\": \"service\"," +
                "                \"modelInvariantId\": \"b7d923c9-6175-41f1-91ba-4565c4953408\"," +
                "                \"modelName\": \"ServiceContainerMultiplepProbes\"," +
                "                \"modelVersionId\": \"ee6d61be-4841-4f98-8f23-5de9da846ca7\"," +
                "                \"modelVersion\": \"1.0\"" +
                "              }" +
                "            }" +
                "          }," +
                "          {" +
                "            \"relatedInstance\": {" +
                "              \"instanceId\": \"d35bf534-7d8e-4cb4-87f9-0a8bb6cd47b2\"," +
                "              \"instanceDirection\": \"source\"," +
                "              \"modelInfo\": {" +
                "                \"modelType\": \"connectionPoint\"" +
                "              }" +
                "            }" +
                "          }" +
                "        ]" +
                "      }" +
                "    }";
    }
}
