package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVnfALaCarteServiceCypress2 extends PresetMSOCreateVnfALaCarteServiceCypress {

    public PresetMSOCreateVnfALaCarteServiceCypress2(String overrideRequestId, String serviceInstanceId, String vnfInstanceName, String lineOfBusinessName) {
        super(overrideRequestId, serviceInstanceId, vnfInstanceName, lineOfBusinessName);
    }

    @Override
    public Object getRequestBody() {
        return "{\"requestDetails\":" +
                "{\"requestInfo\":" +
                    "{\"instanceName\":\""+vnfInstanceName+"\"," +
                    "\"productFamilyId\":\"a9a77d5a-123e-4ca2-9eb9-0b015d2ee0fb\"," +
                    "\"source\":\"VID\"," +
                    "\"suppressRollback\":false," +
                    "\"requestorId\":\"us16807000\"}," +
                "\"lineOfBusiness\":{\"lineOfBusinessName\":\""+lineOfBusinessName+"\"}," +
                "\"cloudConfiguration\":" +
                    "{\"lcpCloudRegionId\":\"hvf6\"," +
                    addCloudOwnerIfNeeded() +
                    "\"tenantId\":\"229bcdc6eaeb4ca59d55221141d01f8e\"}," +
                "\"platform\":{\"platformName\":\"xxx1\"}," +
                "\"modelInfo\":" +
                    "{\"modelCustomizationId\":\"b3c76f73-eeb5-4fb6-9d31-72a889f1811c\"," +
                    "\"modelCustomizationName\":\"2017-388_PASQUALE-vPE 0\"," +
                    "\"modelVersionId\":\"afacccf6-397d-45d6-b5ae-94c39734b168\"," +
                    "\"modelName\":\"2017-388_PASQUALE-vPE\"," +
                    "\"modelInvariantId\":\"72e465fe-71b1-4e7b-b5ed-9496118ff7a8\"," +
                    "\"modelType\":\"vnf\"," +
                    "\"modelVersion\":\"4.0\"}," +
                "\"requestParameters\":{" +
                    "\"testApi\": \"GR_API\", " +
                    "\"userParams\":[]" +
                "}," +
                "\"relatedInstanceList\":[" +
                    "{\"relatedInstance\":{" +
                        "\"instanceId\":\"f8791436-8d55-4fde-b4d5-72dd2cf13cfb\"," +
                        "\"modelInfo\":{" +
                            "\"modelVersionId\":\"6b528779-44a3-4472-bdff-9cd15ec93450\"," +
                            "\"modelName\":\"action-data\"," +
                            "\"modelInvariantId\":\"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0\"," +
                            "\"modelType\":\"service\"," +
                            "\"modelVersion\":\"1.0\"" +
                "}}}]}}";
    }
}
