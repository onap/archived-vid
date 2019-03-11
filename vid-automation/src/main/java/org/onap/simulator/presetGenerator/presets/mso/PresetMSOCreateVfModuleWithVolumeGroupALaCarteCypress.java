package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress extends PresetMSOBaseCreateInstancePost {
    private final String serviceInstanceId;
    private final String vnfInstanceId;
    private final String volumeGroupInstanceId;
    private final boolean isVolumeGroupPreset;
    private PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress(Boolean isVolumeGroupPreset, String requestId, String serviceInstanceId, String vnfInstanceId, String volumeGroupInstanceId, String testApi, boolean withTestApi) {
        super(requestId, isVolumeGroupPreset ? volumeGroupInstanceId : DEFAULT_INSTANCE_ID, testApi, withTestApi);
        this.serviceInstanceId = serviceInstanceId;
        this.vnfInstanceId = vnfInstanceId;
        this.isVolumeGroupPreset = isVolumeGroupPreset;
        this.volumeGroupInstanceId = volumeGroupInstanceId;
    }

    public static PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress forVfModule(String requestId, String serviceInstanceId, String vnfInstanceId, String volumeGroupInstanceId, String testApi, boolean withTestApi) {
        return new PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress(false, requestId, serviceInstanceId, vnfInstanceId, volumeGroupInstanceId, testApi, withTestApi);
    }

    public static PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress forVolumeGroup(String requestId, String serviceInstanceId, String vnfInstanceId, String testApi, boolean withTestApi) {
        return new PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress(true, requestId, serviceInstanceId, vnfInstanceId, requestId,testApi, withTestApi);
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public String getReqPath() {
        if (isVolumeGroupPreset) {
            return getRootPath() + "/serviceInstances/v./" + serviceInstanceId + "/vnfs/" + vnfInstanceId + "/volumeGroups";
        } else {
            return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs/" + vnfInstanceId + "/vfModules";
        }
    }

    @Override
    public Object getRequestBody() {
        return "" +
                "{" +
                "  \"requestDetails\":{" +
                "    \"modelInfo\":{" +
                "      \"modelType\":\"" + select("volumeGroup", "vfModule") + "\"," +
                "      \"modelInvariantId\":\"7253ff5c-97f0-4b8b-937c-77aeb4d79aa1\"," +
                "      \"modelVersionId\":\"25284168-24bb-4698-8cb4-3f509146eca5\"," +
                "      \"modelName\":\"2017488PASQUALEVpe..PASQUALE_vRE_BV..module-1\"," +
                "      \"modelVersion\":\"6\"," +
                "      \"modelCustomizationId\":\"f7e7c365-60cf-49a9-9ebf-a1aa11b9d401\"," +
                "      \"modelCustomizationName\":\"2017488PASQUALEVpe..PASQUALE_vRE_BV..module-1\"" +
                "    }," +
                "    \"cloudConfiguration\":{" +
                "      \"lcpCloudRegionId\":\"my region\"," +
                       addCloudOwnerIfNeeded() +
                "      \"tenantId\":\"092eb9e8e4b7412e8787dd091bc58e86\"" +
                "    }," +
                "    \"requestInfo\":{" +
                "      \"instanceName\":\"" + select("puwesovabe_vol", "puwesovabe") + "\"," +
                "      \"source\":\"VID\"," +
                "      \"suppressRollback\":false," +
                "      \"requestorId\":\"us16807000\"" +
                "    }," +
                "    \"relatedInstanceList\":[{" +
                "        \"relatedInstance\":{" +
                "          \"instanceId\":\"" + serviceInstanceId + "\"," +
                "          \"modelInfo\":{" +
                "            \"modelType\":\"service\"," +
                "            \"modelName\":\"action-data\"," +
                "            \"modelInvariantId\":\"e49fbd11-e60c-4a8e-b4bf-30fbe8f4fcc0\"," +
                "            \"modelVersion\":\"1.0\"," +
                "            \"modelVersionId\":\"2f80c596-27e5-4ca9-b5bb-e03a7fd4c0fd\"" +
                "          }" +
                "        }" +
                "     }, {" +
                "        \"relatedInstance\":{" +
                "          \"instanceId\":\"" + vnfInstanceId + "\"," +
                "          \"modelInfo\":{" +
                "            \"modelType\":\"vnf\"," +
                "            \"modelName\":\"2017-488_PASQUALE-vPE\"," +
                "            \"modelInvariantId\":\"72e465fe-71b1-4e7b-b5ed-9496118ff7a8\"," +
                "            \"modelVersion\":\"5.0\"," +
                "            \"modelVersionId\":\"69e09f68-8b63-4cc9-b9ff-860960b5db09\"," +
                "            \"modelCustomizationId\":\"1da7b585-5e61-4993-b95e-8e6606c81e45\"," +
                "            \"modelCustomizationName\":\"2017-488_PASQUALE-vPE 0\"" +
                "          }" +
                "        }" +
                select("", "      }, {" +
                "        \"relatedInstance\":{" +
                "          \"modelInfo\":{" +
                "            \"modelType\":\"volumeGroup\"" +
                "          }," +
                "          \"instanceId\":\"" + volumeGroupInstanceId + "\"," +
                "          \"instanceName\":\"puwesovabe_vol\"" +
                "        } ") +
                "      }" +
                "    ]," +
                "    \"requestParameters\":{" +
               addTestApi()+
                "       \"userParams\":[{" +
                "          \"2017488_PASQUALEvpe0_vnf_instance_name\":\"mtnj309me6\"," +
                "          \"2017488_PASQUALEvpe0_vnf_config_template_version\":\"17.2\"," +
                "          \"PASQUALEvpe0_bandwidth\":\"10\"," +
                "          \"2017488_PASQUALEvpe0_AIC_CLLI\":\"ATLMY8GA\"," +
                "          \"PASQUALEvpe0_bandwidth_units\":\"Gbps\"" +
                "        }" +
                "      ]," +
                "      \"usePreload\":true" +
                "    }" +
                "  }" +
                "}";
    }

    private String select(String ofVolumeGroup, String ofVfModule) {
        return isVolumeGroupPreset ? ofVolumeGroup : ofVfModule;
    }
}
