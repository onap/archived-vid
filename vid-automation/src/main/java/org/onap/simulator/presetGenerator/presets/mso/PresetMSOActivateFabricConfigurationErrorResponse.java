package org.onap.simulator.presetGenerator.presets.mso;

import org.onap.simulator.presetGenerator.presets.BasePresets.BaseMSOPreset;
import org.springframework.http.HttpMethod;

public class PresetMSOActivateFabricConfigurationErrorResponse extends BaseMSOPreset {
    private final String serviceInstanceId;
    private final int errorCode;
    public static final String DEFAULT_SERVICE_INSTANCE_ID = "3f93c7cb-2fd0-4557-9514-e189b7b04f9d";

    public PresetMSOActivateFabricConfigurationErrorResponse() {
        this(null, 0);
    }

    public PresetMSOActivateFabricConfigurationErrorResponse(String serviceInstanceId) {
        this(serviceInstanceId, 0);
    }

    public PresetMSOActivateFabricConfigurationErrorResponse(String serviceInstanceId, int errorCode) {
        this.serviceInstanceId = serviceInstanceId != null ? serviceInstanceId : DEFAULT_SERVICE_INSTANCE_ID;
        this.errorCode = errorCode > 0 ? errorCode : 500;
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
                "    \"cloudConfiguration\": {" +
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
        return "{\"error\":\"222\",\"message\":\"error message\"}";
    }

    @Override
    public int getResponseCode() {
        return errorCode;
    }
}
