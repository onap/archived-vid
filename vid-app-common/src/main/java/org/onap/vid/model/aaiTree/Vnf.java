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

import static org.onap.vid.aai.util.AAITreeConverter.VNF_TYPE;

import java.util.HashMap;
import java.util.Map;

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
