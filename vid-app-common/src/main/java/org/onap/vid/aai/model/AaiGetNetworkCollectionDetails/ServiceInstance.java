package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.onap.vid.aai.model.interfaces.AaiModelWithRelationships;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceInstance implements AaiModelWithRelationships {

    private final String serviceInstanceId;
    private final String serviceInstanceName;
    private final String resourceVersion;
    private final RelationshipList relationshipList;

    public ServiceInstance(
            @JsonProperty("service-instance-id") String serviceInstanceId,
            @JsonProperty("service-instance-name") String serviceInstanceName,
            @JsonProperty("resource-version") String resourceVersion,
            @JsonProperty("relationship-list") RelationshipList relationshipList
    ) {
        this.serviceInstanceId = serviceInstanceId;
        this.serviceInstanceName = serviceInstanceName;
        this.resourceVersion = resourceVersion;
        this.relationshipList = relationshipList;
    }

    @JsonProperty("service-instance-id")
    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    @JsonProperty("service-instance-name")
    @JsonInclude(NON_NULL)
    public String getServiceInstanceName() {
        return serviceInstanceName;
    }

    @JsonProperty("resource-version")
    public String getResourceVersion() {
        return resourceVersion;
    }

    @Override
    @JsonProperty("relationship-list")
    public RelationshipList getRelationshipList() {
        return relationshipList;
    }
}
