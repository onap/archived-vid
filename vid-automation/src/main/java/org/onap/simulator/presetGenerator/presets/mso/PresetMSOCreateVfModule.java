package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVfModule extends PresetMSOBaseCreateInstancePost {
    private String serviceInstanceId;
    private String vnfInstanceId;
    private String instanceName;
    private String modelVersionId;
    private String modelInvariantId;
    private String serviceName;

    public PresetMSOCreateVfModule(String serviceInstanceId, String vnfInstanceId, String cloudOwner) {
        this(serviceInstanceId, vnfInstanceId, cloudOwner, "aa", "240376de-870e-48df-915a-31f140eedd2c",
                "709d1be4-9a3f-4a29-8c4d-a20465e808a3", "Demo Service 1");
    }

    public PresetMSOCreateVfModule(String serviceInstanceId, String vnfInstanceId,
                                   String cloudOwner, String instanceName, String modelVersionId, String modelInvariantId, String serviceName) {
        this.serviceInstanceId = serviceInstanceId;
        this.vnfInstanceId = vnfInstanceId;
        this.cloudOwner = cloudOwner;
        this.instanceName = instanceName;
        this.modelVersionId = modelVersionId;
        this.modelInvariantId = modelInvariantId;
        this.serviceName = serviceName;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs/"+vnfInstanceId+"/vfModules";
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                    "\"requestDetails\":{" +
                        "\"requestInfo\":{" +
                            "\"instanceName\":\"" + instanceName + "\"," +
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
                            "\"lcpCloudRegionId\":\"hvf6\"," +
                            addCloudOwnerIfNeeded() +
                            "\"tenantId\":\"092eb9e8e4b7412e8787dd091bc58e86\"" +
                        "}," +
                        "\"relatedInstanceList\":[" +
                            "{" +
                                "\"relatedInstance\":{" +
                                    "\"instanceId\":\"" + serviceInstanceId + "\","+
                                    "\"modelInfo\":{" +
                                        "\"modelType\":\"service\"," +
                                        "\"modelName\":\"" + serviceName + "\","+
                                        "\"modelInvariantId\":\"" + modelInvariantId + "\","+
                                        "\"modelVersion\":\"1.0\"," +
                                        "\"modelVersionId\":\"" + modelVersionId + "\"" +
                                    "}" +
                                "}" +
                            "}" +
                        "]" +
                    "}" +
                "}";
    }
}
