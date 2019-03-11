package org.onap.simulator.presetGenerator.presets.mso;


public class PresetMSODeleteNetworkAlaCarteCypress extends PresetMSOBaseDelete {

    private final String userId;
    private final String serviceInstanceId;
    private final String networkInstanceId;

    public PresetMSODeleteNetworkAlaCarteCypress(String requestId, String serviceInstanceId, String networkInstanceId, String userId) {
        super(requestId, networkInstanceId);
        this.userId = userId;
        this.serviceInstanceId = serviceInstanceId;
        this.networkInstanceId = networkInstanceId;
    }

    @Override
    public String getReqPath() {
        return "/mso/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/networks/" + networkInstanceId;
    }
//
//    @Override
//    public Map<String, String> getRequestHeaders() {
//        Map<String, String> map = super.getRequestHeaders();
//        map.put("X-RequestorID", userId);
//        return map;
//
//    }

    @Override
    public String getRequestBody() {
        return "{" +
                "   \"requestDetails\":{" +
                "      \"modelInfo\":{" +
                "            \"modelInvariantId\":\"379f816b-a7aa-422f-be30-17114ff50b7c\",\n" +
                "            \"modelVersionId\":\"ddc3f20c-08b5-40fd-af72-c6d14636b986\",\n" +
                "            \"modelName\":\"ExtVL\",\n" +
                "            \"modelVersion\":\"37.0\",\n" +
                "            \"modelType\":\"network\",\n" +
                "            \"modelCustomizationId\":\"94fdd893-4a36-4d70-b16a-ec29c54c184f\",\n" +
                "            \"modelCustomizationName\":\"ExtVL 0\"" +
                "      }," +
                "      \"cloudConfiguration\":{" +
                "         \"lcpCloudRegionId\":\"hvf6\"," +
                            addCloudOwnerIfNeeded() +
                "         \"tenantId\":\"229bcdc6eaeb4ca59d55221141d01f8e\"" +
                "      }," +
                "      \"requestInfo\":{" +
                "         \"source\":\"VID\"," +
                "         \"requestorId\":\"" + userId + "\"" +
                "      }" +
                "   }" +
                "}";
    }

}
