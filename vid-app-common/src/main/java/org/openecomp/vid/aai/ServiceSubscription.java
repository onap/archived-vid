package org.openecomp.vid.aai;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

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
