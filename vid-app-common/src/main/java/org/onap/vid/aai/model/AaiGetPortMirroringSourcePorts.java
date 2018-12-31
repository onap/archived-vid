package org.onap.vid.aai.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AaiGetPortMirroringSourcePorts {

    private final List<SimpleResult> results;

    public AaiGetPortMirroringSourcePorts(@JsonProperty("results") List<SimpleResult> results) {
        this.results = results;
    }

    public List<SimpleResult> getResults() {
        return results;
    }
}
