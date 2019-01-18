/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Modifications Copyright 2019 Nokia
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

import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.model.CategoryParameter.Family;
import org.onap.vid.services.CategoryParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static org.onap.vid.utils.Logging.getMethodName;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 * The Class PropertyController.
 */
@RestController
public class PropertyController extends RestrictedBaseController{

	private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(PropertyController.class);
	private CategoryParameterService categoryParameterService;
	private SystemPropertiesService systemPropertiesService;

	@Autowired
	public PropertyController(CategoryParameterService service, SystemPropertiesService systemPropertiesWrapper) {
		categoryParameterService = service;
		systemPropertiesService = systemPropertiesWrapper;
	}
	
	/**
	 * Welcome.
	 *
	 * @param request the request
	 * @return the model and view
	 */
	@RequestMapping(value = {"/propertyhome" }, method = RequestMethod.GET)
	public ModelAndView welcome(HttpServletRequest request) {
		LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== PropertyController welcome start");
		return new ModelAndView(getViewName());
	}
	
	/**
	 * Gets the property.
	 *
	 * @param name the name
	 * @param defaultvalue the defaultvalue
	 * @param request the request
	 * @return the property
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/get_property/{name}/{defaultvalue}", method = RequestMethod.GET)
	public ResponseEntity<String> getProperty (@PathVariable("name") String name, @PathVariable("defaultvalue") String defaultvalue,
			HttpServletRequest request) {

		String methodName = "getProperty";	
		LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + " start");
		try {
			String propertyName = name.replace('_', '.');
			String pvalue = systemPropertiesService.getProperty(propertyName);
			if ( ( pvalue == null ) || ( pvalue.length() == 0 ) ) {
				pvalue = defaultvalue;
			}
			LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== " + methodName + " returning " + pvalue);
			return ResponseEntity.status(OK).body(pvalue);
		}
		catch (Exception e) {
			LOGGER.info(EELFLoggerDelegate.errorLogger,  "<== " + "." + methodName + e.toString());
			LOGGER.debug(EELFLoggerDelegate.debugLogger,  "<== " + "." + methodName + e.toString());
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Internal error occurred: " + e.getMessage());
		}
	}

	/**
	 * Gets the owning entity properties.
	 * @param request the request
	 * @return the property
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/category_parameter", method = RequestMethod.GET)
	public ResponseEntity getCategoryParameter(HttpServletRequest request, @RequestParam(value="familyName", required = true) Family familyName) {
		LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodName());
		try {
			CategoryParametersResponse response = categoryParameterService.getCategoryParameters(familyName);
			LOGGER.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodName(), response);
			return ResponseEntity.status(OK).body(response);
		}
		catch (Exception e) {
			LOGGER.error("failed to retrieve category parameter list from DB.", e);
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Internal error occurred: " + e.getMessage());
		}
	}


}
