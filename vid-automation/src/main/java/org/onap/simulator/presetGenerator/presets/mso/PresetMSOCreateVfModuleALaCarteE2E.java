package org.onap.simulator.presetGenerator.presets.mso;

import org.apache.commons.lang3.ObjectUtils;
import vid.automation.test.infra.ModelInfo;
import vid.automation.test.infra.ModelInfoWithCustomization;

public class PresetMSOCreateVfModuleALaCarteE2E extends PresetMSOCreateVfModuleBase {

    protected final String requestorId;
    protected final ModelInfo serviceModelInfo;
    private final String instanceName;
    private final ModelInfoWithCustomization resourceModelInfo;
    private final String relatedInstance;
    protected final String lcpCloudRegionId;
    protected final String tenantId;

    public PresetMSOCreateVfModuleALaCarteE2E(
        String overrideRequestId,
        String responseInstanceId,
        String serviceInstanceId,
        String vnfInstanceId,
        String requestorId,
        ModelInfo serviceModelInfo,
        String instanceName,
        ModelInfoWithCustomization resourceModelInfo,
        String relatedInstance, String lcpCloudRegionId, String tenantId) {
            super(overrideRequestId, responseInstanceId, serviceInstanceId, vnfInstanceId, resourceModelInfo.resourceType);
            this.requestorId = requestorId;
            this.serviceModelInfo = serviceModelInfo;
            this.instanceName = instanceName;
            this.resourceModelInfo = resourceModelInfo;
            this.relatedInstance = relatedInstance;
            this.lcpCloudRegionId = lcpCloudRegionId;
            this.tenantId = tenantId;
    }

    @Override
    public Object getRequestBody() {
        return "{"
            + "    \"requestDetails\": {"
            +   resourceModelInfo.createMsoModelInfo()
            + "        \"cloudConfiguration\": {"
            + "            \"lcpCloudRegionId\": \"" + lcpCloudRegionId + "\","
            +               addCloudOwnerIfNeeded()
            + "            \"tenantId\": \"" + tenantId + "\""
            + "        },"
            + "        \"requestInfo\": {"
            +           addInstanceName()
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
            + "            }" + addRelatedInstance()
            + "        ],"
            + "        \"requestParameters\": {"
            + "               \"usePreload\": false,"
            + "               \"userParams\": [{"
            + "                    \"name\": \"param\","
            + "                    \"value\": \"ABCD\""
            + "                }, {"
            + "                    \"name\": \"vnf_instance_name\","
            + "                    \"value\": \"sample\""
            + "                }"
            + "            ],"
            + "            \"testApi\": \"GR_API\""
            + "        }"
            + "    }"
            + "}";
    }

    private String addInstanceName() {
        return instanceName==null ? "" :
            "\"instanceName\": \""+instanceName+"\",";
    }

    private String addRelatedInstance() {
        return ObjectUtils.defaultIfNull(relatedInstance, "");
    }
}
