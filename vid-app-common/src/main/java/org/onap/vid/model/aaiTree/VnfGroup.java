package org.onap.vid.model.aaiTree;

import org.onap.vid.aai.util.AAITreeConverter;


public class VnfGroup extends Node {

    public static final String INSTANCE_GROUP_TYPE = "instance-group-type";
    public static final String INSTANCE_GROUP_ROLE = "instance-group-role";
    public static final String INSTANCE_GROUP_FUNCTION = "instance-group-function";

    private String instanceGroupRole;
    private String instanceGroupFunction;


    public VnfGroup(AAITreeNode node) {
        super(node, AAITreeConverter.ModelType.instanceGroup);
    }

    public static VnfGroup from(AAITreeNode node) {
        VnfGroup vnfGroup = new VnfGroup(node);
        if (node.getAdditionalProperties().get(INSTANCE_GROUP_TYPE) != null) {
            vnfGroup.setInstanceType(node.getAdditionalProperties().get(INSTANCE_GROUP_TYPE).toString());
        }
        if (node.getAdditionalProperties().get(INSTANCE_GROUP_FUNCTION) != null) {
            vnfGroup.setInstanceGroupFunction(node.getAdditionalProperties().get(INSTANCE_GROUP_FUNCTION).toString());
        }
        if (node.getAdditionalProperties().get(INSTANCE_GROUP_ROLE) != null) {
            vnfGroup.setInstanceGroupRole(node.getAdditionalProperties().get(INSTANCE_GROUP_ROLE).toString());
        }

        return vnfGroup;
    }

    public String getInstanceGroupRole() {
        return instanceGroupRole;
    }

    public void setInstanceGroupRole(String instanceGroupRole) {
        this.instanceGroupRole = instanceGroupRole;
    }

    public String getInstanceGroupFunction() {
        return instanceGroupFunction;
    }

    public void setInstanceGroupFunction(String instanceGroupFunction) {
        this.instanceGroupFunction = instanceGroupFunction;
    }


}