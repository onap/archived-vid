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

import org.onap.vid.aai.AaiClientInterface;
import org.onap.vid.aai.model.AaiGetTenatns.GetTenantsResponse;
import org.onap.vid.aai.model.Permissions;
import org.onap.vid.model.aaiTree.RelatedVnf;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Controller to handle a&ai new requests.
 */

@RestController
public class AaiController2 extends VidRestrictedBaseController {

    private final AaiService aaiService;
    private final RoleProvider roleProvider;
    private final AaiClientInterface aaiClient;

    @Autowired
    public AaiController2(AaiService aaiService, RoleProvider roleProvider, AaiClientInterface aaiClient) {
        this.aaiService = aaiService;
        this.roleProvider = roleProvider;
        this.aaiClient = aaiClient;
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
                .isServicePermitted(subscriberId, serviceType);

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


}
