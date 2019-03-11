package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public class PresetMSODeactivateAndCloudDelete extends BaseMSOPreset {
    private final String serviceInstanceId;
    private final String vnfInstanceId;
    private final String vfModuleInstanceId;
    private final String requestId;
    public static final String DEFAULT_SERVICE_INSTANCE_ID = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
    public static final String DEFAULT_REQUEST_ID = "318cc766-b673-4a50-b9c5-471f68914584";

    public PresetMSODeactivateAndCloudDelete(String serviceInstanceId, String vnfInstanceId, String vfModuleInstanceId, String requestId, String cloudOwner) {
        this.serviceInstanceId = serviceInstanceId != null ? serviceInstanceId : DEFAULT_SERVICE_INSTANCE_ID;
        this.vnfInstanceId = vnfInstanceId;
        this.vfModuleInstanceId = vfModuleInstanceId;
        this.requestId = requestId != null ? requestId : DEFAULT_REQUEST_ID;
        this.cloudOwner = cloudOwner;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/vnfs/" + vnfInstanceId + "/vfModules/" + vfModuleInstanceId + "/deactivateAndCloudDelete";
    }

    @Override
    public String getRequestBody() {
                return "{" +
                        "  \"requestDetails\": {" +
                        "      \"modelInfo\": {" +
                        "          \"modelType\": \"vfModule\"" +
                        "      }," +
                        "      \"cloudConfiguration\": {" +
                        "          \"lcpCloudRegionId\": \"hvf6\"," +
                                    addCloudOwnerIfNeeded() +
                        "          \"tenantId\": \"bae71557c5bb4d5aac6743a4e5f1d054\"" +
                        "      }," +
                        "      \"requestInfo\": {" +
                        "          \"source\": \"VID\"," +
                        "          \"requestorId\": \"us16807000\"" +
                        "      }," +
                        "    \"requestParameters\": { " +
                        "      \"testApi\": \"GR_API\", " +
                        "      \"userParams\": [] " +
                        "    } " +
                        "  }" +
                        "}";
    }

    @Override
    public Object getResponseBody() {
        return "{\"requestReferences\":{\"instanceId\":\"" + serviceInstanceId + "\",\"requestId\":\"" + requestId + "\"}}";
    }

    @Override
    public int getResponseCode() {
        return 202;
    }

    @Override
    public boolean isStrictMatch() {
        return true;
    }
}
