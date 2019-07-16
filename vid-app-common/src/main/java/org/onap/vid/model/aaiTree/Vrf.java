package org.onap.vid.model.aaiTree;

import java.util.HashMap;
import java.util.Map;

public class Vrf extends Node {

    private Map<String, Network> networks = new HashMap<>();
    private Map<String, VpnBinding> vpns = new HashMap<>();


    public Vrf(AAITreeNode node){
        super(node);
    }

    public static Vrf from(AAITreeNode node) {
        Vrf vrf = new Vrf(node);

        node.getChildren().forEach(child -> {
            if (child.getType() == NodeType.NETWORK) {
                vrf.getNetworks().put(child.getUniqueNodeKey(), Network.from(child));
            }
            if (child.getType() == NodeType.VPN_BINDING) {
                vrf.getVpns().put(child.getUniqueNodeKey(), VpnBindingKt.from(child));
            }
        });
        return vrf;
    }

    public Map<String, Network> getNetworks() {
        return networks;
    }

    public void setNetworks(Map<String, Network> networks) {
        this.networks = networks;
    }

    public Map<String, VpnBinding> getVpns() {
        return vpns;
    }

    public void setVpns(Map<String, VpnBinding> vpns) {
        this.vpns = vpns;
    }
}
