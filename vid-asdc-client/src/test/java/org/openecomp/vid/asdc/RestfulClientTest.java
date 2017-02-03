/*-
 * ============LICENSE_START=======================================================
 * VID ASDC Client
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

package org.openecomp.vid.asdc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openecomp.vid.asdc.rest.RestfulAsdcClient;

/**
 * The Class RestfulClientTest.
 */
@Ignore
public class RestfulClientTest extends BaseClientTest {

	/** The rest client. */
	private Client restClient;
	
	/** The uri. */
	private URI uri;
	
	/** The properties. */
	private Properties properties;
	
	/** The auth. */
	private String auth;
	
	/**
	 * Sets the up.
	 *
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Before
	public void setUp() throws URISyntaxException, IOException {
		final InputStream propertiesFile = getClass().getClassLoader().getResourceAsStream("asdc.properties");		
		
		properties = new Properties();
		properties.load(propertiesFile);
		
		final String protocol = properties.getProperty("protocol", "http");
		
		restClient = ClientBuilder.newBuilder()
						.hostnameVerifier(new HostnameVerifier() {

							@Override
							public boolean verify(String arg0, SSLSession arg1) {
								return true;
							}
						})
						.build();
		uri = new URI(protocol + "://" + properties.getProperty("host", "localhost") + ":" + properties.getProperty("port", "80") + "/");
		auth = properties.getProperty("auth");
	}

	/**
	 * Test resources.
	 *
	 * @throws AsdcCatalogException the asdc catalog exception
	 */
	@Test
	public void testResources() throws AsdcCatalogException {
		
		runResourceTests(new RestfulAsdcClient.Builder(restClient, uri).auth(auth).build());
	}
	
	/**
	 * Test services.
	 *
	 * @throws AsdcCatalogException the asdc catalog exception
	 * @throws URISyntaxException the URI syntax exception
	 */
	@Test
	public void testServices() throws AsdcCatalogException, URISyntaxException {

		runServiceTests(new RestfulAsdcClient.Builder(restClient, uri).auth(auth).build());
	}
}
