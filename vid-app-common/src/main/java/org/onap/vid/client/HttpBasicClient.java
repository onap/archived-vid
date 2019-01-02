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

package org.onap.vid.client;


import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

/**
 *  General HTTP client.
 */

public class HttpBasicClient{
	
	/** The servlet context. */
	@Autowired 
	private ServletContext servletContext;
	
	/** The logger. */
	EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(HttpBasicClient.class);
	
	/**
	 * Obtain a basic HTTP client .
	 *
	 * @return Client client object
	 * @throws Exception the exception
	 */
	public static Client getClient() {
		
		ClientConfig config = new ClientConfig();
		config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
		
		return ClientBuilder.newClient(config)
				.register(org.onap.vid.aai.util.CustomJacksonJaxBJsonProvider.class);
	}	
}  
