package org.onap.simulator.presetGenerator.presets.mso.configuration;

import org.onap.simulator.presetGenerator.presets.mso.PresetMSOBaseCreateInstancePost;

public class PresetMSOCreateConfiguration extends PresetMSOBaseCreateInstancePost {

    protected final String serviceInstanceId;
    protected final boolean isError;
    private final int errorCode;
    private final String errorPayload;

    public PresetMSOCreateConfiguration(String serviceInstanceId) {
        super("b6dc9806-b094-42f7-9386-a48de8218ce8", "f36f5734-e9df-4fbf-9f35-61be13f028a1");
        this.serviceInstanceId = serviceInstanceId;
        this.cloudOwner = "irma-aic";
        this.isError = false;
        this.errorCode = 0;
        this.errorPayload = null;
    }

    public PresetMSOCreateConfiguration(String serviceInstanceId, int errorCode, String errorPayload) {
        super("b6dc9806-b094-42f7-9386-a48de8218ce8", "f36f5734-e9df-4fbf-9f35-61be13f028a1");
        this.serviceInstanceId = serviceInstanceId;
        this.cloudOwner = "irma-aic";
        this.isError = true;
        this.errorCode = errorCode;
        this.errorPayload = errorPayload;
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public int getResponseCode() {
        return isError ? errorCode : super.getResponseCode();
    }

    @Override
    public Object getResponseBody() {
        return isError ? errorPayload : super.getResponseBody();
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + this.serviceInstanceId + "/configurations";
    }

    @Override
    public Object getRequestBody() {
        return "" +
                "{" +
                " \"requestDetails\": {" +
                "    \"modelInfo\": {" +
                "      \"modelType\": \"configuration\"," +
                "      \"modelInvariantId\": \"c30a024e-a6c6-4670-b73c-3df64eb57ff6\"," +
                "      \"modelVersionId\": \"f58d039d-4cfc-40ec-bd75-1f05f0458a6c\"," +
                "      \"modelName\": \"Port Mirroring Configuration By Policy\"," +
                "      \"modelVersion\": \"1.0\"," +
                "      \"modelCustomizationId\": \"4b7ebace-bad6-4526-9be6-bf248e20fc5f\"," +
                "      \"modelCustomizationName\": \"Port Mirroring Configuration By Policy 1\"" +
                "    }," +
                "    \"cloudConfiguration\": {" +
                addCloudOwnerIfNeeded() +
                "      \"lcpCloudRegionId\": \"AAIAIC25\"" +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"instanceName\": \"dummy_instance\"," +
                "      \"source\": \"VID\"," +
                "      \"requestorId\": \"us16807000\"" +
                "    }," +
                "    \"relatedInstanceList\": [" +
                "      {" +
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
                "      }," +
                "      {" +
                "        \"relatedInstance\": {" +
                "          \"instanceId\": \"9be14a4f-7367-4cf9-96a1-f08f10f485a7\"," +
                "          \"instanceDirection\": \"source\"," +
                "          \"modelInfo\": {" +
                "            \"modelType\": \"vnf\"," +
                "            \"modelInvariantId\": \"51f2c559-1aba-4fd4-bbf9-8cbbef85ff2a\"," +
                "            \"modelVersionId\": \"cb05b259-9f26-4b33-b96c-13c2c202c091\"," +
                "            \"modelName\": \"vf_vEPDG\"," +
                "            \"modelVersion\": \"2.0\"," +
                "            \"modelCustomizationId\": \"35aeaae9-74f7-4b6a-adda-65edb0110361\"" +
                "          }" +
                "        }" +
                "      }," +
                "      {" +
                "        \"relatedInstance\": {" +
                "          \"instanceName\": \"AS-pnf2-10219--as988q\"," +
                "          \"instanceDirection\": \"destination\"," +
                "          \"modelInfo\": {" +
                "            \"modelType\": \"pnf\"" +
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
