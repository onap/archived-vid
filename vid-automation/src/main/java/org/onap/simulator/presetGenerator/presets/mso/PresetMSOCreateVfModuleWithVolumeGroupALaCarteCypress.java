package org.onap.simulator.presetGenerator.presets.mso;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

public class PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress extends PresetMSOBaseCreateInstancePost {
    private final String serviceInstanceId;
    private final String vnfInstanceId;
    private final String volumeGroupInstanceId;
    private final boolean isVolumeGroupPreset;
    private final String lcpCloudRegionId;

    private PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress(Boolean isVolumeGroupPreset,
        String requestId,
        String serviceInstanceId,
        String vnfInstanceId,
        String volumeGroupInstanceId,
        String lcpCloudRegionIdOverride,
        String testApi,
        boolean withTestApi
    ) {
        super(requestId, isVolumeGroupPreset ? volumeGroupInstanceId : DEFAULT_INSTANCE_ID, testApi, withTestApi);
        this.serviceInstanceId = serviceInstanceId;
        this.vnfInstanceId = vnfInstanceId;
        this.isVolumeGroupPreset = isVolumeGroupPreset;
        this.volumeGroupInstanceId = volumeGroupInstanceId;
        this.lcpCloudRegionId = defaultIfEmpty(lcpCloudRegionIdOverride, "my region");
    }

    public static PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress forVfModule(String requestId,
        String serviceInstanceId,
        String vnfInstanceId,
        String volumeGroupInstanceId,
        String lcpCloudRegionIdOverride,
        String testApi,
        boolean withTestApi
    ) {
        return new PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress(false, requestId, serviceInstanceId, vnfInstanceId, volumeGroupInstanceId, lcpCloudRegionIdOverride, testApi, withTestApi);
    }

    public static PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress forVolumeGroup(String requestId,
        String serviceInstanceId,
        String vnfInstanceId,
        String lcpCloudRegionIdOverride,
        String testApi,
        boolean withTestApi
    ) {
        return new PresetMSOCreateVfModuleWithVolumeGroupALaCarteCypress(true, requestId, serviceInstanceId, vnfInstanceId, requestId, lcpCloudRegionIdOverride, testApi, withTestApi);
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }

    @Override
    public String getReqPath() {
        String trailer = isVolumeGroupPreset ? "/volumeGroups" : "/vfModules";
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs/" + vnfInstanceId + trailer;
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
                "      \"modelName\":\"2017488PasqualeVpe..PASQUALE_vRE_BV..module-1\"," +
                "      \"modelVersion\":\"6\"," +
                "      \"modelCustomizationId\":\"f7e7c365-60cf-49a9-9ebf-a1aa11b9d401\"," +
                "      \"modelCustomizationName\":\"2017488PasqualeVpe..PASQUALE_vRE_BV..module-1\"" +
                "    }," +
                "    \"cloudConfiguration\":{" +
            "      \"lcpCloudRegionId\":\"" + lcpCloudRegionId + "\"," +
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
                "\"userParams\": [{"
            + "                    \"name\": \"pasqualevpe0_bandwidth\","
            + "                    \"value\": \"10\""
            + "                }, {"
            + "                    \"name\": \"2017488_pasqualevpe0_vnf_instance_name\","
            + "                    \"value\": \"mtnj309me6\""
            + "                }, {"
            + "                    \"name\": \"2017488_pasqualevpe0_vnf_config_template_version\","
            + "                    \"value\": \"17.2\""
            + "                }, {"
            + "                    \"name\": \"2017488_pasqualevpe0_AIC_CLLI\","
            + "                    \"value\": \"ATLMY8GA\""
            + "                }, {"
            + "                    \"name\": \"pasqualevpe0_bandwidth_units\","
            + "                    \"value\": \"Gbps\""
            + "                }"
            + "            ]," +
                "      \"usePreload\":true" +
                "    }" +
                "  }" +
                "}";
    }

    private String select(String ofVolumeGroup, String ofVfModule) {
        return isVolumeGroupPreset ? ofVolumeGroup : ofVfModule;
    }
}
