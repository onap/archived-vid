package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AaiGetInstanceGroupsByCloudRegion {

    private final List<InstanceGroupWrapper> results;

    public AaiGetInstanceGroupsByCloudRegion(@JsonProperty("results") List<InstanceGroupWrapper> results) {
        this.results = results;
    }

    public List<InstanceGroupWrapper> getResults() {
        return results;
    }
}
