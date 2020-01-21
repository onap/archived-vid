package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVnfVlanTagging extends PresetMSOBaseCreateInstancePost {
    private final boolean ecompNamingEnabled;
    private String serviceInstanceId;
    private String serviceModelVersionId;

    public PresetMSOCreateVnfVlanTagging(String serviceInstanceId, String serviceModelVersionId, boolean ecompNamingEnabled) {
        this.serviceInstanceId = serviceInstanceId;
        this.serviceModelVersionId = serviceModelVersionId;
        this.ecompNamingEnabled = ecompNamingEnabled;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs";
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                "    \"requestInfo\": {" +
                "      \"productFamilyId\": \"e433710f-9217-458d-a79d-1c7aff376d89\"," +
                "      \"source\": \"VID\"," +
                "      \"suppressRollback\": false," +
                "      \"instanceName\" : \"NewName\"," +
//                        addInstanceNameIfNeeded()+
                "      \"requestorId\": \"us16807000\"" +
                "    }," +
                "    \"lineOfBusiness\": {" +
                "      \"lineOfBusinessName\": \"ONAP\"" +
                "    }," +
                "    \"cloudConfiguration\": {" +
                "      \"lcpCloudRegionId\": \"AAIAIC25\"," +
                        addCloudOwnerIfNeeded() +
                "      \"tenantId\": \"092eb9e8e4b7412e8787dd091bc58e86\"" +
                "    }," +
                "    \"platform\": {" +
                "      \"platformName\": \"xxx1\"" +
                "    }," +
                "    \"modelInfo\": {" +
                "      \"modelCustomizationId\": \"882e5dcb-ba9f-4766-8cde-e326638107db\"," +
                "      \"modelCustomizationName\": \"vDOROTHEA 0\"," +
                "      \"modelVersionId\": \"61535073-2e50-4141-9000-f66fea69b433\"," +
                "      \"modelName\": \"vDOROTHEA\"," +
                "      \"modelInvariantId\": \"fcdf49ce-6f0b-4ca2-b676-a484e650e734\"," +
                "      \"modelType\": \"vnf\"," +
                "      \"modelVersion\": \"0.2\"" +
                "    }," +
                "    \"requestParameters\": {" +
                "      \"userParams\": []," +
                "      \"testApi\": \"GR_API\"" +
                "    }," +
                "    \"relatedInstanceList\": [{" +
                "        \"relatedInstance\": {" +
                "          \"instanceId\": \""+ serviceInstanceId +"\"," +
                "          \"modelInfo\": {" +
                "            \"modelVersionId\": \""+ serviceModelVersionId +"\"," +
                "            \"modelName\": \"vDOROTHEA_srv\"," +
                "            \"modelInvariantId\": \"9aa04749-c02c-432d-a90c-18caa361c833\"," +
                "            \"modelType\": \"service\"," +
                "            \"modelVersion\": \"1.0\"" +
                "          }" +
                "        }" +
                "      }, {" +
                "        \"relatedInstance\": {" +
                "          \"instanceId\": \"AAI-12002-test3-vm230w\"," +
                "          \"modelInfo\": {" +
                "            \"modelType\": \"networkInstanceGroup\"" +
                "          }" +
                "        }" +
                "      }, {" +
                "        \"relatedInstance\": {" +
                "          \"instanceId\": \"AAI-12002-test3-vm230w\"," +
                "          \"modelInfo\": {" +
                "            \"modelType\": \"networkInstanceGroup\"" +
                "          }" +
                "        }" +
                "      }" +
                "    ]" +
                "  }" +
                "}";
    }

    private String addInstanceNameIfNeeded() {
        return ecompNamingEnabled ? "": "\"instanceName\" : \"NewName\",";
    }
}
