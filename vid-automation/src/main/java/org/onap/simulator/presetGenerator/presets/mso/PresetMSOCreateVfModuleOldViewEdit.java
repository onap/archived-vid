package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVfModuleOldViewEdit extends PresetMSOBaseCreateInstancePost {
    private final String serviceInstanceId;
    private final String vnfInstanceId;
    private final String instanceName;

    public PresetMSOCreateVfModuleOldViewEdit(String overrideRequestId, String responseInstanceId ,
                                              String serviceInstanceId, String vnfInstanceId,
                                              String instanceName, String msoTestApi,
                                              String cloudOwner) {
        super(overrideRequestId, responseInstanceId, msoTestApi);
        this.serviceInstanceId = serviceInstanceId;
        this.vnfInstanceId = vnfInstanceId;
        this.instanceName = instanceName;
        this.cloudOwner = cloudOwner;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs/"+vnfInstanceId+"/vfModules";
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                "    \"requestInfo\": {" +
                "      \"instanceName\": \""+instanceName+"\"," +
                "      \"source\": \"VID\"," +
                "      \"suppressRollback\": false," +
                "      \"requestorId\": \"em35993000\"" +
                "    }," +
                "    \"modelInfo\": {" +
                "      \"modelType\": \"vfModule\"," +
                "      \"modelInvariantId\": \"d9f9c851-9543-476e-b3c2-a2e5284a26aa\"," +
                "      \"modelVersionId\": \"d205e01d-e5da-4e68-8c52-f95cb0607959\"," +
                "      \"modelName\": \"Vsp1710pid298109Vwinifred..mmsc_mod1_ltm..module-8\"," +
                "      \"modelVersion\": \"1\"," +
                "      \"modelCustomizationId\": \"e81b58ce-ae9b-4bde-9f81-9962a5007756\"," +
                "      \"modelCustomizationName\": \"Vsp1710pid298109Vwinifred..mmsc_mod1_ltm..module-8\"" +
                "    }," +
                "    \"requestParameters\": {" +
                addTestApi() +
                "      \"usePreload\": false" +
                "    }," +
                "    \"cloudConfiguration\": {" +
                "      \"lcpCloudRegionId\": \"mdt1\"," +
                        addCloudOwnerIfNeeded() +
                "      \"tenantId\": \"092eb9e8e4b7412e8787dd091bc58e86\"" +
                "    }," +
                "    \"relatedInstanceList\": [{" +
                "        \"relatedInstance\": {" +
                "          \"instanceId\": \""+serviceInstanceId+"\"," +
                "          \"modelInfo\": {" +
                "            \"modelType\": \"service\"," +
                "            \"modelName\": \"ServicevWINIFREDPID298109\"," +
                "            \"modelInvariantId\": \"a8dcd72d-d44d-44f2-aa85-53aa9ca0c657\"," +
                "            \"modelVersion\": \"1.0\"," +
                "            \"modelVersionId\": \"aa2f8e9c-9e47-4b15-a95c-4a93855ac61b\"" +
                "          }" +
                "        }" +
                "      }, {" +
                "        \"relatedInstance\": {" +
                "          \"instanceId\": \""+vnfInstanceId+"\"," +
                "          \"modelInfo\": {" +
                "            \"modelType\": \"vnf\"," +
                "            \"modelName\": \"VSP1710PID298109_vWINIFRED\"," +
                "            \"modelInvariantId\": \"e7961100-cde6-4b5a-bcda-b8945086950a\"," +
                "            \"modelVersion\": \"1.0\"," +
                "            \"modelVersionId\": \"959a7ba0-89ee-4984-9af6-65d5bdda4b0e\"," +
                "            \"modelCustomizationId\": \"6b8fc7dc-2db1-4283-a222-b07d10595495\"," +
                "            \"modelCustomizationName\": \"VSP1710PID298109_vWINIFRED 0\"" +
                "          }" +
                "        }" +
                "      }" +
                "    ]" +
                "  }" +
                "}";
    }
}
