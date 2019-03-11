package org.onap.simulator.presetGenerator.presets.sdc;

import org.springframework.http.HttpMethod;

public class PresetSDCGetServiceToscaModelGetEmptyResult extends SdcPresetWithModelVersionId {

    public PresetSDCGetServiceToscaModelGetEmptyResult(String modelVersionId) {
        super(modelVersionId);
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
        return "";
    }
}
