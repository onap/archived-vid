package org.onap.simulator.presetGenerator.presets.mso;

import static org.apache.commons.lang3.StringUtils.defaultIfEmpty;

import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class PresetMSOCreateVfModuleALaCarteCypress extends PresetMSOCreateVfModuleBase {

    protected final Map<Keys, String> names;

    public enum Keys {
        lcpCloudRegionId, tenantId,
        modelVersionId, modelName, modelVersion, modelCustomizationId, modelCustomizationName, instanceName, modelInvariantId
    }

    public static Map<Keys, String> lcpCloudRegionIdAndTenantIdNames(String lcpCloudRegionId, String tenantId) {
      return ImmutableMap.of(
          Keys.lcpCloudRegionId, defaultIfEmpty(lcpCloudRegionId, "hvf6"),
          Keys.tenantId, defaultIfEmpty(tenantId, "624eb554b0d147c19ff8885341760481")
      );
    }

    public static final Map<Keys, String> module0Names = ImmutableMap.<Keys, String>builder()
            .put(Keys.instanceName, "mimazepubi")
            .put(Keys.modelInvariantId, "b34833bb-6aa9-4ad6-a831-70b06367a091")
            .put(Keys.modelVersionId, "f8360508-3f17-4414-a2ed-6bc71161e8db")
            .put(Keys.modelName, "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0")
            .put(Keys.modelVersion, "5")
            .put(Keys.modelCustomizationId, "a55961b2-2065-4ab0-a5b7-2fcee1c227e3")
            .put(Keys.modelCustomizationName, "2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0")
            .build();

    public static final Map<Keys, String> module2Names = ImmutableMap.<Keys, String>builder()
            .put(Keys.instanceName, "bnmgtrx")
            .put(Keys.modelInvariantId, "eff8cc59-53a1-4101-aed7-8cf24ecf8339")
            .put(Keys.modelVersionId, "0a0dd9d4-31d3-4c3a-ae89-a02f383e6a9a")
            .put(Keys.modelName, "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2")
            .put(Keys.modelVersion, "6")
            .put(Keys.modelCustomizationId, "3cd946bb-50e0-40d8-96d3-c9023520b557")
            .put(Keys.modelCustomizationName, "2017488PasqualeVpe..PASQUALE_vPFE_BV..module-2")
            .build();

    public PresetMSOCreateVfModuleALaCarteCypress(String overrideRequestId, String serviceInstanceId, String vnfInstanceId, Map<Keys, String> names, Map<Keys, String> names2, String testApi, boolean withTestApi) {
        super(overrideRequestId, DEFAULT_INSTANCE_ID, serviceInstanceId, vnfInstanceId, "vfModule");
        this.names = ImmutableMap.<Keys, String>builder().putAll(names).putAll(names2).build();
        this.msoTestApi = testApi;
        this.withTestApi = withTestApi;
    }

    @Override
    public Object getRequestBody() {
        return "" +
                "{" +
                "  \"requestDetails\":{" +
                "    \"modelInfo\":{" +
                "      \"modelType\":\"vfModule\"," +
                "      \"modelInvariantId\":\"" + names.get(Keys.modelInvariantId) + "\"," +
                "      \"modelVersionId\":\"" + names.get(Keys.modelVersionId) + "\"," +
                "      \"modelName\":\"" + names.get(Keys.modelName) + "\"," +
                "      \"modelVersion\":\"" + names.get(Keys.modelVersion) + "\"," +
                "      \"modelCustomizationId\":\"" + names.get(Keys.modelCustomizationId) + "\"," +
                "      \"modelCustomizationName\":\"" + names.get(Keys.modelCustomizationName) + "\"" +
                "    }," +
                "    \"cloudConfiguration\":{" +
                "      \"lcpCloudRegionId\":\"" + defaultIfEmpty(names.get(Keys.lcpCloudRegionId), "hvf6") + "\"," +
                      addCloudOwnerIfNeeded() +
                "      \"tenantId\":\"" + defaultIfEmpty(names.get(Keys.tenantId), "624eb554b0d147c19ff8885341760481") + "\"" +
                "    }," +
                "    \"requestInfo\":{" +
                "      \"instanceName\":\"" + names.get(Keys.instanceName) + "\"," +
                "      \"source\":\"VID\"," +
                "      \"suppressRollback\":true," +
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
                "      }, {" +
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
                "      }" +
                "    ]," +
                "    \"requestParameters\":{" +
                        addTestApi()+
                "      \"userParams\":[" +
                "      ]," +
                "      \"usePreload\":false" +
                "    }" +
                "  }" +
                "}";
    }
}
