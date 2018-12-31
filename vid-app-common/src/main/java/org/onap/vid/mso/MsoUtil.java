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

package org.onap.vid.mso;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.joshworks.restclient.http.HttpResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.glassfish.jersey.client.ClientResponse;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;

import static org.onap.vid.utils.Logging.getMethodName;

/**
 * The Class MsoUtil.
 */
public class MsoUtil {
	
	/** The logger. */
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(MsoUtil.class);

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
		String respStr = "";
		int statuscode = 0;
		if ( cres != null ) {
			respStr = cres.readEntity(String.class);
			statuscode = cres.getStatus();
		}
		MsoResponseWrapper w = MsoUtil.wrapResponse ( respStr, statuscode );
		return (w);
	}
	
	/**
	 * Wrap response.
	 *
	 * @param rs the rs
	 * @return the mso response wrapper
	 */
	public static MsoResponseWrapper wrapResponse (RestObject<String> rs) {
		String respStr = null;
		int status = 0;
		if ( rs != null ) {
			respStr = rs.get() != null ? rs.get() : rs.getRaw();
			status = rs.getStatusCode();
		}
		MsoResponseWrapper w = MsoUtil.wrapResponse ( respStr, status );
		return (w);
	}	
	
	public static <T> MsoResponseWrapper wrapResponse (HttpResponse<T> rs) {
		MsoResponseWrapper w = new MsoResponseWrapper();
		w.setStatus (rs.getStatus());
		if(rs.getRawBody() != null) {
			w.setEntity(ObjectUtils.toString(rs.getBody()));
		}
		return w;
	}

	/**
	 * Convert pojo to string.
	 *
	 * @param <T> the generic type
	 * @param t the t
	 * @return the string
	 * @throws JsonProcessingException the json processing exception
	 */
	public static <T> String convertPojoToString ( T t ) {
		ObjectMapper mapper = new ObjectMapper();
		String rJsonStr = "";
	    if ( t != null ) {
		    try {
		    	rJsonStr = mapper.writeValueAsString(t);
		    }
		    catch ( com.fasterxml.jackson.core.JsonProcessingException j ) {
		    	logger.debug(EELFLoggerDelegate.debugLogger,getMethodName() + " Unable to parse object of type " + t.getClass().getName() + " as json", j);
		    }
	    }
	    return (rJsonStr);
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
