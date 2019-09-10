package org.onap.simulator.presetGenerator.presets.mso.configuration;

import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;

public class PresetMSOActOnConfiguration extends PresetMSOBaseCreateInstancePost {

    private final String action;

    public PresetMSOActOnConfiguration(String action) {
        this.action = action;
        this.cloudOwner = "att-nc";
    }

    public PresetMSOActOnConfiguration(String action, String requestId, String responseInstanceId) {
        super(requestId, responseInstanceId);
        this.action = action;
        this.cloudOwner = "att-nc";
    }


    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/c187e9fe-40c3-4862-b73e-84ff056205f6/configurations/9533-config-LB1113/" + action;
    }

    @Override
    public Object getRequestBody() {
        return "" +
                "{" +
                "  \"requestDetails\": {" +
                "    \"modelInfo\": {" +
                "      \"modelType\": \"configuration\"," +
                "      \"modelInvariantId\": \"model-invariant-id-9533\"," +
                "      \"modelVersionId\": \"model-version-id-9533\"," +
                "      \"modelCustomizationId\": \"08a181aa-72eb-435f-9593-e88a3ad0a86b\"" +
                "    }," +
                "    \"cloudConfiguration\": {" +
                addCloudOwnerIfNeeded() +
                "      \"lcpCloudRegionId\": \"mdt1\"," +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"source\": \"VID\"," +
                "      \"requestorId\": \"us16807000\"" +
                "    }," +
                "    \"relatedInstanceList\": [{" +
                "        \"relatedInstance\": {" +
                "          \"instanceId\": \"c187e9fe-40c3-4862-b73e-84ff056205f6\"," +
                "          \"modelInfo\": {" +
                "            \"modelType\": \"service\"," +
                "            \"modelInvariantId\": \"b7d923c9-6175-41f1-91ba-4565c4953408\"," +
                "            \"modelVersionId\": \"ee6d61be-4841-4f98-8f23-5de9da846ca7\"," +
                "            \"modelName\": \"ServiceContainerMultiplepProbes\"," +
                "            \"modelVersion\": \"1.0\"" +
                "          }" +
                "        }" +
                "      }" +
                "    ]," +
                "    \"requestParameters\": {" +
                "      \"userParams\": []" +
                "    }" +
                "  }" +
                "}";
    }

}
