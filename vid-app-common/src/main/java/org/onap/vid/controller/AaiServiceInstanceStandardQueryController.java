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


import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Network;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.ServiceInstance;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Vlan;
import org.onap.vid.aai.model.AaiGetNetworkCollectionDetails.Vnf;
import org.onap.vid.aai.util.ServiceInstanceStandardQuery;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.model.VidNotions;
import org.onap.vid.properties.Features;
import org.onap.vid.services.VidService;
import org.onap.vid.utils.Multival;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.manager.FeatureManager;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping(AaiServiceInstanceStandardQueryController.AAI_STANDARD_QUERY)
public class AaiServiceInstanceStandardQueryController extends VidRestrictedBaseController {

    public static final String AAI_STANDARD_QUERY = "aai/standardQuery";

    private final ServiceInstanceStandardQuery serviceInstanceStandardQuery;
    private final FeatureManager featureManager;
    private final VidService sdcService;

    @Autowired
    public AaiServiceInstanceStandardQueryController(FeatureManager featureManager, ServiceInstanceStandardQuery serviceInstanceStandardQuery, VidService sdcService) {
        this.featureManager = featureManager;
        this.serviceInstanceStandardQuery = serviceInstanceStandardQuery;
        this.sdcService = sdcService;
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

        final ServiceInstance serviceInstance =
                serviceInstanceStandardQuery.fetchServiceInstance(globalCustomerId, serviceType, serviceInstanceId);

        Multival<ServiceInstance, Multival<Vnf, Multival<Network, Vlan>>> l3NetworksWithVlansForVnfForService =  fetchVnfsForService(serviceInstance);
        Multival<ServiceInstance, Multival<Network, Vlan>> l3NetworksWithVlansForService = fetchNetworksForService(serviceInstance);

        // translate to response's format
        return new VlansByNetworksHierarchy(
                l3NetworksWithVlansForService.getValues().stream().map(this::translateNetworksFormat
                    ).collect(toList()),

                l3NetworksWithVlansForVnfForService.getValues().stream().map(vnfWithNetworks ->
                        new VnfVlansByNetworks(vnfWithNetworks.getKey().getVnfId(),
                                vnfWithNetworks.getValues().stream().map(this::translateNetworksFormat
                                ).collect(toList())
                        )
                ).collect(toList())
        );
    }

    private Multival<ServiceInstance, Multival<Vnf, Multival<Network, Vlan>>> fetchVnfsForService(ServiceInstance serviceInstance) {
        final Multival<ServiceInstance, Vnf> vnfsForService =
                serviceInstanceStandardQuery.fetchRelatedVnfs(serviceInstance);

        final Multival<ServiceInstance, Multival<Vnf, Network>> vnfsWithL3NetworksForService =
                vnfsForService.mapEachVal(vnf -> serviceInstanceStandardQuery.fetchRelatedL3Networks("vnf", vnf));

        return  vnfsWithL3NetworksForService.mapEachVal(vnfMulti->
                        vnfMulti.mapEachVal(serviceInstanceStandardQuery::fetchRelatedVlanTags)
                );

    }

    private Multival<ServiceInstance, Multival<Network, Vlan>> fetchNetworksForService(ServiceInstance serviceInstance) {
        final Multival<ServiceInstance, Network> l3NetworksForService =
                serviceInstanceStandardQuery.fetchRelatedL3Networks("service", serviceInstance);

        return l3NetworksForService.mapEachVal(serviceInstanceStandardQuery::fetchRelatedVlanTags);
    }

    private NetworksToVlans translateNetworksFormat(Multival<Network, Vlan> networkWithVlan) {
        return new NetworksToVlans(
                networkWithVlan.getKey().getNetworkId(),
                networkWithVlan.getKey().getNetworkName(),
                networkWithVlan.getKey().getNetworkType(),
                networkWithVlan.getKey().getOrchestrationStatus(),
                networkWithVlan.getValues().stream().map(
                        vlan -> new NetworksToVlans.Vlan(vlan.getVlanIdInner())
                ).collect(toList())
        );
    }

    protected boolean isModelOf5g(UUID sdcModelUuid) throws AsdcCatalogException {
        final ServiceModel serviceModel = sdcService.getService(sdcModelUuid.toString());
        if (serviceModel == null) {
            throw new GenericUncheckedException("Internal error while fetching Service Model: " + sdcModelUuid);
        }
        VidNotions.ModelCategory serviceModelCategory = serviceModel.getService().getVidNotions().getModelCategory();
        return serviceModelCategory.equals(VidNotions.ModelCategory.IS_5G_PROVIDER_NETWORK_MODEL) ||
                serviceModelCategory.equals(VidNotions.ModelCategory.IS_5G_FABRIC_CONFIGURATION_MODEL);
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
            public final String vlanIdInner;

            private Vlan(String vlanIdInner) {
                this.vlanIdInner = vlanIdInner;
            }
        }

    }
}
