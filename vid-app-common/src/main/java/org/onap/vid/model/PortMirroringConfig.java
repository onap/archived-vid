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

package org.onap.vid.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.onap.sdc.toscaparser.api.RequirementAssignments;

import java.util.List;

public class PortMirroringConfig extends Node {

    /** The model customization name. */
    private String modelCustomizationName;

    /** The port miroring requirements for source/collector */
    @JsonIgnore
    private RequirementAssignments requirementAssignments;

    private List<String> sourceNodes;

    private List<String> collectorNodes;

    private boolean configurationByPolicy;

    public PortMirroringConfig() {
        super();
        this.configurationByPolicy = false;
    }

    public String getModelCustomizationName() {
        return modelCustomizationName;
    }

    public void setModelCustomizationName(String modelCustomizationName) {
        this.modelCustomizationName = modelCustomizationName;
    }

    public RequirementAssignments getRequirementAssignments() {
        return requirementAssignments;
    }

    public void setRequirementAssignments(RequirementAssignments requirementAssignments) {
        this.requirementAssignments = requirementAssignments;
    }

    public List<String> getSourceNodes() {
        return sourceNodes;
    }

    public void setSourceNodes(List<String> sourceNodes) {
        this.sourceNodes = sourceNodes;
    }

    public List<String> getCollectorNodes() {
        return collectorNodes;
    }

    public void setCollectorNodes(List<String> collectorNodes) {
        this.collectorNodes = collectorNodes;
    }

    public void setConfigurationByPolicy(boolean configurationByPolicy) {
        this.configurationByPolicy = configurationByPolicy;
    }

    public boolean isConfigurationByPolicy() {
        return configurationByPolicy;
    }
}
