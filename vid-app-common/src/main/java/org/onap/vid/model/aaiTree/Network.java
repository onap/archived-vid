package org.onap.vid.model.aaiTree;

import org.onap.vid.aai.util.AAITreeConverter;

import static org.onap.vid.aai.util.AAITreeConverter.NETWORK_TYPE;

public class Network extends Node {

    public Network(AAITreeNode node) {
        super(node, AAITreeConverter.ModelType.network);
    }

    public static Network from(AAITreeNode node) {
        Network network = new Network(node);
        if (node.getAdditionalProperties().get(NETWORK_TYPE) != null) {
            network.setInstanceType(node.getAdditionalProperties().get(NETWORK_TYPE).toString());
        }
        return network;
    }
}
