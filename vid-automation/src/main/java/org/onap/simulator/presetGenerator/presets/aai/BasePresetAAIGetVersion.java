package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BasePresetAAIGetVersion extends BaseAAIPreset {
    public String modelVersionId1;
    public String modelInvariantId;

    public BasePresetAAIGetVersion(String modelVersionId1, String modelInvariantId) {
        this.modelVersionId1 = modelVersionId1;
        this.modelInvariantId = modelInvariantId;
    }
    @Override
    public HttpMethod getReqMethod() {
        return HttpMethod.PUT;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/query";
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of("format", Collections.singletonList("resource"));
    }

    @Override
    public Object getRequestBody() {
        return "{\"start\" : \"service-design-and-creation/models/\", \"query\" : \"query/serviceModels-byDistributionStatus?distributionStatus=DISTRIBUTION_COMPLETE_OK\"}";
    }

    public String getModelVersionId1() {
        return modelVersionId1;
    }

    public String getModelInvariantId() {
        return modelInvariantId;
    }


}
