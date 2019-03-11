package org.onap.simulator.presetGenerator.presets.mso;

public class PresetMSODeleteVfModuleCypress extends PresetMSODeleteVfModule {

    public PresetMSODeleteVfModuleCypress() {
        super(null, null, null, null);
    }

    public PresetMSODeleteVfModuleCypress(String requestId, String serviceInstanceId, String vnfInstanceId, String vfModuleInstanceId) {
        super(requestId, serviceInstanceId, vnfInstanceId, vfModuleInstanceId);
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "   \"requestDetails\":{" +
                "      \"modelInfo\":{" +
                "         \"modelCustomizationName\":\"2017488PASQUALEVpe..PASQUALE_vRE_BV..module-1\"," +
                "         \"modelCustomizationId\":\"f7e7c365-60cf-49a9-9ebf-a1aa11b9d401\"," +
                "         \"modelInvariantId\":\"7253ff5c-97f0-4b8b-937c-77aeb4d79aa1\"," +
                "         \"modelVersionId\":\"25284168-24bb-4698-8cb4-3f509146eca5\"," +
                "         \"modelName\":\"2017488PASQUALEVpe..PASQUALE_vRE_BV..module-1\"," +
                "         \"modelType\":\"vfModule\"," +
                "         \"modelVersion\":\"6\"" +
                "      }," +
                "      \"cloudConfiguration\":{" +
                "         \"lcpCloudRegionId\":\"JANET25\"," +
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
