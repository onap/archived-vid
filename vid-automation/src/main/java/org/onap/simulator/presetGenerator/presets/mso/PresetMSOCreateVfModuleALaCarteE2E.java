package org.onap.simulator.presetGenerator.presets.mso;

import vid.automation.test.infra.ModelInfo;

public class PresetMSOCreateVfModuleALaCarteE2E extends PresetMSOCreateVfModuleBase {

    protected final String requestorId;
    protected final ModelInfo serviceModelInfo;

    public PresetMSOCreateVfModuleALaCarteE2E(
        String overrideRequestId,
        String serviceInstanceId,
        String vnfInstanceId,
        String requestorId,
        ModelInfo serviceModelInfo) {
        super(overrideRequestId, serviceInstanceId, vnfInstanceId);
        this.requestorId = requestorId;
        this.serviceModelInfo = serviceModelInfo;
    }

    @Override
    public Object getRequestBody() {
        return "{"
            + "    \"requestDetails\": {"
            + "        \"modelInfo\": {"
            + "            \"modelCustomizationName\": \"Vocg1804Vf..base_ocg..module-0\","
            + "            \"modelCustomizationId\": \"a7b333d7-7633-4197-b40d-80fcfcadee94\","
            + "            \"modelInvariantId\": \"e9c795c8-6b98-4db3-bd90-a84b8ca5181b\","
            + "            \"modelVersionId\": \"815db6e5-bdfd-4cb6-9575-82c36df8747a\","
            + "            \"modelName\": \"Vocg1804Vf..base_ocg..module-0\","
            + "            \"modelType\": \"vfModule\","
            + "            \"modelVersion\": \"4\""
            + "        },"
            + "        \"cloudConfiguration\": {"
            + "            \"lcpCloudRegionId\": \"hvf6\","
            +               addCloudOwnerIfNeeded()
            + "            \"tenantId\": \"bae71557c5bb4d5aac6743a4e5f1d054\""
            + "        },"
            + "        \"requestInfo\": {"
            + "            \"source\": \"VID\","
            + "            \"suppressRollback\": false,"
            + "            \"requestorId\": \""+requestorId+"\""
            + "        },"
            + "        \"relatedInstanceList\": [{"
            + "                \"relatedInstance\": {"
            +                   serviceModelInfo.createMsoModelInfo()
            + "                    \"instanceId\": \""+serviceInstanceId+"\""
            + "                }"
            + "            }, {"
            + "                \"relatedInstance\": {"
            + "                    \"modelInfo\": {"
            + "                        \"modelCustomizationName\": \"vOCG_1804_VF 0\","
            + "                        \"modelCustomizationId\": \"e9ed1da0-c078-426a-8e84-6f4e85eace59\","
            + "                        \"modelInvariantId\": \"db23d71a-4cb4-4030-9c9b-e3f886c2b35c\","
            + "                        \"modelVersionId\": \"aca3f7b1-15f9-45a5-b182-b8b5aca84a76\","
            + "                        \"modelName\": \"vOCG_1804_VF\","
            + "                        \"modelType\": \"vnf\","
            + "                        \"modelVersion\": \"4.0\""
            + "                    },"
            + "                    \"instanceId\": \""+vnfInstanceId+"\""
            + "                }"
            + "            }"
            + "        ],"
            + "        \"requestParameters\": {"
            + "            \"userParams\": [{"
            + "                    \"param\": \"ABCD\","
            + "                    \"vnf_instance_name\": \"sample\""
            + "                }"
            + "            ],"
            + "            \"testApi\": \"VNF_API\""
            + "        }"
            + "    }"
            + "}";
    }
}
