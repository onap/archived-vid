package org.onap.simulator.presetGenerator.presets.mso;

import vid.automation.test.infra.ModelInfo;

public class PresetMSOCreateVnfALaCarteE2E extends PresetMSOCreateVnfBase {

    private final String requestorId;
    protected String lineOfBusinessName;
    protected final ModelInfo serviceModelInfo;

    public PresetMSOCreateVnfALaCarteE2E(
        String overrideRequestId,
        String serviceInstanceId,
        String vnfInstanceId,
        String lineOfBusinessName,
        String requestorId,
        ModelInfo serviceModelInfo) {
        super(overrideRequestId, serviceInstanceId, vnfInstanceId);
        this.lineOfBusinessName = lineOfBusinessName;
        this.requestorId = requestorId;
        this.serviceModelInfo = serviceModelInfo;
    }

    @Override
    public Object getRequestBody() {
        return "{"
            + "    \"requestDetails\": {"
            + "        \"modelInfo\": {"
            + "            \"modelCustomizationName\": \"vOCG_1804_VF 0\","
            + "            \"modelCustomizationId\": \"e9ed1da0-c078-426a-8e84-6f4e85eace59\","
            + "            \"modelInvariantId\": \"db23d71a-4cb4-4030-9c9b-e3f886c2b35c\","
            + "            \"modelVersionId\": \"aca3f7b1-15f9-45a5-b182-b8b5aca84a76\","
            + "            \"modelName\": \"vOCG_1804_VF\","
            + "            \"modelType\": \"vnf\","
            + "            \"modelVersion\": \"4.0\""
            + "        },"
            + "        \"cloudConfiguration\": {"
            + "            \"lcpCloudRegionId\": \"some legacy region\","
            +               addCloudOwnerIfNeeded()
            + "            \"tenantId\": \"092eb9e8e4b7412e8787dd091bc58e86\""
            + "        },"
            + "        \"requestInfo\": {"
            + "            \"source\": \"VID\","
            + "            \"suppressRollback\": false,"
            + "            \"requestorId\": \""+requestorId+"\""
            + "        },"
            + "        \"platform\": {"
            + "            \"platformName\": \"platform\""
            + "        },"
            + "        \"lineOfBusiness\": {"
            + "            \"lineOfBusinessName\": \""+lineOfBusinessName+"\""
            + "        },"
            + "        \"relatedInstanceList\": [{"
            + "                \"relatedInstance\": {"
            +                   serviceModelInfo.createMsoModelInfo()
            + "                    \"instanceId\": \""+serviceInstanceId+"\""
            + "                }"
            + "            }"
            + "        ],"
            + "        \"requestParameters\": {"
            + "            \"userParams\": [],"
            + "            \"testApi\": \"VNF_API\""
            + "        }"
            + "    }"
            + "}";
    }
}
