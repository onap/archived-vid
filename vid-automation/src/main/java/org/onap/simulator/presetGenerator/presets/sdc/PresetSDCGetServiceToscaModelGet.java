package org.onap.simulator.presetGenerator.presets.sdc;

import org.springframework.http.HttpMethod;
import vid.automation.test.infra.ModelInfo;

public class PresetSDCGetServiceToscaModelGet extends SdcPresetWithModelVersionId {

    private String file;

    public PresetSDCGetServiceToscaModelGet(ModelInfo modelInfo) {
        this(modelInfo.modelVersionId, modelInfo.zipFileName);
    }

    public PresetSDCGetServiceToscaModelGet(String modelVersionId, String file) {
        super(modelVersionId);
        this.file = file;
    }

    public HttpMethod getReqMethod() {
        return HttpMethod.GET;
    }

    @Override
    public String getReqPath() {
        return super.getReqPath()+"/toscaModel";
    }

    @Override
    public String getFile() {
        return file;
    }
}
