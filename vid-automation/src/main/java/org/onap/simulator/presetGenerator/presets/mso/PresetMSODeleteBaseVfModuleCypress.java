package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSODeleteBaseVfModuleCypress extends PresetMSODeleteVfModule {

    public PresetMSODeleteBaseVfModuleCypress() {
        super(null, null, null, null);
    }

    public PresetMSODeleteBaseVfModuleCypress(String requestId, String serviceInstanceId, String vnfInstanceId, String vfModuleInstanceId) {
        super(requestId, serviceInstanceId, vnfInstanceId, vfModuleInstanceId);
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "   \"requestDetails\":{" +
                "      \"modelInfo\":{" +
                "         \"modelCustomizationName\":\"2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0\"," +
                "         \"modelCustomizationId\":\"a55961b2-2065-4ab0-a5b7-2fcee1c227e3\"," +
                "         \"modelInvariantId\":\"b34833bb-6aa9-4ad6-a831-70b06367a091\"," +
                "         \"modelVersionId\":\"f8360508-3f17-4414-a2ed-6bc71161e8db\"," +
                "         \"modelName\":\"2017488PasqualeVpe..PASQUALE_base_vPE_BV..module-0\"," +
                "         \"modelType\":\"vfModule\"," +
                "         \"modelVersion\":\"5\"" +
                "      }," +
                "      \"cloudConfiguration\":{" +
                "         \"lcpCloudRegionId\":\"AAIAIC25\"," +
                            addCloudOwnerIfNeeded() +
                "         \"tenantId\":\"092eb9e8e4b7412e8787dd091bc58e86\"," +
                "      }," +
                "      \"requestInfo\":{" +
                "         \"source\":\"VID\"," +
                "         \"requestorId\":\"us16807000\"" +
                "      }" +
                "   }" +
                "}";
    }
}
