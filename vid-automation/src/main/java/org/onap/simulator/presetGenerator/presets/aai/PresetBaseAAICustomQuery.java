package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.onap.simulator.presetGenerator.presets.BasePresets.BaseAAIPreset;
import org.springframework.http.HttpMethod;

public abstract class PresetBaseAAICustomQuery extends BaseAAIPreset {

    private final String requestBodyStart;
    private final String requestBodyQuery;
    private final FORMAT format;

    public enum FORMAT {
        RESOURCE, SIMPLE;

        public String value() {
            return this.name().toLowerCase();
        }
    }
    public PresetBaseAAICustomQuery(FORMAT format, String requestBodyStart, String requestBodyQuery) {
        this.format = format;
        this.requestBodyStart = requestBodyStart;
        this.requestBodyQuery = requestBodyQuery;
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
        return ImmutableMap.of(
                "format", Collections.singletonList(format.value())
        );
    }

    @Override
    public Object getRequestBody() {
        return ImmutableMap.of(
                "start", requestBodyStart,
                "query", requestBodyQuery
        );
    }

}
