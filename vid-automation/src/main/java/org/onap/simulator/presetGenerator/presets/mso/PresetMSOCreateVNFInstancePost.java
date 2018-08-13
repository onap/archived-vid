package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVNFInstancePost extends PresetMSOBaseCreateServiceInstancePost {
    private String serviceInstanceId;
    private String serviceModelVersionId;

    public PresetMSOCreateVNFInstancePost(String serviceInstanceId, String serviceModelVersionId) {
        this.serviceInstanceId = serviceInstanceId;
        this.serviceModelVersionId = serviceModelVersionId;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstances/v./" + serviceInstanceId + "/vnfs";
    }

    @Override
    public Object getRequestBody() {
        return "{\"requestDetails\":{\"requestInfo\":{\"productFamilyId\":\"e433710f-9217-458d-a79d-1c7aff376d89\",\"source\":\"VID\",\"suppressRollback\":false,\"requestorId\":\"us16807000\"},\"lineOfBusiness\":{\"lineOfBusinessName\":\"ecomp\"},\"cloudConfiguration\":{\"lcpCloudRegionId\":\"AAIAIC25\",\"tenantId\":\"092eb9e8e4b7412e8787dd091bc58e86\"},\"platform\":{\"platformName\":\"plat1\"},\"modelInfo\":{\"modelCustomizationId\":\"882e5dcb-ba9f-4766-8cde-e326638107db\",\"modelCustomizationName\":\"vDBE 0\",\"modelVersionId\":\"61535073-2e50-4141-9000-f66fea69b433\",\"modelName\":\"vDBE\",\"modelInvariantId\":\"fcdf49ce-6f0b-4ca2-b676-a484e650e734\",\"modelType\":\"vnf\",\"modelVersion\":\"0.2\"},\"requestParameters\":{\"userParams\":[],\"testApi\":\"GR_API\"},\"relatedInstanceList\":[{\"relatedInstance\":{\"instanceId\":\"" + serviceInstanceId + "\",\"modelInfo\":{\"modelVersionId\":\"" + serviceModelVersionId + "\",\"modelName\":\"vDBE_srv\",\"modelInvariantId\":\"9aa04749-c02c-432d-a90c-18caa361c833\",\"modelType\":\"service\",\"modelVersion\":\"1.0\"}}},{\"relatedInstance\":{\"instanceId\":\"AAI-12002-test3-vm230w\",\"modelInfo\":{\"modelName\":\"oam_group\",\"modelType\":\"networkCollection\",\"modelVersion\":\"1\",\"modelVersionId\":\"a0efd5fc-f7be-4502-936a-a6c6392b958f\",\"modelInvariantId\":\"9384abf9-1231-4da4-bd8d-89e4d2f8a749\"}}},{\"relatedInstance\":{\"instanceId\":\"AAI-12002-test3-vm230w\",\"modelInfo\":{\"modelName\":\"oam_group\",\"modelType\":\"networkCollection\",\"modelVersion\":\"1\",\"modelVersionId\":\"a0efd5fc-f7be-4502-936a-a6c6392b958f\",\"modelInvariantId\":\"9384abf9-1231-4da4-bd8d-89e4d2f8a749\"}}}]}}";
    }
}
