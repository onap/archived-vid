package org.onap.simulator.presetGenerator.presets.aai;

import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PresetAAISearchNodeQueryNonEmptyResult extends PresetAAIBaseSearchNodeQuery {

    private final String type;
    private final String name;

    public PresetAAISearchNodeQueryNonEmptyResult(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public Object getResponseBody() {
        return "{ \"something\": [] }";
    }

    @Override
    public int getResponseCode() {
        return 200;
    }

    @Override
    public String getReqPath() {
        return getRootPath() + "/nodes/" + searchPath();
    }

    @Override
    public Map<String, List> getQueryParams() {
        return ImmutableMap.of(searchKey(), Collections.singletonList(name));
    }

    private String searchPath() {
        return type + "s";
    }

    private String searchKey() {
        return type + "-name";
    }
}
