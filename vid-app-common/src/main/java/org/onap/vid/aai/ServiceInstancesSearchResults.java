package org.onap.vid.aai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.model.ServiceInstanceSearchResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceInstancesSearchResults {
    @JsonProperty("service-instances")
    public List<ServiceInstanceSearchResult> serviceInstances;
}
