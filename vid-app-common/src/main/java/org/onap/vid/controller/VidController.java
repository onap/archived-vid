/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * Modifications Copyright 2018 - 2019 Nokia
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

import java.util.Collection;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.SecureServices;
import org.onap.vid.asdc.beans.Service;
import org.onap.vid.exceptions.VidServiceUnavailableException;
import org.onap.vid.model.PombaInstance.PombaRequest;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.roles.Role;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.InstantiationTemplatesService;
import org.onap.vid.services.PombaService;
import org.onap.vid.services.VidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class VidController extends RestrictedBaseController {

    private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VidController.class);

    private final VidService vidService;
    private final AaiService aaiService;
    private final RoleProvider roleProvider;
    private final PombaService pombaService;
    private final InstantiationTemplatesService instantiationTemplatesService;

    @Autowired
    public VidController(VidService vidService, AaiService aaiService, RoleProvider roleProvider,
        PombaService pombaService, InstantiationTemplatesService instantiationTemplatesService) {
        this.vidService = vidService;
        this.aaiService = aaiService;
        this.roleProvider = roleProvider;
        this.pombaService = pombaService;
        this.instantiationTemplatesService = instantiationTemplatesService;
    }

    /**
     * @param request the request
     * @return the services
     */
    @RequestMapping(value = {"/rest/models/services"}, method = RequestMethod.GET)
    public SecureServices getServices(HttpServletRequest request) {
        LOG.info("Start API for browse SDC was called");
        SecureServices secureServices = new SecureServices();
        List<Role> roles = roleProvider.getUserRoles(request);

        Collection<Service> servicesByDistributionStatus = aaiService.getServicesByDistributionStatus();

        Collection<Service> servicesWithTemplatesIndication =
            instantiationTemplatesService.setOnEachServiceIsTemplateExists(servicesByDistributionStatus);

        secureServices.setServices(servicesWithTemplatesIndication);
		secureServices.setReadOnly(roleProvider.userPermissionIsReadOnly(roles));

        return secureServices;
    }


    /**
     * @param uuid the uuid
     * @return the services
     * @throws VidServiceUnavailableException the vid service unavailable exception
     */
    @RequestMapping(value = {"/rest/models/services/{uuid}"}, method = RequestMethod.GET)
    public ServiceModel getService(@PathVariable("uuid") String uuid) throws VidServiceUnavailableException {
        try {
            return vidService.getService(uuid);
        } catch (AsdcCatalogException e) {
            LOG.error("Failed to retrieve service definitions from SDC. Error: " + e.getMessage(), e);
            throw new VidServiceUnavailableException("Failed to retrieve service definitions from SDC", e);
        }
    }

    @RequestMapping(value = "/rest/models/reset", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void invalidateServiceModelCache() {
        vidService.invalidateServiceCache();
    }

    /**
     * @return the services view
     * @throws VidServiceUnavailableException the vid service unavailable exception
     */
    // FIX ME: Circular view path [serviceModels]: would dispatch back to the current handler URL [/serviceModels] again.
    @RequestMapping(value = {"/serviceModels"}, method = RequestMethod.GET)
    public ModelAndView getServicesView() {
        return new ModelAndView("serviceModels");
    }

    @RequestMapping(value = {"/rest/models/services/verifyService"}, method = RequestMethod.POST)
    public void verifyServiceInstance(@RequestBody PombaRequest pombaRequest) {
        pombaService.verify(pombaRequest);
    }
}
