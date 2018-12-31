package org.onap.vid.model.aaiTree;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class AAITreeNode {

    private String type;
    private int uniqueNumber;
    private String orchestrationStatus;
    private String provStatus;
    private Boolean inMaint = null;
    private String modelVersionId;
    private String modelCustomizationId;
    private String modelInvariantId;
    private String id;
    private String name;
    private String modelVersion;
    private String modelName;
    private String modelCustomizationName;
    private final List<AAITreeNode> children = Collections.synchronizedList(new LinkedList<>());
    private Map<String, Object> additionalProperties = new HashMap<>();
    private String keyInModel;
    private AAITreeNode parent;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUniqueNumber() {
        return uniqueNumber;
    }

    public void setUniqueNumber(int uniqueNumber) {
        this.uniqueNumber = uniqueNumber;
    }
    
    public String getOrchestrationStatus() {
        return orchestrationStatus;
    }

    public void setOrchestrationStatus(String orchestrationStatus) {
        this.orchestrationStatus = orchestrationStatus;
    }

    public String getProvStatus() {
        return provStatus;
    }

    public void setProvStatus(String provStatus) {
        this.provStatus = provStatus;
    }

    public Boolean getInMaint() {
        return inMaint;
    }

    public void setInMaint(Boolean inMaint) {
        this.inMaint = inMaint;
    }

    public String getModelVersionId() {
        return modelVersionId;
    }

    public void setModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    public String getModelCustomizationId() {
        return modelCustomizationId;
    }

    public void setModelCustomizationId(String modelCustomizationId) {
        this.modelCustomizationId = modelCustomizationId;
    }

    public String getModelInvariantId() {
        return modelInvariantId;
    }

    public void setModelInvariantId(String modelInvariantId) {
        this.modelInvariantId = modelInvariantId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelCustomizationName() {
        return modelCustomizationName;
    }

    public void setModelCustomizationName(String modelCustomizationName) {
        this.modelCustomizationName = modelCustomizationName;
    }

    public List<AAITreeNode> getChildren() {
        return children;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public String getNodeKey() {
        if (this.keyInModel != null) {
            return this.keyInModel;
        }

        return StringUtils.defaultIfEmpty(this.modelVersionId, "not provided");
    }

    public String getUniqueNodeKey() {
        return getNodeKey() + ":" + String.format("%03d", this.uniqueNumber);
    }

    public void setKeyInModel(String keyInModel) {
        this.keyInModel = keyInModel;
    }

    public String getKeyInModel() {
        return keyInModel;
    }

    public AAITreeNode getParent() {
        return parent;
    }

    public void setParent(AAITreeNode parent) {
        this.parent = parent;
    }

    public void addChildren(List<AAITreeNode> children) {
        for (AAITreeNode child : children) {
            child.setParent(this);
        }

        this.getChildren().addAll(children);
    }
}
