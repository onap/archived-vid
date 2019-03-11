package org.onap.simulator.presetGenerator.presets.BasePresets;

import org.springframework.http.HttpMethod;

/**
 * Created by itzikliderman on 21/12/2017.
 */
public abstract class BaseMSOPresetServiceInstanceOperationsPost extends BaseMSOPreset {
    @Override
    protected String getRootPath() {
        return super.getRootPath() + "/cloudResources/v1/operationalEnvironments/ENV-UUID";
    }

    @Override
    public Object getResponseBody() {
        return "{"+
                "      \"requestReferences\": {"+
                "        \"instanceId\": \"dbe54591-c8ed-46d3-abc7-d3a24873dfbd\","+
                "        \"requestId\": \"dbe54591-c8ed-46d3-abc7-d3a24873sssa\""+
                "      }"+
                "    }";
    }

    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.POST;
    }

    @Override
    public int getResponseCode() {
        return 202;
    }
}
