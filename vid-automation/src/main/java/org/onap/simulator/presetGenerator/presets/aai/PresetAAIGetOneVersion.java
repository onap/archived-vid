package org.onap.simulator.presetGenerator.presets.aai;

public class PresetAAIGetOneVersion extends BasePresetAAIGetVersion {

    public PresetAAIGetOneVersion(String modelVersionId1,
                                       String modelInvariantId) {
        super(modelVersionId1, modelInvariantId);
    }

    @Override
    public Object getResponseBody() {
        return "{\"results\": [" +
                "        {" +
                "          \"model\": {" +
                "            \"model-invariant-id\": \"" + getModelInvariantId() + "\"," +
                "            \"model-type\": \"resource\"," +
                "            \"resource-version\": \"1500138206526\"," +
                "            \"model-vers\": {" +
                "              \"model-ver\": [" +
                "                {" +
                "                  \"model-version-id\": \"" + getModelVersionId1() + "\"," +
                "                  \"model-name\": \"action-data\"," +
                "                  \"model-version\": \"1.0\"," +
                "                  \"model-description\": \"decontamination arm circus ammonia hump edge\"," +
                "                  \"resource-version\": \"1500137463984\"" +
                "                }" +
                "              ]" +
                "            }" +
                "          }" +
                "        }" +
                "     ]}";
    }
}
