package org.opencomp.simulator.presetGenerator.presets.aai;

public class PresetAAIGetServiceModelList extends BasePresetAAIGetVersion {

    public String modelInvariant2;
    public String modelInvariant3;
    public PresetAAIGetServiceModelList(String modelVersionId1,
                                        String modelInvariantId1, String modelInvariant2, String modelInvariant3) {
        super(modelVersionId1, modelInvariantId1);
        this.modelInvariant2 = modelInvariant2;
        this.modelInvariant3 = modelInvariant3;
    }

    public String getModelInvariant2() {
        return modelInvariant2;
    }

    public void setModelInvariant2(String modelInvariant2) {
        this.modelInvariant2 = modelInvariant2;
    }

    public String getModelInvariant3() {
        return modelInvariant3;
    }

    public void setModelInvariant3(String modelInvariant3) {
        this.modelInvariant3 = modelInvariant3;
    }

    @Override
    public Object getResponseBody() {
        return "{\n" +
                "\"results\": [\n" +
                "   {\"model\":    { \n" +
                "     \"model-invariant-id\": \"" + getModelInvariantId() + "\"," +
                "      \"model-type\": \"service\",\n" +
                "      \"resource-version\": \"1515103312329\",\n" +
                "      \"model-vers\": {\"model-ver\":       [\n" +
                "                  {\n" +
                "            \"model-version-id\": \"" + getModelVersionId1() + "\"," +
                "            \"model-name\": \"AAAvIRC_mm779p_Service\",\n" +
                "            \"model-version\": \"1.0\",\n" +
                "            \"distribution-status\": \"DISTRIBUTION_COMPLETE_OK\",\n" +
                "            \"model-description\": \"tbd\",\n" +
                "            \"resource-version\": \"1516206395612\"\n" +
                "         }\n" +
                "      ]}\n" +
                "   }},\n" +
                "   {\"model\":    { \n" +
                "      \"model-invariant-id\": \"" + getModelInvariant2() + "\"," +
                "      \"model-type\": \"service\",\n" +
                "      \"resource-version\": \"1515103312329\",\n" +
                "      \"model-vers\": {\"model-ver\":       [\n" +
                "                  {\n" +
                "            \"model-version-id\": \"1dae721c-a1ef-435f-b811-760c23f467bf\",\n" +
                "            \"model-name\": \"BBBvIRC_mm779p_Service\",\n" +
                "            \"model-version\": \"3.0\",\n" +
                "            \"model-description\": \"tbd\",\n" +
                "            \"resource-version\": \"1516025197086\"\n" +
                "         }\n" +
                "      ]}\n" +
                "   }},\n" +
                "   {\"model\":    { \n" +
                "      \"model-invariant-id\": \"" + getModelInvariant3() + "\"," +
                "      \"model-type\": \"service\",\n" +
                "      \"resource-version\": \"1515103312329\",\n" +
                "      \"model-vers\": {\"model-ver\":       [\n" +
                "{\n" +
                "            \"model-version-id\": \"29236d45-e790-4c17-a115-1533cc09b7b1\",\n" +
                "            \"model-name\": \"CCCvIRC_mm779p_Service\",\n" +
                "            \"model-version\": \"4.0\",\n" +
                "            \"distribution-status\": \"DISTRIBUTION_COMPLETE_ERROR\",\n" +
                "            \"model-description\": \"tbd\",\n" +
                "            \"resource-version\": \"1517319724440\"\n" +
                "         }\n" +
                "      ]}\n" +
                "   }}\n" +
                "]}\n";
    }
}
