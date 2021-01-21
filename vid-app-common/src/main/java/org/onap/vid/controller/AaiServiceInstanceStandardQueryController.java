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

package org.onap.vid.controller;


import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.text.StrSubstitutor;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.VidNotions;
import org.onap.vid.model.aaiTree.AAITreeNode;
import org.onap.vid.model.aaiTree.NodeType;
import org.onap.vid.properties.Features;
import org.onap.vid.services.AAIServiceTree;
import org.onap.vid.services.VidService;
import org.onap.vid.utils.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.manager.FeatureManager;


@RestController
@RequestMapping(AaiServiceInstanceStandardQueryController.AAI_STANDARD_QUERY)
public class AaiServiceInstanceStandardQueryController extends VidRestrictedBaseController {

    static final String AAI_STANDARD_QUERY = "aai/standardQuery";
    private static final String SERVICE_INSTANCE_URI_TEMPLATE = "" +
            "business/customers/customer/${global-customer-id}" +
            "/service-subscriptions/service-subscription/${service-type}" +
            "/service-instances/service-instance/${service-instance-id}";

    private final FeatureManager featureManager;
    private final VidService sdcService;
    private final AAIServiceTree aaiServiceTree;

    @Autowired
    public AaiServiceInstanceStandardQueryController(FeatureManager featureManager,
                                                     VidService sdcService, AAIServiceTree aaiServiceTree) {
        this.featureManager = featureManager;
        this.sdcService = sdcService;
        this.aaiServiceTree = aaiServiceTree;
    }

    @RequestMapping(value = "vlansByNetworks", method = RequestMethod.GET)
    public VlansByNetworksHierarchy getNetworksToVlansByServiceInstance(HttpServletRequest request,
                                                                           @RequestParam("sdcModelUuid") UUID sdcModelUuid,
                                                                           @RequestParam("globalCustomerId") String globalCustomerId,
                                                                           @RequestParam("serviceType") String serviceType,
                                                                           @RequestParam("serviceInstanceId") String serviceInstanceId
    ) throws AsdcCatalogException {
        if (!featureManager.isActive(Features.FLAG_PRESENT_PROVIDER_NETWORKS_ASSOCIATIONS)) {
            return new VlansByNetworksHierarchy();
        }

        if (!isModelOf5g(sdcModelUuid)) {
            return new VlansByNetworksHierarchy();
        }

        Tree<AAIServiceTree.AaiRelationship> pathsToSearch = new Tree<>(new AAIServiceTree.AaiRelationship(NodeType.SERVICE_INSTANCE));
        pathsToSearch.addPath(AAIServiceTree.toAaiRelationshipList(NodeType.GENERIC_VNF, NodeType.NETWORK, NodeType.VLAN_TAG));
        pathsToSearch.addPath(AAIServiceTree.toAaiRelationshipList(NodeType.NETWORK, NodeType.VLAN_TAG));

        AAITreeNode aaiTree = aaiServiceTree.buildAAITree(getServiceInstanceUri(globalCustomerId, serviceType, serviceInstanceId),
                null, HttpMethod.GET, pathsToSearch, false).get(0);

        // translate to response's format
        return new VlansByNetworksHierarchy(
            aaiTree.getChildren().stream()
                .filter(child -> child.getType() == NodeType.NETWORK)
                .map(this::translateNetworksFormat)
                .collect(toList()),

            aaiTree.getChildren().stream()
                    .filter(child -> child.getType() == NodeType.GENERIC_VNF)
                    .map(vnf -> new VnfVlansByNetworks(vnf.getId(),
                                    vnf.getChildren().stream()
                                            .map(this::translateNetworksFormat)
                                            .collect(toList())
                            ))
                    .collect(toList())
        );
    }

    private String getServiceInstanceUri(String globalCustomerId, String serviceType, String serviceInstanceId) {
        return new StrSubstitutor(ImmutableMap.of(
                "global-customer-id", globalCustomerId,
                "service-type", serviceType,
                "service-instance-id", serviceInstanceId
        )).replace(SERVICE_INSTANCE_URI_TEMPLATE);

    }

    private NetworksToVlans translateNetworksFormat(AAITreeNode networkWithVlan) {
        return new NetworksToVlans(
                networkWithVlan.getId(),
                networkWithVlan.getName(),
                Objects.toString(networkWithVlan.getAdditionalProperties().get("network-type"), null),
                networkWithVlan.getOrchestrationStatus(),
                networkWithVlan.getChildren().stream().map(
                        vlan -> new NetworksToVlans.Vlan(
                                Objects.toString(vlan.getAdditionalProperties().get("vlan-id-outer"), null)
                        )
                ).collect(toList())
        );
    }

    protected boolean isModelOf5g(UUID sdcModelUuid) throws AsdcCatalogException {
        final ServiceModel serviceModel = sdcService.getService(sdcModelUuid.toString());
        if (serviceModel == null) {
            throw new GenericUncheckedException("Internal error while fetching Service Model: " + sdcModelUuid);
        }
        if (serviceModel.getService() == null || serviceModel.getService().getVidNotions() == null) {
            return false;
        }

        VidNotions.ModelCategory serviceModelCategory = serviceModel.getService().getVidNotions().getModelCategory();
        return (serviceModelCategory == VidNotions.ModelCategory.IS_5G_PROVIDER_NETWORK_MODEL) ||
                (serviceModelCategory == VidNotions.ModelCategory.IS_5G_FABRIC_CONFIGURATION_MODEL);
    }

    protected static class VlansByNetworksHierarchy {
        public final Collection<NetworksToVlans> serviceNetworks;
        public final Collection<VnfVlansByNetworks> vnfNetworks;

        public VlansByNetworksHierarchy() {
            this(Collections.emptySet(), Collections.emptySet());
        }

        public VlansByNetworksHierarchy(Collection<NetworksToVlans> serviceNetworks, Collection<VnfVlansByNetworks> vnfNetworks) {
            this.serviceNetworks = serviceNetworks;
            this.vnfNetworks = vnfNetworks;
        }
    }

    protected static class VnfVlansByNetworks {
        public final String vnfId;
        public final Collection<NetworksToVlans> networks;

        public VnfVlansByNetworks(String vnfId, Collection<NetworksToVlans> networks) {
            this.vnfId = vnfId;
            this.networks = networks;
        }
    }

    protected static class NetworksToVlans {
        public final String networkId;
        public final String name;
        public final String nodeType;
        public final String nodeStatus;
        public final Collection<Vlan> vlans;

        private NetworksToVlans(String networkId, String name, String nodeType, String nodeStatus, Collection<Vlan> vlans) {
            this.networkId = networkId;
            this.name = name;
            this.nodeType = nodeType;
            this.nodeStatus = nodeStatus;
            this.vlans = vlans;
        }

        private static class Vlan {
            public final String vlanIdOuter;

            private Vlan(String vlanIdOuter) {
                this.vlanIdOuter = vlanIdOuter;
            }
        }

    }
}
