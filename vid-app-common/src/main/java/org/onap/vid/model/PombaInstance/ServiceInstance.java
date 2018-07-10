package org.onap.vid.model.PombaInstance;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceInstance {

    @JsonProperty
    public String serviceInstanceId;

    @JsonProperty
    public String modelVersionId;

    @JsonProperty
    public String modelInvariantId;

    @JsonProperty
    public String customerId;

    @JsonProperty
    public String serviceType;
}
