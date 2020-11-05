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

package org.onap.vid.aai.util;

import static org.onap.vid.asdc.parser.ToscaParserImpl2.Constants.A_LA_CARTE;

import java.util.Map;
import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.model.ModelUtil;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.CollectionResource;
import org.onap.vid.model.aaiTree.Network;
import org.onap.vid.model.aaiTree.Node;
import org.onap.vid.model.aaiTree.NodeType;
import org.onap.vid.model.aaiTree.ServiceInstance;
import org.onap.vid.model.aaiTree.Vnf;
import org.onap.vid.model.aaiTree.VnfGroup;
import org.onap.vid.model.aaiTree.Vrf;
import org.onap.vid.mso.model.ModelInfo;
import org.springframework.stereotype.Component;

@Component
public class AAITreeConverter {

    public static final String VNF_TYPE = "vnf-type";
    public static final String PNF_TYPE = "pnf-type";
    public static final String NETWORK_TYPE = "network-type";
    public static final String NETWORK_ROLE = "network-role";
    public static final String PHYSICAL_NETWORK_NAME = "physical-network-name";
    public static final String SERVICE_INSTANCE = "service-instance";
    public static final String TENANT = "tenant";
    public static final String VPN_BINDING = "vpn-binding";

    public static final String IS_BASE_VF_MODULE = "is-base-vf-module";
    public static final String SERVICE_INSTANCE_SERVICE_INSTANCE_NAME = "service-instance.service-instance-name";
    public static final String SERVICE_INSTANCE_SERVICE_INSTANCE_ID = "service-instance.service-instance-id";
    public static final String TENANT_TENANT_NAME = "tenant.tenant-name";

    private final ModelUtil modelUtil;

    @Inject
    public AAITreeConverter(ModelUtil modelUtil) {
        this.modelUtil = modelUtil;
    }

    public ServiceInstance convertTreeToUIModel(AAITreeNode rootNode, String globalCustomerId, String serviceType, String instantiationType, String instanceRole, String instanceType) {
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setInstanceId(rootNode.getId());
        serviceInstance.setInstanceName(rootNode.getName());
        serviceInstance.setOrchStatus(rootNode.getOrchestrationStatus());
        serviceInstance.setGlobalSubscriberId(globalCustomerId);
        serviceInstance.setSubscriptionServiceType(serviceType);
        serviceInstance.setIsALaCarte(StringUtils.equals(instantiationType, A_LA_CARTE));

        serviceInstance.setModelInfo(createModelInfo(rootNode));

        //set children: vnf, network,group
        rootNode.getChildren().forEach(child -> {
            if (child.getType() == NodeType.GENERIC_VNF) {
                serviceInstance.getVnfs().put(child.getUniqueNodeKey(), Vnf.from(child));
            } else if (child.getType() == NodeType.NETWORK) {
                serviceInstance.getNetworks().put(child.getUniqueNodeKey(), Network.from(child));
            } else if (child.getType() == NodeType.INSTANCE_GROUP) {
                serviceInstance.getVnfGroups().put(child.getUniqueNodeKey(), new VnfGroup(child));
            } else if (child.getType() == NodeType.COLLECTION_RESOURCE) {
                serviceInstance.getCollectionResources().put(child.getUniqueNodeKey(), new CollectionResource(child));
            } else if (isChildVrf(instanceType, instanceRole, child)){
                serviceInstance.getVrfs().put(child.getUniqueNodeKey(), Vrf.from(child));
            }
        });

        serviceInstance.setExistingVNFCounterMap(
                getExistingCounterMap(serviceInstance.getVnfs())
        );

        serviceInstance.setExistingNetworksCounterMap(
                getExistingCounterMap(serviceInstance.getNetworks())
        );


        serviceInstance.setExistingVnfGroupCounterMap(
                getExistingCounterMap(serviceInstance.getVnfGroups())
        );

        serviceInstance.setExistingVRFCounterMap(
                getExistingCounterMap(serviceInstance.getVrfs())
        );

        return serviceInstance;
    }

    protected boolean isChildVrf(String instanceType, String serviceRole, AAITreeNode child) {
        return child.getType() == NodeType.CONFIGURATION && StringUtils.equalsIgnoreCase(instanceType, "BONDING") && StringUtils.equalsIgnoreCase(serviceRole, "INFRASTRUCTURE-VPN");
    }

    private <T extends Node> Map<String, Long> getExistingCounterMap(Map<String, T> nodeList) {
        return modelUtil.getExistingCounterMap(nodeList, Node::getModelInfo);
    }

    private static ModelInfo createModelInfo(AAITreeNode aaiNode) {
        ModelInfo modelInfo = new ModelInfo();
        modelInfo.setModelType(aaiNode.getType().getModelType());
        modelInfo.setModelName(aaiNode.getModelName());
        modelInfo.setModelVersion(aaiNode.getModelVersion());
        modelInfo.setModelVersionId(aaiNode.getModelVersionId());
        modelInfo.setModelInvariantId(aaiNode.getModelInvariantId());
        modelInfo.setModelCustomizationId(aaiNode.getModelCustomizationId());

        return modelInfo;
    }
}
