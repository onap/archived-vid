package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateNetworkALaCarteServiceCypress2 extends PresetMSOBaseCreateInstancePost {
    private String serviceInstanceId;
    private String networkName;
    private String serviceModelName;
    private String serviceModelVersionId;
    private String platformName;


    public PresetMSOCreateNetworkALaCarteServiceCypress2(String overrideRequestId, String serviceInstanceId, String networkName) {
        this(overrideRequestId, serviceInstanceId, networkName, "ComplexService", "6e59c5de-f052-46fa-aa7e-2fca9d674c44",
            "xxx1");
    }

    public PresetMSOCreateNetworkALaCarteServiceCypress2(String overrideRequestId, String serviceInstanceId,
        String networkName, String serviceModelName, String serviceModelVersionId, String platformName) {
        super(overrideRequestId);
        this.serviceInstanceId = serviceInstanceId;
        this.networkName = networkName;
        this.serviceModelName = serviceModelName;
        this.serviceModelVersionId = serviceModelVersionId;
        this.platformName = platformName;
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
        return "{\"requestDetails\":" +
                "{\"requestInfo\":" +
                    "{\"instanceName\":\"" + networkName + "\"," +
                    "\"productFamilyId\":\"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"," +
                    "\"source\":\"VID\"," +
                    "\"suppressRollback\":false," +
                    "\"requestorId\":\"us16807000\"}," +
                "\"lineOfBusiness\":{\"lineOfBusinessName\":\"zzz1\"}," +
                "\"cloudConfiguration\":" +
                    "{\"lcpCloudRegionId\":\"hvf6\"," +
                    addCloudOwnerIfNeeded() +
                    "\"tenantId\":\"229bcdc6eaeb4ca59d55221141d01f8e\"}," +
            "\"platform\":{\"platformName\":\"" + platformName + "\"}," +
                "\"modelInfo\":" +
                    "{\"modelCustomizationId\":\"94fdd893-4a36-4d70-b16a-ec29c54c184f\"," +
                    "\"modelCustomizationName\":\"ExtVL 0\"," +
                    "\"modelVersionId\":\"ddc3f20c-08b5-40fd-af72-c6d14636b986\"," +
                    "\"modelName\":\"ExtVL\"," +
                    "\"modelInvariantId\":\"379f816b-a7aa-422f-be30-17114ff50b7c\"," +
                    "\"modelType\":\"network\"," +
                    "\"modelVersion\":\"37.0\"}," +
                "\"requestParameters\":{" +
                    "\"testApi\": \"GR_API\", " +
                    "\"userParams\":[]}," +
                    "\"relatedInstanceList\":[" +
                    "{\"relatedInstance\":{" +
                        "\"instanceId\":\"" + serviceInstanceId + "\"," +
                        "\"modelInfo\":{" +
                            "\"modelVersionId\":\"" + serviceModelVersionId + "\"," +
                            "\"modelName\":\"" + serviceModelName + "\"," +
                            "\"modelInvariantId\":\"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0\"," +
                            "\"modelType\":\"service\"," +
                            "\"modelVersion\":\"1.0\"}}}]}}";
    }
}
