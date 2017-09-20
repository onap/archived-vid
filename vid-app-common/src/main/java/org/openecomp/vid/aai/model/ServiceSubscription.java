package org.openecomp.vid.aai.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServiceSubscription {
	
    @JsonProperty("service-type")
    public String serviceType;

    @JsonProperty("resource-version")
    public String resourceVersion;

    @JsonProperty("service-instances")
    public ServiceInstances serviceInstances;


}
