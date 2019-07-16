/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.model.aaiTree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.RelationshipList;
import org.onap.vid.mso.model.CloudConfiguration;

import java.util.*;

public class AAITreeNode {

    private NodeType type;
    private String orchestrationStatus;
    private String provStatus;
    private boolean inMaint;
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
    private CloudConfiguration cloudConfiguration;
    private RelationshipList relationshipList;
    private String keyInModel;
    private AAITreeNode parent;

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
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

    public boolean getInMaint() {
        return inMaint;
    }

    public void setInMaint(boolean inMaint) {
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
        return this.id;
    }

    public void setKeyInModel(String keyInModel) {
        this.keyInModel = keyInModel;
    }

    public String getKeyInModel() {
        return keyInModel;
    }

    //prevent cyclic serialization of parent and children
    @JsonIgnore
    public AAITreeNode getParent() {
        return parent;
    }

    public void setParent(AAITreeNode parent) {
        this.parent = parent;
    }

    public CloudConfiguration getCloudConfiguration() {
        return cloudConfiguration;
    }

    public void setCloudConfiguration(CloudConfiguration cloudConfiguration) {
        this.cloudConfiguration = cloudConfiguration;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }

    public void addChildren(List<AAITreeNode> children) {
        for (AAITreeNode child : children) {
            child.setParent(this);
        }

        this.getChildren().addAll(children);
    }
}
