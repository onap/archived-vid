package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InstanceGroup {
    @JsonProperty("instance-group-role")
    private String instanceGroupRole;
    @JsonProperty("model-invariant-id")
    private String modelInvariantId;
    @JsonProperty("model-version-id")
    private String modelVersionId;
    private String id;
    private String description;
    @JsonProperty("instance-group-type")
    private String instanceGroupType;
    @JsonProperty("resource-version")
    private String resourceVersion;
    @JsonProperty("instance-group-name")
    private String instanceGroupName;
    @JsonProperty("instance-group-function")
    private String instanceGroupFunction;
    @JsonProperty("relationship-list")
    private RelationshipList relationshipList;

    public InstanceGroup(){
        super();
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public InstanceGroup(
                        @JsonProperty("instance-group-role")
                        String instanceGroupRole,
                        @JsonProperty("model-invariant-id")
                        String modelInvariantId,
                        @JsonProperty("model-version-id")
                        String modelVersionId,
                        @JsonProperty(value = "id", required = true)
                        String id,
                        @JsonProperty(value = "description", required = true)
                        String description,
                        @JsonProperty(value = "instance-group-type", required = true)
                        String instanceGroupType,
                        @JsonProperty("resource-version")
                        String resourceVersion,
                        @JsonProperty("instance-group-name")
                        String instanceGroupName,
                        @JsonProperty("instance-group-function")
                        String instanceGroupFunction,
                        @JsonProperty("relationship-list")
                        RelationshipList relationshipList) {
        this.instanceGroupRole = instanceGroupRole;
        this.modelInvariantId = modelInvariantId;
        this.modelVersionId = modelVersionId;
        this.id = id;
        this.description = description;
        this.instanceGroupType = instanceGroupType;
        this.resourceVersion = resourceVersion;
        this.instanceGroupName = instanceGroupName;
        this.instanceGroupFunction = instanceGroupFunction;
        this.relationshipList = relationshipList;
    }

    public String getInstanceGroupRole() {
        return instanceGroupRole;
    }

    public String getModelInvariantId() {
        return modelInvariantId;
    }

    public String getModelVersionId() {
        return modelVersionId;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getInstanceGroupType() {
        return instanceGroupType;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public String getInstanceGroupName() {
        return instanceGroupName;
    }

    public String getInstanceGroupFunction() {
        return instanceGroupFunction;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

}
