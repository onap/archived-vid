/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.controllers;

import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.onap.vid.asdc.AsdcCatalogException;
import org.onap.vid.asdc.beans.SecureServices;
import org.onap.vid.exceptions.VidServiceUnavailableException;
import org.onap.vid.model.ServiceModel;
import org.onap.vid.roles.Role;
import org.onap.vid.roles.RoleProvider;
import org.onap.vid.services.AaiService;
import org.onap.vid.services.VidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//import org.onap.vid.model.Service;

@RestController
public class VidController extends RestrictedBaseController {

	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VidController.class);

	private final VidService service;

	@Autowired
	public VidController(VidService vidService) throws SdcToscaParserException{

		service = vidService;
	}

	@Autowired
	private AaiService aaiService;

	@Autowired
	RoleProvider roleProvider;

//	/**
//	 * Gets the services.
//	 *
//	 * @param request the request
//	 * @return the services
//	 * @throws VidServiceUnavailableException the vid service unavailable exception
//	 */
//	@RequestMapping(value={"/rest/models/services"}, method = RequestMethod.GET)
//	public SecureServices getServices(HttpServletRequest request) throws VidServiceUnavailableException {
//		try {
//			AaiService aaiService = new AaiServiceImpl();
//			LOG.info("Start API for browse ASDC was called");
//			SecureServices secureServices = new SecureServices();
//			Map<String, String[]> requestParams = request.getParameterMap();
//			List<Role> roles = roleProvider.getUserRoles(request);
//			secureServices.setServices(aaiService.getServicesByDistributionStatus());
//			secureServices.setServices(service.getServices(requestParams));
//			secureServices.setReadOnly(roleProvider.userPermissionIsReadOnly(roles));
//			return secureServices;
//		} catch (AsdcCatalogException e) {
//			LOG.error("Failed to retrieve service definitions from SDC", e);
//			throw new VidServiceUnavailableException("Failed to retrieve service definitions from SDC", e);
//		} catch (Throwable t) {
//			LOG.debug("Unexpected error while retrieving service definitions from SDC: " + t.getMessage() + ":", t);
//			t.printStackTrace();
//			throw new VidServiceUnavailableException("Unexpected error while retrieving service definitions from SDC: " + t.getMessage(), t);
//		}
//	}

	/**
	 * Gets the services.
	 *
	 * @param request the request
	 * @return the services
	 * @throws VidServiceUnavailableException the vid service unavailable exception
	 */
	@RequestMapping(value={"/rest/models/services"}, method = RequestMethod.GET)
	public SecureServices getServices(HttpServletRequest request) throws VidServiceUnavailableException {
		try {
			LOG.info("Start API for browse ASDC was called");
			SecureServices secureServices = new SecureServices();
			List<Role> roles = roleProvider.getUserRoles(request);
			secureServices.setServices(aaiService.getServicesByDistributionStatus());
			//Disable roles until AAF integration finishes
			//secureServices.setReadOnly(roleProvider.userPermissionIsReadOnly(roles));
			secureServices.setReadOnly(false);
			return secureServices;
		}
		catch (Exception t) {
			LOG.debug("Unexpected error while retrieving service definitions from A&AI: " + t.getMessage() + ":", t);
			t.printStackTrace();
			throw new VidServiceUnavailableException("Unexpected error while retrieving service definitions from A&AI: " + t.getMessage(), t);
		}
	}



	/**
	 * Gets the services.
	 *
	 * @param uuid the uuid
	 * @return the services
	 * @throws VidServiceUnavailableException the vid service unavailable exception
	 */
	@RequestMapping(value={"/rest/models/services/{uuid}"}, method = RequestMethod.GET)
	public ServiceModel getServices(@PathVariable("uuid") String uuid, HttpServletRequest request) throws VidServiceUnavailableException {
		try {
			return service.getService(uuid);
		} catch (AsdcCatalogException e) {
			LOG.error("Failed to retrieve service definitions from SDC", e);
			throw new VidServiceUnavailableException("Failed to retrieve service definitions from SDC", e);
		}
	}


	/**
	 * Gets the services view.
	 *
	 * @param request the request
	 * @return the services view
	 * @throws VidServiceUnavailableException the vid service unavailable exception
	 */
	@RequestMapping(value={"/serviceModels"}, method=RequestMethod.GET)
	public ModelAndView getServicesView(HttpServletRequest request) throws VidServiceUnavailableException {
		return new ModelAndView("serviceModels");
	}
}
