package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVnfALaCarteOldViewEdit extends PresetMSOCreateVnfBase {
    private String vnfInstanceName;
    private boolean haveLOB;

    public PresetMSOCreateVnfALaCarteOldViewEdit(String overrideRequestId, String serviceInstanceId,
                                                 String vnfInstanceName, boolean haveLOB, String msoTestApi) {
        super(overrideRequestId, serviceInstanceId);
        this.vnfInstanceName = vnfInstanceName;
        this.haveLOB = haveLOB;
        this.msoTestApi = msoTestApi;
        this.withTestApi = true;
    }

    @Override
    public Object getRequestBody() {
        return  "{" +
                "      \"requestDetails\": {" +
                "        \"requestInfo\": {" +
                "          \"instanceName\": \""+vnfInstanceName+"\"," +
                "          \"source\": \"VID\"," +
                "          \"suppressRollback\": false," +
                "          \"requestorId\": \"mo37915000\"," +
                "          \"productFamilyId\": \"ebc3bc3d-62fd-4a3f-a037-f619df4ff034\"" +
                "        }," +
                "        \"modelInfo\": {" +
                "          \"modelType\": \"vnf\"," +
                "          \"modelInvariantId\": \"e7961100-cde6-4b5a-bcda-b8945086950a\"," +
                "          \"modelVersionId\": \"959a7ba0-89ee-4984-9af6-65d5bdda4b0e\"," +
                "          \"modelName\": \"VSP1710PID298109_vWINIFRED\"," +
                "          \"modelVersion\": \"1.0\"," +
                "          \"modelCustomizationId\": \"6b8fc7dc-2db1-4283-a222-b07d10595495\"," +
                "          \"modelCustomizationName\": \"VSP1710PID298109_vWINIFRED 0\"" +
                "        }," +
                "        \"requestParameters\": {" +
                addTestApi() +
                "          \"userParams\": []" +
                "        }," +
                "        \"cloudConfiguration\": {" +
                "          \"lcpCloudRegionId\": \"some legacy region\"," +
                           addCloudOwnerIfNeeded() +
                "          \"tenantId\": \"092eb9e8e4b7412e8787dd091bc58e86\"" +
                "        }," +
                selectLob("\"lineOfBusiness\": {\"lineOfBusinessName\": \"ECOMP\"},", "") +
                "        \"platform\": {" +
                "          \"platformName\": \"platform\"" +
                "        }," +
                "        \"relatedInstanceList\": [{" +
                "          \"relatedInstance\": {" +
                "            \"instanceId\": \""+serviceInstanceId+"\"," +
                "            \"modelInfo\": {" +
                "              \"modelType\": \"service\"," +
                "              \"modelName\": \"ServicevWINIFREDPID298109\"," +
                "              \"modelInvariantId\": \"a8dcd72d-d44d-44f2-aa85-53aa9ca0c657\"," +
                "              \"modelVersion\": \"1.0\"," +
                "              \"modelVersionId\": \"aa2f8e9c-9e47-4b15-a95c-4a93855ac61b\"" +
                "            }" +
                "          }" +
                "        }" +
                "        ]" +
                "      }"+
                "}";
    }

    private String selectLob(String lob, String noLob) {
        return haveLOB ? lob : noLob;
    }
}
