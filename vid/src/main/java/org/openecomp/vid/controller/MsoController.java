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


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.UUID;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.glassfish.jersey.client.ClientResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openecomp.vid.model.ExceptionResponse;
import org.openecomp.vid.mso.MsoProperties;
import org.openecomp.vid.mso.MsoResponseWrapper;
import org.openecomp.vid.mso.MsoRestInterfaceFactory;
import org.openecomp.vid.mso.MsoRestInterfaceIfc;
import org.openecomp.vid.mso.MsoUtil;
import org.openecomp.vid.mso.RestObject;
import org.openecomp.vid.mso.rest.RequestDetails;
import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.openecomp.portalsdk.core.controller.RestrictedBaseController;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.portalsdk.core.util.SystemProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class MsoController.
 */
@RestController
@RequestMapping("mso")
public class MsoController extends RestrictedBaseController{
	
	/** The view name. */
	String viewName;
	
	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoController.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	/** The Constant SVC_INSTANCE_ID. */
	public final static String SVC_INSTANCE_ID = "<service_instance_id>";
	
	/** The Constant VNF_INSTANCE_ID. */
	public final static String VNF_INSTANCE_ID = "<vnf_instance_id>";
	
	/**
	 * Welcome.
	 *
	 * @param request the request
	 * @return the model and view
	 */
	public ModelAndView welcome(HttpServletRequest request) {
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== MsoController welcome start");
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + " MSO_SERVER_URL=" +
				 SystemProperties.getProperty(MsoProperties.MSO_SERVER_URL) );
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
	 * Creates the svc instance.
	 *
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_create_svc_instance", method = RequestMethod.POST)
	public ResponseEntity<String> createSvcInstance(HttpServletRequest request) throws Exception {
		String methodName = "createSvcInstance";
		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start" );
		
		RequestDetails mso_request = retrieveRequestObject (request);
		String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
	    
		MsoResponseWrapper w = createInstance(mso_request, p);
		// always return OK, the MSO status code is embedded in the body
		
        return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
	
	}
	
	/**
	 * Creates the vnf.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value="/mso_create_vnf_instance/{serviceInstanceId}", method = RequestMethod.POST)  	
	public ResponseEntity<String> createVnf(@PathVariable("serviceInstanceId") String serviceInstanceId, HttpServletRequest request) throws Exception {
		
		String methodName = "createVnf";		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		
		RequestDetails mso_request = retrieveRequestObject (request);
	    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_VNF_INSTANCE);
	    
	    if ( p == null || p.isEmpty()) {
	    	throw new Exception ( "Vnf instance path is not defined");
	    }
	    // /serviceInstances/v2/<service_instance_id>/vnfs
	    String vnf_path = p.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId );
	    MsoResponseWrapper w = createInstance(mso_request, vnf_path);

		// always return OK, the MSO status code is embedded in the body
		
        return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
	
	}
	
	/**
	 * Creates the nw instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_create_nw_instance/{serviceInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> createNwInstance(@PathVariable("serviceInstanceId") String serviceInstanceId, HttpServletRequest request) throws Exception {
	
		String methodName = "createNwInstance";		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start, serviceInstanceId = " + serviceInstanceId  );
		
		RequestDetails mso_request = retrieveRequestObject (request);
		
	    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
	    
	    if ( p == null || p.isEmpty()) {
	    	throw new Exception ( "Network instance path is not defined");
	    }
	    // /serviceInstances/v2/<serviceInstanceId>/networks/
	    
	    String nw_path = p.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId );
	    MsoResponseWrapper w = createInstance(mso_request, nw_path);
	   
		// always return OK, the MSO status code is embedded in the body
		
        return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
	
	}
	
	/**
	 * Creates the volume group instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param vnfInstanceId the vnf instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_create_volumegroup_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> createVolumeGroupInstance(@PathVariable("serviceInstanceId") String serviceInstanceId, @PathVariable("vnfInstanceId") String vnfInstanceId, 
			HttpServletRequest request) throws Exception {
		String methodName = "createVolumeGroupInstance";
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		
		RequestDetails mso_request = retrieveRequestObject (request);
		String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
	    
	    if ( p == null || p.isEmpty()) {
	    	throw new Exception ( "Volume group instance path is not defined");
	    }
	    String path = p.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
	    path = path.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);
	    
	    MsoResponseWrapper w = createInstance(mso_request, path);
        
		// always return OK, the MSO status code is embedded in the body
        return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
	}
	
	/**
	 * Creates the vf module instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param vnfInstanceId the vnf instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_create_vfmodule_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> createVfModuleInstance(@PathVariable("serviceInstanceId") String serviceInstanceId, 
			@PathVariable("vnfInstanceId") String vnfInstanceId, HttpServletRequest request) throws Exception {
		String methodName = "createVfModuleInstance";		
		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		RequestDetails mso_request = retrieveRequestObject (request);
	    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
	    
	    if ( p == null || p.isEmpty()) {
	    	throw new Exception ( "VF module instance path is not defined");
	    }
	    // /serviceInstances/v2/<serviceInstanceId>/vnfs/<vnfInstanceId>/vfmodules
	    String path = p.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId);
	    path = path.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId);
	    
	    MsoResponseWrapper w = createInstance(mso_request, path);
		
		// always return OK, the MSO status code is embedded in the body
		
        return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
	}
	
	/**
	 * Creates the instance.
	 *
	 * @param request the request
	 * @param path the path
	 * @return the mso response wrapper
	 * @throws ClientHandlerException the client handler exception
	 * @throws Exception the exception
	 */
	protected MsoResponseWrapper createInstance(RequestDetails request, String path) throws Exception {
		String methodName = "createInstance";	
		logger.debug(dateFormat.format(new Date()) + "<== " + methodName + " start");
		
		try {
			MsoRestInterfaceIfc restController = MsoRestInterfaceFactory.getInstance();
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " calling Post, request = (" + request + ")");	
			
			RestObject<String> restObjStr = new RestObject<String>();
			String str = new String();
			restObjStr.set(str);
			restController.<String>Post(str, request, "", path, restObjStr );
			MsoResponseWrapper w = MsoUtil.wrapResponse (restObjStr);
			
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());
			return w;
		} catch (Exception e) {
			logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			throw e;
		}
	}
	
	/**
	 * Delete svc instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_delete_svc_instance/{serviceInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteSvcInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
			HttpServletRequest request) throws Exception {
		
		String methodName = "deleteSvcInstance";	
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		
		RequestDetails mso_request = retrieveRequestObject (request);
	    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_SVC_INSTANCE);
	    String path = p + "/" + serviceInstanceId;
	    MsoResponseWrapper w = deleteInstance ( mso_request, path );
	  
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());
		// always return OK, the MSO status code is embedded in the body
  		
        return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
	    
	}
	
	/**
	 * Delete vnf.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param vnfInstanceId the vnf instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_delete_vnf_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}", method = RequestMethod.POST)
	
	public ResponseEntity<String> deleteVnf(@PathVariable("serviceInstanceId") String serviceInstanceId, @PathVariable("vnfInstanceId") String vnfInstanceId, 
			HttpServletRequest request) throws Exception {
		String methodName = "deleteVnf";		
		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		
		RequestDetails mso_request = retrieveRequestObject (request);
	    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_VNF_INSTANCE);
	    if ( p == null || p.isEmpty()) {
	    	throw new Exception ( "Vnf instance path is not defined");
	    }
	    // /serviceInstances/v2/<service_instance_id>/vnfs/
	    String vnf_path = p.replaceFirst(SVC_INSTANCE_ID, vnfInstanceId );
	    MsoResponseWrapper w = deleteInstance ( mso_request, vnf_path + "/" + vnfInstanceId );
	    
		// always return OK, the MSO status code is embedded in the body
        return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
		
	}
							  
  							/**
  							 * Delete vf module.
  							 *
  							 * @param serviceInstanceId the service instance id
  							 * @param vnfInstanceId the vnf instance id
  							 * @param vfModuleId the vf module id
  							 * @param request the request
  							 * @return the response entity
  							 * @throws Exception the exception
  							 */
  							//mso_delete_vf_module/bc305d54-75b4-431b-adb2-eb6b9e546014/vnfs/fe9000-0009-9999/vfmodules/abeeee-abeeee-abeeee
	@RequestMapping(value = "/mso_delete_vfmodule_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}/vfModules/{vfModuleId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteVfModule (
			@PathVariable("serviceInstanceId") String serviceInstanceId, @PathVariable("vnfInstanceId") String vnfInstanceId,
			@PathVariable("vfModuleId") String vfModuleId, HttpServletRequest request) throws Exception {
		
		String methodName = "deleteVfModule";
		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		
		RequestDetails mso_request = retrieveRequestObject (request);
	    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_VF_MODULE_INSTANCE);
	    if ( p == null || p.isEmpty()) {
	    	throw new Exception ( "VF Module instance path is not defined");
	    }
	    // /serviceInstances/v2/<serviceInstanceId>/vnfs/<vnfInstanceId>/vfmodules
	    String path = p.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId );
	    path = path.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId );
	    MsoResponseWrapper w = deleteInstance ( mso_request, path + "/" + vfModuleId);
	    
		// always return OK, the MSO status code is embedded in the body
        return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );

	}

	/**
	 * Delete volume group instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param vnfInstanceId the vnf instance id
	 * @param volumeGroupId the volume group id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_delete_volumegroup_instance/{serviceInstanceId}/vnfs/{vnfInstanceId}/volumeGroups/{volumeGroupId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteVolumeGroupInstance (
			@PathVariable("serviceInstanceId") String serviceInstanceId, @PathVariable("vnfInstanceId") String vnfInstanceId, @PathVariable("volumeGroupId") String volumeGroupId,
			HttpServletRequest request) throws Exception {
		
		String methodName = "deleteVolumeGroupInstance";		
		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		RequestDetails mso_request = retrieveRequestObject (request);
		
	    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_VOLUME_GROUP_INSTANCE);
	    if ( p == null || p.isEmpty()) {
	    	throw new Exception ( "Volume group instance path is not defined");
	    }
	    // /serviceInstances/v2/{serviceInstanceId}/volumeGroups
	    String path = p.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId );
	    path = path.replaceFirst(VNF_INSTANCE_ID, vnfInstanceId );
	    MsoResponseWrapper w = deleteInstance ( mso_request, path + "/" + volumeGroupId);
	    
		// always return OK, the MSO status code is embedded in the body
        return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
	}
	
	/**
	 * Delete nw instance.
	 *
	 * @param serviceInstanceId the service instance id
	 * @param networkInstanceId the network instance id
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_delete_nw_instance/{serviceInstanceId}/networks/{networkInstanceId}", method = RequestMethod.POST)
	public ResponseEntity<String> deleteNwInstance(@PathVariable("serviceInstanceId") String serviceInstanceId,
		@PathVariable("networkInstanceId") String networkInstanceId, HttpServletRequest request) throws Exception {
	
		String methodName = "deleteNwInstance";		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		
		RequestDetails mso_request = retrieveRequestObject (request);
		
	    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_NETWORK_INSTANCE);
	    if ( p == null || p.isEmpty()) {
	    	throw new Exception ( "Network instance path is not defined");
	    }
	    // /serviceInstances/v2/<service_instance_id>/networks
	    String path = p.replaceFirst(SVC_INSTANCE_ID, serviceInstanceId );
	    MsoResponseWrapper w = deleteInstance ( mso_request, path + "/" + networkInstanceId);
	    
		// always return OK, the MSO status code is embedded in the body
        return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
	    
	}
	
	/**
	 * Delete instance.
	 *
	 * @param request the request
	 * @param path the path
	 * @return the mso response wrapper
	 * @throws Exception the exception
	 */
	protected  MsoResponseWrapper deleteInstance(RequestDetails request, String path) throws Exception {
		String methodName = "deleteInstance";	
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		
		try {
			MsoRestInterfaceIfc restController = MsoRestInterfaceFactory.getInstance();
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " calling Delete, path =[" + path + "]");
		
			RestObject<String> restObjStr = new RestObject<String>();
			String str = new String();
			restObjStr.set(str);
			restController.<String>Delete(str, request, "", path, restObjStr );
			MsoResponseWrapper w = MsoUtil.wrapResponse (restObjStr);
			
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());
			return w;

		} catch (Exception e) {
			logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			throw e;
		}
		
	}
	
	/**
	 * Gets the orchestration request.
	 *
	 * @param requestId the request id
	 * @param request the request
	 * @return the orchestration request
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_get_orch_req/{requestId}", method = RequestMethod.GET)
	public ResponseEntity<String> getOrchestrationRequest(@PathVariable("requestId") String requestId,
			HttpServletRequest request) throws Exception {
		
		String methodName = "getOrchestrationRequest";		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		MsoResponseWrapper w = null;
		try {
			MsoRestInterfaceIfc restController = MsoRestInterfaceFactory.getInstance();
		    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQ);
		    String path = p + "/" + requestId;
		    
		    RestObject<String> restObjStr = new RestObject<String>();
			String str = new String();
			restObjStr.set(str);

		    restController.<String>Get(str, "", path, restObjStr);
		    
		    w = MsoUtil.wrapResponse (restObjStr);
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());
			// always return OK, the MSO status code is embedded in the body
	  		
		} 
		catch (Exception e) {
			logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			throw e;
		}
		// always return OK, the MSO status code is embedded in the body
  		return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
	}
	
	
	/**
	 * Gets the orchestration requests.
	 *
	 * @param filterString the filter string
	 * @param request the request
	 * @return the orchestration requests
	 * @throws Exception the exception
	 */
	@RequestMapping(value = "/mso_get_orch_reqs/{filterString}", method = RequestMethod.GET)
	public ResponseEntity<String> getOrchestrationRequests(@PathVariable("filterString") String filterString,
			HttpServletRequest request) throws Exception {
		
		String methodName = "getOrchestrationRequests";		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		MsoResponseWrapper w = null;
		try {
			MsoRestInterfaceIfc restController = MsoRestInterfaceFactory.getInstance();
		    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQS);
		    String path = p + filterString;
		    
		    RestObject<String> restObjStr = new RestObject<String>();
			String str = new String();
			restObjStr.set(str);

		    restController.<String>Get(str, "", path, restObjStr);
		   
		    w = MsoUtil.wrapResponse (restObjStr);
		    logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());
		}
		catch (Exception e) {
			logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			throw e;
		}
		// always return OK, the MSO status code is embedded in the body
  		return ( new ResponseEntity<String>(w.getResponse(), HttpStatus.OK) );
	}
	 	
	/**
	 * Gets the orchestration requests for svc instance.
	 *
	 * @param svc_instance_id the svc instance id
	 * @return the orchestration requests for svc instance
	 * @throws Exception the exception
	 */
	public MsoResponseWrapper getOrchestrationRequestsForSvcInstance (String svc_instance_id) throws Exception {
		
		String methodName = "getOrchestrationRequestsForSvcInstance";		
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start");
		MsoResponseWrapper w = null;
		
		try {
			MsoRestInterfaceIfc restController = MsoRestInterfaceFactory.getInstance();
		    String p = SystemProperties.getProperty(MsoProperties.MSO_REST_API_GET_ORC_REQS);
		    String path = p + svc_instance_id;
		    
		    RestObject<String> restObjStr = new RestObject<String>();
			String str = new String();
			restObjStr.set(str);
			
			restController.<String>Get(str, "", path, restObjStr);
			w = MsoUtil.wrapResponse (restObjStr);
		    logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " w=" + w.getResponse());
		
		}
		catch (Exception e) {
			logger.info(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
			throw e;
		}
		return w;
	}
	
	/**
	 * Exception handler.
	 *
	 * @param e the e
	 * @param response the response
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@ExceptionHandler(Exception.class)
	private void exceptionHandler(Exception e, HttpServletResponse response) throws IOException {

		/*
		 * The following "logger.error" lines "should" be sufficient for logging the exception.
		 * However, the console output in my Eclipse environment is NOT showing ANY of the
		 * logger statements in this class. Thus the temporary "e.printStackTrace" statement
		 * is also included.
		 */
		
		String methodName = "exceptionHandler";	
		logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + e.toString());
		StringWriter sw = new StringWriter();
				e.printStackTrace(new PrintWriter(sw));
		logger.error(EELFLoggerDelegate.errorLogger, sw.toString());

		/*
		 *  Temporary - IF the above  mentioned "logger.error" glitch is resolved ...
		 *  this statement could be removed since it would then likely result in duplicate
		 *  trace output. 
		 */
		e.printStackTrace(System.err);

		response.setContentType("application/json; charset=UTF-8");
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		ExceptionResponse exceptionResponse = new ExceptionResponse();
		exceptionResponse.setException(e.getClass().toString().replaceFirst("^.*\\.", ""));
		exceptionResponse.setMessage(e.getMessage());

		response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));

		response.flushBuffer();

	}

	/**
	 * Parses the orchestration requests for svc instance.
	 *
	 * @param resp the resp
	 * @return the list
	 * @throws ParseException the parse exception
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public List<JSONObject> parseOrchestrationRequestsForSvcInstance ( ClientResponse resp ) throws org.json.simple.parser.ParseException, Exception {
		
		String methodName = "parseOrchestrationRequestsForSvcInstance";
		
		ArrayList<JSONObject> json_list = new ArrayList<JSONObject>();
		
		String rlist_str = resp.readEntity (String.class);
		logger.debug (EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + " Response string: " + rlist_str);
		
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(rlist_str);
			
			JSONObject jsonObject = (JSONObject) obj;
		
			JSONArray requestList = (JSONArray) jsonObject.get("requestList");
			
			if ( requestList != null && ! (requestList.isEmpty()) )
				for ( Object container : requestList) {
			
					JSONObject containerJsonObj = (JSONObject) container;
					//logger.debug(dateFormat.format(new Date()) +  "<== " + "." + methodName + " reqJsonObj: " + containerJsonObj.toJSONString());
					JSONObject reqJsonObj = (JSONObject) containerJsonObj.get("request");
					
					//logger.debug(dateFormat.format(new Date()) +  "<== " + "." + methodName + " reqJsonObj.requestId: " + 
						//	reqJsonObj.get("requestId") );
					JSONObject result = new JSONObject();
					
					result.put("requestId", reqJsonObj.get ("requestId"));
					if ( reqJsonObj.get("requestType") != null ) {
						result.put("requestType", (reqJsonObj.get("requestType").toString()));
					}
					JSONObject req_status = (JSONObject)reqJsonObj.get("requestStatus");
					if ( req_status != null ) {
						result.put("timestamp", (req_status.get("timestamp")));
						result.put("requestState", (req_status.get("requestState")));
						result.put("statusMessage", (req_status.get("statusMessage")));
						result.put("percentProgress", (req_status.get("percentProgress")));
					} 
					json_list.add (result);
				}
		} catch (org.json.simple.parser.ParseException pe) {
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + " Parse exception: " + pe.toString());
			throw pe;
		} catch (Exception e) {
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) +  "<== " + "." + methodName + " Exception: " + e.toString());
			throw e;
		}
		return ( json_list );
	} 
	
	/**
	 * Retrieve request object.
	 *
	 * @param request the request
	 * @return the request details
	 * @throws Exception the exception
	 */
	public RequestDetails retrieveRequestObject ( HttpServletRequest request ) throws Exception {
			
		String methodName = "retrieveRequestObject";
		logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " start" );
		
		ObjectMapper mapper = new ObjectMapper();
		//JSON from String to Object
		RequestDetails mso_request;
		try {
			mso_request = mapper.readValue(request.getInputStream(), RequestDetails.class);
		}
		catch ( Exception e ) {
			logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + " Unable to read json object RequestDetails e=" + e.getMessage());
			throw e;
		}
		if ( mso_request == null) {
			logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + " mso_request is null");
			throw new Exception ("RequestDetails is missing");
		}
		try {
			String json_req = mapper.writeValueAsString(mso_request);
			logger.debug(EELFLoggerDelegate.debugLogger, dateFormat.format(new Date()) + "<== " + methodName + " request=[" + json_req + "]");
		}
		catch ( Exception e ) {
			logger.error(EELFLoggerDelegate.errorLogger, dateFormat.format(new Date()) + "<== " + methodName + " Unable to convert RequestDetails to json string e=" + e.getMessage());
			throw e;
		}
		return (mso_request);
	}
}
