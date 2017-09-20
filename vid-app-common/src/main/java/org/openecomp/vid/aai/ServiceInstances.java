package org.openecomp.vid.aai;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceInstances {
	
	@JsonProperty("service-instance")
	public List<ServiceInstance> serviceInstance;

}
