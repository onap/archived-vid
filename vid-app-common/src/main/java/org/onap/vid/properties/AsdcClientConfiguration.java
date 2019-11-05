/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
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

package org.onap.vid.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * The Class AsdcClientConfiguration.
 */
@Configuration

	@PropertySource(value="asdc.properties",  ignoreResourceNotFound = true)
	@PropertySource(value="${container.classpath:}/WEB-INF/conf/asdc.properties", ignoreResourceNotFound = true)

public class AsdcClientConfiguration {

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

	@Value("${asdc.client.type}")
	private AsdcClientType asdcClientType;

	/** The asdc client host. */
	@Value("${asdc.client.rest.host}")
	private String asdcClientHost;

	/** The asdc client port. */
	@Value("${asdc.client.rest.port}")
	private int asdcClientPort;

	/** The asdc client auth. */
	@Value("${asdc.client.rest.auth}")
	public String asdcClientAuth;

	/** The asdc client protocol. */
	@Value("${asdc.client.rest.protocol}")
	public String asdcClientProtocol;

	/**
	 * Gets the asdc client type.
	 *
	 * @return the asdc client type
	 */
	public AsdcClientType getAsdcClientType() {
		return asdcClientType;
	}

	/**
	 * Gets the asdc client host.
	 *
	 * @return the asdc client host
	 */
	public String getAsdcClientHost() {
		return asdcClientHost;
	}

	/**
	 * Gets the asdc client port.
	 *
	 * @return the asdc client port
	 */
	public int getAsdcClientPort() {
		return asdcClientPort;
	}

	/**
	 * Gets the asdc client auth.
	 *
	 * @return the asdc client auth
	 */
	public String getAsdcClientAuth() {
		return asdcClientAuth;
	}

	/**
	 * Gets the asdc client protocol.
	 *
	 * @return the asdc client protocol
	 */
	public String getAsdcClientProtocol() {
		return asdcClientProtocol;
	}

	/**
	 * The Enum AsdcClientType.
	 */
	public enum AsdcClientType {

		/** The in memory. */
		IN_MEMORY,

		/** The rest. */
		REST,

		/** The local. */
		LOCAL
	}
}
