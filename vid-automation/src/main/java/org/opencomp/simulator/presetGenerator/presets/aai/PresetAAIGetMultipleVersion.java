package org.opencomp.simulator.presetGenerator.presets.aai;

public class PresetAAIGetMultipleVersion extends BasePresetAAIGetVersion {
        public String modelVersionId2;
        public String modelVersionId3;
        public PresetAAIGetMultipleVersion(String modelVersionId1,
                                           String modelVersionId2,
                                           String modelVersionId3,
                                           String modelInvariantId) {
            super( modelVersionId1, modelInvariantId);
            this.modelVersionId2 = modelVersionId2;
            this.modelVersionId3 = modelVersionId3;
        }
        public String getModelVersionId2() {
            return modelVersionId2;
        }
        public String getModelVersionId3() {
            return modelVersionId3;
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
                    "                }," +
                    "				{" +
                    "                  \"model-version-id\": \"" + getModelVersionId3() + "\"," +
                    "                  \"model-name\": \"action-data\"," +
                    "                  \"model-version\": \"3.0\"," +
                    "                  \"model-description\": \"Non decontamination arm circus ammonia hump edge\"," +
                    "                  \"resource-version\": \"1500137463986\"" +
                    "                }," +
                    "				{" +
                    "                  \"model-version-id\": \"" + getModelVersionId2() + "\"," +
                    "                  \"model-name\": \"action-data\"," +
                    "                  \"model-version\": \"2.0\"," +
                    "                  \"model-description\": \"The oldest one\"," +
                    "                  \"resource-version\": \"1500137463980\"" +
                    "                }" +
                    "              ]" +
                    "            }" +
                    "          }" +
                    "        }" +
                    "      ]}";
        }

    }