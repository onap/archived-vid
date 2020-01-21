package org.onap.simulator.presetGenerator.presets.mso;

import org.springframework.http.HttpMethod;

public class PresetMSODeleteALaCarteService extends PresetMSODeleteService {

    private final int responseCode;

    public PresetMSODeleteALaCarteService(String requestId, String serviceInstanceId) {
        super(requestId, serviceInstanceId);
        responseCode = 202;
    }

    public PresetMSODeleteALaCarteService(String requestId, String serviceInstanceId, int responseCode) {
        super(requestId, serviceInstanceId);
        this.responseCode = responseCode;
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public Object getRequestBody() {
        return "{" +
                "  \"requestDetails\": {" +
                "    \"modelInfo\": {" +
                "      \"modelInvariantId\": \"7ee41ce4-4827-44b0-a48e-2707a59905d2\"," +
                "      \"modelVersionId\": \"4117a0b6-e234-467d-b5b9-fe2f68c8b0fc\"," +
                "      \"modelName\": \"Grouping Service for Test\"," +
                "      \"modelType\": \"service\"," +
                "      \"modelVersion\": \"1.0\"" +
                "    }," +
                "    \"requestInfo\": {" +
                "      \"source\": \"VID\"," +
                "      \"requestorId\": \"us16807000\"" +
                "    }," +
                "    \"requestParameters\": {" +
                "       \"testApi\": \"GR_API\","+
                "      \"aLaCarte\": true" +
                "    }" +
                "  }" +
                "}";

    }

    @Override
    protected String getRootPath() {
        return "/mso/serviceInstantiation/v./serviceInstances/";

    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.DELETE;
    }
}
