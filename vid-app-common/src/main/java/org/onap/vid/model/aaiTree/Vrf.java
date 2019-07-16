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
