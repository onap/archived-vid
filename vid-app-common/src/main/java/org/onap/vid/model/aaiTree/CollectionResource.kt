package org.onap.vid.model.aaiTree

import org.apache.commons.lang.StringUtils

fun isNfc(node: AAITreeNode): Boolean {
    return node.type == NodeType.INSTANCE_GROUP &&
            node.additionalProperties["instance-group-type"] != null &&
            StringUtils.equalsIgnoreCase(node.additionalProperties["instance-group-type"].toString(), "L3-NETWORK")
}

class CollectionResource(node: AAITreeNode) : Node(node) {

    val ncfs: Map<String, NCF> = node.children.filter { isNfc(it) }.map { it.uniqueNodeKey to NCF(it) }.toMap()

}