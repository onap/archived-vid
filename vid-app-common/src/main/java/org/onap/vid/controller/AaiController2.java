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

import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.AaiGetVnfResponse;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.ModelVer;
import org.onap.vid.aai.model.Permissions;
import org.onap.vid.model.aaiTree.Network;
import org.onap.vid.model.aaiTree.RelatedVnf;
import org.onap.vid.model.aaiTree.VpnBinding;
import org.onap.vid.properties.Features;
import org.onap.vid.roles.PermissionProperties;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.togglz.core.manager.FeatureManager;

/**
 * Controller to handle a&ai new requests.
 */

@RestController
public class AaiController2 extends VidRestrictedBaseController {

    private final AaiService aaiService;
    private final RoleProvider roleProvider;
    private final AaiClientInterface aaiClient;
    private final FeatureManager featureManager;

    @Autowired
    public AaiController2(AaiService aaiService, RoleProvider roleProvider, AaiClientInterface aaiClient, FeatureManager featureManager) {
        this.aaiService = aaiService;
        this.roleProvider = roleProvider;
        this.aaiClient = aaiClient;
        this.featureManager = featureManager;
    }

    @RequestMapping(value = "/aai_get_homing_by_vfmodule/{vnfInstanceId}/{vfModuleId}", method = RequestMethod.GET)
    public GetTenantsResponse getHomingDataByVfModule(@PathVariable("vnfInstanceId") String vnfInstanceId,
                                                      @PathVariable("vfModuleId") String vfModuleId){
        return aaiService.getHomingDataByVfModule(vnfInstanceId, vfModuleId);
    }

    @RequestMapping(value = "/aai_get_service_instance_topology/{subscriberId}/{serviceType}/{serviceInstanceId}",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getServiceInstanceTree(@PathVariable("subscriberId") String globalCustomerId,
                                         @PathVariable("serviceType") String serviceInstanceType,
                                         @PathVariable("serviceInstanceId") String serviceInstanceId) {
        return aaiService.getAAIServiceTree(globalCustomerId, serviceInstanceType, serviceInstanceId);
    }

    @RequestMapping(value = "/aai_reset_cache/{cacheName}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void resetCache(@PathVariable("cacheName") String cacheName) {
        aaiClient.resetCache(cacheName);
    }

    @RequestMapping(value = "/roles/service_permissions", method = RequestMethod.GET)
    public Permissions servicePermissions(HttpServletRequest request,
                                          @RequestParam(value = "subscriberId") String subscriberId,
                                          @RequestParam(value = "serviceType") String serviceType) {

        final boolean isEditPermitted = roleProvider
                .getUserRolesValidator(request)
                .isServicePermitted(new PermissionProperties(subscriberId, serviceType));

        return new Permissions(isEditPermitted);
    }

    @RequestMapping(value = "/aai_search_group_members",
            method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RelatedVnf> searchGroupMembers(@RequestParam("subscriberId") String globalCustomerId,
                                               @RequestParam("serviceType") String serviceType,
                                               @RequestParam("serviceInvariantId") String invariantId,
                                               @RequestParam("groupType") String groupType,
                                               @RequestParam("groupRole") String groupRole) {
        return aaiService.searchGroupMembers(globalCustomerId, serviceType, invariantId, groupType, groupRole);
    }

    @RequestMapping(value = "/aai_get_vpn_list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VpnBinding> getVpnList() {
        return aaiService.getVpnListByVpnType("SERVICE-INFRASTRUCTURE");
    }

    @RequestMapping(value = "/aai_get_active_networks",
        method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Network> getActiveNetworkList(
        @RequestParam("cloudRegion") String cloudRegion,
        @RequestParam("tenantId") String tenantId,
        @RequestParam(value = "networkRole", required = false) String networkRole) {
        return aaiService.getL3NetworksByCloudRegion(cloudRegion, tenantId, networkRole)
            .stream()
            .filter(Network::isBoundToVpn)
            .filter(network -> StringUtils.isNotEmpty(network.getInstanceName()))
            .filter(network -> StringUtils.equalsIgnoreCase(network.getOrchStatus(), "active"))
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/aai_get_newest_model_version_by_invariant/{invariantId}",
        method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ModelVer getNewestModelVersionByInvariant(@PathVariable("invariantId") String invariantId) {
        return aaiService.getNewestModelVersionByInvariantId(invariantId);
    }

    @GetMapping(value = "/get_vnf_data_by_globalid_and_service_type/{globalCustomerId}/{serviceType}")
    public AaiGetVnfResponse getVnfDataByGlobalIdAndServiceType(
        @PathVariable("globalCustomerId") String globalCustomerId,
        @PathVariable("serviceType") String serviceType,
        @RequestParam(name="nfRole", required = false) String nfRole,
        @RequestParam(name="cloudRegion", required = false) String cloudRegion) {
        if (featureManager.isActive(Features.FLAG_FLASH_REDUCED_RESPONSE_CHANGEMG)){
            return aaiClient.getVnfsByParamsForChangeManagement(globalCustomerId, serviceType, nfRole, cloudRegion).getT();
        }
        return aaiService.getVNFData(globalCustomerId, serviceType).getT();
    }
}
