package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.aai.PresetAAIGetCloudOwnersByCloudRegionId;

public class PresetMSOCreateVNFInstanceOnlyRelatedServiceInstance extends PresetMSOBaseCreateInstancePost {
    private final String suffix;
    private String serviceInstanceId;
    private String vnfInstanceName;

    public PresetMSOCreateVNFInstanceOnlyRelatedServiceInstance(String vnfInstanceName, String vnfRequestId, String serviceInstanceId, String vnfInstanceId, int suffix) {
        super(vnfRequestId, vnfInstanceId);
        this.vnfInstanceName = vnfInstanceName;
        this.serviceInstanceId = serviceInstanceId;
        this.cloudOwner = PresetAAIGetCloudOwnersByCloudRegionId.ATT_NC;
        this.suffix = formatSuffix(suffix);
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs";
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                "    \"modelInfo\": {" +
                "      \"modelType\": \"vnf\"," +
                "      \"modelInvariantId\": \"ff5256d1-5a33-55df-13ab-12abad84e7ff\"," +
                "      \"modelVersionId\": \"fe042c22-ba82-43c6-b2f6-8f1fc4164091\"," +
                "      \"modelName\": \"vSAMP12\"," +
                "      \"modelVersion\": \"1.0\"," +
                "      \"modelCustomizationName\": \"vSAMP12 1\"," +
                "      \"modelCustomizationId\": \"a7f1d08e-b02d-11e6-80f5-76304dec7eb7\"" +
                "    }," +
                "    \"cloudConfiguration\": {" +
                "      \"lcpCloudRegionId\": \"mdt1\"," +
                       addCloudOwnerIfNeeded() +
                "      \"tenantId\": \"88a6ca3ee0394ade9403f075db23167e\"" +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"instanceName\": \"" + vnfInstanceName+suffix + "\"," +
                "      \"productFamilyId\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"," +
                "      \"source\": \"VID\"," +
                "      \"suppressRollback\": true," +
                "      \"requestorId\": \"us16807000\"" +
                "    }," +
                "    \"platform\": {" +
                "      \"platformName\": \"vnf_platformName\"" +
                "    }," +
                "    \"lineOfBusiness\": {" +
                "      \"lineOfBusinessName\": \"vnf_lineOfBusinessName\"" +
                "    }," +
                "    \"relatedInstanceList\": [" +
                "      {" +
                "        \"relatedInstance\": {" +
                "          \"instanceId\": \"" + serviceInstanceId + "\"," +
                "          \"modelInfo\": {" +
                "            \"modelInvariantId\": \"0367689e-d41e-483f-b200-eab17e4a7f8d\"," +
                "            \"modelVersionId\": \"e3c34d88-a216-4f1d-a782-9af9f9588705\"," +
                "            \"modelName\": \"gayawabawe\"," +
                "            \"modelVersion\": \"5.1\"" +
                "          }" +
                "        }" +
                "      }" +
                "    ]," +
                "    \"requestParameters\": {" +
                "      \"userParams\": []," +
                "      \"testApi\": \"GR_API\"" +
                "    }" +
                "  }" +
                "}";
    }
}
