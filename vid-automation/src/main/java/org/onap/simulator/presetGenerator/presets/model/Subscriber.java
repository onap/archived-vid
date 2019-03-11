package org.onap.simulator.presetGenerator.presets.model;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Subscriber {

    @JsonProperty("global-customer-id")
    public String globalCustomerId;

    @JsonProperty("subscriber-name")
    public String subscriberName;

    @JsonProperty("subscriber-type")
    public String subscriberType;

    @JsonProperty("resource-version")
    public String resourceVersion;

    @JsonProperty("relationship-list")
    public Map relationshipList;
}
