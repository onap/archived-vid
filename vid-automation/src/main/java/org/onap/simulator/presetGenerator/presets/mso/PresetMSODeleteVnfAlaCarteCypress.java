package org.onap.simulator.presetGenerator.presets.mso;


public class PresetMSODeleteVnfAlaCarteCypress extends PresetMSOBaseDelete {

    private final String userId;
    private final String serviceInstanceId;
    private final String vnfInstanceId;

    public PresetMSODeleteVnfAlaCarteCypress(String requestId, String serviceInstanceId, String vnfInstanceId, String userId) {
        super(requestId, vnfInstanceId);
        this.userId = userId;
        this.serviceInstanceId = serviceInstanceId;
        this.vnfInstanceId = vnfInstanceId;
    }

    @Override
    public String getReqPath() {
        return "/mso/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs/" + vnfInstanceId;
    }

    @Override
    public String getRequestBody() {
        return "{" +
                "   \"requestDetails\":{" +
                "      \"modelInfo\":{" +
                "            \"modelInvariantId\":\"72e465fe-71b1-4e7b-b5ed-9496118ff7a8\",\n" +
                "            \"modelVersionId\":\"69e09f68-8b63-4cc9-b9ff-860960b5db09\",\n" +
                "            \"modelName\":\"2017-488_PASQUALE-vPE\",\n" +
                "            \"modelVersion\":\"5.0\",\n" +
                "            \"modelType\":\"vnf\",\n" +
                "            \"modelCustomizationId\":\"1da7b585-5e61-4993-b95e-8e6606c81e45\",\n" +
                "            \"modelCustomizationName\":\"2017-488_PASQUALE-vPE 0\"" +
                "      }," +
                "      \"cloudConfiguration\":{" +
                "         \"lcpCloudRegionId\":\"some legacy region\"," +
                            addCloudOwnerIfNeeded() +
                "         \"tenantId\":\"092eb9e8e4b7412e8787dd091bc58e86\"" +
                "      }," +
                "      \"requestInfo\":{" +
                "         \"source\":\"VID\"," +
                "         \"requestorId\":\"" + userId + "\"" +
                "      }" +
                "   }" +
                "}";
    }

}
