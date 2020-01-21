package org.onap.simulator.presetGenerator.presets.mso;


public abstract class PresetMSOBaseDeleteWithCloudConfiguration extends PresetMSOBaseDelete {

    private final String modelType;

    public PresetMSOBaseDeleteWithCloudConfiguration(String requestId, String modelType) {
        super(requestId);
        this.modelType = modelType;
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                "    \"modelInfo\": {" +
                "      \"modelType\": \""+modelType+"\"" +
                //"      \"modelName\": \"\",  //required for VfModule" +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"source\": \"VID\"," +
                "      \"requestorId\": \"us16807000\"" +
                "    }," +
                "    \"requestParameters\": {" +
                "      \"testApi\": \"GR_API\"" +
                "    }," +
                "    \"cloudConfiguration\": {" +
                "      \"lcpCloudRegionId\": \"hvf6\"," +
                        addCloudOwnerIfNeeded() +
                "      \"tenantId\": \"bae71557c5bb4d5aac6743a4e5f1d054\"" +
                "    }" +
                "  }" +
                "}";
    }
}
