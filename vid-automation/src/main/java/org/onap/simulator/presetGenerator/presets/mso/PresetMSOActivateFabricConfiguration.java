package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public class PresetMSOActivateFabricConfiguration extends BaseMSOPreset {
    private final String serviceInstanceId;
    private final String requestId;
    public static final String DEFAULT_SERVICE_INSTANCE_ID = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";
    public static final String DEFAULT_REQUEST_ID = "318cc766-b673-4a50-b9c5-471f68914584";

    public PresetMSOActivateFabricConfiguration() {
        this(null, null);
    }

    public PresetMSOActivateFabricConfiguration(String serviceInstanceId) {
        this(serviceInstanceId, null);
    }

    public PresetMSOActivateFabricConfiguration(String serviceInstanceId, String requestId) {
        this.serviceInstanceId = serviceInstanceId != null ? serviceInstanceId : DEFAULT_SERVICE_INSTANCE_ID;
        this.requestId = requestId != null ? requestId : DEFAULT_REQUEST_ID;
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.POST;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/serviceInstantiation/v./serviceInstances/" + serviceInstanceId + "/activateFabricConfiguration";
    }

    @Override
    public String getRequestBody() {
                return "{" +
                "  \"requestDetails\": {" +
                "    \"modelInfo\": {" +
                "      \"modelType\": \"service\"" +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"source\": \"VID\"" +
                "    }," +
                "    \"requestParameters\": {" +
                "      \"aLaCarte\": false" +
                "    }" +
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
}
