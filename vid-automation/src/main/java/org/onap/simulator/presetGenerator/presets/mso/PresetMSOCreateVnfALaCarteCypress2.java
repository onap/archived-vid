package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVnfALaCarteCypress2 extends PresetMSOCreateVnfBase {

    private String vnfInstanceName;
    private final String lcpCloudRegionId;
    private final String tenantId;

    public PresetMSOCreateVnfALaCarteCypress2(String overrideRequestId, String serviceInstanceId, String responseInstanceId, String vnfInstanceName, String testApi, boolean withTestApi) {
        super(overrideRequestId, serviceInstanceId, responseInstanceId);
        this.vnfInstanceName = vnfInstanceName;
        this.msoTestApi = testApi;
        this.withTestApi = withTestApi;
        lcpCloudRegionId = "just another region";
        tenantId = "092eb9e8e4b7412e8787dd091bc58e86";
    }

    public String getLcpCloudRegionId() {
        return lcpCloudRegionId;
    }

    public String getTenantId() {
        return tenantId;
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
                "\"lineOfBusiness\":{\"lineOfBusinessName\":\"zzz1\"}," +
                "\"cloudConfiguration\":" +
            "{\"lcpCloudRegionId\":\"" + getLcpCloudRegionId() + "\"," +
                    addCloudOwnerIfNeeded() +
            "\"tenantId\":\"" + getTenantId() + "\"}," +
                "\"platform\":{\"platformName\":\"xxx1,platform\"}," +
                "\"modelInfo\":" +
                    "{\"modelCustomizationId\":\"1da7b585-5e61-4993-b95e-8e6606c81e45\"," +
                    "\"modelCustomizationName\":\"2017-488_PASQUALE-vPE 0\"," +
                    "\"modelVersionId\":\"69e09f68-8b63-4cc9-b9ff-860960b5db09\"," +
                    "\"modelName\":\"2017-488_PASQUALE-vPE\"," +
                    "\"modelInvariantId\":\"72e465fe-71b1-4e7b-b5ed-9496118ff7a8\"," +
                    "\"modelType\":\"vnf\"," +
                    "\"modelVersion\":\"5.0\"}," +
                "\"requestParameters\":{" +
                addTestApi()+
                    "\"userParams\":[]}, " +
                    "\"relatedInstanceList\":[" +
                        "{\"relatedInstance\":{" +
                            "\"instanceId\":\""+serviceInstanceId+"\"," +
                            "\"modelInfo\":{" +
                                "\"modelVersionId\":\"2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd\"," +
                                "\"modelName\":\"action-data\"," +
                                "\"modelInvariantId\":\"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0\"," +
                                "\"modelType\":\"service\"," +
                                "\"modelVersion\":\"1.0\"" +
                    "}}}]" +
                "}}";
    }
}
