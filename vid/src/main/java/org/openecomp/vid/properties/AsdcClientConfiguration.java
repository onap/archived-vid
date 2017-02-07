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

package org.openecomp.vid.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value="${container.classpath:}/WEB-INF/conf/asdc.properties")
public class AsdcClientConfiguration {

	@Value("${asdc.client.type}")
	private AsdcClientType asdcClientType;
	
	@Value("${asdc.client.rest.host}")
	private String asdcClientHost;
	
	@Value("${asdc.client.rest.port}")
	private int asdcClientPort;
	
	@Value("${asdc.client.rest.auth}")
	public String asdcClientAuth;

	@Value("${asdc.client.rest.protocol}")
	public String asdcClientProtocol;
	
	public AsdcClientType getAsdcClientType() {
		return asdcClientType;
	}

	public String getAsdcClientHost() {
		return asdcClientHost;
	}

	public int getAsdcClientPort() {
		return asdcClientPort;
	}

	public String getAsdcClientAuth() {
		return asdcClientAuth;
	}

	public String getAsdcClientProtocol() {
		return asdcClientProtocol;
	}
	
	public enum AsdcClientType {
		IN_MEMORY,
		REST
	}
}
