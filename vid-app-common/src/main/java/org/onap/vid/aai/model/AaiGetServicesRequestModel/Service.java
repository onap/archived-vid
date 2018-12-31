package org.onap.vid.aai.model.AaiGetServicesRequestModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
