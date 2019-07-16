package org.onap.vid.model.aaiTree

import org.apache.commons.lang.StringUtils

class NCF(node: AAITreeNode) : InstanceGroup(node) {
    val numberOfNetworks: Int = if (node.relationshipList != null && node.relationshipList.relationship != null) {
        node.relationshipList.relationship
                .filter { StringUtils.equalsIgnoreCase(it.relatedTo, "L3-NETWORK") }
                .count()
    } else 0
}

