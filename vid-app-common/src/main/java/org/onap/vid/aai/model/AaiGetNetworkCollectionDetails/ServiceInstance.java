package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceInstance {

    @JsonProperty("service-instance-id")
    public String serviceInstanceId;

    @JsonProperty("resource-version")
    public String resourceVersion;

    @JsonProperty("relationship-list")
    public RelationshipList relationshipList;
}
