package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateNetworkALaCarteOldViewEdit extends PresetMSOBaseCreateInstancePost {
    private final String networkInstanceName;
    private final String serviceInstanceId;
    private final String platform;

    public PresetMSOCreateNetworkALaCarteOldViewEdit(String overrideRequestId, String serviceInstanceId, String responseInstanceId, String networkInstanceName, String platform, String cloudOwner) {
        super(overrideRequestId, responseInstanceId);
        this.serviceInstanceId = serviceInstanceId;
        this.networkInstanceName = networkInstanceName;
        this.platform = platform == null ? "" : platform;
        this.cloudOwner = cloudOwner;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/networks";
    }

    @Override
    public Object getRequestBody() {
       return "{\"requestDetails\": {" +
                "        \"requestInfo\": {" +
                "          \"instanceName\": \"" + networkInstanceName + "\"," +
                "          \"source\": \"VID\"," +
                "          \"suppressRollback\": false," +
                "          \"requestorId\": \"em1536000\"," +
                "          \"productFamilyId\": \"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"" +
                "        }," +
                "        \"modelInfo\": {" +
                "          \"modelType\": \"network\"," +
                "          \"modelInvariantId\": \"de01afb5-532b-451d-aac4-ff9ff0644060\"," +
                "          \"modelVersionId\": \"ac815c68-35b7-4ea4-9d04-92d2f844b27c\"," +
                "          \"modelName\": \"AIC30_CONTRAIL_BASIC\"," +
                "          \"modelVersion\": \"3.0\"," +
                "          \"modelCustomizationId\": \"e94d61f7-b4b2-489a-a4a7-30b1a1a80daf\"," +
                "          \"modelCustomizationName\": \"AIC30_CONTRAIL_BASIC 0\"" +
                "        }," +
                "        \"requestParameters\": {" +
                "          \"testApi\": \"GR_API\"," +
                "          \"userParams\": []" +
                "        }," +
                "        \"lineOfBusiness\": {" +
                "            \"lineOfBusinessName\": \"zzz1\"" +
                "        }," +
                "        \"cloudConfiguration\": {" +
                "          \"lcpCloudRegionId\": \"One\"," +
                addCloudOwnerIfNeeded() +
                "          \"tenantId\": \"c630e297a3ae486497d63eacec1d7c14\"" +
                "        }," +
                addPlatformIfNeeded(platform) +
                "        \"relatedInstanceList\": [" +
                "          {" +
                "            \"relatedInstance\": {" +
                "              \"instanceId\": \"" + serviceInstanceId + "\"," +
                "              \"modelInfo\": {" +
                "                \"modelType\": \"service\"," +
                "                \"modelName\": \"Using VID for VoIP Network Instantiations Shani\"," +
                "                \"modelInvariantId\": \"5b9c0f33-eec1-484a-bf77-736a6644d7a8\"," +
                "                \"modelVersion\": \"1.0\"," +
                "                \"modelVersionId\": \"b75e0d22-05ff-4448-9266-5f0d4e1dbbd6\"" +
                "              }" +
                "            }" +
                "          }" +
                "        ]" +
                "      }}";
    }
}
