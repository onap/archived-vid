package org.onap.vid.model.aaiTree

class VnfGroup(node: AAITreeNode) : InstanceGroup(node) {

    val vnfs: Map<String, RelatedVnf> = node.children
            .filter { it.type == NodeType.GENERIC_VNF }
            .map { it.uniqueNodeKey to RelatedVnf.from(it) }
            .toMap()

}