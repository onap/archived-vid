package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVolumeGroupOldViewEdit extends PresetMSOBaseCreateInstancePost {
    private final String serviceInstanceId;
    private final String vnfInstanceId;
    private final String instanceName;

    public PresetMSOCreateVolumeGroupOldViewEdit(String overrideRequestId, String responseInstanceId ,
                                                 String serviceInstanceId, String vnfInstanceId,
                                                 String instanceName, String msoTestApi) {
        super(overrideRequestId, responseInstanceId, msoTestApi);
        this.serviceInstanceId = serviceInstanceId;
        this.vnfInstanceId = vnfInstanceId;
        this.instanceName = instanceName;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs/"+vnfInstanceId+"/volumeGroups";
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
                "      \"modelType\": \"volumeGroup\"," +
                "      \"modelInvariantId\": \"6931e88a-fbcc-4ca9-8583-876b669c3106\"," +
                "      \"modelVersionId\": \"13f022c4-651e-4326-b8e1-61e9a8c7a7ad\"," +
                "      \"modelName\": \"Vsp1710pid298109Vwinifred..mmsc_mod6_eca_oam..module-3\"," +
                "      \"modelVersion\": \"1\"," +
                "      \"modelCustomizationId\": \"020af091-cc66-46db-876c-02f14b4a795f\"," +
                "      \"modelCustomizationName\": \"Vsp1710pid298109Vwinifred..mmsc_mod6_eca_oam..module-3\"" +
                "    }," +
                "    \"requestParameters\": {" +
                        addTestApi() +
                "    }," +
                "    \"cloudConfiguration\": {" +
                "      \"lcpCloudRegionId\": \"some legacy region\"," +
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
