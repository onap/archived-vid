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

package org.openecomp.vid.controller;

import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.sdc.tosca.parser.exceptions.SdcToscaParserException;
import org.openecomp.vid.asdc.AsdcCatalogException;
import org.openecomp.vid.exceptions.VidServiceUnavailableException;
import org.openecomp.vid.model.ServiceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.openecomp.vid.services.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Map;

//import org.openecomp.vid.model.Service;

@RestController
public class VidController extends RestrictedBaseController {
	
	private static final EELFLoggerDelegate LOG = EELFLoggerDelegate.getLogger(VidController.class);

	private final VidService service;

	@Autowired
	public VidController(VidService vidService) throws SdcToscaParserException{

		service = vidService;
	}
//	
	/**
	 * Gets the services.
	 *
	 * @param request the request
	 * @return the services
	 * @throws VidServiceUnavailableException the vid service unavailable exception
	 */
	@RequestMapping(value={"/rest/models/services"}, method = RequestMethod.GET)
	public Collection<org.openecomp.vid.asdc.beans.Service> getServices(HttpServletRequest request) throws VidServiceUnavailableException {
		try {
			Map<String, String[]> requestParams = request.getParameterMap();
			return service.getServices(requestParams);
		} catch (AsdcCatalogException e) {
			LOG.error("Failed to retrieve service definitions from SDC", e);
			throw new VidServiceUnavailableException("Failed to retrieve service definitions from SDC", e);
		} catch (Throwable t) {
			LOG.debug("Unexpected error while retrieving service definitions from SDC: " + t.getMessage() + ":", t);
			t.printStackTrace();
			throw new VidServiceUnavailableException("Unexpected error while retrieving service definitions from SDC: " + t.getMessage(), t);
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
	public ServiceModel getServices(@PathVariable("uuid") String uuid) throws VidServiceUnavailableException {
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
