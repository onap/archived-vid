package org.onap.simulator.presetGenerator.presets.sdc;

import org.springframework.http.HttpMethod;

public class PresetSDCGetServiceToscaModelGetInvalidResponse extends SdcPresetWithModelVersionId {

    private final int httpCode;

    public PresetSDCGetServiceToscaModelGetInvalidResponse(String modelVersionId, int httpCode) {
        super(modelVersionId);
        this.httpCode = httpCode;
    }

    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return super.getReqPath()+"/toscaModel";
    }

    @Override
    public Object getResponseBody() {
        return "simulated error description from sdc";
    }

    @Override
    public int getResponseCode() {
        return httpCode;
    }
}
