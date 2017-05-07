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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;

/**
 * The Class PropertyController.
 */
@RestController
public class PropertyController extends RestrictedBaseController{
	
	/** The view name. */
	String viewName;
	
	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PropertyController.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	/**
	 * Welcome.
	 *
	 * @param request the request
	 * @return the model and view
	 */
	@RequestMapping(value = {"/propertyhome" }, method = RequestMethod.GET)
	public ModelAndView welcome(HttpServletRequest request) {
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== PropertyController welcome start");
		return new ModelAndView(getViewName());		
	}
	
	/* (non-Javadoc)
	 * @see org.openecomp.portalsdk.core.controller.RestrictedBaseController#getViewName()
	 */
	public String getViewName() {
		return viewName;
	}
	
	/* (non-Javadoc)
	 * @see org.openecomp.portalsdk.core.controller.RestrictedBaseController#setViewName(java.lang.String)
	 */
	public void setViewName(String _viewName) {
		this.viewName = _viewName;
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
			HttpServletRequest request) throws Exception {
		
		String methodName = "getProperty";	
		ResponseEntity<String> resp = null;
		String pvalue = null;
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		
		try {
			// convert "_" to "." in the property name
			if (name == null || name.length() == 0 ) {
				return ( new ResponseEntity<String> (defaultvalue, HttpStatus.OK));
			}
			// convert "_" to "." in the property name
			String propertyName = name.replace('_', '.');
			pvalue = SystemProperties.getProperty(propertyName);
			if ( ( pvalue == null ) || ( pvalue.length() == 0 ) ) {
				pvalue = defaultvalue;
			}
			resp = new ResponseEntity<String>(pvalue, HttpStatus.OK);
		}
		catch (Exception e) {
			logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			throw e;
		}
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " returning " + pvalue);
  		return ( resp );
	}

}
