/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

import static org.onap.vid.utils.Logging.getMethodName;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import javax.servlet.http.HttpServletRequest;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.category.CategoryParametersResponse;
import org.onap.vid.model.CategoryParameter.Family;
import org.onap.vid.services.CategoryParameterService;
import org.onap.vid.services.CategoryParameterServiceWithRoles;
import org.onap.vid.utils.SystemPropertiesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class PropertyController extends RestrictedBaseController {

    private static final String ERROR_MESSAGE = "Internal error occurred: ";
    private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(PropertyController.class);
    private final CategoryParameterService categoryParameterService;
    private final SystemPropertiesWrapper systemPropertiesWrapper;

    @Autowired
    public PropertyController(CategoryParameterServiceWithRoles service, SystemPropertiesWrapper systemPropertiesWrapper) {
        categoryParameterService = service;
        this.systemPropertiesWrapper = systemPropertiesWrapper;
    }

    @RequestMapping(value = {"/propertyhome"}, method = RequestMethod.GET)
    public ModelAndView welcome(HttpServletRequest request) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== PropertyController welcome start");
        return new ModelAndView(getViewName());
    }

    @RequestMapping(value = "/get_property/{name}/{defaultvalue}", method = RequestMethod.GET)
    public ResponseEntity<String> getProperty(@PathVariable("name") String name,
        @PathVariable("defaultvalue") String defaultvalue,
        HttpServletRequest request) {

        String methodName = "getProperty";
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== {} {}", methodName, " start");
        try {
            String propertyName = name.replace('_', '.');
            String pvalue = systemPropertiesWrapper.getOrDefault(propertyName, defaultvalue);
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== {} {} {}", methodName, "returning", pvalue);
            return ResponseEntity.status(OK).body(pvalue);
        } catch (Exception e) {
            LOGGER.info(EELFLoggerDelegate.errorLogger, "<== {} {}", methodName, e.toString());
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "<== {} {}", methodName, e.toString());
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ERROR_MESSAGE + e.getMessage());
        }
    }

    @RequestMapping(value = "/category_parameter", method = RequestMethod.GET)
    public ResponseEntity getCategoryParameter(HttpServletRequest request,
        @RequestParam(value = "familyName", required = true) Family familyName) {
        LOGGER.debug(EELFLoggerDelegate.debugLogger, "start {}({})", getMethodName());
        try {
            CategoryParametersResponse response = categoryParameterService.getCategoryParameters(familyName);
            LOGGER.debug(EELFLoggerDelegate.debugLogger, "end {}() => {}", getMethodName(), response);
            return ResponseEntity.status(OK).body(response);
        } catch (Exception e) {
            LOGGER.error("failed to retrieve category parameter list from DB.", e);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ERROR_MESSAGE + e.getMessage());
        }
    }
}
