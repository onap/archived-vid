package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateNetworkALaCarteCypress extends PresetMSOBaseCreateInstancePost {
    private final String networkInstanceName;
    private final String serviceInstanceId;


    public PresetMSOCreateNetworkALaCarteCypress(String overrideRequestId, String serviceInstanceId, String responseInstanceId, String networkInstanceName, String msoTestApi, boolean withTestApi) {
        super(overrideRequestId, responseInstanceId, msoTestApi, withTestApi);
        this.serviceInstanceId = serviceInstanceId;
        this.networkInstanceName = networkInstanceName;
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
        return "{\"requestDetails\": {" +

                "\"modelInfo\":{" +
                    "\"modelCustomizationId\":\"94fdd893-4a36-4d70-b16a-ec29c54c184f\"," +
                    "\"modelCustomizationName\":\"ExtVL 0\"," +
                    "\"modelInvariantId\":\"379f816b-a7aa-422f-be30-17114ff50b7c\"," +
                    "\"modelName\":\"ExtVL\"," +
                    "\"modelVersion\":\"37.0\"," +
                    "\"modelVersionId\":\"ddc3f20c-08b5-40fd-af72-c6d14636b986\"," +
                    "\"modelType\":\"network\"" +
                "}," +
                "\"cloudConfiguration\":" +
                    "{\"lcpCloudRegionId\":\"lcpRegionText\"," +
                    addCloudOwnerIfNeeded() +
                    "\"tenantId\":\"092eb9e8e4b7412e8787dd091bc58e86\"}," +
                "\"requestInfo\":" +
                    "{\"instanceName\":\""+ networkInstanceName +"\"," +
                    "\"productFamilyId\":\"ebc3bc3d-62fd-4a3f-a037-f619df4ff034\"," +
                    "\"source\":\"VID\"," +
                    "\"suppressRollback\":false," +
                    "\"requestorId\":\"us16807000\"}," +
                //not sure it should be here
                "\"platform\":{\"platformName\":\"xxx1\"}," +
                //not sure it should be here
                "\"lineOfBusiness\":{\"lineOfBusinessName\":\"zzz1\"}," +

                "\"relatedInstanceList\":[" +
                    "{\"relatedInstance\":{" +
                        "\"instanceId\":\""+serviceInstanceId+"\"," +
                        "\"modelInfo\":{" +
                            "\"modelVersionId\":\"2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd\"," +
                            "\"modelName\":\"action-data\"," +
                            "\"modelInvariantId\":\"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0\"," +
                            "\"modelType\":\"service\"," +
                            "\"modelVersion\":\"1.0\"" +
                "}}}]," +

                "\"requestParameters\":{" +
                this.addTestApi() +
                    "\"userParams\":[]}" +
                "}}";
    }
}
