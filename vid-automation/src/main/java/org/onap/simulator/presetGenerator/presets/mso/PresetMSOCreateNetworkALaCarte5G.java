package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;

public class PresetMSOCreateNetworkALaCarte5G extends PresetMSOBaseCreateInstancePost {
    private String serviceInstanceId;
    private String networkName;
    private final String requestorId;


    public PresetMSOCreateNetworkALaCarte5G(String overrideRequestId, String serviceInstanceId, String networkName, String requestorId) {
        super(overrideRequestId);
        this.serviceInstanceId = serviceInstanceId;
        this.networkName = networkName;
        this.requestorId = requestorId;
        this.cloudOwner = PresetAAIGetCloudOwnersByCloudRegionId.ATT_AIC;
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/networks";
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                "    \"modelInfo\": {" +
                "      \"modelCustomizationName\": \"SR-IOV Provider-1\"," +
                "      \"modelCustomizationId\": \"0a0287b1-74a3-4c40-9bbb-9f56601e4fc4\"," +
                "      \"modelInvariantId\": \"379f816b-a7aa-422f-be30-17114ff50b7c\"," +
                "      \"modelVersionId\": \"840ffc47-e4cf-46de-8e23-525fd8c6fdc3\"," +
                "      \"modelName\": \"ExtVL\"," +
                "      \"modelType\": \"network\"," +
                "      \"modelVersion\": \"49.0\"" +
                "    }," +
                "    \"cloudConfiguration\": {" +
                "      \"lcpCloudRegionId\": \""+PresetAAIGetCloudOwnersByCloudRegionId.SOME_LEGACY_REGION+"\"," +
                        addCloudOwnerIfNeeded() +
                "      \"tenantId\": \"092eb9e8e4b7412e8787dd091bc58e86\"," +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"instanceName\": \""+networkName+"\"," +
                "      \"source\": \"VID\"," +
                "      \"suppressRollback\": false," +
                "      \"requestorId\": \""+requestorId+"\"" +
                "    }," +
                "    \"platform\": {" +
                "      \"platformName\": \"platform\"" +
                "    }," +
                "    \"lineOfBusiness\": {" +
                "      \"lineOfBusinessName\": \"ONAP\"" +
                "    }," +
                "    \"relatedInstanceList\": [{" +
                "        \"relatedInstance\": {" +
                "          \"modelInfo\": {" +
                "            \"modelInvariantId\": \"16e56d12-40b3-4db1-a40e-d48c36679e2e\"," +
                "            \"modelVersionId\": \"4659e8bd-0920-4eed-8ec5-550b4c8dceeb\"," +
                "            \"modelName\": \"SR-IOV Provider-1\"," +
                "            \"modelType\": \"service\"," +
                "            \"modelVersion\": \"1.0\"" +
                "          }," +
                "          \"instanceId\": \""+serviceInstanceId+"\"" +
                "        }" +
                "      }" +
                "    ]," +
                "    \"requestParameters\": {" +
                "      \"testApi\": \"GR_API\"," +
                "      \"userParams\": []" +
                "    }" +
                "  }" +
                "}";
    }
}
