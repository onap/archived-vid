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


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.openecomp.portalsdk.core.controller.RestrictedBaseController;


/**
 * The Class BrowseServiceTypesController.
 */
@RestController
public class BrowseServiceTypesController extends RestrictedBaseController{
	
	/** The view name. */
	String viewName;
	
	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(BrowseServiceTypesController.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	/** The servlet context. */
	private @Autowired ServletContext servletContext;
	
	/**
	 * Welcome.
	 *
	 * @param request the request
	 * @return the model and view
	 */
	@RequestMapping(value = {"/browseservicetypes" }, method = RequestMethod.GET)
	public ModelAndView welcome(HttpServletRequest request) {
		
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
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}


}
