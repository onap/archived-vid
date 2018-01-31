package org.onap.vid.aai.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServiceInstances {
	
	@JsonProperty("service-instance")
	public List<ServiceInstance> serviceInstance;

}
