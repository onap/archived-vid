package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVfModuleInstancePost extends PresetMSOBaseCreateServiceInstancePost{
    private String serviceInstanceId;
    private String vnfInstanceId;

    public PresetMSOCreateVfModuleInstancePost(String serviceInstanceId, String vnfInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
        this.vnfInstanceId = vnfInstanceId;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstances/v./" + serviceInstanceId + "/vnfs/"+vnfInstanceId+"/vfModules";
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                    "\"requestDetails\":{" +
                        "\"requestInfo\":{" +
                            "\"instanceName\":\"aa\"," +
                            "\"source\":\"VID\"," +
                            "\"suppressRollback\":false," +
                            "\"requestorId\":\"us16807000\"" +
                        "}," +
                        "\"modelInfo\":{" +
                            "\"modelType\":\"vfModule\"," +
                            "\"modelInvariantId\":\"\"," +
                            "\"modelVersionId\":\"\"," +
                            "\"modelName\":\"\"," +
                            "\"modelVersion\":\"\"," +
                            "\"modelCustomizationId\":\"\"," +
                            "\"modelCustomizationName\":\"\"" +
                        "}," +
                        "\"requestParameters\":{" +
                            "\"userParams\":[" +
                            "]," +
                            "\"usePreload\":false" +
                        "}," +
                        "\"cloudConfiguration\":{" +
                            "\"lcpCloudRegionId\":\"mdt1\"," +
                            "\"tenantId\":\"092eb9e8e4b7412e8787dd091bc58e86\"" +
                        "}," +
                        "\"relatedInstanceList\":[" +
                            "{" +
                                "\"relatedInstance\":{" +
                                    "\"instanceId\":\"" + serviceInstanceId + "\","+
                                    "\"modelInfo\":{" +
                                        "\"modelType\":\"service\"," +
                                        "\"modelName\":\"Demo Service 1\"," +
                                        "\"modelInvariantId\":\"709d1be4-9a3f-4a29-8c4d-a20465e808a3\"," +
                                        "\"modelVersion\":\"1.0\"," +
                                        "\"modelVersionId\":\"240376de-870e-48df-915a-31f140eedd2c\"" +
                                    "}" +
                                "}" +
                            "}" +
                        "]" +
                    "}" +
                "}";
    }
}
