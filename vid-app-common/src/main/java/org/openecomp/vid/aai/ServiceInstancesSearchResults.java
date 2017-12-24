package org.openecomp.vid.aai;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.openecomp.vid.model.ServiceInstanceSearchResult;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceInstancesSearchResults {
    @JsonProperty("service-instances")
    public List<ServiceInstanceSearchResult> serviceInstances;
}
