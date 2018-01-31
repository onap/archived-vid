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

package org.onap.vid.policy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openecomp.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.onap.vid.policy.rest.RequestDetails;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PolicyRestInt {
	
	/** The logger. */
	private static final EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(PolicyRestInt.class);
	
	/** The Constant dateFormat. */
	final static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSSS");
	
	/** The request date format. */
	public DateFormat requestDateFormat = new SimpleDateFormat("EEE, dd MMM YYYY HH:mm:ss z");
	
	public PolicyRestInt() {
		requestDateFormat.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
	}

	/**
	 * Log request.
	 *
	 * @param r the r
	 */
	public void logRequest ( RequestDetails r ) {
    	String methodName = "logRequest";
	    ObjectMapper mapper = new ObjectMapper();
	    String r_json_str = "";
	    if ( r != null ) {
	    	r_json_str = r.toString();
		    try {
		    	r_json_str = mapper.writeValueAsString(r);
		    }
		    catch ( com.fasterxml.jackson.core.JsonProcessingException j ) {
		    	logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " Unable to parse request as json");
		    }
	    }
	    logger.debug(EELFLoggerDelegate.debugLogger,dateFormat.format(new Date()) + "<== " +  methodName + " Request=(" + r_json_str + ")");  
    }
}