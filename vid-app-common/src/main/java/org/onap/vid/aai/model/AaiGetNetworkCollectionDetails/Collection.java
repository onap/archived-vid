package org.onap.vid.aai.model.AaiGetNetworkCollectionDetails;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Collection {
    @JsonProperty("collection-id")
    private String collectionId;
    @JsonProperty("model-invariant-id")
    private String modelInvariantId;
    @JsonProperty("model-version-id")
    private String modelVersionId;
    @JsonProperty("collection-name")
    private String collectionName;
    @JsonProperty("collection-type")
    private String collectionType;
    @JsonProperty("collection-role")
    private String collectionRole;
    @JsonProperty("collection-function")
    private String collectionFunction;
    @JsonProperty("collection-customization-id")
    private String collectionCustomizationId;
    @JsonProperty("relationship-list")
    private RelationshipList relationshipList;
    @JsonProperty("resource-version")
    private String resourceVersion;

    @JsonProperty("collection-id")
    public String getCollectionId() {
        return collectionId;
    }

    @JsonProperty("collection-id")
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }

    @JsonProperty("model-invariant-id")
    public String getModelInvariantId() {
        return modelInvariantId;
    }

    @JsonProperty("model-invariant-id")
    public void setModelInvariantId(String modelInvariantId) {
        this.modelInvariantId = modelInvariantId;
    }

    @JsonProperty("model-version-id")
    public String getModelVersionId() {
        return modelVersionId;
    }

    @JsonProperty("model-version-id")
    public void setModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    @JsonProperty("collection-name")
    public String getCollectionName() {
        return collectionName;
    }

    @JsonProperty("collection-name")
    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    @JsonProperty("collection-type")
    public String getCollectionType() {
        return collectionType;
    }

    @JsonProperty("collection-type")
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    @JsonProperty("collection-role")
    public String getCollectionRole() {
        return collectionRole;
    }

    @JsonProperty("collection-role")
    public void setCollectionRole(String collectionRole) {
        this.collectionRole = collectionRole;
    }

    @JsonProperty("collection-function")
    public String getCollectionFunction() {
        return collectionFunction;
    }

    @JsonProperty("collection-function")
    public void setCollectionFunction(String collectionFunction) {
        this.collectionFunction = collectionFunction;
    }

    @JsonProperty("collection-customization-id")
    public String getCollectionCustomizationId() {
        return collectionCustomizationId;
    }

    @JsonProperty("collection-customization-id")
    public void setCollectionCustomizationId(String collectionCustomizationId) {
        this.collectionCustomizationId = collectionCustomizationId;
    }

    @JsonProperty("relationship-list")
    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    @JsonProperty("relationship-list")
    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }

    @JsonProperty("resource-version")
    public String getResourceVersion() {
        return resourceVersion;
    }

    @JsonProperty("resource-version")
    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

}
