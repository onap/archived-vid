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
