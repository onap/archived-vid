package org.openecomp.vid.aai.model.AaiGetServicesRequestModel;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by Oren on 7/17/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Service {
    @JsonProperty("service-id")
    public String serviceId;
    @JsonProperty("service-description")
    public String serviceDescription;
    @JsonProperty("resource-version")
    public String resourceVersion;
    @JsonProperty("is-permitted")
    public boolean isPermitted;

}
