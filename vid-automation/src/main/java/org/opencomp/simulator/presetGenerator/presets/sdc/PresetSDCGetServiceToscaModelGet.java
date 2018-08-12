package org.opencomp.simulator.presetGenerator.presets.sdc;

import org.springframework.http.HttpMethod;

/**
 * Created by itzikliderman on 21/12/2017.
 */
public class PresetSDCGetServiceToscaModelGet extends SdcPresetWithModelVersionId {

    private String file;

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
