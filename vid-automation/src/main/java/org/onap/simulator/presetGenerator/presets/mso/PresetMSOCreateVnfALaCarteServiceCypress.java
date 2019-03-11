package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVnfALaCarteServiceCypress extends PresetMSOCreateVnfBase {
    protected String vnfInstanceName;
    protected String lineOfBusinessName;

    public PresetMSOCreateVnfALaCarteServiceCypress(String overrideRequestId, String serviceInstanceId, String vnfInstanceName, String lineOfBusinessName) {
        super(overrideRequestId, serviceInstanceId);
        this.vnfInstanceName = vnfInstanceName;
        this.lineOfBusinessName = lineOfBusinessName;
    }

    @Override
    public Object getRequestBody() {
        return "{\"requestDetails\":" +
                "{\"requestInfo\":" +
                    "{\"instanceName\":\""+vnfInstanceName+"\"," +
                    "\"productFamilyId\":\"36b4733a-53f4-4cc8-8ff0-9172e5fc4b8e\"," +
                    "\"source\":\"VID\"," +
                    "\"suppressRollback\":false," +
                    "\"requestorId\":\"us16807000\"}," +
                "\"lineOfBusiness\":{\"lineOfBusinessName\":\""+lineOfBusinessName+"\"}," +
                "\"cloudConfiguration\":" +
                    "{\"lcpCloudRegionId\":\"hvf6\"," +
                    addCloudOwnerIfNeeded() +
                    "\"tenantId\":\"bae71557c5bb4d5aac6743a4e5f1d054\"}," +
                "\"platform\":{\"platformName\":\"platform\"}," +
                "\"modelInfo\":" +
                    "{\"modelCustomizationId\":\"91415b44-753d-494c-926a-456a9172bbb9\"," +
                    "\"modelCustomizationName\":\"VF_vGeraldine 0\"," +
                    "\"modelVersionId\":\"d6557200-ecf2-4641-8094-5393ae3aae60\"," +
                    "\"modelName\":\"VF_vGeraldine\"," +
                    "\"modelInvariantId\":\"4160458e-f648-4b30-a176-43881ffffe9e\"," +
                    "\"modelType\":\"vnf\"," +
                    "\"modelVersion\":\"2.0\"}," +
                "\"requestParameters\":{" +
                    "\"testApi\": \"GR_API\", " +
                    "\"userParams\":[]" +
                "}," +
                "\"relatedInstanceList\":[" +
                    "{\"relatedInstance\":{" +
                        "\"instanceId\":\"f8791436-8d55-4fde-b4d5-72dd2cf13cfb\"," +
                        "\"modelInfo\":{" +
                            "\"modelVersionId\":\"6e59c5de-f052-46fa-aa7e-2fca9d674c44\"," +
                            "\"modelName\":\"ComplexService\"," +
                            "\"modelInvariantId\":\"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0\"," +
                            "\"modelType\":\"service\"," +
                            "\"modelVersion\":\"1.0\"" +
                "}}}]}}";
    }
}
