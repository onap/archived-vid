package org.onap.vid.aai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceSubscription {
	
    @JsonProperty("service-type")
    public String serviceType;

    @JsonProperty("resource-version")
    public String resourceVersion;

    @JsonProperty("service-instances")
    public ServiceInstances serviceInstances;

    @JsonProperty("is-permitted")
    public boolean isPermitted =false;
}
