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

package org.openecomp.vid.mso;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.glassfish.jersey.client.ClientResponse;

import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.openecomp.vid.controller.MsoController;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The Class MsoUtil.
 */
public class MsoUtil {
	
	/** The logger. */
	private static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoController.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	/**
	 * Wrap response.
	 *
	 * @param body the body
	 * @param statusCode the status code
	 * @return the mso response wrapper
	 */
	public static MsoResponseWrapper wrapResponse ( String body, int statusCode ) {
		
		MsoResponseWrapper w = new MsoResponseWrapper();
		w.setStatus (statusCode);
		w.setEntity(body);
		
		return w;
	}
	
	/**
	 * Wrap response.
	 *
	 * @param cres the cres
	 * @return the mso response wrapper
	 */
	public static MsoResponseWrapper wrapResponse (ClientResponse cres) {	
		String resp_str = "";
		if ( cres != null ) {
			resp_str = cres.readEntity(String.class);
		}
		int statuscode = cres.getStatus();
		MsoResponseWrapper w = MsoUtil.wrapResponse ( resp_str, statuscode );
		return (w);
	}
	
	/**
	 * Wrap response.
	 *
	 * @param rs the rs
	 * @return the mso response wrapper
	 */
	public static MsoResponseWrapper wrapResponse (RestObject<String> rs) {	
		String resp_str = "";
		int status = 0;
		if ( rs != null ) {
			resp_str = rs.get();
			status = rs.getStatusCode();
		}
		MsoResponseWrapper w = MsoUtil.wrapResponse ( resp_str, status );
		return (w);
	}	
	
	/**
	 * Convert pojo to string.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @return the string
	 * @throws JsonProcessingException the json processing exception
	 */
	public static <T> String convertPojoToString ( T t ) throws com.fasterxml.jackson.core.JsonProcessingException {
		
		String methodName = "convertPojoToString";
		ObjectMapper mapper = new ObjectMapper();
		String r_json_str = "";
	    if ( t != null ) {
		    try {
		    	r_json_str = mapper.writeValueAsString(t);
		    }
		    catch ( com.fasterxml.jackson.core.JsonProcessingException j ) {
		    	logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " Unable to parse object as json");
		    }
	    }
	    return (r_json_str);
	}
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
}
