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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.openecomp.aai.util.AAIRestInterface;
import org.openecomp.vid.model.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;

/**
 * The Class SearchExistingSIController.
 */
@RestController
public class SearchExistingSIController extends RestrictedBaseController{
	
	/** The view name. */
	String viewName;
	
	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(SearchExistingSIController.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	/** The trans id. */
	protected String transId;
	
	/** The from app id. */
	protected String fromAppId = "VID";
	
	/** The model. */
	private Map<String, Object> model = new HashMap<String, Object>();
	
	/** The servlet context. */
	private @Autowired ServletContext servletContext;
	
	/**
	 * Welcome.
	 *
	 * @param request the request
	 * @return the model and view
	 */
	@RequestMapping(value = {"/searchexistingsi" }, method = RequestMethod.GET)
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

	/**
	 * Do get subscfriber list.
	 *
	 * @return the response entity
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value="/searchexistingsi_get_subscribers",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)  	
	public ResponseEntity<Result> doGetSubscfriberList() throws IOException, UnsupportedEncodingException  {
	
	   String  res1 = (String) model.get("subscribernames");
	   if (res1 == null)
	   {
		   res1 = getSubscribers();
	   }
		
	  return new ResponseEntity<Result>(new Result(res1),HttpStatus.OK);
	
	}
	
	/**
	 * Do refresh subscfriber list.
	 *
	 * @return the response entity
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@RequestMapping(value="/searchexistingsi_refresh_subscribers",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)  	
	public ResponseEntity<Result> doRefreshSubscfriberList() throws IOException, UnsupportedEncodingException {
	
	   
      String res1 = getSubscribers();
	
	  return new ResponseEntity<Result>(new Result(res1),HttpStatus.OK);
	
	}
	
	/**
	 * Gets the subscribers.
	 *
	 * @return the subscribers
	 */
	private String getSubscribers() throws UnsupportedEncodingException
	{
	     File certiPath = GetCertificatesPath();
	 	 AAIRestInterface restContrller = new AAIRestInterface(certiPath.getAbsolutePath());
	 	 String res1 = restContrller.RestGet(fromAppId, transId, "business/customers?depth=0", false);
	     model.put("subscribernames", res1);
	     
	     return res1;
	}
	
	/**
	 * Gets the certificates path.
	 *
	 * @return the file
	 */
	private File GetCertificatesPath()
	{
		if (servletContext != null)
			return new File( servletContext.getRealPath("/WEB-INF/cert/") );
	 
		
		return null;
	}
	

}
