package org.onap.vid.model.aaiTree;

import java.util.HashMap;
import java.util.Map;

import static org.onap.vid.aai.util.AAITreeConverter.VNF_TYPE;

public class Vnf extends Node {

    private Map<String, Map<String, VfModule>> vfModules = new HashMap<>();
    private Map<String, Network> networks = new HashMap<>();

    public Vnf(AAITreeNode node) {
        super(node);
        fillCloudConfigurationProperties(this, node.getCloudConfiguration());
    }

    public Map<String, Map<String, VfModule>> getVfModules() {
        return vfModules;
    }

    public void setVfModules(Map<String, Map<String, VfModule>> vfModules) {
        this.vfModules = vfModules;
    }

    public Map<String, Network> getNetworks() {
        return networks;
    }

    public void setNetworks(Map<String, Network> networks) {
        this.networks = networks;
    }

    public static Vnf from(AAITreeNode node) {
        Vnf vnf = new Vnf(node);
        if (node.getAdditionalProperties().get(VNF_TYPE) != null) {
            vnf.setInstanceType(node.getAdditionalProperties().get(VNF_TYPE).toString());
        }

        node.getChildren().forEach(child -> {
            if (child.getType() == NodeType.VF_MODULE) {
                vnf.getVfModules().putIfAbsent(child.getNodeKey(), new HashMap<>());
                vnf.getVfModules().get(child.getNodeKey())
                        .put(child.getUniqueNodeKey(), VfModule.from(child));
            } else if (child.getType() == NodeType.NETWORK) {
                vnf.getNetworks().put(child.getUniqueNodeKey(), Network.from(child));
            }
        });

        return vnf;
    }
}
