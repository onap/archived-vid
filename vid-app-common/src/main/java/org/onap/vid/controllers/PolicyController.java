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

package  org.onap.vid.controllers;

import org.json.simple.JSONObject;
import org.onap.vid.policy.*;
import org.onap.portalsdk.core.controller.RestrictedBaseController;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Controller to handle Policy requests.
 */

@RestController
public class PolicyController extends RestrictedBaseController{	
		
	/** The logger. */
	private static final EELFLoggerDelegate LOGGER = EELFLoggerDelegate.getLogger(PolicyController.class);
	
	@RequestMapping(value="/get_policy",method = RequestMethod.POST)	
	public ResponseEntity<String> getPolicyInfo( HttpServletRequest request, @RequestBody JSONObject policy_request) {
		
		LOGGER.debug("#####################POLICY API CALL STARTED ###############"+ PolicyProperties.POLICY_GET_CONFIG_VAL);
		LOGGER.debug("#####################Policy Request ###############"+policy_request.toString());

		String path = PolicyProperties.getProperty(PolicyProperties.POLICY_GET_CONFIG_VAL);
		PolicyResponseWrapper policyResWrapper = getPolicyConfig(policy_request,path);
		
		LOGGER.debug("$$$$$$$$$$$$$$$$$$$$$$ " + new ResponseEntity<String>(policyResWrapper.getResponse(), HttpStatus.OK).toString());
		
		return ( new ResponseEntity<String>(policyResWrapper.getResponse(), HttpStatus.valueOf(policyResWrapper.getStatus())) );		
	}
	
	protected static PolicyResponseWrapper getPolicyConfig(JSONObject request, String path) {
		String methodName = "getPolicyConfig";
		String uuid = UUID.randomUUID().toString();
		LOGGER.debug(  "starting getPolicyConfig ");
		
		try {
			//STARTING REST API CALL AS AN FACTORY INSTACE
			PolicyRestInterfaceIfc restController = PolicyRestInterfaceFactory.getInstance();	
			
			RestObject<String> restObjStr = new RestObject<String>();
			String str = new String();
			restObjStr.set(str);
			restController.<String>Post(str, request, uuid, path, restObjStr );
			PolicyResponseWrapper policyRespWrapper = PolicyUtil.wrapResponse (restObjStr);
			
			LOGGER.debug( "<== " + methodName + " w=" + policyRespWrapper.getResponse());
			return policyRespWrapper;
		} catch (Exception e) {
			LOGGER.debug(  "EXCEPTION in getPolicyConfig <== " + "." + methodName + e.toString());
			throw e;
		}
	}
}

