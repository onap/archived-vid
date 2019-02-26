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

package org.onap.vid.asdc.beans.tosca;

import org.onap.sdc.toscaparser.api.Property;

import java.util.List;

public class InputProperties {
    private String sourceType;
    private String vfModuleLabel;
    private String paramName;

    public InputProperties(){

    }

    public InputProperties(List<Property> properties) {
        for(Property property: properties) {
            if (property.getName().equals("source_type")) {
                this.sourceType = (String)property.getValue();
            } else if (property.getName().equals("param_name")) {
                this.paramName = (String)property.getValue();
            } else if (property.getName().equals("vf_module_label")) {
                this.vfModuleLabel = getPropertyValueAsString(property);
            }
        }
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getVfModuleLabel() {
        return vfModuleLabel;
    }

    public void setVfModuleLabel(String vfModuleLabel) {
        this.vfModuleLabel = vfModuleLabel;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    private String getPropertyValueAsString(Property property) {
        return property.getValue().toString().substring(1, property.getValue().toString().length()-1);
    }





}
