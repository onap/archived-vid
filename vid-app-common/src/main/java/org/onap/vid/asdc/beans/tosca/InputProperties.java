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
                continue;
            } else if (property.getName().equals("param_name")) {
                this.paramName = (String)property.getValue();
                continue;
            } else if (property.getName().equals("vf_module_label")) {
                this.vfModuleLabel = getPropertyValueAsString(property);
                continue;
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
