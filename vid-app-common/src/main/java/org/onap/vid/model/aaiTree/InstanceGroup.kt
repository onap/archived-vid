package org.onap.vid.model.aaiTree

open class InstanceGroup(aaiNode: AAITreeNode) : Node(aaiNode) {
    var instanceGroupRole: String? = null
    var instanceGroupFunction: String? = null

    init {
        val INSTANCE_GROUP_TYPE = "instance-group-type"
        val INSTANCE_GROUP_ROLE = "instance-group-role"
        val INSTANCE_GROUP_FUNCTION = "instance-group-function"

        if (aaiNode.additionalProperties[INSTANCE_GROUP_TYPE] != null) {
            instanceType = aaiNode.additionalProperties[INSTANCE_GROUP_TYPE].toString()
        }
        if (aaiNode.additionalProperties[INSTANCE_GROUP_FUNCTION] != null) {
            instanceGroupFunction = aaiNode.additionalProperties[INSTANCE_GROUP_FUNCTION].toString()
        }
        if (aaiNode.additionalProperties[INSTANCE_GROUP_ROLE] != null) {
            instanceGroupRole = aaiNode.additionalProperties[INSTANCE_GROUP_ROLE].toString()
        }
    }

}
